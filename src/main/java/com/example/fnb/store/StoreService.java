package com.example.fnb.store;

import com.example.fnb.store.dto.StoreCreateDto;
import com.example.fnb.store.dto.StoreDto;

import java.util.List;

public interface StoreService {
    StoreDto createStore(StoreCreateDto createDto);
    List<StoreDto> getStores();
    StoreDto getStoreByCode(String code);
}
