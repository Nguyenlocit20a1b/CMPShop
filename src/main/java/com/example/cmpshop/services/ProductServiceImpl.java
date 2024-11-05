package com.example.cmpshop.services;

import com.example.cmpshop.Specifications.ProductSpecifications;
import com.example.cmpshop.dto.ProductDto;
import com.example.cmpshop.entities.Product;
import com.example.cmpshop.exceptions.ResourceNotFoundEx;
import com.example.cmpshop.mapper.ProductMapper;
import com.example.cmpshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<ProductDto> getAllProduct(Long categoryId, Long typeId , Long brandId) {
        Specification<Product> productSpecification = Specification.where(null);
        if(null != categoryId) {
            productSpecification = productSpecification.and(ProductSpecifications.hasCategoryId(categoryId));
        }
        if(null != typeId) {
            productSpecification = productSpecification.and(ProductSpecifications.hasCategoryTypeId(typeId));
        }
        if(null != brandId) {
            productSpecification = productSpecification.and(ProductSpecifications.hasCategoryTypeId(typeId));
        }
        List<Product> products = productRepository.findAll(productSpecification);
        return productMapper.getProductDtos(products);
    }
    @Override
    public List<ProductDto> getProductsBySlugKeyword(String slug) {
        List <Product> products = productRepository.findBySlugContaining(slug);
        if (products.isEmpty()) {
            throw  new ResourceNotFoundEx("No products found with the given keyword !");
        }
        return productMapper.getProductDtos(products);
    }
}
