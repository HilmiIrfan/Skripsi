package com.doyatama.university.controller;

import com.doyatama.university.model.School;
import com.doyatama.university.model.User;
import com.doyatama.university.payload.ApiResponse;
import com.doyatama.university.payload.UserSummary;
import com.doyatama.university.payload.auth.JwtAuthenticationResponse;
import com.doyatama.university.payload.auth.LoginRequest;
import com.doyatama.university.payload.auth.SignUpRequest;
import com.doyatama.university.repository.SchoolRepository;
import com.doyatama.university.repository.UserRepository;
import com.doyatama.university.security.JwtTokenProvider;
import com.doyatama.university.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
        // private SchoolService schoolService = new SchoolService();
        private SchoolRepository schoolRepository = new SchoolRepository();

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        UserRepository userRepository;

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        JwtTokenProvider tokenProvider;

        @PostMapping("/signin")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginRequest.getUsernameOrEmail(),
                                                loginRequest.getPassword()));
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = tokenProvider.generateToken(authentication);
                UserSummary userSummary = new UserSummary(userPrincipal.getId(), userPrincipal.getUsername(),
                                userPrincipal.getName(), userPrincipal.getSchoolId(), userPrincipal.getSchoolName(),
                                userPrincipal.getRoles().equalsIgnoreCase("1") ? "ROLE_ADMINISTRATOR"
                                                : userPrincipal.getRoles().equalsIgnoreCase("2") ? "ROLE_OPERATOR"
                                                                : userPrincipal.getRoles().equalsIgnoreCase("3")
                                                                                ? "ROLE_TEACHER"
                                                                                : userPrincipal.getRoles()
                                                                                                .equalsIgnoreCase("4")
                                                                                                                ? "ROLE_DUDI"
                                                                                                                : "ROLE_STUDENT",
                                "", "");
                return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, userSummary));
        }

        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws IOException {
                if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                        return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                                        HttpStatus.BAD_REQUEST);
                }

                if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                        return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                                        HttpStatus.BAD_REQUEST);
                }

                // Get time now
                ZoneId zoneId = ZoneId.of("Asia/Jakarta");
                ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
                Instant instant = zonedDateTime.toInstant();

                // Mengambil objek School langsung menggunakan SchoolRepository
                School school = schoolRepository.findById(signUpRequest.getSchoolId());
                if (school == null) {
                        return new ResponseEntity<>(
                                        new ApiResponse(false,
                                                        "School not found with ID: " + signUpRequest.getSchoolId()),
                                        HttpStatus.NOT_FOUND);
                }

                // Membuat user baru dengan objek School yang sudah diambil
                User user = new User(
                                null, // Using null to let the DB generate ID or use UUID.randomUUID().toString() for
                                      // custom ID
                                signUpRequest.getName(),
                                signUpRequest.getUsername(),
                                signUpRequest.getEmail(),
                                signUpRequest.getPassword(),
                                school,
                                signUpRequest.getRoles(),
                                instant);

                user.setPassword(passwordEncoder.encode(user.getPassword()));

                User result = userRepository.save(user);

                URI location = ServletUriComponentsBuilder
                                .fromCurrentContextPath().path("/users/{username}")
                                .buildAndExpand(result.getUsername()).toUri();

                return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
        }

}
