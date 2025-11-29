package com.example.fnb.menu;

import com.example.fnb.menu.dto.ProductCreateDto;
import com.example.fnb.menu.dto.ProductDto;
import com.example.fnb.menu.dto.ProductUpdateDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<ProductDto> getProducts(int pageNumber, int pageSize, String sortBy, String searchKey, UUID categoryId);
    List<ProductDto> getProductsByIdsIn(List<UUID> productIds);
    ProductDto getProductById(UUID id);
    ProductDto getProductBySlug(String slug);
    ProductDto createProduct(ProductCreateDto productDto);
    ProductDto updateProduct(UUID productId, ProductUpdateDto productUpdateDto);
    ProductDto updateAvailableStatusForProduct(UUID productId, String storeCode, boolean available);
    ProductDto deleteProduct(UUID productId);
}
