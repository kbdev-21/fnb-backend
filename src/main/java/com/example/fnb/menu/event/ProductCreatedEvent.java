package com.example.fnb.menu.event;

import com.example.fnb.menu.dto.ProductDto;
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
