package com.example.fnb.discount.web;

import com.example.fnb.discount.DiscountService;
import com.example.fnb.discount.dto.CreateDiscountDto;
import com.example.fnb.discount.dto.DiscountDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.security.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/api/discounts")
    public ResponseEntity<DiscountDto> create(@RequestBody @Valid CreateDiscountDto request) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.createDiscount(request));
    }

    @GetMapping("/api/discounts")
    public ResponseEntity<Page<DiscountDto>> getAll(
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "20") int pageSize
    ) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF);
        return ResponseEntity.ok(discountService.getAllDiscounts(pageNumber, pageSize));
    }

    @DeleteMapping("/api/discounts/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        discountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
