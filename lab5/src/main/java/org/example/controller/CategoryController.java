package org.example.controller;

import org.example.*;
import org.example.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryCollectionDto> getAll() {
        return service.findAll().stream()
                .map(c -> new CategoryCollectionDto(c.getId(), c.getName()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryReadDto> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(c -> ResponseEntity.ok(new CategoryReadDto(c.getId(), c.getName())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CategoryReadDto create(@RequestBody CategoryCreateUpdateDto dto) {
        Category category = service.save(new Category(UUID.randomUUID(), dto.name()));
        return new CategoryReadDto(category.getId(), category.getName());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return service.findById(id).map(category -> {
            service.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}