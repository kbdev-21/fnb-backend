package com.example.fnb.collection.web;

import com.example.fnb.collection.CollectionService;
import com.example.fnb.collection.dto.AddProductToCollectionDto;
import com.example.fnb.collection.dto.CollectionCreateDto;
import com.example.fnb.collection.dto.CollectionDto;
import com.example.fnb.collection.dto.CollectionDtoDetail;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.security.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }


    @PostMapping("/api/collections")
    public ResponseEntity<CollectionDtoDetail> createCollection(@RequestBody @Valid CollectionCreateDto dto) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);
        return ResponseEntity.status(HttpStatus.CREATED).body(collectionService.createCollection(dto));
    }

    @GetMapping("/api/collections")
    public ResponseEntity<?> getAllCollections() {
        return ResponseEntity.ok(collectionService.getAllCollections());
    }

    @GetMapping("/api/collections/{id}")
    public ResponseEntity<CollectionDtoDetail> getCollectionById(@PathVariable UUID id) {
        return ResponseEntity.ok(collectionService.getCollectionById(id));
    }

    @GetMapping("/api/collections/by-slug/{slug}")
    public ResponseEntity<CollectionDtoDetail> getCollectionBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(collectionService.getCollectionBySlug(slug));
    }

    @PostMapping("api/collections/{collectionId}/products")
    public ResponseEntity<CollectionDtoDetail> addProductsToCollection(@PathVariable UUID collectionId, @RequestBody @Valid AddProductToCollectionDto dto) {
        return ResponseEntity.ok(collectionService.addProductOnCollection(collectionId, dto.getProductIds()));
    }
}
