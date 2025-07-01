package com.example.ecommerce.product_service.services;

import com.example.ecommerce.product_service.model.ProductPurchaseRequestDTO;
import com.example.ecommerce.product_service.model.ProductPurchaseResponseDTO;
import com.example.ecommerce.product_service.model.ProductRequestDTO;
import com.example.ecommerce.product_service.model.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO getProductById(Integer id);

    List<ProductResponseDTO> getAllProducts();

    Integer createProduct(ProductRequestDTO productRequestDTO);

    List<ProductPurchaseResponseDTO> purchaseProducts(List<ProductPurchaseRequestDTO> productRequestDTOs);

}
