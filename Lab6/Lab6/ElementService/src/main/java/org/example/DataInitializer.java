package org.example;

import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Component
public class DataInitializer {

    private final CategoryService categoryService;
    private final ElementService elementService;
    public DataInitializer(CategoryService categoryService, ElementService elementService) {
        this.categoryService = categoryService;
        this.elementService = elementService;

    }

    @PostConstruct
    public void init() {

        Category electronics = new Category(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Electronics");
        Category books = new Category(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Books");
        Category groceries = new Category(UUID.fromString("33333333-3333-3333-3333-333333333333"), "Groceries");

        categoryService.save(electronics);
        categoryService.save(books);
        categoryService.save(groceries);

        Element e1  = new Element(UUID.randomUUID(), "Smartphone", 699.99, electronics);
        Element e2  = new Element(UUID.randomUUID(), "Laptop", 1299.99, electronics);
        Element e3  = new Element(UUID.randomUUID(), "Novel", 19.99, books);
        Element e4  = new Element(UUID.randomUUID(), "Cookbook", 29.50, books);
        Element e5  = new Element(UUID.randomUUID(), "Milk", 1.99, groceries);
        Element e6  = new Element(UUID.randomUUID(), "Cheese", 3.49, groceries);
        Element e7  = new Element(UUID.randomUUID(), "Headphones", 799.99, electronics);
        Element e8  = new Element(UUID.randomUUID(), "Keyboard", 599.99, electronics);
        Element e9  = new Element(UUID.randomUUID(), "Witcher", 15.99, books);
        Element e10 = new Element(UUID.randomUUID(), "Eggs", 5.99, groceries);

        elementService.save(e1);
        elementService.save(e2);
        elementService.save(e3);
        elementService.save(e4);
        elementService.save(e5);
        elementService.save(e6);
        elementService.save(e7);
        elementService.save(e8);
        elementService.save(e9);
        elementService.save(e10);


//        electronics.addElement(e1);
//        electronics.addElement(e2);
//        electronics.addElement(e7);
//        electronics.addElement(e8);
//
//        books.addElement(e3);
//        books.addElement(e4);
//        books.addElement(e9);
//
//        groceries.addElement(e5);
//        groceries.addElement(e6);
//        groceries.addElement(e10);

    }
}
