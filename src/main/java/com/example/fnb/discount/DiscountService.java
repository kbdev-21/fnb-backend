package com.example.fnb.discount;

import com.example.fnb.discount.dto.CreateDiscountRequestDto;
import com.example.fnb.discount.dto.DiscountDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface DiscountService {
    DiscountDto createDiscount(CreateDiscountRequestDto createDiscountRequestDto);
    List<DiscountDto> getAllDiscounts();
    BigDecimal validateAndCalculatePriceAfterAppliedDiscount(String discountCode, BigDecimal beforePrice, String customerPhoneNum);
}
