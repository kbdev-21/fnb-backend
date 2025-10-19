package com.example.fnb.discount;

import com.example.fnb.discount.dto.CreateDiscountDto;
import com.example.fnb.discount.dto.DiscountDto;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountService {
    DiscountDto createDiscount(CreateDiscountDto createDiscountDto);
    List<DiscountDto> getAllDiscounts();
    BigDecimal validateAndCalculatePriceAfterAppliedDiscount(String discountCode, BigDecimal beforePrice, String customerPhoneNum);
}
