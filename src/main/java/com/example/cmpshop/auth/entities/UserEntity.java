package com.example.cmpshop.auth.entities;

import com.example.cmpshop.convertor.StringCryptoConverter;
import com.example.cmpshop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * Entity class đại diện cho bảng "User" trong cơ sở dữ liệu.
 * Sử dụng JPA để ánh xạ dữ liệu và Lombok để giảm boilerplate code.
 * <p>
 * - @Table: Định nghĩa bảng cơ sở dữ liệu.
 * - @Entity: Đánh dấu class là một entity của JPA.
 * - @Data: Sinh các phương thức getter, setter, toString, equals, hashCode.
 * - @NoArgsConstructor: Tạo constructor không tham số.
 * - @AllArgsConstructor: Tạo constructor đầy đủ tham số.
 * - @Builder: Hỗ trợ xây dựng đối tượng bằng Builder Pattern.
 * <p>
 *    Note: Đảm bảo rằng `StringCryptoConverter` được cấu hình để xử lý mã hóa và giải mã
 *  cho các trường phoneNumber và email.
 * Class này cũng triển khai giao diện {@link UserDetails} để tích hợp với Spring Security.
 */
@Table(name = "User")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;

    @Column(length = 512 , nullable = false, unique = true)
    private String email; // Encrypted email
    @Column(length =  512  , name = "email_signature")
    private String emailSignature; // Signature for email verification

    /** Trường số điện thoại di động được chú thích bằng
    @Convert để sử dụng StringCryptoConverter để mã hóa và giải mã dữ liệu. */
    @Convert(converter = StringCryptoConverter.class)
    private String phoneNumber;
    @Column(name = "password", length = 255)
    @JsonIgnore
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderEnum provider;
    private String verificationCode;
    private boolean enabled = false;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "AUTH_USER_AUTHORITY", joinColumns = @JoinColumn(referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
    private Set<RoleEntity> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String prefixRole = "ROLE_";
        Set<GrantedAuthority> authorities = new HashSet<>();
        // Add roles as authorities
        for (RoleEntity role : this.authorities) {
            if (role.getName() != null) {
                authorities.add(new SimpleGrantedAuthority(prefixRole + role.getName().toUpperCase()));
            }
            // Add permissions for each role
            if (role.getPermissions() != null) {
                for (PermissionEntity permission : role.getPermissions()) {
                    if (permission.getName() != null) {
                        authorities.add(new SimpleGrantedAuthority(permission.getName()));
                    }
                }
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
