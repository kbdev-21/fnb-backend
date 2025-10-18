package com.example.fnb.store.domain;

import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.store.StoreService;
import com.example.fnb.store.domain.entity.Store;
import com.example.fnb.store.domain.entity.StoreTable;
import com.example.fnb.store.domain.repository.StoreRepository;
import com.example.fnb.store.domain.repository.StoreTableRepository;
import com.example.fnb.store.dto.StoreCreateDto;
import com.example.fnb.store.dto.StoreDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreTableRepository storeTableRepository;

    private final ModelMapper modelMapper;

    public StoreServiceImpl(StoreRepository storeRepository, StoreTableRepository storeTableRepository, ModelMapper modelMapper) {
        this.storeRepository = storeRepository;
        this.storeTableRepository = storeTableRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public StoreDto createStore(StoreCreateDto createDto) {
        var newStore = new Store();
        newStore.setId(UUID.randomUUID());
        newStore.setCode(createDto.getCode());
        newStore.setDisplayName(createDto.getDisplayName());
        newStore.setPhoneNum(createDto.getPhoneNum());
        newStore.setEmail(createDto.getEmail());
        newStore.setCity(createDto.getCity());
        newStore.setFullAddress(createDto.getFullAddress());
        newStore.setOpen(createDto.isOpen());
        newStore.setCreatedAt(Instant.now());

        var newTables = createDto.getTables()
            .stream().map(dto -> createDtoToTableEntity(dto, newStore))
            .toList();
        newStore.setTables(newTables);

        var savedStore = storeRepository.save(newStore);
        return modelMapper.map(savedStore, StoreDto.class);
    }

    @Override
    public List<StoreDto> getStores() {
        var stores = storeRepository.findAll();
        return stores.stream().map(s -> modelMapper.map(s, StoreDto.class)).toList();
    }

    @Override
    public StoreDto getStoreByCode(String code) {
        var store = storeRepository.findByCode(code).orElseThrow(
            () -> new DomainException(DomainExceptionCode.STORE_NOT_FOUND)
        );
        return modelMapper.map(store, StoreDto.class);
    }

    private StoreTable createDtoToTableEntity(StoreCreateDto.StoreCreateDtoTable tableCreateDto, Store store) {
        if(!tableCreateDto.getCode().contains(store.getCode())) {
            throw new DomainException(DomainExceptionCode.TABLE_CODE_MUST_INCLUDE_ITS_STORE_CODE);
        }

        var storeTable = new StoreTable();
        storeTable.setId(UUID.randomUUID());
        storeTable.setCode(tableCreateDto.getCode());
        storeTable.setDisplayName(tableCreateDto.getDisplayName());
        storeTable.setDescription(tableCreateDto.getDescription());
        storeTable.setCreatedAt(Instant.now());
        storeTable.setStore(store);
        return storeTable;
    }
}
