package com.example.fnb.discount.web;

import com.example.fnb.discount.DiscountService;
import com.example.fnb.discount.dto.DiscountDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

//    @PostMapping("/api/discounts")
//    DiscountDto create(@RequestBody @Valid ) {
//        return discountService.createDiscount();
//    }
}
