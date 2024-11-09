package com.example.cmpshop.dto;

import com.example.cmpshop.config.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**The ProductDto class is a Data Transfer Object (DTO) that is used to transfer data between
 * the service layer and the controller or between different layers of the application.
 * DTOs are typically used to encapsulate data and avoid exposing entity objects directly (like ProductEntity).
 * This helps with encapsulation and abstraction, ensuring that the data transferred is only the necessary fields.**/
public class ProductDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private String address;
    private String model;
    private String serial;
    private DeviceStatus status;
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
