package com.example.ecommerce.order_service.services;

import com.example.ecommerce.order_service.models.ProductPurchaseRequestDTO;
import com.example.ecommerce.order_service.models.ProductPurchaseResponseDTO;

import java.util.List;

public interface ProductService {

    List<ProductPurchaseResponseDTO> executePurchaseProducts(List<ProductPurchaseRequestDTO> requestBody);

}
