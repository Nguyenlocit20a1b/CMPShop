package com.example.cmpshop.services.impl;

import com.example.cmpshop.config.AppConfig;
import com.example.cmpshop.config.DeviceStatus;
import com.example.cmpshop.exceptions.InvalidParameterException;
import com.example.cmpshop.exceptions.InvalidRangeException;
import com.example.cmpshop.services.IProductService;
import com.example.cmpshop.specifications.ProductSpecifications;
import com.example.cmpshop.dtos.ProductDto;
import com.example.cmpshop.entities.ProductEntity;
import com.example.cmpshop.exceptions.ResourceNotFoundEx;
import com.example.cmpshop.mapper.ProductMapper;
import com.example.cmpshop.repositories.ProductRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import org.slf4j.Logger;

@Service
@Transactional
/** The ProductServiceImpl class implements the IProductService interface {@link IProductService}
 which likely defines the business logic for handling product-related operations. */
public class ProductServiceImpl implements IProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    private int minimumYear = AppConfig.MIN_YEAR;
    private int currentYear = AppConfig.CURRENT_YEAR;

    private double minPriceLimit = AppConfig.MIN_PRICE_LIMIT;

    private double maxPriceLimit = AppConfig.MAX_PRICE_LIMIT;
    // lọc sản  phẩm với các bộ Specification, phân trang và sắp xếp

    /**
     * Filters products based on optional criteria such as slug, brand ID, price range, and production year range,
     * with support for pagination and sorting.
     *
     * @param slug     Optional unique identifier or URL-friendly name for a specific product. Filter results to match this slug if provided.
     * @param brandId  Optional brand ID to filter products by. Only products from this brand will be included if specified.
     * @param minPrice Optional minimum price filter. Products priced at or above this value are included.
     * @param maxPrice Optional maximum price filter. Products priced at or below this value are included.
     * @param minYear  Optional minimum production year. Only products produced in this year or later are included.
     * @param maxYear  Optional maximum production year. Only products produced in this year or earlier are included.
     * @param pageable Pagination and sorting information for the query, specifying page number, size, and sorting direction.
     * @return Page<ProductDto> A paginated list of ProductDto objects that match the specified filters and pagination criteria.
     * If no products match the filters, an empty page is returned.
     */
    @Override
    public Page<ProductDto> filterProducts(String slug, Long brandId, Double minPrice, Double maxPrice, Integer minYear, Integer maxYear, Pageable pageable) {
        //check invalid and range for param
        checkNoParamsForBasicSearch(slug, brandId, minPrice, maxPrice, minYear, maxYear);
        if (slug == null) {
            logger.warn("The system doesn't know what product you are looking for, please provide slug!");
            throw new ResourceNotFoundEx("No products found with the given keyword !");
        }
        // Khởi tạo Specification và tìm kiếm
        Specification<ProductEntity> productSpecification = ProductSpecifications.hasSlug(slug)
                .and(brandId != null ? ProductSpecifications.hasBrandId(brandId) : null)
                .and(minPrice != null && maxPrice != null ? ProductSpecifications.hasPriceBetween(minPrice, maxPrice) : null)
                .and(minYear != null && maxYear != null ? ProductSpecifications.hasYearOfManufacture(minYear, maxYear) : null);

        Page<ProductEntity> products = productRepository.findAll(productSpecification, pageable);
        // Kiểm tra kết quả và ném ngoại lệ nếu không tìm thấy sản phẩm
        if (products.isEmpty()) {
            logger.info("The product you are looking for does not exist.!");
            throw new ResourceNotFoundEx("No products found with the given keyword !");
        }
        return products.map(productMapper::mapToProductDTO);
    }

    /**
     * Retrieves a list of products filtered by category, category type, status, and address.
     *
     * @param categoryId     The ID of the category to filter products by. Only products within this category are returned if specified.
     * @param categoryTypeId The ID of the category type to further filter products within the specified category.
     *                       Only products matching this category type are included if specified.
     * @param status         The status of the product (e.g., "available", "sold"). Only products with this status are returned if provided.
     * @param address        The address or location filter. Products will be filtered based on this address if specified.
     * @return List<ProductDto> A list of ProductDto objects that match the specified filtering criteria. If no products match
     * the criteria, an empty list is returned.
     */
    @Override
    public List<ProductDto> getProductByCategoryStatusAndAddress(Long categoryId, Long categoryTypeId, String status, String address) {
        String deviceStatus = null;
        if (null != status) {
            try {
                deviceStatus = DeviceStatus.valueOf(status.toUpperCase()).name();
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid status provided: {}. Setting deviceStatus to null.", status, e);
                deviceStatus = null;
            }
        }
        //check param
        checkNoParamsForAdvancedSearch(categoryId, categoryTypeId, deviceStatus, address);
        List<ProductEntity> products = productRepository.searchProductsCategoryStatusAddress(categoryId, categoryTypeId, deviceStatus, address);
        if (products == null || products.isEmpty()) {
            throw new ResourceNotFoundEx("No products found with the given keyword !");
        }
        return productMapper.getProductDtos(products);
    }

    private void checkNoParamsForAdvancedSearch(Long categoryId, Long categoryTypeId, String status, String address) {
        if (categoryId == null && categoryTypeId == null && status == null && StringUtils.isBlank(address)) {
            logger.warn("No valid parameters provided for advanced search. At least one parameter must be specified.");
            throw new InvalidParameterException("At least one search parameter must be provided.");
        }
    }

    private void checkNoParamsForBasicSearch(String slug, Long brandId, Double minPrice, Double maxPrice, Integer minYear, Integer maxYear) {
        if (StringUtils.isBlank(slug) && brandId == null && minPrice == null && maxPrice == null && minYear == null && maxYear == null) {
            logger.warn("No valid parameters provided for basic search. At least one parameter must be specified");
            throw new InvalidParameterException("At least one search parameter must be provided.");
        }
        // check range year
        if ((minYear != null && (minYear < minimumYear || minYear > currentYear)) ||
                (maxYear != null && (maxYear < minimumYear || maxYear > currentYear))) {
            logger.warn("Kiểm tra năm sản xuất của sản phẩm đang không hợp lí");
            throw new InvalidRangeException("Năm sản xuất phải nằm trong khoảng từ" + minimumYear + " đến " + currentYear);
        }
        // check range price
        if ((minPrice != null && (minPrice < minPriceLimit || minPrice > maxPriceLimit)) ||
                (maxPrice != null && (maxPrice < minPriceLimit || maxPrice > maxPriceLimit))) {
            logger.warn("Kiểm tra về khoảng giá của sản phẩm");
            throw new InvalidRangeException("Giá phải nằm trong khoảng từ 0 triệu đến 50 tỷ đồng.");
        }
    }
}
