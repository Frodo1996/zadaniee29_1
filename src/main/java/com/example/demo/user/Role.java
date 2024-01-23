package com.example.demo.user;

public enum Role {
    ROLE_USER("User"),
    ROLE_ADMIN("Admin");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
