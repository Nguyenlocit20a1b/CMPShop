package com.example.cmpshop.auth.services.impl;

import com.example.cmpshop.auth.config.JWTTokenHelper;
import com.example.cmpshop.auth.dto.request.RegisterRequest;
import com.example.cmpshop.auth.dto.response.RegisterResponse;
import com.example.cmpshop.auth.dto.response.UserToken;
import com.example.cmpshop.auth.entities.ProviderTypes;
import com.example.cmpshop.auth.entities.UserEntity;
import com.example.cmpshop.auth.helper.VerificationCodeGenerator;
import com.example.cmpshop.auth.reponsitory.UserDetailRepository;
import com.example.cmpshop.auth.services.IAuthenticationService;
import com.example.cmpshop.exceptions.AuthenticationFailedException;
import com.example.cmpshop.exceptions.ResourceNotFoundEx;
import com.example.cmpshop.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

@Service
public class AuthenticationService implements IAuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    EmailService emailService;
    @Autowired
    JWTTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorizationService authorityService;

    /**
     * Xác thực người dùng bằng username và password, đồng thời sinh token JWT nếu xác thực thành công.
     *
     * @param username Tên đăng nhập của người dùng.
     * @param password Mật khẩu của người dùng.
     * @return Đối tượng UserToken chứa JWT token.
     * @throws UnauthorizedException         Nếu tài khoản chưa kích hoạt.
     * @throws AuthenticationFailedException Nếu xác thực thất bại.
     */
    public UserToken authenticateUser(String username, CharSequence password) {
        try {
            // Tạo đối tượng xác thực với username và password
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            // Gửi yêu cầu xác thực tới AuthenticationManager
            Authentication authenticationResponse = this.authenticationManager.authenticate(authentication);
            if (authenticationResponse.isAuthenticated()) {
                UserEntity user = (UserEntity) authenticationResponse.getPrincipal();
                if (!user.isEnabled()) { // kiểm tra nếu tài khoản chưa kích hoạt
                    log.warn("Tài khoản chưa xác thực: {}", username);
                    throw new UnauthorizedException("Tài khoản chưa xác thực");
                }
                // Tạo JWT token
                String token = jwtTokenHelper.generateToken(user.getEmail());
                UserToken userToken = UserToken.builder().token(token).build();
                return userToken;
            }
        } catch (BadCredentialsException e) {
            log.warn("Xác thực thất bại cho tài khoản: {}", username);
            throw new AuthenticationFailedException("Tên người dùng hoặc mật khẩu không chính xác");
        }
        log.error("Xác thực thất bại không rõ lý do cho tài khoản: {}", username);
        throw new AuthenticationFailedException("Đăng nhập không thành công");
    }

    /**
     * Tạo tài khoản người dùng mới và gửi email xác minh.
     *
     * @param request Thông tin đăng ký từ người dùng (họ, tên, email, số điện thoại, mật khẩu).
     * @return Đối tượng RegisterResponse chứa mã phản hồi và thông điệp kết quả.
     * @throws ServerErrorException Nếu có lỗi trong quá trình tạo tài khoản.
     */
    public RegisterResponse createUser(RegisterRequest request) {
        UserEntity userExisting = userDetailRepository.findByEmail(request.getEmail());
        if (null != userExisting) {
            return RegisterResponse.builder()
                    .code(400)
                    .message("Email already exist!")
                    .build();
        }
        try {
            String code = VerificationCodeGenerator.generateCode();
            log.info("Generated verification code: {}", code);
            UserEntity user = UserEntity.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .verificationCode(code)
                    .provider(ProviderTypes.MANUAL)
                    .enabled(false)
                    .authorities(authorityService.getUserAuthority())
                    .build();
            // save user
            userDetailRepository.save(user);
            // Call method to send  email
            emailService.sendMail(user);
            return RegisterResponse.builder()
                    .code(200)
                    .message("User created !")
                    .build();
        } catch (Exception e) {
            log.error("Error creating account");
            throw new ServerErrorException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Kích hoạt tài khoản người dùng bằng cách xác minh email.
     *
     * @param userName Email của người dùng cần xác minh.
     * @throws ResourceNotFoundEx Nếu không tìm thấy người dùng với email được cung cấp.
     */
    public void verifyUser(String userName) {
        UserEntity user = userDetailRepository.findByEmail(userName);
        if (user == null) {
            log.warn("Không tìm thấy người dùng với email: {}", userName);
            throw new ResourceNotFoundEx("User not found with email: " + userName);
        }
        user.setEnabled(true);
        userDetailRepository.save(user);
        log.info("Tài khoản với email {} đã được kích hoạt thành công", userName);
    }
}
