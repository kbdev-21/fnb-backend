package com.example.fnb.store;

import com.example.fnb.store.dto.CreateStoreDto;
import com.example.fnb.store.dto.StoreDto;
import com.example.fnb.store.dto.TableDto;

import java.util.List;

public interface StoreService {
    StoreDto createStore(CreateStoreDto createDto);
    List<StoreDto> getStores();
    StoreDto getStoreByCode(String code);
    TableDto getTableByCodeAndStoreCode(String tableCode, String storeCode);
}
