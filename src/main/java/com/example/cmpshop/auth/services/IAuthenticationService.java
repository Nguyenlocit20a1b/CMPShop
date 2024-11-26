package com.example.cmpshop.auth.services;

import com.example.cmpshop.auth.dto.request.RegisterRequest;
import com.example.cmpshop.auth.dto.response.RegisterResponse;
import com.example.cmpshop.auth.dto.response.UserToken;

import java.security.GeneralSecurityException;

/**
 * Interface cung cấp các dịch vụ liên quan đến người dùng trong hệ thống.
 * Được sử dụng để định nghĩa các hành vi (operations) mà các lớp triển khai cần cung cấp.
 */
public interface IAuthenticationService {
    /**
     * Xác thực thông tin đăng nhập của người dùng.
     *
     * @param username tên đăng nhập của người dùng.
     * @param password mật khẩu của người dùng (dưới dạng chuỗi ký tự).
     * @return UserToken chứa thông tin token được cấp nếu xác thực thành công.
     */
    UserToken authenticateUser(String username, CharSequence password);

    /**
     * Tạo mới một người dùng trong hệ thống.
     *
     * @param registerRequest đối tượng chứa thông tin đăng ký người dùng.
     * @return RegisterResponse chứa thông tin về kết quả đăng ký.
     */
    RegisterResponse createUser(RegisterRequest registerRequest);

    /**
     * Xác minh người dùng dựa trên tên đăng nhập.
     * Thường được sử dụng trong quy trình kích hoạt tài khoản hoặc bảo mật.
     *
     * @param phoneNumber tên đăng nhập của người dùng cần xác minh.
     */
    void verifyUser(String phoneNumber) ;
}
