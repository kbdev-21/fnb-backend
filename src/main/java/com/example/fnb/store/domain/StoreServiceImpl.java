package com.example.fnb.store.domain;

import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.store.StoreService;
import com.example.fnb.store.domain.entity.Store;
import com.example.fnb.store.domain.repository.StoreRepository;
import com.example.fnb.store.dto.CreateStoreDto;
import com.example.fnb.store.dto.StoreDto;
import com.example.fnb.store.dto.UpdateStoreDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;

    public StoreServiceImpl(StoreRepository storeRepository, ModelMapper modelMapper) {
        this.storeRepository = storeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StoreDto createStore(CreateStoreDto createDto) {
        var store = new Store();
        store.setId(UUID.randomUUID());
        store.setCode(createDto.getCode());
        store.setDisplayName(createDto.getDisplayName());
        store.setPhoneNum(createDto.getPhoneNum());
        store.setEmail(createDto.getEmail());
        store.setCity(createDto.getCity());
        store.setFullAddress(createDto.getFullAddress());
        store.setOpen(true);
        store.setCreatedAt(Instant.now());

        var saved = storeRepository.save(store);
        return modelMapper.map(saved, StoreDto.class);
    }

    @Override
    public List<StoreDto> getStores() {
        var stores = storeRepository.findAll();
        return stores.stream().map(s -> modelMapper.map(s, StoreDto.class)).toList();
    }

    @Override
    public StoreDto getStoreById(UUID id) {
        var store = storeRepository.findById(id).orElseThrow(
            () -> new DomainException(DomainExceptionCode.STORE_NOT_FOUND)
        );
        return modelMapper.map(store, StoreDto.class);
    }

    @Override
    public StoreDto getStoreByCode(String code) {
        var store = storeRepository.findByCode(code).orElseThrow(
            () -> new DomainException(DomainExceptionCode.STORE_NOT_FOUND)
        );
        return modelMapper.map(store, StoreDto.class);
    }

    @Override
    public StoreDto updateStore(UUID id, UpdateStoreDto dto) {
        var store = storeRepository.findById(id).orElseThrow(
            () -> new DomainException(DomainExceptionCode.STORE_NOT_FOUND)
        );

        if (dto.getDisplayName() != null) {
            store.setDisplayName(dto.getDisplayName());
        }
        if (dto.getPhoneNum() != null) {
            store.setPhoneNum(dto.getPhoneNum());
        }
        if (dto.getEmail() != null) {
            store.setEmail(dto.getEmail());
        }
        if (dto.getCity() != null) {
            store.setCity(dto.getCity());
        }
        if (dto.getFullAddress() != null) {
            store.setFullAddress(dto.getFullAddress());
        }

        var saved = storeRepository.save(store);
        return modelMapper.map(saved, StoreDto.class);
    }

    @Override
    public void deleteStore(UUID id) {
        var store = storeRepository.findById(id).orElseThrow(
            () -> new DomainException(DomainExceptionCode.STORE_NOT_FOUND)
        );
        storeRepository.delete(store);
    }
}
