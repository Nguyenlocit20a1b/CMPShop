package com.example.cmpshop.auth.reponsitory;

import com.example.cmpshop.auth.entities.Authority;
import com.example.cmpshop.auth.entities.RoleTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(Enum user);
}
