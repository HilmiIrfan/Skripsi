package com.doyatama.university.controller;

import com.doyatama.university.model.User;
import com.doyatama.university.payload.*;
import com.doyatama.university.security.CurrentUser;
import com.doyatama.university.security.UserPrincipal;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.service.UserService;
import com.doyatama.university.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/user/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(),
                currentUser.getSchoolId(), currentUser.getSchoolName(),
                currentUser.getRoles().equalsIgnoreCase("1") ? "ROLE_ADMINISTRATOR"
                        : currentUser.getRoles().equalsIgnoreCase("2") ? "ROLE_OPERATOR"
                                : currentUser.getRoles().equalsIgnoreCase("3") ? "ROLE_TEACHER"
                                        : currentUser.getRoles().equalsIgnoreCase("4") ? "ROLE_DUDI" : "ROLE_STUDENT",
                "", "");
        return userSummary;
    }

    @GetMapping("/users/{userId}")
    public PagedResponse<User> getUserById(@PathVariable String userId) throws IOException {
        return userService.getUserById(userId);
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username)
            throws IOException {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email)
            throws IOException {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) throws IOException {
        User user = userRepository.findByUsername(username);
        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(),
                user.getCreatedAt());
        return userProfile;
    }

    @GetMapping("/users/not-used-account")
    public PagedResponse<User> getUserNotUses(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) throws IOException {
        return userService.getUserNotUsedAccount(page, size);
    }

    @GetMapping("/users")
    public PagedResponse<User> getUsers(
            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @CurrentUser UserPrincipal currentUser) throws IOException {

        String schoolID = currentUser.getSchoolId();
        String role = currentUser.getRoles(); // Ambil role user saat ini

        if ("1".equals(role)) {
            // Admin: tampilkan semua user
            return userService.getAllUser(page, size, "*", true);
        } else {
            // Bukan admin: tampilkan user selain admin, hanya dari sekolah yang sama
            return userService.getAllUser(page, size, schoolID, false);
        }
    }

    @PostMapping("/users/add")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest userRequest) throws IOException {
        try {
            User user = userService.createUser(userRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{userId}")
                    .buildAndExpand(user.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "User Created Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @PutMapping("/users/edit/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @Valid @RequestBody UserRequest userRequest)
            throws IOException {
        try {
            User user = userService.updateUser(userId, userRequest);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{userId}")
                    .buildAndExpand(user.getId()).toUri();

            return ResponseEntity.created(location)
                    .body(new ApiResponse(true, "User Updated Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

    @DeleteMapping("/users/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) throws IOException {
        try {
            userService.deleteUserById(userId);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse(true, "User Deleted Successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "An unexpected error occurred."));
        }
    }

}
