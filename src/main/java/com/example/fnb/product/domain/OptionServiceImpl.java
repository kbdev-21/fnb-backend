package com.example.fnb.product.domain;

import com.example.fnb.product.OptionService;
import com.example.fnb.product.domain.repository.OptionRepository;
import com.example.fnb.product.dto.OptionDto;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    private final ModelMapper modelMapper;

    public OptionServiceImpl(OptionRepository optionRepository, ModelMapper modelMapper) {
        this.optionRepository = optionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OptionDto getOptionById(UUID id) {
        var option = optionRepository.findById(id).orElseThrow(
            () -> new DomainException(DomainExceptionCode.OPTION_NOT_FOUND)
        );
        return modelMapper.map(option, OptionDto.class);
    }
}
