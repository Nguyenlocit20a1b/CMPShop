package com.example.cmpshop.auth.services.impl;

import com.example.cmpshop.auth.dto.request.PermissionRequest;
import com.example.cmpshop.auth.dto.request.RoleRequest;
import com.example.cmpshop.auth.dto.request.UpdateUserRolesRequest;
import com.example.cmpshop.auth.entities.*;
import com.example.cmpshop.auth.reponsitory.AuthorityRepository;
import com.example.cmpshop.auth.reponsitory.PermissionRepository;
import com.example.cmpshop.auth.reponsitory.UserDetailRepository;
import com.example.cmpshop.auth.services.IAuthorizationService;
import com.example.cmpshop.exceptions.ResourceNotFoundEx;
import com.example.cmpshop.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AuthorizationService implements IAuthorizationService {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserDetailRepository userDetailRepository;

    /**
     * Lấy quyền mặc định của người dùng mới (USER).
     *
     * @return Bộ tập hợp (Set) chứa quyền mặc định của người dùng.
     *  @throws ResourceNotFoundEx Nếu quyền không tồn tại.
     */
    public Set<RoleEntity> getUserAuthority() {
        Set<RoleEntity> authorities = new HashSet<>();
        Optional<RoleEntity> authority = authorityRepository.findByName(RoleEnum.USER.toString());
        if (authority.isPresent()) {
            authorities.add(authority.get());
        } else {
            log.error("Không tìm thấy quyền mặc định: USER");
            throw new ResourceNotFoundEx("Quyền USER không tồn tại");
        }
        return authorities;
    }

    /**
     * Tạo mới một quyền (Permission) trong hệ thống.
     *
     * @param request đối tượng PermissionRequest chứa name, endPoint , method
     * @return Đối tượng PermissionEntity đã được lưu trữ.
     * @throws UnauthorizedException Nếu quyền đã tồn tại.
     */
    public PermissionEntity createPermission(PermissionRequest request) {
        String permissionToSave = request.getName().toUpperCase();
        String methodToSave = request.getMethod().toUpperCase();

        if (permissionRepository.existsByName(permissionToSave)) {
            log.warn("Quyền đã tồn tại, không tạo thêm: {}", permissionToSave);
            throw new UnauthorizedException("Quyền này đã tồn tại");
        }
        // Kiểm tra method có hợp lệ không
        if (!HttpMethodEnum.contains(methodToSave)) {
            log.warn("Phương thức không hợp lệ: {}", methodToSave);
            throw new IllegalArgumentException("Method không hợp lệ. Chỉ chấp nhận GET, POST, PUT, DELETE.");
        }
        PermissionEntity permission = PermissionEntity.builder()
                .name(permissionToSave)
                .method(methodToSave)
                .endPoint(request.getEndPoint())
                .build();
        return permissionRepository.save(permission);
    }

    /**
     * Tạo mới một vai trò (Role) trong hệ thống, kèm theo danh sách quyền liên kết.
     *
     * @param roleRequest Đối tượng RoleRequest chứa tên vai trò, mô tả và danh sách quyền.
     * @return Vai trò (RoleEntity) đã được tạo và lưu trữ.
     * @throws UnauthorizedException    Nếu vai trò đã tồn tại.
     * @throws IllegalArgumentException Nếu một số quyền không tồn tại.
     */
    public RoleEntity createAuthority(RoleRequest roleRequest) {
        String roleToSave = roleRequest.getName().toUpperCase();
        if (authorityRepository.existsByName(roleToSave)) {
            log.warn("Role đã tồn tại, không tạo thêm: {}", roleToSave);
            throw new UnauthorizedException("Role này đã tồn tại");
        }
        Set<PermissionEntity> permissions = permissionRepository.findByNameIn(roleRequest.getPermissionNames());
        if (permissions.size() != roleRequest.getPermissionNames().size()) {
            throw new IllegalArgumentException("Một số quyền không tồn tại");
        }
        RoleEntity authority = RoleEntity.builder()
                .name(roleToSave)
                .description(roleRequest.getDescription())
                .permissions(permissions)
                .build();
        return authorityRepository.save(authority);
    }

    /**
     * Cập nhật vai trò (Role) cho người dùng.
     *
     * @param request Đối tượng UpdateUserRolesRequest chứa email của người dùng và danh sách vai trò.
     * @return Người dùng (UserDetails) đã được cập nhật.
     * @throws UsernameNotFoundException Nếu không tìm thấy người dùng.
     * @throws IllegalArgumentException  Nếu một số vai trò không tồn tại.
     */
    public UserDetails updateUserRoles(UpdateUserRolesRequest request) throws UsernameNotFoundException {
        Optional<UserEntity> user = userDetailRepository.findByPhoneNumber(request.getPhoneNumber());
        if (user.isEmpty()) {
            log.warn("Không tìm thấy người dùng với email: {}", request.getPhoneNumber());
            throw new UsernameNotFoundException("User Not Found with userName" + request.getPhoneNumber());
        }
        // Tìm các RoleEntities từ tên của role
        Set<RoleEntity> roles = authorityRepository.findByNameIn(request.getRoleNames());
        if (roles.size() != request.getRoleNames().size()) {
            throw new IllegalArgumentException("Một số role không tồn tại");
        }
        // Cập nhật vai trò cho người dùng
        user.get().setAuthorities(roles);
        UserEntity updatedUser = userDetailRepository.save(user.get());
        return updatedUser;
    }

}
