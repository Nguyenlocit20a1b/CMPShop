package com.example.cmpshop.auth.controllers;

import com.example.cmpshop.auth.dto.RegisterRequest;
import com.example.cmpshop.auth.dto.RegisterResponse;
import com.example.cmpshop.auth.entities.Authority;
import com.example.cmpshop.auth.services.AuthenticationService;
import com.example.cmpshop.auth.services.AuthorityService;
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

@RequestMapping("/api/admin/auth")
@RestController
public class AdminController {
    @Autowired
    AuthorityService authorityService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    UserDetailsService userDetailsService;
    @PostMapping("/create-author")
    public ResponseEntity<?> createAuthority (@RequestBody Map<String, String> map ){
        String roleName = map.get("roleName");
        String description = map.get("description");
        Authority authority = authorityService.createAuthority(roleName, description);
        return new ResponseEntity<>(authority, authority != null ? HttpStatus.OK  : HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/register-admin")
    public ResponseEntity<RegisterResponse> register (@RequestBody RegisterRequest request) {
        RegisterResponse registerResponse = authenticationService.createUser(request,true);
        return new ResponseEntity<>(registerResponse,registerResponse.getCode() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
