package com.example.cmpshop.auth.services;

import com.example.cmpshop.auth.dto.RegisterRequest;
import com.example.cmpshop.auth.dto.RegisterResponse;
import com.example.cmpshop.auth.dto.UserToken;
import com.example.cmpshop.auth.entities.ProviderTypes;
import com.example.cmpshop.auth.entities.UserEntity;
import com.example.cmpshop.auth.helper.VerificationCodeGenerator;
import com.example.cmpshop.auth.reponsitory.UserDetailRepository;
import com.example.cmpshop.exceptions.AuthenticationFailedException;
import com.example.cmpshop.exceptions.InvalidParameterException;
import com.example.cmpshop.exceptions.ResourceNotFoundEx;
import com.example.cmpshop.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;
@Service
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    AuthenticationManager authenticationManager;

    public UserToken authenticateUser(String username, CharSequence password) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticationResponse = this.authenticationManager.authenticate(authentication);

            if (authenticationResponse.isAuthenticated()) {
                UserEntity user = (UserEntity) authenticationResponse.getPrincipal();
                if (!user.isEnabled()) { // kiểm tra nếu tài khoản chưa kích hoạt
                    log.warn("Tài khoản chưa xác thực: {}", username);
                    throw new UnauthorizedException("Tài khoản chưa xác thực");
                }
                // Sinh JWT token
                String token = "TOKENFORYOU"; // Thay thế chuỗi "Token for you"
                UserToken userToken = UserToken.builder().token(token).build();
                return userToken;
            }
        } catch (BadCredentialsException e) {
            log.warn("Xác thực thất bại cho tài khoản: {}", username);
            throw new AuthenticationFailedException("Tên người dùng hoặc mật khẩu không chính xác");
        }
        log.error("Xác thực thất bại không rõ lý do cho tài khoản: {}", username);
        throw new AuthenticationFailedException("Xác thực không thành công");
    }

    public RegisterResponse createUser(RegisterRequest request, boolean hasRoleAdmin) {
        UserEntity userExisting = userDetailRepository.findByEmail(request.getEmail());
        if(null != userExisting) {
            return RegisterResponse.builder()
                    .code(400)
                    .message("Email already exist!")
                    .build();
        }
        try {
            String code = VerificationCodeGenerator.generateCode();
            System.out.println(code);
            UserEntity user =  UserEntity.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .verificationCode(code)
                    .provider(ProviderTypes.MANUAL)
                    .enabled(false)
                    .authorities(hasRoleAdmin ? authorityService.getAdminAuthority() : authorityService.getUserAuthority())
                    .build();
            // save user
            userDetailRepository.save(user);
            // Call method to send  email
            return RegisterResponse.builder()
                    .code(200)
                    .message("User created !")
                    .build();
        } catch (Exception e) {
            log.error("Error creating account");
            throw new ServerErrorException(e.getMessage(), e.getCause());
        }
    }

    public void verifyUser (String userName) {
        UserEntity user = userDetailRepository.findByEmail(userName);
        user.setEnabled(true);
        userDetailRepository.save(user);
    }
}
