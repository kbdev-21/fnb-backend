package com.example.fnb.discount;

import com.example.fnb.discount.dto.CreateDiscountDto;
import com.example.fnb.discount.dto.DiscountDto;
import com.example.fnb.discount.dto.DiscountValidateResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface DiscountService {
    DiscountDto createDiscount(CreateDiscountDto createDiscountDto);
    Page<DiscountDto> getAllDiscounts(int pageNumber, int pageSize);
    DiscountValidateResult validateAndCalculateDiscountAmount(String discountCode, BigDecimal subtotalAmount);
    void deleteById(UUID id);
}
