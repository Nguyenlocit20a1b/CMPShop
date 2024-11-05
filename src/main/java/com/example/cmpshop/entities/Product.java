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
    private String name;
    @Column(nullable = false,unique = true)
    private String slug;
    @Column
    private String description;
    @Column(nullable = false)
    private BigDecimal price ;
    @Column(nullable = false)
    private String address ;
    private String yearOfManufacture ;
    private int usageTime;
    private String model;
    private String serial;
    @Column(nullable = false)
    private Boolean status; // trạng thái thiết bị
    @Column(nullable = false)
    private Boolean isActive; // trạng thái thiết bị
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryType_id", nullable = false)
    @JsonIgnore
    private CategoryType categoryType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @JsonIgnore
    private Brand brand;
    @Column(nullable = false , updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
}
