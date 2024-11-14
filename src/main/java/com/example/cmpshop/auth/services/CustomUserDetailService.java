package com.example.cmpshop.auth.services;

import com.example.cmpshop.auth.entities.UserEntity;
import com.example.cmpshop.auth.reponsitory.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userDetailRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("User Not Found with userName" + username);
        }
        return user;
    }

}
