package com.example.cmpshop.auth.services;

import com.example.cmpshop.auth.dto.UserToken;
import com.example.cmpshop.auth.entities.Authority;
import com.example.cmpshop.auth.entities.RoleTypes;
import com.example.cmpshop.auth.entities.UserEntity;
import com.example.cmpshop.auth.reponsitory.AuthorityRepository;
import com.example.cmpshop.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AuthorityService {
    private static final Logger log = LoggerFactory.getLogger(AuthorityService.class);
    @Autowired
    private AuthorityRepository authorityRepository;
    public List<Authority> getUserAuthority() {
        List<Authority> authorities = new ArrayList<>();
        Authority authority = authorityRepository.findByName(RoleTypes.USER);
        authorities.add(authority);
        return authorities;
    }
    public List<Authority> getAdminAuthority() {
        List<Authority> authorities = new ArrayList<>();
        Authority authority = authorityRepository.findByName(RoleTypes.ADMIN);
        authorities.add(authority);
        return authorities;
    }
    public Authority createAuthority(String role,String description) {
        if(RoleTypes.contains(role)) {
            log.warn("Quyền đã tồn tại, không tạo thêm: {}", role);
            throw new UnauthorizedException("Quyền này đã tồn tại");
        }
        Authority authority = Authority.builder()
                .name(RoleTypes.valueOf(role))
                .description(description)
                .build();
        return authorityRepository.save(authority);
    }

}
