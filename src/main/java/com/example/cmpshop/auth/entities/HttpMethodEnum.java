package com.example.cmpshop.auth.entities;

public enum HttpMethodEnum {
    GET, POST, PUT, DELETE;

    // Phương thức kiểm tra xem giá trị có nằm trong enum không
    public static boolean contains(String method) {
        try {
            // Kiểm tra xem phương thức có tồn tại trong enum không
            HttpMethodEnum.valueOf(method);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
