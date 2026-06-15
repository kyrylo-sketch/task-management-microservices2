package com.task.auth_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "refreshTokens")
public class RefreshToken {
    @Id
    private String id;
    private String token;
    private LocalDateTime expiryAt;
    private User user;

    public RefreshToken() {}

    public RefreshToken(String token, LocalDateTime expiryAt, User user) {
        this.token = token;
        this.expiryAt = expiryAt;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryAt() {
        return expiryAt;
    }

    public void setExpiryAt(LocalDateTime expiryAt) {
        this.expiryAt = expiryAt;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
