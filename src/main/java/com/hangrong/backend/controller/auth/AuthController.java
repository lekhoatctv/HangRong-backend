package com.hangrong.backend.controller.auth;

import com.hangrong.backend.dto.LoginRequest;
import com.hangrong.backend.dto.LoginResponse;
import com.hangrong.backend.security.JwtUtil;
import org.springframework.web.bind.annotation.*;
import com.hangrong.backend.repository.UserRepository;
import com.hangrong.backend.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")

public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return userRepository.findByUsername(request.getUsername())
                .filter(u -> passwordEncoder.matches(
                        request.getPassword(),
                        u.getPassword()))
                .map(u -> {
                    String token = JwtUtil.generateToken(u.getId(), u.getUsername());
                    LoginResponse response = new LoginResponse(
                            true,
                            u.getId(),
                            u.getUsername(),
                            token);
                    response.setMessage("Login successful");
                    return response;
                })
                .orElseGet(() -> {
                    LoginResponse response = new LoginResponse(
                            false,
                            null,
                            null,
                            null);
                    response.setMessage("Invalid username or password");
                    return response;
                });
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody LoginRequest request) {

        Map<String, Object> res = new HashMap<>();

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            res.put("success", false);
            res.put("message", "Username already exists");
            return res;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        res.put("success", true);
        res.put("message", "Register successful");
        return res;
    }

    /**
     * 🎯 API /auth/me - Lấy thông tin user từ JWT token
     * ✅ KHÔNG cần username/password
     * ✅ KHÔNG cần query param
     * ✅ Chỉ cần JWT token trong header
     */
    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest request) {

        // Lấy thông tin user từ JWT filter đã set vào request
        // JWT lưu userId dưới dạng Integer, không phải Long
        Object userIdObj = request.getAttribute("userId");
        Long userId = userIdObj instanceof Integer ? ((Integer) userIdObj).longValue() : (Long) userIdObj;
        String username = (String) request.getAttribute("username");

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("username", username);

        return result;
    }
}
