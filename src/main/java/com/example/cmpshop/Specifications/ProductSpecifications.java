package com.example.cmpshop.Specifications;

import com.example.cmpshop.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProductSpecifications {
    // lọc các product có categoryId và hasCategoryTypeId
    public static Specification<Product> hasCategoryId(Long categoryId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"),categoryId));
    }
    public static  Specification<Product> hasCategoryTypeId(Long typeId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoryType").get("id"),typeId));
    }
    public static  Specification<Product> hasBrandId(Long brandId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brands").get("id"),brandId));
    }
}
