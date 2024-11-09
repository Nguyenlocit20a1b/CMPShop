package com.example.cmpshop.controllers;


import com.example.cmpshop.config.DeviceStatus;
import com.example.cmpshop.dto.ProductDto;
import com.example.cmpshop.services.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** This class would typically contain methods to handle various HTTP requests
 * (e.g., GET) for managing or searching products.
 * Injecting dependencies such as the ProductService via constructor injection
 * to interact with the service layer {@link IProductService}.
 * response (http code, message, data)**/
@RestController
@RequestMapping("api/search")
public class ProductController {
    private final IProductService iProductService;
    //  ko cần phải khởi tạo IProductService
    @Autowired
    public ProductController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @Tag(name = "search product ", description = "Get methods of filter products with options for APIs ")
    @Operation(summary = "Search products",
            description = "Search product list based on option such as slug, brand , price and year. Returned products are paginated.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get success ", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductController.class))}),
            @ApiResponse(responseCode = "404", description = "Products not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid Parameter",
                    content = @Content)
    })
    /**
     * Handles GET requests to filter and retrieve products based on various optional filtering parameters.
     *
     * @param slug      Optional product slug for specific product identification. Can be null to ignore this filter.
     * @param brandId   Optional brand ID to filter products by specific brand. Can be null if no brand filter is applied.
     * @param minPrice  Optional minimum price to filter products. Only products priced equal to or above this value are returned.
     * @param maxPrice  Optional maximum price to filter products. Only products priced equal to or below this value are returned.
     * @param minYear   Optional minimum production year for products. Only products from this year onward are returned.
     * @param maxYear   Optional maximum production year for products. Only products from this year or earlier are returned.
     * @param page      Optional page number for paginated results. Defaults to 0 if not specified, indicating the first page.
     * @param size      Optional number of items per page. Defaults to 15, setting the maximum number of items returned per page.
     * @param sortBy    Optional field by which to sort the product results (e.g., "price", "name"). Defaults to no sorting if null.
     * @param sortOrder Optional sorting order for results (e.g., "asc" for ascending, "desc" for descending). Defaults to no specific order if null.
     * @return          ResponseEntity containing the filtered list of products, or an appropriate status code if no products match the criteria.
     */
    @GetMapping()
    public ResponseEntity<?> filterProductWithOptions(
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder
    ) {
        // Xử lý phân trang và sắp xếp
        Pageable pageable = createPageRequest(page, size, sortBy, sortOrder);
        Page<ProductDto> productPage = iProductService.filterProducts(slug, brandId, minPrice, maxPrice, minYear, maxYear, pageable);
        return new ResponseEntity<>(productPage, HttpStatus.OK);
    }

    @Tag(name = "search product advanced", description = "Get methods of filter products with options for APIs ")
    @Operation(summary = "Search advanced",
            description = "Search product list based on category id, category type id, status and address. Returned products are paginated.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Get success ", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductController.class))}),
            @ApiResponse(responseCode = "404", description = "Products not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid Parameter",
                    content = @Content)
    })
    /**
     * Retrieves a list of products filtered by category, category type, status, and address.
     *
     * @param categoryId      Optional ID of the category to filter products by. If specified, only products within this category are returned.
     * @param categoryTypeId  Optional ID of the category type to further filter products. If specified, products must match this type within the category.
     * @param status          Optional status of the product (e.g., "ACTIVE", "INACTIVE"). If provided, only products with this status are returned.
     * @param address         Optional address or location filter. Products will be filtered based on this address if specified.
     *
     * @return ResponseEntity<?> containing the filtered list of products based on provided criteria, along with an appropriate HTTP status code.
     *         If no products match the criteria, the response may contain an empty list or a suitable message.
     */
    @GetMapping("/advanced")
    public ResponseEntity<?> getProductListWithCategoryStatusAndAddress(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long categoryTypeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String address) {

        List<ProductDto> productDtoList = iProductService.getProductByCategoryStatusAndAddress(categoryId, categoryTypeId, status, address);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    /** Tạo Pageable cho phân trang và sắp xếp */
    private Pageable createPageRequest(int page, int size, String sortBy, String sortOrder) {
        if (sortBy == null || sortOrder == null) {
            // Nếu sortBy hoặc sortOrder là null, không áp dụng sắp xếp
            return PageRequest.of(page, size);
        } else {
            // Áp dụng sắp xếp dựa trên sortBy và sortOrder nếu có giá trị
            Sort sort = Sort.by(sortOrder.equalsIgnoreCase("asc") ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy));
            return PageRequest.of(page, size, sort);
        }
    }


}
