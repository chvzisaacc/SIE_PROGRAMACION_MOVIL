package com.example.prueba;
public class LoginResponse {
    private User user;
    private String access_token;
    public User getUser() { return user; }
    public String getAccessToken() { return access_token; }

    public static class User {
        private String id;
        public String getId() { return id; }
    }
}