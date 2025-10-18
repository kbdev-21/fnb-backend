package com.example.fnb.discount.web;

import com.example.fnb.discount.DiscountService;
import com.example.fnb.discount.dto.CreateDiscountRequestDto;
import com.example.fnb.discount.dto.DiscountDto;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.utils.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/api/discounts")
    public ResponseEntity<DiscountDto> create(@RequestBody @Valid CreateDiscountRequestDto request) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountService.createDiscount(request));
    }

    @GetMapping("/api/discounts")
    public ResponseEntity<List<DiscountDto>> getAll() {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF);
        return ResponseEntity.ok(discountService.getAllDiscounts());
    }
}
