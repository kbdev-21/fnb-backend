package com.example.fnb.product.domain;

import com.example.fnb.category.CategoryService;
import com.example.fnb.product.ProductCreatedEvent;
import com.example.fnb.product.ProductService;
import com.example.fnb.product.domain.entity.Option;
import com.example.fnb.product.domain.entity.Product;
import com.example.fnb.product.domain.entity.Topping;
import com.example.fnb.product.domain.repository.ProductRepository;
import com.example.fnb.product.dto.*;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.utils.StringUtil;
import com.example.fnb.store.StoreService;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    private final CategoryService categoryService;

    private final ProductRepository productRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(CategoryService categoryService, ProductRepository productRepository, ApplicationEventPublisher eventPublisher, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
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

        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public Page<ProductDto> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> allProducts = productRepository.findAll(pageable);
        var productDtos = allProducts.stream().map(
                product -> modelMapper.map(product, ProductDto.class)
        ).toList();

        return new PageImpl<>(productDtos, pageable, allProducts.getTotalElements());
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new DomainException(DomainExceptionCode.PRODUCT_NOT_FOUND));

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow(() -> new DomainException(DomainExceptionCode.PRODUCT_NOT_FOUND));

        return modelMapper.map(product, ProductDto.class);
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
        return modelMapper.map(product, ProductDto.class);
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
