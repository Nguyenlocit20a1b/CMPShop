package com.example.cmpshop.repositories;

import com.example.cmpshop.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{
    @Procedure(name = "searchProductsCategoryStatusAddress")
    @Transactional()
    List<Product> searchProductsCategoryStatusAddress(
            @Param("p_category_id") Long categoryId,
            @Param("p_category_type_id") Long categoryTypeId,
            @Param("p_status") Boolean status,
            @Param("p_address") String address
    );
}
