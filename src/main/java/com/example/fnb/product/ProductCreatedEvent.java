package com.example.fnb.product;

import com.example.fnb.product.dto.ProductDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ProductCreatedEvent extends ApplicationEvent {
    private ProductDto newProduct;

    public ProductCreatedEvent(Object source, ProductDto newProduct) {
        super(source);
        this.newProduct = newProduct;
    }
}
