package com.example.fnb.product;

import com.example.fnb.product.dto.OptionDto;

import java.util.UUID;

public interface OptionService {
    OptionDto getOptionById(UUID id);
}
