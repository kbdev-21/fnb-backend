package com.example.fnb.menu.domain;

import com.example.fnb.menu.domain.repository.CategoryRepository;
import com.example.fnb.menu.event.ProductCreatedEvent;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CategoryEventListener {
    private final CategoryRepository repository;

    public CategoryEventListener(CategoryRepository repository) {
        this.repository = repository;
    }

    @EventListener
    @Async
    public void handleProductCreatedEvent(ProductCreatedEvent event) {
//        var categoryId = event.getNewProduct().getCategoryId();
//        var category = repository.findById(categoryId).orElseThrow(
//            () -> new DomainException(DomainExceptionCode.CATEGORY_NOT_FOUND)
//        );
//        category.setProductsCount(category.getProductsCount() + 1);
//        repository.save(category);
    }
}
