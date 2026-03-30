package org.example.controller;

import org.example.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sync/categories")
public class CategorySyncController {

    private final CategoryService categoryService;
    private final ElementRepository elementRepository; // Direct repo access for bulk delete is efficient here

    public CategorySyncController(CategoryService categoryService, ElementRepository elementRepository) {
        this.categoryService = categoryService;
        this.elementRepository = elementRepository;
    }

    // 1. EVENT: Category Created or Updated
    @PutMapping("/{id}")
    public ResponseEntity<Void> syncCategory(@PathVariable UUID id, @RequestBody CategorySyncDto dto) {
        categoryService.findById(id).ifPresentOrElse(
                existing -> {
                    // We recreate it to update the name (since setters were missing in your entity)
                    // In a real app, adding a setName() setter is better.
                    Category updated = new Category(id, dto.name());
                    categoryService.save(updated);
                },
                () -> {
                    categoryService.save(new Category(id, dto.name()));
                }
        );
        return ResponseEntity.ok().build();
    }

    // 2. EVENT: Category Deleted
    @DeleteMapping("/{id}")
    @Transactional // Required for deleteByCategory
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        return categoryService.findById(id).map(category -> {
            // Requirement: "remove associated elements"
            elementRepository.deleteByCategory(category);

            // Then remove the local category record
            categoryService.deleteById(id);

            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    public record CategorySyncDto(String name) {}
}