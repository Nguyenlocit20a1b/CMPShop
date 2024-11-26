package com.example.cmpshop.auth.reponsitory;

import com.example.cmpshop.auth.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/** Định nghĩa các phương thức để tìm kiếm và xử lý các đối tượngUserEntity trong cơ sở dữ liệu.
 *
  */
@Repository
public interface UserDetailRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Tìm kiếm một UserEntity dựa trên email của người dùng.
     * Phương thức này trả về một đối tượng UserEntity nếu tìm thấy người dùng với email tương ứng,
     * nếu không tìm thấy sẽ trả về null.
     *
     * @param phoneNumber sđt của người dùng cần tìm kiếm.
     * @return một đối tượng UserEntity nếu tìm thấy, null nếu không tìm thấy.
     */
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}
