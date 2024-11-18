package com.example.cmpshop.auth.services;

import com.example.cmpshop.auth.dto.request.RoleRequest;
import com.example.cmpshop.auth.dto.request.UpdateUserRolesRequest;
import com.example.cmpshop.auth.entities.PermissionEntity;
import com.example.cmpshop.auth.entities.RoleEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
/**
 * Service interface hoặc lớp xử lý logic liên quan đến quyền (Role) và phân quyền (Permission)
 * của người dùng.
 */
public interface IAuthorizationService {
    /**
     * Lấy danh sách các quyền (roles) của người dùng hiện tại.
     *
     * @return Một tập hợp (Set) các RoleEntity đại diện cho các quyền của người dùng.
     */
    Set<RoleEntity> getUserAuthority();
    /**
     * Tạo một permission mới trong hệ thống.
     *
     * @param name Tên của permission (phải là duy nhất).
     * @param description Mô tả ngắn gọn về permission.
     * @return Một đối tượng PermissionEntity đại diện cho permission đã được tạo.
     */
    PermissionEntity createPermission(String name, String description);
    /**
     * Tạo một role mới trong hệ thống và gắn kèm các permission cho role đó.
     *
     * @param roleRequest Đối tượng {@link RoleRequest} chứa thông tin email người dùng và danh sách permission mới.
     * @return Một đối tượng RoleEntity đại diện cho role đã được tạo.
     */
    RoleEntity createAuthority(RoleRequest roleRequest);
    /**
     * Cập nhật danh sách các roles cho một người dùng cụ thể.
     *
     * @param updateUserRolesRequest Đối tượng {@link UpdateUserRolesRequest} chứa thông tin email người dùng và danh sách roles mới.
     * @return Một đối tượng UserDetails đại diện cho thông tin người dùng sau khi cập nhật.
     * @throws UsernameNotFoundException Nếu không tìm thấy người dùng với email được cung cấp.
     */

    UserDetails updateUserRoles(UpdateUserRolesRequest updateUserRolesRequest) throws UsernameNotFoundException;
}
