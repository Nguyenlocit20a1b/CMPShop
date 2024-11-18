package com.example.cmpshop.auth.reponsitory;
import com.example.cmpshop.auth.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;
/**
 * AuthorityRepository dùng để tương tác với cơ sở dữ liệu với bảng {@link RoleEntity}
 * Định nghĩa các phương thức để tìm kiếm và xử lý các đối tượng Role
 *
 */
@Repository
public interface AuthorityRepository extends JpaRepository<RoleEntity, Long> {
    /**
     * Tìm một RoleEntity theo tên của Role.
     *
     * @param user tên của role (dạng Enum) cần tìm kiếm.
     * @return Optional<RoleEntity> chứa RoleEntity nếu tìm thấy, hoặc Optional.empty() nếu không tìm thấy.
     */
    Optional<RoleEntity> findByName(Enum user);

    /**
     * Kiểm tra xem một role có tồn tại trong cơ sở dữ liệu hay không dựa trên tên role.
     *
     * @param name tên của role cần kiểm tra.
     * @return true nếu role tồn tại trong cơ sở dữ liệu, false nếu không tồn tại.
     */
    boolean existsByName(String name);

    /**
     * Tìm các RoleEntity từ một tập hợp các tên role.
     *
     * @param roleNames tập hợp các tên role cần tìm.
     * @return một Set<RoleEntity> chứa các RoleEntity phù hợp với các tên role trong danh sách.
     */
    Set<RoleEntity> findByNameIn(Set<String> roleNames);
}
