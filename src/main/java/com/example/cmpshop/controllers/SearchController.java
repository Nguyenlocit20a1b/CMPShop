package com.example.cmpshop.controllers;

import ch.qos.logback.core.util.StringUtil;
import com.example.cmpshop.dto.ProductDto;
import com.example.cmpshop.services.IProductService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/search")
public class SearchController {
    private IProductService iProductService;

    @Autowired
    public SearchController(IProductService iProductService) {
        this.iProductService = iProductService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductDto>> getAllProducts(@RequestParam(required = false) Long categoryId,
                                                           @RequestParam (required = false) Long typeId ,
                                                           @RequestParam (required = false) Long brandId,
                                                           @RequestParam(required = false) String slug){
        List<ProductDto> productDtoList = new ArrayList<>();
        if(StringUtils.isNotBlank(slug)){
            productDtoList = iProductService.getProductsBySlugKeyword(slug);
        } else {
            productDtoList = iProductService.getAllProduct(categoryId, typeId , brandId);
        }
        return new ResponseEntity<>(productDtoList, HttpStatus.OK);
    }
}
