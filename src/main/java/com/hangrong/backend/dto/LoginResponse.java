package com.hangrong.backend.dto;

public class LoginResponse {

    private boolean success;
    private Long userId;
    private String username;
    private String message;
    private String token;

    public LoginResponse(boolean success, Long userId, String username, String token) {
        this.success = success;
        this.userId = userId;
        this.username = username;
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

}
