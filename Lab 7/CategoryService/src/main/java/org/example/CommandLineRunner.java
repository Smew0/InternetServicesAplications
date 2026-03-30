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
    private final ConfigurableApplicationContext context;
    private final Scanner scanner = new Scanner(System.in);

    public CommandLineRunner(CategoryService categoryService, ConfigurableApplicationContext context) {
        this.categoryService = categoryService;
        this.context = context;
    }

    @Override
    public void run(String... args) {
        boolean running = true;
        while (running) {
            System.out.println("\nCommands:  | 2 - categories | 7 - exit");

            String cmd = scanner.nextLine();

            switch (cmd) {
                case "2" -> categoryService.findAll().forEach(System.out::println);

                case "7" -> {
                    running = false;
                    int exitCode = SpringApplication.exit(context, () -> 0);
                    System.exit(exitCode);
                }
                default -> System.out.println("Unknown command");
            }
        }
    }

}
