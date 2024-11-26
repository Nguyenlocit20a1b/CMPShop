package com.example.cmpshop.auth.controllers;

import com.example.cmpshop.auth.dto.request.LoginRequest;
import com.example.cmpshop.auth.dto.request.RegisterRequest;
import com.example.cmpshop.auth.dto.response.RegisterResponse;
import com.example.cmpshop.auth.dto.response.UserToken;
import com.example.cmpshop.auth.entities.UserEntity;
import com.example.cmpshop.auth.services.impl.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.GeneralSecurityException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Đăng ký người dùng mới vào hệ thống.
     *
     * @param request chứa các thông tin người dùng cần đăng ký như tên, email, số điện thoại, mật khẩu.
     * @return ResponseEntity chứa kết quả đăng ký và mã trạng thái.
     */
    @Operation(summary = "User Registration", description = "Register a new user into the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully registered.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid registration details or user already exists.",
                    content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        RegisterResponse registerResponse = authenticationService.createUser(request);
        return new ResponseEntity<>(registerResponse, (registerResponse != null) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    /**
     * Xác minh email người dùng thông qua mã xác minh.
     *
     * @param map chứa email và mã xác minh người dùng nhập vào.
     * @return ResponseEntity với mã trạng thái xác nhận nếu mã xác minh đúng, hoặc lỗi nếu sai.
     */
    @Operation(summary = "Email Verification", description = "Verify the email address of the user using a verification code.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email successfully verified.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid verification code or email.",
                    content = @Content)
    })
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody Map<String, String> map)  {
        String phoneNumber = map.get("phoneNumber");
        String code = map.get("code");
        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(phoneNumber);
        if (null != user && user.getVerificationCode().equals(code)) {
            authenticationService.verifyUser(phoneNumber);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Đăng nhập người dùng và trả về một JWT token.
     *
     * @param request chứa email và mật khẩu của người dùng cần đăng nhập.
     * @return ResponseEntity chứa JWT token nếu đăng nhập thành công, hoặc lỗi nếu thông tin không chính xác.
     */
    @Operation(summary = "User Login", description = "Authenticate a user and return a JWT token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful and token generated.",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserToken.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid email or password.",
                    content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@RequestBody @Valid LoginRequest request) {
        UserToken token = authenticationService.authenticateUser(request.getPhoneNumber(), request.getPassword());
        return new ResponseEntity<>(token, token != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
