package com.example.fnb.collection.domain;

import com.example.fnb.collection.CollectionService;
import com.example.fnb.collection.domain.entity.Collection;
import com.example.fnb.collection.domain.entity.ProductCollection;
import com.example.fnb.collection.domain.repository.CollectionRepository;
import com.example.fnb.collection.dto.CollectionCreateDto;
import com.example.fnb.collection.dto.CollectionDto;
import com.example.fnb.collection.dto.CollectionDtoDetail;
import com.example.fnb.product.domain.entity.Product;
import com.example.fnb.product.domain.repository.ProductRepository;
import com.example.fnb.product.dto.ProductDto;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.modelmapper.ModelMapper;
import com.example.fnb.shared.utils.StringUtil;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CollectionServiceImpl  implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository, ModelMapper modelMapper, ProductRepository productRepository) {
        this.collectionRepository = collectionRepository;
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    @Override
    public CollectionDto createCollection(CollectionCreateDto dto) {
        String slug = StringUtil.createSlug(dto.getName());

        Collection newCollection = new Collection();
        newCollection.setId(UUID.randomUUID());
        newCollection.setName(dto.getName());
        newCollection.setSlug(slug);
        newCollection.setDescription(dto.getDescription());
        newCollection.setImgUrl(dto.getImgUrl());
        newCollection.setCreatedAt(Instant.now());
        newCollection.setSortOrder(0);
        newCollection.setProductsCount(dto.getProductIds() != null ? dto.getProductIds().size() : 0);

        if (dto.getProductIds() != null && !dto.getProductIds().isEmpty()) {
            List<ProductCollection> pcs = dto.getProductIds().stream().map(pid -> {
                ProductCollection pc = new ProductCollection();
                pc.setId(UUID.randomUUID());
                pc.setCollectionId(newCollection.getId());
                pc.setProductId(pid);
                return pc;
            }).toList();
            newCollection.setProductCollections(pcs);
        }

        Collection savedCollection = collectionRepository.save(newCollection);

        CollectionDto result = modelMapper.map(savedCollection, CollectionDto.class);

        List<UUID> productIds = savedCollection.getProductCollections().stream()
                .map(ProductCollection::getProductId)
                .toList();
        result.setProductIds(productIds);

        return result;
    }

    @Override
    public CollectionDtoDetail addProductOnCollection(UUID collectionId, List<UUID> productIds) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.COLLECTION_NOT_FOUND));

        if (productIds == null || productIds.isEmpty()) {
            throw new DomainException(DomainExceptionCode.EMPTY_VALUE);
        }

        List<Product> products = productRepository.findAllById(productIds);
        if (products.isEmpty()) {
            throw new DomainException(DomainExceptionCode.PRODUCT_NOT_FOUND);
        }

        List<UUID> existingProductIds = collection.getProductCollections().stream()
                .map(ProductCollection::getProductId)
                .toList();

        List<ProductCollection> newProductCollections = products.stream()
                .filter(p -> !existingProductIds.contains(p.getId()))
                .map(p -> {
                    ProductCollection pc = new ProductCollection();
                    pc.setId(UUID.randomUUID());
                    pc.setCollectionId(collectionId);
                    pc.setProductId(p.getId());
                    return pc;
                })
                .toList();

        collection.getProductCollections().addAll(newProductCollections);

        collection.setProductsCount(collection.getProductCollections().size());

        Collection saved = collectionRepository.save(collection);

        CollectionDtoDetail dto = modelMapper.map(saved, CollectionDtoDetail.class);

        List<ProductDto> productDtos = productRepository.findAllById(
                saved.getProductCollections().stream().map(ProductCollection::getProductId).toList()
        ).stream().map(p -> modelMapper.map(p, ProductDto.class)).toList();

        dto.setProducts(productDtos);
        return dto;
    }


    @Override
    public CollectionDtoDetail getCollectionById(UUID id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.COLLECTION_NOT_FOUND));

        CollectionDtoDetail dto = modelMapper.map(collection, CollectionDtoDetail.class);

        List<UUID> productIds = collection.getProductCollections().stream()
                .map(ProductCollection::getProductId)
                .toList();

        if (!productIds.isEmpty()) {
            List<Product> products = productRepository.findAllById(productIds);
            List<ProductDto> productDtos = products.stream()
                    .map(p -> modelMapper.map(p, ProductDto.class))
                    .toList();
            dto.setProducts(productDtos);
        }

        return dto;
    }

    @Override
    public CollectionDtoDetail getCollectionBySlug(String slug) {
        Collection collection = collectionRepository.findBySlug(slug)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.COLLECTION_NOT_FOUND));

        CollectionDtoDetail dto = modelMapper.map(collection, CollectionDtoDetail.class);

        List<UUID> productIds = collection.getProductCollections().stream()
                .map(ProductCollection::getProductId)
                .toList();

        if (!productIds.isEmpty()) {
            List<Product> products = productRepository.findAllById(productIds);
            List<ProductDto> productDtos = products.stream()
                    .map(p -> modelMapper.map(p, ProductDto.class))
                    .toList();
            dto.setProducts(productDtos);
        }

        return dto;
    }


    @Override
    public List<CollectionDto> getAllCollections() {
        List<Collection> collections = collectionRepository.findAll();

        return collections.stream().map(collection -> {
            CollectionDto dto = modelMapper.map(collection, CollectionDto.class);

            List<UUID> productIds = collection.getProductCollections().stream()
                    .map(ProductCollection::getProductId)
                    .toList();
            dto.setProductIds(productIds);

            return dto;
        }).toList();
    }
}
