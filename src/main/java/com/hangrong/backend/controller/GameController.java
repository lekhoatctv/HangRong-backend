package com.hangrong.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    /**
     * 🎮 API vào game - BẮT BUỘC phải có JWT token
     * ✅ Chặn vào game nếu chưa login (JWT Filter tự động chặn)
     * ✅ Chỉ user đã login mới vào được
     */
    @GetMapping("/enter")
    public Map<String, Object> enterGame(HttpServletRequest request) {

        // Lấy thông tin user từ JWT token (đã được filter set vào request)
        // JWT lưu userId dưới dạng Integer, không phải Long
        Object userIdObj = request.getAttribute("userId");
        Long userId = userIdObj instanceof Integer ? ((Integer) userIdObj).longValue() : (Long) userIdObj;
        String username = (String) request.getAttribute("username");

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Welcome to the game!");
        result.put("userId", userId);
        result.put("username", username);
        result.put("gameUrl", "/game/play"); // URL của game thực tế

        return result;
    }

    /**
     * 🎮 API chơi game
     */
    @GetMapping("/play")
    public Map<String, Object> playGame(HttpServletRequest request) {

        Object userIdObj = request.getAttribute("userId");
        Long userId = userIdObj instanceof Integer ? ((Integer) userIdObj).longValue() : (Long) userIdObj;
        String username = (String) request.getAttribute("username");

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Game is running...");
        result.put("player", username);
        result.put("userId", userId);
        result.put("gameData", Map.of(
                "level", 1,
                "score", 0,
                "status", "active"));

        return result;
    }

    /**
     * 🎮 API lấy thông tin trạng thái game
     */
    @GetMapping("/status")
    public Map<String, Object> getGameStatus(HttpServletRequest request) {

        Object userIdObj = request.getAttribute("userId");
        Long userId = userIdObj instanceof Integer ? ((Integer) userIdObj).longValue() : (Long) userIdObj;
        String username = (String) request.getAttribute("username");

        Map<String, Object> result = new HashMap<>();
        result.put("userId", userId);
        result.put("username", username);
        result.put("isPlaying", true);
        result.put("lastPlayed", System.currentTimeMillis());

        return result;
    }
}
