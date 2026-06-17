package com.doyatama.university.security;

import com.doyatama.university.exception.ResourceNotFoundException;
import com.doyatama.university.model.User;
import com.doyatama.university.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        User user = null;
        try {
            user = Optional.ofNullable(userRepository.findByUsername(username))
                    .orElseThrow(
                            () -> new UsernameNotFoundException("User not found with username or email : " + username));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(String id) throws IOException {
        User user = Optional.ofNullable(userRepository.findById(id.toString())).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id));

        return UserPrincipal.create(user);
    }
}