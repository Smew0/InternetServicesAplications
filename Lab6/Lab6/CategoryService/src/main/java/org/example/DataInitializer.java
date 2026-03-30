package org.example;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Component
public class DataInitializer {

    private final CategoryService categoryService;

    public DataInitializer(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostConstruct
    public void init() {

        Category electronics = new Category(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Electronics");
        Category books = new Category(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Books");
        Category groceries = new Category(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Groceries");


        categoryService.save(electronics);
        categoryService.save(books);
        categoryService.save(groceries);
    }
}
