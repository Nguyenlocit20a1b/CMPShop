package com.example.cmpshop.auth.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import java.util.Set;

/**
 * Entity class đại diện cho bảng "Authority" trong cơ sở dữ liệu.
 * Sử dụng JPA để ánh xạ dữ liệu và Lombok để giảm boilerplate code.
 *
 * - @Table: Định nghĩa bảng cơ sở dữ liệu.
 * - @Entity: Đánh dấu class là một entity của JPA.
 * - @Data: Sinh các phương thức getter, setter, toString, equals, hashCode.
 * - @NoArgsConstructor: Tạo constructor không tham số.
 * - @AllArgsConstructor: Tạo constructor đầy đủ tham số.
 * - @Builder: Hỗ trợ xây dựng đối tượng bằng Builder Pattern.
 *
 * Class này cũng triển khai giao diện {@link GrantedAuthority} để tích hợp với Spring Security.
 * đại diện cho các quyền (authorities) được gán cho người dùng.
 */
@Table(name = "Authority")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissions ;
}
