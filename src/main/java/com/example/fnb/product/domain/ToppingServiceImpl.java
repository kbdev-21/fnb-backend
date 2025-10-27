package com.example.fnb.product.domain;

import com.example.fnb.product.ToppingService;
import com.example.fnb.product.domain.repository.ToppingRepository;
import com.example.fnb.product.dto.ToppingDto;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ToppingServiceImpl implements ToppingService {
    private final ToppingRepository toppingRepository;
    private final ModelMapper modelMapper;

    public ToppingServiceImpl(ToppingRepository toppingRepository, ModelMapper modelMapper) {
        this.toppingRepository = toppingRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ToppingDto getToppingById(UUID id) {
        var topping = toppingRepository.findById(id).orElseThrow(
            () -> new DomainException(DomainExceptionCode.TOPPING_NOT_FOUND)
        );
        return modelMapper.map(topping, ToppingDto.class);
    }
}
