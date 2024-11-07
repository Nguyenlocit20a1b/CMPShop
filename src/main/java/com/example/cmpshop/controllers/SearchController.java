package com.example.cmpshop.controllers;


import com.example.cmpshop.dto.ProductDto;
import com.example.cmpshop.services.IProductService;
import io.micrometer.common.util.StringUtils;
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

@RestController
@RequestMapping("api/search")
public class SearchController {
    private final IProductService iProductService;

    @Autowired
    public SearchController(IProductService iProductService) {

        this.iProductService = iProductService;
    }

    @Tag(name = "get", description = "Get methods of filter products with options for APIs ")
    @Operation(summary = "Search products",
            description = "Search product list based on option such as slug, brand , price and year. Returned products are paginated.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SearchController.class)) }),
            @ApiResponse(responseCode = "404", description = "Products not found",
                    content = @Content) })
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
    @Operation(summary = "Search advanced",
              description = "Search product list based on category id, category type id, status and address. Returned products are paginated.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SearchController.class)) }),
            @ApiResponse(responseCode = "404", description = "Products not found",
                    content = @Content) })
    @GetMapping("/advanced")
    public ResponseEntity<?> getProductListWithCategoryStatusAndAddress(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long categoryTypeId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String address) {
        List<ProductDto> productDtoList = iProductService.getProductByCategoryStatusAndAddress(categoryId, categoryTypeId, status, address);
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }

    // Tạo Pageable cho phân trang và sắp xếp
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
