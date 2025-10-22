package com.example.fnb.product.web;

import com.example.fnb.product.ProductService;
import com.example.fnb.product.dto.ProductCreateDto;
import com.example.fnb.product.dto.ProductDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/api/products/by-slug/{slug}")
    public ResponseEntity<ProductDto> getProductBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(productService.getProductBySlug(slug));
    }

    @PatchMapping("/api/products/{productId}/available-status")
    public ProductDto updateAvailableStatus(
        @PathVariable UUID productId,
        @RequestParam(required = true) String storeCode,
        @RequestParam(required = true) boolean available
    ) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF);
        if(SecurityUtil.getCurrentUserRole() == UserRole.STAFF) {
            SecurityUtil.onlyAllowStaffOfStoreCode(storeCode);
        }
        return productService.updateAvailableStatusForProduct(productId, storeCode, available);
    }
}