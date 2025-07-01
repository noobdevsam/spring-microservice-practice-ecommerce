package com.example.ecommerce.product_service.mapper;

import com.example.ecommerce.product_service.entities.Category;
import com.example.ecommerce.product_service.entities.Product;
import com.example.ecommerce.product_service.model.ProductPurchaseResponseDTO;
import com.example.ecommerce.product_service.model.ProductRequestDTO;
import com.example.ecommerce.product_service.model.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product productRequestDTOtoProduct(ProductRequestDTO productRequestDTO) {
        if (productRequestDTO == null) {
            return null;
        }

        return Product.builder()
                .id(productRequestDTO.id())
                .name(productRequestDTO.name())
                .description(productRequestDTO.description())
                .price(productRequestDTO.price())
                .availableQuantity(productRequestDTO.availableQuantity())
                .category(
                        Category.builder()
                                .id(productRequestDTO.categoryId())
                                .build()
                )
                .build();
    }

    public ProductResponseDTO productToProductResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }

    public ProductPurchaseResponseDTO productToProductPurchaseResponseDTO(Product product, double quantity) {
        return new ProductPurchaseResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}
