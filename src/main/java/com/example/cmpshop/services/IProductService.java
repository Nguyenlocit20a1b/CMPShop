package com.example.cmpshop.services;

import com.example.cmpshop.config.DeviceStatus;
import com.example.cmpshop.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing and retrieving products with various filtering options.
 */
public interface IProductService {
    /**
     * Filters products based on optional parameters including slug, brand ID, price range, and production year range,
     * with support for pagination.
     *
     * @param slug     The unique identifier or URL-friendly name for a specific product. If specified, filters products matching this slug.
     * @param brandId  The ID of the brand to filter by. If specified, only products belonging to this brand are returned.
     * @param minPrice The minimum price filter. If specified, only products priced at or above this value are included.
     * @param maxPrice The maximum price filter. If specified, only products priced at or below this value are included.
     * @param minYear  The minimum production year filter. If specified, only products produced from this year onward are included.
     * @param maxYear  The maximum production year filter. If specified, only products produced up to this year are included.
     * @param pageable Pagination and sorting information, specifying page number, page size, and sorting options.
     * @return Page<ProductDto> A paginated list of ProductDto objects that match the specified filters.
     */
    public Page<ProductDto> filterProducts(String slug, Long brandId, Double minPrice, Double maxPrice, Integer minYear, Integer maxYear, Pageable pageable);

    /**
     * Retrieves products filtered by category, category type, status, and address.
     *
     * @param categoryId     The ID of the category to filter products by. If specified, only products within this category are returned.
     * @param categoryTypeId The ID of the category type for further filtering within the category. If specified, products must match this type.
     * @param status         The status of the product (e.g., "available", "sold"). If specified, only products with this status are included.
     * @param address        The address or location filter. If specified, filters products based on this address.
     * @return List<ProductDto> A list of ProductDto objects that match the specified filtering criteria. If no products match, an empty list is returned.
     */
    public List<ProductDto> getProductByCategoryStatusAndAddress(Long categoryId, Long categoryTypeId, String status, String address);

}
