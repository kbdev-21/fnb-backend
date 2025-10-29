package com.example.fnb.product.domain;

import com.example.fnb.category.CategoryService;
import com.example.fnb.category.dto.CategoryDto;
import com.example.fnb.image.domain.entity.Image;
import com.example.fnb.image.domain.repository.ImageRepository;
import com.example.fnb.product.event.ProductCreatedEvent;
import com.example.fnb.product.ProductService;
import com.example.fnb.product.domain.entity.Option;
import com.example.fnb.product.domain.entity.Product;
import com.example.fnb.product.domain.entity.Topping;
import com.example.fnb.product.domain.repository.ProductRepository;
import com.example.fnb.product.dto.*;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.utils.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final CategoryService categoryService;

    private final ProductRepository productRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    public ProductServiceImpl(CategoryService categoryService, ProductRepository productRepository, ApplicationEventPublisher eventPublisher, ModelMapper modelMapper, ImageRepository imageRepository) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
    }

    @Override
    public ProductDto createProduct(ProductCreateDto dto) {
        var category = categoryService.getCategoryById(dto.getCategoryId());

        String slug = StringUtil.createSlug(dto.getName());

        Product newProduct = new Product();
        newProduct.setId(UUID.randomUUID());
        newProduct.setName(dto.getName());
        newProduct.setNormalizedName(StringUtil.normalizeVietnamese(dto.getName()));
        newProduct.setSlug(slug);
        newProduct.setDescription(dto.getDescription());
        newProduct.setBasePrice(dto.getBasePrice());
        newProduct.setComparePrice(dto.getComparePrice());
        newProduct.setImgUrl(dto.getImgUrl());
        newProduct.setUnavailableAtStoreCodes(new HashSet<>());
        newProduct.setCategoryId(category.getId());
        newProduct.setCreatedAt(Instant.now());

        List<Topping> toppings = dto.getToppings()
            .stream().map(t -> createDtoToToppingEntity(t, newProduct))
            .toList();
        newProduct.setToppings(toppings);

        List<Option> options = dto.getOptions()
            .stream().map(optionDto -> createDtoToOptionEntity(optionDto, newProduct))
            .toList();
        newProduct.setOptions(options);

        Product savedProduct = productRepository.save(newProduct);
        var productDto = modelMapper.map(savedProduct, ProductDto.class);

        eventPublisher.publishEvent(new ProductCreatedEvent(this, productDto));

        return mapToDtoFromEntity(savedProduct);
    }

    @Override
    public Page<ProductDto> getProducts(int page, int size, String sortBy) {
        Sort sort;
        if (sortBy.startsWith("-")) {
            sort = Sort.by(sortBy.substring(1)).descending();
        } else {
            sort = Sort.by(sortBy).ascending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> allProducts = productRepository.findAll(pageable);
        var productDtos = mapToDtosFromEntities(allProducts.getContent());

        return new PageImpl<>(productDtos, pageable, allProducts.getTotalElements());
    }

    @Override
    public List<ProductDto> getProductsByIdsIn(List<UUID> productIds) {
        List<Product> allProducts = productRepository.findAllById(productIds);

        return mapToDtosFromEntities(allProducts);
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new DomainException(DomainExceptionCode.PRODUCT_NOT_FOUND));

        return mapToDtoFromEntity(product);
    }

    @Override
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow(() -> new DomainException(DomainExceptionCode.PRODUCT_NOT_FOUND));

        return mapToDtoFromEntity(product);
    }

    @Override
    public ProductDto updateAvailableStatusForProduct(UUID productId, String storeCode, boolean available) {
        var product = productRepository.findById(productId).orElseThrow(
            () -> new DomainException(DomainExceptionCode.PRODUCT_NOT_FOUND)
        );

        if(available) {
            product.getUnavailableAtStoreCodes().remove(storeCode);
        }
        else {
            product.getUnavailableAtStoreCodes().add(storeCode);
        }

        productRepository.save(product);
        return mapToDtoFromEntity(product);
    }

    private ProductDto mapToDtoFromEntity(Product product) {
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        var categoryDto = categoryService.getCategoryById(product.getCategoryId());
        dto.setCategory(categoryDto);
        return dto;
    }

    private List<ProductDto> mapToDtosFromEntities(List<Product> products) {
        if (products.isEmpty()) return List.of();

        Set<UUID> categoryIds = products.stream()
            .map(Product::getCategoryId)
            .collect(Collectors.toSet());

        List<CategoryDto> categoryDtos = categoryService.getCategoriesByIdIns(categoryIds.stream().toList());
        Map<UUID, CategoryDto> categoryMap = categoryDtos.stream()
            .collect(Collectors.toMap(CategoryDto::getId, c -> c));

        return products.stream()
            .map(product -> {
                ProductDto dto = modelMapper.map(product, ProductDto.class);
                dto.setCategory(categoryMap.get(product.getCategoryId()));
                return dto;
            })
            .toList();
    }

    private Option createDtoToOptionEntity(ProductCreateDtoOption dto, Product product) {
        Option option = new Option();
        option.setId(UUID.randomUUID());
        option.setName(dto.getName());
        option.setProduct(product);
        List<Option.Selection> selections = dto.getValues()
                .stream().map(selectionDto -> {
                    var selection = new Option.Selection();
                    selection.setId(UUID.randomUUID());
                    selection.setPriceChange(selectionDto.getPriceChange());
                    selection.setValue(selectionDto.getName());
                    return selection;
                }).toList();
        option.setSelections(selections);
        return option;
    }

    private Topping createDtoToToppingEntity(ProductCreateDtoTopping dto, Product product) {
        var topping = new Topping();
        topping.setName(dto.getName());
        topping.setPriceChange(dto.getPriceChange());
        topping.setId(UUID.randomUUID());
        topping.setProduct(product);
        return topping;
    }
}
