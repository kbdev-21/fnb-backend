package com.example.fnb.discount.domain;

import com.example.fnb.discount.DiscountService;
import com.example.fnb.discount.dto.CreateDiscountDto;
import com.example.fnb.discount.dto.DiscountDto;
import com.example.fnb.discount.dto.DiscountValidateResult;
import com.example.fnb.shared.enums.DiscountType;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final ModelMapper modelMapper;

    public DiscountServiceImpl(DiscountRepository discountRepository, ModelMapper modelMapper) {
        this.discountRepository = discountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DiscountDto createDiscount(CreateDiscountDto createDiscountDto) {
        var discountType = createDiscountDto.getDiscountType();
        var discountValue = createDiscountDto.getDiscountValue();
        var maxFixedAmount = discountType == DiscountType.FIXED ? null : createDiscountDto.getMaxFixedAmount();

        if (discountType == DiscountType.PERCENTAGE && discountValue.compareTo(BigDecimal.valueOf(1.0)) > 0) {
            throw new DomainException(DomainExceptionCode.INVALID_DISCOUNT_VALUE);
        }

        var newDiscount = new Discount();
        newDiscount.setId(UUID.randomUUID());
        newDiscount.setCode(createDiscountDto.getCode());
        newDiscount.setDiscountType(discountType);
        newDiscount.setDiscountValue(discountValue);
        newDiscount.setMaxFixedAmount(maxFixedAmount);
        newDiscount.setMinApplicablePrice(createDiscountDto.getMinApplicablePrice());
        newDiscount.setUsed(false);
        newDiscount.setCreatedAt(Instant.now());
        newDiscount.setExpiredAt(createDiscountDto.getExpiredAt());
        newDiscount.setUsedByPhoneNum(null);

        var savedDiscount = discountRepository.save(newDiscount);
        return modelMapper.map(savedDiscount, DiscountDto.class);
    }

    @Override
    public Page<DiscountDto> getAllDiscounts(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        var discounts = discountRepository.findAll(pageable);
        var discountDtos = discounts.stream()
            .map(d -> modelMapper.map(d, DiscountDto.class))
            .toList();

        return new PageImpl<>(discountDtos, pageable, discounts.getTotalElements());
    }

    @Override
    public DiscountValidateResult validateAndCalculateDiscountAmount(String discountCode, BigDecimal subtotalAmount) {
        var discount = discountRepository.findByCode(discountCode).orElse(null);
        if(discount == null) {
            return new DiscountValidateResult(null, BigDecimal.ZERO);
        }

        if (discount.getExpiredAt() != null && discount.getExpiredAt().isBefore(Instant.now())) {
            return new DiscountValidateResult(null, BigDecimal.ZERO);
        }

        if (discount.isUsed()) {
            return new DiscountValidateResult(null, BigDecimal.ZERO);
        }

        if (
            discount.getMinApplicablePrice() != null &&
            subtotalAmount.compareTo(discount.getMinApplicablePrice()) < 0
        ) {
            return new DiscountValidateResult(null, BigDecimal.ZERO);
        }

        BigDecimal reduction = switch (discount.getDiscountType()) {
            case FIXED -> discount.getDiscountValue();
            case PERCENTAGE -> {
                var rawReduction = subtotalAmount.multiply(discount.getDiscountValue());
                yield discount.getMaxFixedAmount() == null
                    ? rawReduction
                    : rawReduction.min(discount.getMaxFixedAmount());
            }
        };

        return new DiscountValidateResult(discount.getCode(), reduction);
    }

    @Override
    public void deleteById(UUID id) {
        var discount = discountRepository.findById(id)
            .orElseThrow(() -> new DomainException(DomainExceptionCode.DISCOUNT_NOT_EXISTED));

        discountRepository.delete(discount);
    }
}
