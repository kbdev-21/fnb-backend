package com.example.fnb.store.web;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.security.SecurityUtil;
import com.example.fnb.store.StoreService;
import com.example.fnb.store.dto.CreateStoreDto;
import com.example.fnb.store.dto.StoreDto;
import com.example.fnb.store.dto.TableDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StoreController {
    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/api/stores")
    public ResponseEntity<StoreDto> create(@RequestBody @Valid CreateStoreDto createDto) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(createDto));
    }

    @GetMapping("/api/stores")
    public ResponseEntity<List<StoreDto>> getMany() {
        return ResponseEntity.ok(storeService.getStores());
    }

    @GetMapping("/api/stores/by-code/{storeCode}/tables/{tableCode}")
    public ResponseEntity<TableDto> getTable(@PathVariable String tableCode, @PathVariable String storeCode) {
        return ResponseEntity.ok(storeService.getTableByCodeAndStoreCode(tableCode, storeCode));
    }
}
