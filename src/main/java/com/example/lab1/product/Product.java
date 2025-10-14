package com.example.lab1.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100,
            message = "Name must have between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    //niepusta, max 1000 znaków, definicja TEXT
    @NotBlank
    @Max(1000)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    //niepusta, minimum 0.01, złożona z cyfr
    //format max 10 cyfr, 2 cyfry części ułamkowej
    @NotNull
    @DecimalMin("0.01")
    @Digits(fraction = 2, integer = 10)
    private BigDecimal price;

    //niepusta, typu wyliczeniowego (zapisywanego jako tekst)
    @NotBlank
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    //niepusta, nieujemna
    @NotBlank
    @Min(0)
    private Integer stockQuantity;

    //niepusta
    @NotBlank
    private LocalDateTime createdAt;
    @NotBlank
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
