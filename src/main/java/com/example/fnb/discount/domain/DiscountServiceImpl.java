package com.example.fnb.discount.domain;

import com.example.fnb.discount.DiscountService;
import com.example.fnb.discount.dto.CreateDiscountDto;
import com.example.fnb.discount.dto.DiscountDto;
import com.example.fnb.shared.enums.DiscountType;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
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

        if(discountType == DiscountType.PERCENTAGE && discountValue.compareTo(BigDecimal.valueOf(1.0)) > 0) {
            throw new DomainException(DomainExceptionCode.INVALID_DISCOUNT_VALUE);
        }

        var newDiscount = new Discount();
        newDiscount.setId(UUID.randomUUID());
        newDiscount.setCode(createDiscountDto.getCode());
        newDiscount.setDiscountType(discountType);
        newDiscount.setDiscountValue(createDiscountDto.getDiscountValue());
        newDiscount.setMaxFixedAmount(maxFixedAmount);
        newDiscount.setMinApplicablePrice(createDiscountDto.getMinApplicablePrice());
        newDiscount.setUseOncePerCustomer(createDiscountDto.isUseOncePerCustomer());
        newDiscount.setGlobalUsageLimit(createDiscountDto.getGlobalUsageLimit());
        newDiscount.setUsedPhoneNums(new ArrayList<>());
        newDiscount.setActive(createDiscountDto.isActive());
        newDiscount.setCreatedAt(Instant.now());
        newDiscount.setExpiredAt(createDiscountDto.getExpiredAt());

        var savedDiscount = discountRepository.save(newDiscount);
        return modelMapper.map(savedDiscount, DiscountDto.class);
    }

    @Override
    public List<DiscountDto> getAllDiscounts() {
        var discounts = discountRepository.findAll();
        return discounts.stream().map(d -> modelMapper.map(d, DiscountDto.class)).toList();
    }

    @Override
    public BigDecimal validateAndCalculateDiscountAmount(String discountCode, BigDecimal subtotalAmount, String customerPhoneNum) {
        var discount = discountRepository.findByCode(discountCode).orElseThrow(
            () -> new DomainException(DomainExceptionCode.DISCOUNT_NOT_EXISTED)
        );

        if (discount.getExpiredAt() != null && discount.getExpiredAt().isBefore(Instant.now())) {
            throw new DomainException(DomainExceptionCode.DISCOUNT_EXPIRED);
        }
        if (discount.getMinApplicablePrice() != null && subtotalAmount.compareTo(discount.getMinApplicablePrice()) < 0) {
            throw new DomainException(DomainExceptionCode.PRICE_IS_TOO_LOW_TO_APPLY);
        }
        if(discount.getGlobalUsageLimit() != null && discount.getUsedPhoneNums().size() >= discount.getGlobalUsageLimit()) {
            throw new DomainException(DomainExceptionCode.DISCOUNT_OUT_OF_USE);
        }
        if (discount.isUseOncePerCustomer() && discount.getUsedPhoneNums().contains(customerPhoneNum)) {
            throw new DomainException(DomainExceptionCode.DISCOUNT_OUT_OF_USE);
        }

        return switch(discount.getDiscountType()) {
            case FIXED -> discount.getDiscountValue();
            case PERCENTAGE -> {
                var rawReduction = subtotalAmount.multiply(discount.getDiscountValue());
                yield discount.getMaxFixedAmount() == null
                    ? rawReduction
                    : rawReduction.min(discount.getMaxFixedAmount());
            }
        };
    }
}
