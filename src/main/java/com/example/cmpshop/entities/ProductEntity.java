package com.example.cmpshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;// name product
    @Column(nullable = false, unique = true)
    private String slug;// slug product
    @Column
    private String description; // mô tả
    @Column(nullable = false)
    private BigDecimal price; // giá  máy
    @Column(nullable = false)
    private String address;// địa chỉ máy
    private String yearOfManufacture; // năm sản xuất
    private int usageTime;  // thời gian sử dụng
    private String model; // model máy
    private String serial; // serial máy
    @Column(nullable = false)
    private Boolean status; // trạng thái thiết bị
    @Column(nullable = false)
    private Boolean isActive; // trạng thái tin
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category; // Join với bảng Category n-> 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryType_id", nullable = false)
    @JsonIgnore
    private CategoryType categoryType; // Join với bảng CategoryType  n-> 1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @JsonIgnore
    private Brand brand; // Join với bảng brand  n-> 1
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
}
