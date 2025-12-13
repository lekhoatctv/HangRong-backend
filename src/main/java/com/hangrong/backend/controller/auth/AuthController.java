package com.hangrong.backend.controller.auth;

import com.hangrong.backend.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;
import com.hangrong.backend.repository.UserRepository;
import com.hangrong.backend.model.User;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .filter(u -> u.getPassword().equals(request.getPassword()))
                .map(u -> "OK")
                .orElse("FAIL");
    }

    @PostMapping("/register")
    public String register(@RequestBody LoginRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "EXISTS";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        userRepository.save(user);

        return "OK";
    }
}
