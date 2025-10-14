package com.example.lab1.product;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products");
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public ProductResponse getProductById(Long id) {
        log.info("Getting product by id {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFound(
                        "Product with id " + id + " not found"
                ));
        return mapToResponse(product);
    }

    public List<ProductResponse> getProductsByCategory(ProductCategory category) {
        log.info("Getting products by category {}", category);
        return productRepository.findByCategory(category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public List<ProductResponse> searchProducts(String searchTerm) {
        log.info("Getting products by search term {}", searchTerm);
        return productRepository.findByNameContainingIgnoreCase(searchTerm)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public List<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Getting products by price range {}-{}", minPrice, maxPrice);
        return productRepository.findByPriceBetween(minPrice,maxPrice)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public ProductResponse createProduct(ProductRequest request){
        log.info("Creating product {}", request);
        if (productRepository.existsByName(request.getName())) {
            throw new ProductAlreadyExists("Product with name " + request.getName() + " already exists");
        }
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(request.getCategory());
        product.setStockQuantity(request.getStockQuantity());
        return mapToResponse(productRepository.save(product));
    }
    public ProductResponse updateProduct(Long id, ProductRequest request){
        log.info("Updating product {}", request);
        if (productRepository.existsById(id)) {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                product.get().setName(request.getName());
                product.get().setDescription(request.getDescription());
                product.get().setPrice(request.getPrice());
                product.get().setCategory(request.getCategory());
                product.get().setStockQuantity(request.getStockQuantity());
                return  mapToResponse(productRepository.save(product.get()));
            }

        }
        else  {
            throw new ProductNotFound("Product with id " + id + " not found");
        }
        // TODO chyba nie moze tak byc?
        return null;
    }
    public void deleteProduct(Long id) {
        log.info("Deleting product {}", id);
        if (!productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }
    private ProductResponse mapToResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setCategory(product.getCategory());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
