package com.example.cmpshop.auth.reponsitory;

import com.example.cmpshop.auth.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
@Repository
public interface UserDetailRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmail(String username);
}
