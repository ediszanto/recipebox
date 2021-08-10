package com.example.receipebox.service;

import com.example.receipebox.entity.Authority;
import com.example.receipebox.entity.User;
import com.example.receipebox.exceptions.UserAlreadyRegisteredException;
import com.example.receipebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User add(User user) {
        var existingUser = userRepository.findUserByUsername(user.getEmail());
        existingUser.ifPresentOrElse(u -> {
            throw new UserAlreadyRegisteredException();
        }, () -> {
            Authority authority = new Authority();
            authority.setName("ADMIN");
            user.setAuthorities(List.of(authority));

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        });
        return user;
    }
}
