package com.example.cmpshop.auth.services.impl;

import com.example.cmpshop.auth.entities.UserEntity;
import com.example.cmpshop.auth.reponsitory.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserDetailRepository userDetailRepository;
    /**
     * Tìm người dùng theo email và trả về thông tin người dùng nếu tồn tại.
     * Phương thức này được sử dụng để xác thực người dùng trong Spring Security khi đăng nhập.
     *
     * @param phoneNumber Email của người dùng muốn đăng nhập
     * @return UserDetails đối tượng chứa thông tin người dùng đã xác thực (trả về đối tượng UserEntity)
     * @throws UsernameNotFoundException nếu không tìm thấy người dùng có email này trong hệ thống
     */
    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<UserEntity> user = userDetailRepository.findByPhoneNumber(phoneNumber);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found with userName" + phoneNumber);
        }
        return user.get();
    }

}
