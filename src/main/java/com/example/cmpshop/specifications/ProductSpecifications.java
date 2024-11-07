package com.example.cmpshop.specifications;

import com.example.cmpshop.entities.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


public class ProductSpecifications {
    // lọc các product có brandId và slug khoảng giá và năm sản xuất
    public static Specification<Product> hasBrandId(Long brandId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand").get("id"), brandId));
    }

    // tìm kiếm gần đúng với slug
    public static Specification<Product> hasSlug(String slug) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), "%" + slug.toLowerCase() + "%")));

    }
    public static Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice) {
        return ((root, query, criteriaBuilder) -> {
            Predicate greaterThanOrEqualToMin = criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            Predicate lessThanOrEqualToMax = criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            return criteriaBuilder.and(greaterThanOrEqualToMin, lessThanOrEqualToMax);
        });
    }
    public static Specification<Product> hasYearOfManufacture(int minYear, int maxYear) {
        return ((root, query, criteriaBuilder) -> {
            Predicate greaterThanOrEqualToMin = criteriaBuilder.greaterThanOrEqualTo(root.get("yearOfManufacture"), minYear);
            Predicate lessThanOrEqualToMax = criteriaBuilder.lessThanOrEqualTo(root.get("yearOfManufacture"), maxYear);
            return criteriaBuilder.and(greaterThanOrEqualToMin, lessThanOrEqualToMax);
        });
    }

}
