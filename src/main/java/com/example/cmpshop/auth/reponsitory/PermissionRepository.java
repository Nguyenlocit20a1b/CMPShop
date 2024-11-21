package com.example.cmpshop.auth.reponsitory;
import com.example.cmpshop.auth.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
/**
 * PermissionRepository để tương tác với cơ sở dữ liệu cho bảng PermissionEntity
 *
 */
@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    /**
     * Kiểm tra xem một permission có tồn tại trong cơ sở dữ liệu hay không dựa trên tên permission.
     *
     * @param name tên của permission cần kiểm tra.
     * @return true nếu permission tồn tại trong cơ sở dữ liệu, false nếu không tồn tại.
     */
    boolean existsByName(String name);

    /**
     * Tìm các Permission từ một tập hợp các tên permission.
     *
     * @param permissionNames tập hợp các tên permission cần tìm.
     * @return một Set<PermissionEntity> chứa các PermissionEntity phù hợp với các tên permission trong danh sách.
     */
    Set<PermissionEntity> findByNameIn(Set<String> permissionNames);
    /**
     * Tìm tất cả các Permission
     *
     * @return một List<PermissionEntity> chứa các PermissionEntity  trong danh sách.
     */
    List<PermissionEntity> findAll();
}
