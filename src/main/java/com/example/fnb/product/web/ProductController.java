package com.example.fnb.product.web;

import com.example.fnb.product.ProductService;
import com.example.fnb.product.dto.ProductCreateDto;
import com.example.fnb.product.dto.ProductDto;
import com.example.fnb.product.dto.ProductUpdateDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.security.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid ProductCreateDto dto) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

    @GetMapping("/api/products")
    public ResponseEntity<Page<ProductDto>> getAllProducts(
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "20") int pageSize,
        @RequestParam(required = false, defaultValue = "-createdAt") String sortBy,
        @RequestParam(required = false, defaultValue = "") String categoryId,
        @RequestParam(required = false, defaultValue = "") String searchKey
    ) {
        UUID categoryIdAsUUID = categoryId.isBlank() ? null : UUID.fromString(categoryId);
        return ResponseEntity.ok(productService.getProducts(pageNumber, pageSize, sortBy, searchKey, categoryIdAsUUID));
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/api/products/by-slug/{slug}")
    public ResponseEntity<ProductDto> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getProductBySlug(slug));
    }

    @PatchMapping("/api/products/{productId}")
    public ProductDto updateProduct(
        @PathVariable UUID productId,
        @RequestBody @Valid ProductUpdateDto dto
    ) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return productService.updateProduct(productId, dto);
    }

    @PatchMapping("/api/products/{productId}/available-status")
    public ProductDto updateAvailableStatus(
        @PathVariable UUID productId,
        @RequestParam(required = true) String storeCode,
        @RequestParam(required = true) boolean available
    ) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF);
        var currentRole = SecurityUtil.getCurrentUserRole().orElse(null);
        if (currentRole == UserRole.STAFF) {
            SecurityUtil.onlyAllowStaffOfStoreCode(storeCode);
        }

        return productService.updateAvailableStatusForProduct(productId, storeCode, available);
    }
}