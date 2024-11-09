package com.example.cmpshop.entities;

import com.example.cmpshop.config.AppConfig;
import com.example.cmpshop.config.DeviceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Entity representing a Product in the system, extends {@link BaseEntity}
 * Maps to the 'product' table in the database.
 * This class utilizes Lombok annotations for boilerplate code reduction
 * and JPA annotations to define how it should be persisted.
 */
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity  extends BaseEntity{
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    @Column(nullable = false)
    private String name;// name product
    @NotBlank(message = "Product slug cannot be blank")
    @Size(max = 255, message = "Product slug must not exceed 255 characters")
    @Column(nullable = false, unique = true)
    private String slug;// slug product
    @Column
    private String description; // mô tả
    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") // Giá phải lớn hơn 0
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount with up to 10 digits and 2 decimal places") // Định dạng số
    private BigDecimal price; // giá  máy
    @Column(nullable = false)
    private String address;// địa chỉ máy
    @Min(value = AppConfig.MIN_YEAR, message = "Year of manufacture must be no earlier than" + AppConfig.MIN_YEAR)
    @Max(value = 2024 , message = "Year of manufacture must be no later than 2024")
    private String yearOfManufacture; // năm sản xuất
    private int usageTime;  // thời gian sử dụng
    private String model; // model máy
    private String serial; // serial máy
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeviceStatus status; // trạng thái thiết bị
    @Column(nullable = false)
    private Boolean isActive; // trạng thái tin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private CategoryEntity category; // Join với bảng Category n-> 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryType_id", nullable = false)
    @JsonIgnore
    private CategoryTypeEntity categoryType; // Join với bảng CategoryType  n-> 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @JsonIgnore
    private BrandEntity brand; // Join với bảng brand  n-> 1
}
