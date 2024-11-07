package com.example.cmpshop.services;

import com.example.cmpshop.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IProductService {
    Page<ProductDto> filterProducts(String slug, Long brandId, Double minPrice, Double maxPrice, Integer minYear, Integer maxYear, Pageable pageable);
    List<ProductDto> getProductByCategoryStatusAndAddress(Long categoryId, Long categoryTypeId, Boolean status,String address);

}
