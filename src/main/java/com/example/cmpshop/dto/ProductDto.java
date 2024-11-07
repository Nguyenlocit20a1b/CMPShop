package com.example.cmpshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private String address;
    private String model;
    private String serial;
    private Boolean status;
    private Boolean isActive;
    private int usageTime; // thời gian sử dụng
    private String yearOfManufacture; // năm sản xuất
    private Long brandId;
    private String origin; // xuất xứ
    private String brandName;
    private Long categoryId;
    private String categoryName;// loại
    private Long categoryTypeId;
    private String categoryTypeName;// phân loại
}
