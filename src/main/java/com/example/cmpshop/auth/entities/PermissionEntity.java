package com.example.cmpshop.auth.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;

/**
 * Entity class đại diện cho bảng "Permission" trong cơ sở dữ liệu.
 * Sử dụng JPA để ánh xạ dữ liệu và Lombok để giảm boilerplate code.
 *
 * - @Table: Định nghĩa bảng cơ sở dữ liệu.
 * - @Entity: Đánh dấu class là một entity của JPA.
 * - @Data: Sinh các phương thức getter, setter, toString, equals, hashCode.
 * - @NoArgsConstructor: Tạo constructor không tham số.
 * - @AllArgsConstructor: Tạo constructor đầy đủ tham số.
 * - @Builder: Hỗ trợ xây dựng đối tượng bằng Builder Pattern.
 */
@Table(name = "Permission")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionEntity {
    @Id
    @GeneratedValue
    private  Long id ;
    @Column(nullable = false, unique = true)
    private String name;
    private String method;
    private String endPoint;
}
