package com.example.prueba;

public class EmailRequest {
    private String type = "recovery";
    private String email;
    private String token;

    public EmailRequest(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
