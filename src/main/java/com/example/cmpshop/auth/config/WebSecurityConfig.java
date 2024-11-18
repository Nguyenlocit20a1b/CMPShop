package com.example.cmpshop.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    // danh sach url public
    private final String [] publicApis = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api/public/**",
            "/api/auth/**",
    };
    /**
     * Cấu hình bảo mật cho ứng dụng, xác định cách thức xử lý các yêu cầu HTTP.
     * - Tắt CSRF.
     * - Cấu hình quyền truy cập cho từng URL.
     * - Cấu hình chính sách session.
     * - Thêm bộ lọc JWT để xác thực người dùng.
     *
     * @param http Đối tượng HttpSecurity để cấu hình bảo mật.
     * @return SecurityFilterChain đối tượng cấu hình bảo mật hoàn chỉnh.
     * @throws Exception nếu có lỗi khi cấu hình bảo mật.
     */
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authenticationManager(authenticationManager())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/user/**").hasAnyAuthority("USER_READ_PRIVILEGE","ADMIN_READ_PRIVILEGE")
                        .requestMatchers(HttpMethod.GET, "/api/admin/**").hasAnyAuthority("ADMIN_READ_PRIVILEGE")
                        .requestMatchers(HttpMethod.POST, "/api/admin/**").hasAnyAuthority("ADMIN_WRITE_PRIVILEGE")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasAnyAuthority("ADMIN_WRITE_PRIVILEGE")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasAnyAuthority("ADMIN_DELETE_PRIVILEGE")
                        .anyRequest().authenticated())
                        .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                        .addFilterBefore(new JWTAuthenticationFilter(jwtTokenHelper , userDetailsService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    /**
     * Cấu hình WebSecurity để bỏ qua bảo mật cho các API công khai.
     *
     * @return WebSecurityCustomizer cho phép bỏ qua bảo mật cho các API công khai.
     */
    @Bean
    public WebSecurityCustomizer webSecurityConfigurer() {
        return (web) -> web.ignoring().requestMatchers(publicApis);
    }
    /**
     * Cấu hình AuthenticationManager, sử dụng DaoAuthenticationProvider để xác thực người dùng.
     *
     * @return AuthenticationManager để xác thực người dùng.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);
    }
    /**
     * Tạo một PasswordEncoder để mã hóa mật khẩu người dùng.
     *
     * @return PasswordEncoder sử dụng mã hóa mật khẩu.
     */
    @Bean
    public PasswordEncoder passwordEncoder () {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
