package com.example.cmpshop.mapper;

import com.example.cmpshop.dtos.ProductDto;
import com.example.cmpshop.entities.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This allows ProductMapper to be autowired into other Spring components, such as service classes.
 *Typically, a mapper class is used to transform data  {@link ProductDto} and data models {@link ProductEntity} in the application.
 **/
@Component
public class ProductMapper {
    // get danh sách product DTO dưới dạng list
    public List<ProductDto> getProductDtos(List<ProductEntity> products) {
        return products.stream().map(this::mapToProductDTO).toList();
    }
    //  using builder to map productDTO
    public ProductDto mapToProductDTO(ProductEntity product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .address(product.getAddress())
                .usageTime(product.getUsageTime())
                .model(product.getModel())
                .serial(product.getSerial())
                .status(product.getStatus())
                .isActive(product.getIsActive())
                .price(product.getPrice())
                .yearOfManufacture(product.getYearOfManufacture())
                // Thêm các thuộc tính của Category
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                // Thêm các thuộc tính của CategoryType
                .categoryTypeId(product.getCategoryType().getId())
                .categoryTypeName(product.getCategoryType().getName())
                // Thêm các thuộc tính của Brand
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())
                .origin(product.getBrand().getOrigin())
                .build();
    }
}
