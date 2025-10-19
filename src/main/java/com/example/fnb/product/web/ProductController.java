package com.example.fnb.product.web;

import com.example.fnb.product.ProductService;
import com.example.fnb.product.dto.ProductCreateDto;
import com.example.fnb.product.dto.ProductDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.utils.SecurityUtil;
import jakarta.validation.Valid;
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
    public ProductDto createProduct(@RequestBody @Valid ProductCreateDto dto) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return productService.createProduct(dto);
    }

    @GetMapping("/api/products")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/api/products/{id}")
    public ProductDto getProductById(@PathVariable UUID id) {
        return productService.getProductById(id);
    }

    @GetMapping("/api/products/by-slug/{slug}")
    public ProductDto getProductBySlug(@PathVariable String slug) {
        return productService.getProductBySlug(slug);
    }
}
