package com.example.cmpshop.repositories;
import com.example.cmpshop.entities.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
/** ProductRepository extends JpaRepository and JpaSpecificationExecutor, which provides several key functionalities:
 * The Long parameter indicates the type of the primary key for {@link ProductEntity}.
 * JpaSpecificationExecutor {@link ProductEntity} provides support for dynamic queries using the Specification API.**/
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    /**
     * Searches for products based on category, category type, status, and address using Store Procedure.
     *
     * @param categoryId     The ID of the category used to filter products. Only products within this category are returned.
     * @param categoryTypeId The ID of the category type for additional filtering within the specified category.
     *                       Only products matching this category type are included.
     * @param status         The status of the product (e.g., "ACTIVE", "INACTIVE"). Only products with this status are returned.
     * @param address        The address or location filter. Products are filtered to match this address if specified.
     * @return List<ProductEntity> A list of ProductEntity objects that match the specified criteria.
     * Returns an empty list if no products match the given filters.
     */
    @Procedure(name = "searchProductsCategoryStatusAddress")
    @Transactional()
    List<ProductEntity> searchProductsCategoryStatusAddress(
            @Param("p_category_id") Long categoryId,
            @Param("p_category_type_id") Long categoryTypeId,
            @Param("p_status") String status,
            @Param("p_address") String address
    );
}
