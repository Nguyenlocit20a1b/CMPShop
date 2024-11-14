package com.example.cmpshop.auth.entities;

public enum RoleTypes {
    ADMIN,
    USER;
    public static boolean contains(String role) {
        for (RoleTypes r : RoleTypes.values()) {
            if (r.name().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
