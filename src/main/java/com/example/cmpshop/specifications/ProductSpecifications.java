package com.example.cmpshop.specifications;

import com.example.cmpshop.entities.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * ProductSpecifications is a utility class designed to define reusable and composable specifications
 * for querying ProductEntity objects with specific criteria.
 * This class typically works with Spring Data JPA's Specification API, allowing for dynamic query construction
 * based on various filters, such as product name, category, price range, etc.
 **/
public class ProductSpecifications {
    /**
     * filter products with brandId
     */
    public static Specification<ProductEntity> hasBrandId(Long brandId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand").get("id"), brandId));
    }

    /**
     * search products with slug
     */
    public static Specification<ProductEntity> hasSlug(String slug) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get("slug")), "%" + slug.toLowerCase() + "%")));
    }

    /**
     * filter products with price
     */
    public static Specification<ProductEntity> hasPriceBetween(Double minPrice, Double maxPrice) {
        return ((root, query, criteriaBuilder) -> {
            Predicate greaterThanOrEqualToMin = criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            Predicate lessThanOrEqualToMax = criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            return criteriaBuilder.and(greaterThanOrEqualToMin, lessThanOrEqualToMax);
        });
    }

    /**
     * filter products with year Of Manufacture
     */
    public static Specification<ProductEntity> hasYearOfManufacture(int minYear, int maxYear) {
        return ((root, query, criteriaBuilder) -> {
            Predicate greaterThanOrEqualToMin = criteriaBuilder.greaterThanOrEqualTo(root.get("yearOfManufacture"), minYear);
            Predicate lessThanOrEqualToMax = criteriaBuilder.lessThanOrEqualTo(root.get("yearOfManufacture"), maxYear);
            return criteriaBuilder.and(greaterThanOrEqualToMin, lessThanOrEqualToMax);
        });
    }

}
