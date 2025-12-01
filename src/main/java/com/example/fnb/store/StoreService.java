package com.example.fnb.store;

import com.example.fnb.store.dto.CreateStoreDto;
import com.example.fnb.store.dto.StoreDto;
import com.example.fnb.store.dto.UpdateStoreDto;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    StoreDto createStore(CreateStoreDto createDto);
    List<StoreDto> getStores();
    StoreDto getStoreById(UUID id);
    StoreDto getStoreByCode(String code);
    StoreDto updateStore(UUID id, UpdateStoreDto updateDto);
    void deleteStore(UUID id);
}