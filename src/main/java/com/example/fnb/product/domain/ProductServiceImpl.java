package com.example.fnb.product.domain;

import com.example.fnb.product.ProductService;
import com.example.fnb.product.domain.entity.Option;
import com.example.fnb.product.domain.entity.OptionSelection;
import com.example.fnb.product.domain.entity.Product;
import com.example.fnb.product.dto.*;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.shared.utils.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto createProduct(ProductCreateDto dto) {
        String slug = StringUtil.normalizeVietnamese(dto.getName())
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");

        // tao product moi tu product create dto
        Product newProduct = new Product();
        newProduct.setId(UUID.randomUUID());
        newProduct.setName(dto.getName());
        newProduct.setNormalizedName(StringUtil.normalizeVietnamese(dto.getName()));
        newProduct.setSlug(slug);
        newProduct.setDescription(dto.getDescription());
        newProduct.setBasePrice(dto.getBasePrice());
        newProduct.setComparePrice(dto.getComparePrice());
        newProduct.setImgUrl(dto.getImgUrl());
        newProduct.setCreatedAt(Instant.now());

        // tao toppings moi tu product create dto topping
        List<Product.Topping> toppings = dto.getToppings()
                .stream().map(
                        dtoTopping -> createDtoToToppingEntity(dtoTopping)
                ).toList();
        newProduct.setToppings(toppings);

        // tao option moi tu product create dto option
        List<Option> options = dto.getOptions()
                .stream().map(
                        optionDto -> createDtoToOptionEntity(optionDto, newProduct)
                ).toList();
        newProduct.setOptions(options);

        Product savedProduct = productRepository.save(newProduct);

        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream().map(
                product -> modelMapper.map(product, ProductDto.class)
        ).toList();
    }

    @Override
    public ProductDto getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND));

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug).orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND));

        return modelMapper.map(product, ProductDto.class);
    }

    private Option createDtoToOptionEntity(ProductCreateDtoOption dto, Product product) {
        Option option = new Option();
        option.setId(UUID.randomUUID());
        option.setName(dto.getName());
        option.setProduct(product);
        List<OptionSelection> selections = dto.getValues()
                .stream().map(selectionDto -> {
                    OptionSelection selection = new OptionSelection();
                    selection.setId(UUID.randomUUID());
                    selection.setPriceChange(selectionDto.getPriceChange());
                    selection.setValue(selectionDto.getName());
                    selection.setOption(option);
                    return selection;
                }).toList();
        option.setSelections(selections);
        return option;
    }

    private Product.Topping createDtoToToppingEntity(ProductCreateDtoTopping dto) {
        var topping = new Product.Topping();
        topping.setName(dto.getName());
        topping.setPriceChange(dto.getPriceChange());
        topping.setId(UUID.randomUUID());
        return topping;
    }
}
