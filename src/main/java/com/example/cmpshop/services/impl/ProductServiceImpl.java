package com.example.cmpshop.services;

import com.example.cmpshop.config.AppConfig;
import com.example.cmpshop.exceptions.InvalidParameterException;
import com.example.cmpshop.exceptions.InvalidRangeException;
import com.example.cmpshop.specifications.ProductSpecifications;
import com.example.cmpshop.dto.ProductDto;
import com.example.cmpshop.entities.ProductEntity;
import com.example.cmpshop.exceptions.ResourceNotFoundEx;
import com.example.cmpshop.mapper.ProductMapper;
import com.example.cmpshop.repositories.ProductRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    private int minYear = AppConfig.MIN_YEAR;
    private int currentYear = AppConfig.CURRENT_YEAR;

    private double minPriceLimit = AppConfig.MIN_PRICE_LIMIT;

    private double maxPriceLimit = AppConfig.MAX_PRICE_LIMIT;
    // lọc sản  phẩm với các bộ Specification, phân trang và sắp xếp
    @Override
    public Page<ProductDto> filterProducts(String slug, Long brandId, Double minPrice, Double maxPrice, Integer minYear, Integer maxYear, Pageable pageable) {
        //check invalid and range for param
        checkNoParamsForBasicSearch(slug, brandId, minPrice, maxPrice, minYear, maxYear);
        // Truy vấn động với Specification
        Specification<ProductEntity> productSpecification = Specification.where(null);
        if(slug == null){
            throw new ResourceNotFoundEx("No products found with the given keyword !");
        }
        if (null != slug) {
            productSpecification = productSpecification.and(ProductSpecifications.hasSlug(slug));
        }
        if (null != brandId) {
            productSpecification = productSpecification.and(ProductSpecifications.hasBrandId(brandId));
        }
        if (null != minPrice && null != maxPrice) {
            productSpecification = productSpecification.and(ProductSpecifications.hasPriceBetween(minPrice, maxPrice));
        }
        if (null != minYear && null != maxYear) {
            productSpecification = productSpecification.and(ProductSpecifications.hasYearOfManufacture(minYear, maxYear));
        }
        Page<ProductEntity> products = productRepository.findAll(productSpecification, pageable);

        if (products.isEmpty()) {
            throw new ResourceNotFoundEx("No products found with the given keyword !");
        }
        return products.map(productMapper::mapToProductDTO);
    }

    @Override
    public List<ProductDto> getProductByCategoryStatusAndAddress(Long categoryId, Long categoryTypeId, Boolean status, String address) {
        //check param
        checkNoParamsForAdvancedSearch(categoryId, categoryTypeId, status, address);
        List<ProductEntity> products = productRepository.searchProductsCategoryStatusAddress(categoryId, categoryTypeId, status, address);
        if (products == null || products.isEmpty()) {
            throw new ResourceNotFoundEx("No products found with the given keyword !");
        }
        return productMapper.getProductDtos(products);
    }

    private void checkNoParamsForAdvancedSearch(Long categoryId, Long categoryTypeId, Boolean status, String address) {
        if (categoryId == null && categoryTypeId == null && status == null && StringUtils.isBlank(address)) {
            throw new InvalidParameterException("At least one search parameter must be provided.");
        }
    }

    private void checkNoParamsForBasicSearch(String slug, Long brandId, Double minPrice, Double maxPrice, Integer minYear, Integer maxYear) {
        if (StringUtils.isBlank(slug) && brandId == null && minPrice == null && maxPrice == null && minYear == null && maxYear == null) {
            throw new InvalidParameterException("At least one search parameter must be provided.");
        }
        // Kiểm tra khoảng năm
        if ((minYear != null && (minYear < minYear || minYear > currentYear)) ||
                (maxYear != null && (maxYear < minYear || maxYear > currentYear))) {
            throw new InvalidRangeException("Năm sản xuất phải nằm trong khoảng từ" + minYear + " đến " + currentYear);
        }
        // Kiểm tra khoảng giá`
        if ((minPrice != null && (minPrice < minPriceLimit || minPrice > maxPriceLimit)) ||
                (maxPrice != null && (maxPrice < minPriceLimit || maxPrice > maxPriceLimit))) {
            throw new InvalidRangeException("Giá phải nằm trong khoảng từ 0 triệu đến 50 tỷ đồng.");
        }
    }


}
