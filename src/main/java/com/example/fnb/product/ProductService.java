package com.example.fnb.product;

import com.example.fnb.product.dto.ProductCreateDto;
import com.example.fnb.product.dto.ProductDto;
import com.example.fnb.product.dto.ProductUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<ProductDto> getProducts(int page, int size, String sortBy);
    List<ProductDto> getProductsByIdsIn(List<UUID> productIds);
    ProductDto getProductById(UUID id);
    ProductDto getProductBySlug(String slug);
    ProductDto createProduct(ProductCreateDto productDto);
    ProductDto updateProduct(UUID productId, ProductUpdateDto productUpdateDto);
    ProductDto updateAvailableStatusForProduct(UUID productId, String storeCode, boolean available);
}
