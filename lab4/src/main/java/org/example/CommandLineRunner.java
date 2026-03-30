package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class CommandLineRunner implements org.springframework.boot.CommandLineRunner {

    private final CategoryService categoryService;
    private final ElementService elementService;
    private final ConfigurableApplicationContext context;
    private final Scanner scanner = new Scanner(System.in);

    public CommandLineRunner(CategoryService categoryService, ElementService elementService, ConfigurableApplicationContext context) {
        this.categoryService = categoryService;
        this.elementService = elementService;
        this.context = context;
    }

    @Override
    public void run(String... args) {
        boolean running = true;
        while (running) {
            System.out.println("\nCommands: 1 - list_all | 2 - categories | 3 - elements | 4 - list_by_category | 5 - add | 6 - delete | 7 - exit");

            String cmd = scanner.nextLine();

            switch (cmd) {
                case "1" -> listAll();
                case "2" -> categoryService.findAll().forEach(System.out::println);
                case "3" -> elementService.findAll().forEach(System.out::println);
                case "4" -> listElementsByCategory();
                case "5" -> addElement();
                case "6" -> deleteElement();
                case "7" -> {
                    running = false;
                    int exitCode = SpringApplication.exit(context, () -> 0);
                    System.exit(exitCode);
                }
                default -> System.out.println("Unknown command");
            }
        }
    }

    private void addElement() {

        System.out.print("Element name: ");
        String elementName = scanner.nextLine().trim();

        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());

        List<Category> categories = categoryService.findAll();

        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }

        System.out.println("Available categories:");
        categories.forEach(c -> System.out.println(c.getName()));

        System.out.print("Category name: ");
        String categoryName = scanner.nextLine().trim();

        Category category = categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Element element = new Element(UUID.randomUUID(), elementName, price, category);

        elementService.save(element);

        System.out.println("Element added.");
    }

    private void listAll() {
        categoryService.findAll().forEach(category -> {
            System.out.println(category);

            elementService.findByCategory(category)
                    .forEach(e -> System.out.println("  " + e));
        });
    }

    private void listElementsByCategory() {

        List<Category> categories = categoryService.findAll();

        if (categories.isEmpty()) {System.out.println("No categories");return;}

        categories.forEach(c -> System.out.println(c.getName()));

        System.out.print("Enter category name: ");
        String categoryName = scanner.nextLine().trim();

        Category category = categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));


        List<Element> elements = elementService.findByCategory(category);


        if (elements.isEmpty()) {System.out.println("  no elements");return;}

        elements.forEach(e -> System.out.println("  " + e));
    }

    private void deleteElement() {

        System.out.print("Element name to delete: ");
        elementService.findAll().forEach(System.out::println);
        String name = scanner.nextLine().trim();

        Element element = elementService.findAll().stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Element not found"));

        elementService.deleteById(element.getId());

        System.out.println("Element deleted.");
    }
}
