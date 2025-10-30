package com.example.fnb.collection;


import com.example.fnb.collection.dto.CollectionCreateDto;
import com.example.fnb.collection.dto.CollectionDto;
import com.example.fnb.collection.dto.CollectionDtoDetail;
import com.example.fnb.collection.dto.CollectionUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CollectionService {
    CollectionDtoDetail createCollection(CollectionCreateDto createCollection);
    CollectionDtoDetail addProductOnCollection(UUID collectionId, List<UUID> productIds);
    CollectionDtoDetail getCollectionById(UUID id);
    CollectionDtoDetail getCollectionBySlug(String slug);
    List<CollectionDto> getAllCollections();
    CollectionDtoDetail updateCollection(UUID id, CollectionUpdateDto dto);
}
