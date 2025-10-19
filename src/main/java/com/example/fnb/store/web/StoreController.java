package com.example.fnb.store.web;

import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.utils.SecurityUtil;
import com.example.fnb.store.StoreService;
import com.example.fnb.store.dto.CreateStoreDto;
import com.example.fnb.store.dto.StoreDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
