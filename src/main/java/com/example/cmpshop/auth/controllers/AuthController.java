package com.example.cmpshop.auth.controllers;

import com.example.cmpshop.auth.dto.LoginRequest;
import com.example.cmpshop.auth.dto.RegisterRequest;
import com.example.cmpshop.auth.dto.RegisterResponse;
import com.example.cmpshop.auth.dto.UserToken;
import com.example.cmpshop.auth.entities.UserEntity;
import com.example.cmpshop.auth.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    UserDetailsService userDetailsService;


    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@RequestBody LoginRequest request) {
        UserToken token = authenticationService.authenticateUser(request.getUserName(), request.getPassword());
        return new ResponseEntity<>(token, token != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register (@RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = authenticationService.createUser(request,false);
        return new ResponseEntity<>(registerResponse,registerResponse.getCode() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail (@RequestBody Map<String, String> map) {
        String userName = map.get("userName");
        String code = map.get("code");
        UserEntity user = (UserEntity) userDetailsService.loadUserByUsername(userName);
        // kiểm tra user vs code có map không
        if (null != user && user.getVerificationCode().equals(code)){
            authenticationService.verifyUser(userName);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
