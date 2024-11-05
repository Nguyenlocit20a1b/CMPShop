package com.example.cmpshop.services;

import com.example.cmpshop.dto.ProductDto;

import java.util.List;

public interface IProductService {

    public List<ProductDto> getAllProduct(Long categoryId , Long typeId, Long brandId);
    public List<ProductDto> getProductsBySlugKeyword (String slug);

}
