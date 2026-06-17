package com.doyatama.university.service;

import com.doyatama.university.exception.BadRequestException;
import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.School;
import com.doyatama.university.model.User;
import com.doyatama.university.payload.PagedResponse;
import com.doyatama.university.payload.UserRequest;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.util.AppConstants;
import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, SchoolRepository schoolRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.schoolRepository = schoolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // private static final Logger logger =
    // LoggerFactory.getLogger(UserService.class);

    public PagedResponse<User> getAllUser(int page, int size, String schoolID, boolean includeAdmin)
            throws IOException {
        validatePageNumberAndSize(page, size);

        List<User> userResponse;

        if (schoolID == null || schoolID.isEmpty() || schoolID.equals("*") || schoolID.equals("-")) {
            // Tanpa filter sekolah
            userResponse = userRepository.findAll(size);
        } else {
            // Filter berdasarkan sekolah
            userResponse = userRepository.findUserBySekolah(schoolID, size);
        }

        // Filter jika bukan admin: buang user dengan role "1"
        if (!includeAdmin) {
            userResponse = userResponse.stream()
                    .filter(user -> !"1".equals(user.getRoles()))
                    .collect(Collectors.toList());
        }

        return new PagedResponse<>(userResponse, userResponse.size(), "Successfully get data", 200);
    }

    public PagedResponse<User> getUserById(String userId) throws IOException {
        // Retrieve User
        User userResponse = userRepository.findById(userId);

        List<User> users = userResponse != null ? Collections.singletonList(userResponse) : Collections.emptyList();

        long totalElements = users.size();
        String message = userResponse != null ? "Successfully get data" : "User not found";
        long statusCode = userResponse != null ? 200 : 404;

        return new PagedResponse<>(users, totalElements, message, statusCode);
    }

    public PagedResponse<User> getUserNotUsedAccount(int page, int size) throws IOException {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        List<User> userResponse = userRepository.findUsersNotUsedInLectures(size);

        return new PagedResponse<>(userResponse, userResponse.size(), "Successfully get data", 200);
    }

    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    public User createUser(UserRequest userRequest) throws IOException {
        User user = new User();

        if (userRequest.getIdUser() == null) {
            userRequest.setIdUser(UUID.randomUUID().toString());
        }

        if (userRepository.existsById(userRequest.getIdUser())) {
            throw new IllegalArgumentException("User already exist");
        }

        School schoolResponse = schoolRepository.findById(userRequest.getSchoolId());

        user.setId(userRequest.getIdUser());
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRoles(userRequest.getRoles());
        user.setCreatedAt(Instant.now());
        user.setSchool(schoolResponse);

        return userRepository.save(user);
    }

    public User updateUser(String userId, UserRequest userRequest) throws IOException {
        System.out.println("Masuk ke service" + userRequest.getIdUser());

        User user = new User();

        School schoolResponse = schoolRepository.findById(userRequest.getSchoolId());

        if (userRequest.getIdUser() != null) {
            user.setName(userRequest.getName());
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());
            if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            }

            user.setRoles(userRequest.getRoles());
            user.setSchool(schoolResponse);

            return userRepository.update(userId, user);
        } else {
            return null;
        }
    }

    public void deleteUserById(String userId) throws IOException {
        User userResponse = userRepository.findById(userId);
        if (userResponse.isValid()) {
            userRepository.deleteById(userId);
        } else {
            throw new ResourceNotFoundException("User", "id", userId);
        }
    }

}
