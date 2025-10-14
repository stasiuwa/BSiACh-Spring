package com.example.lab1.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetween(BigDecimal lowPrice, BigDecimal highPrice);

    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0")
    List<Product> findAvaibleProducts();

    @Query("SELECT p FROM Product p WHERE p.price <= :maxPrice AND p.category = :category")
    List<Product> findByCategoryAndMaxPrice(
            @Param("category") ProductCategory category,
            @Param("maxPrice") BigDecimal maxPrice);
    boolean existsByName(String name);
}
