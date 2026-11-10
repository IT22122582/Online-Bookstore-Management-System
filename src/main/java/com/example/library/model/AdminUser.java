package com.example.library.model;

public class AdminUser extends User {
    private String adminLevel; // "SUPER", "DEPARTMENT", "ASSISTANT"

    public AdminUser() {
        super();
        setRole("ADMIN");
    }

    public AdminUser(String id, String username, String password, String email, String adminLevel) {
        super(id, username, password, email, "ADMIN");
        this.adminLevel = adminLevel;
    }

    public String getAdminLevel() { return adminLevel; }
    public void setAdminLevel(String adminLevel) { this.adminLevel = adminLevel; }

    @Override
    public String getUserSummary() {
        return "Administrator (" + adminLevel + " Level) - Username: " + getUsername() + ", Email: " + getEmail();
    }
}
