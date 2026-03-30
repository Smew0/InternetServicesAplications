package org.example.controller;

import org.example.*;
import org.example.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ElementController {
    private final ElementService elementService;
    private final CategoryService categoryService;

    public ElementController(ElementService elementService, CategoryService categoryService) {
        this.elementService = elementService;
        this.categoryService = categoryService;
    }
    // Get all elements
    @GetMapping("/elements")
    public ResponseEntity<List<ElementCollectionDto>> getAllElements() {
        List<ElementCollectionDto> elements = elementService.findAll().stream()
                .map(e -> new ElementCollectionDto(e.getId(), e.getName()))
                .toList();
        return ResponseEntity.ok(elements);
    }

    @GetMapping("/categories/{categoryId}/elements")
    public ResponseEntity<List<ElementCollectionDto>> getElementsByCategory(@PathVariable UUID categoryId) {
        return categoryService.findById(categoryId)
                .map(category -> {
                    List<ElementCollectionDto> dtos = elementService.findByCategory(category).stream()
                            .map(e -> new ElementCollectionDto(e.getId(), e.getName()))
                            .toList();
                    return ResponseEntity.ok(dtos);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/categories/{categoryId}/elements")
    public ResponseEntity<ElementReadDto> createElement(
            @PathVariable UUID categoryId,
            @RequestBody ElementCreateUpdateDto dto) {

        return categoryService.findById(categoryId).map(category -> {
            // Create element with the found local category
            Element element = new Element(UUID.randomUUID(), dto.name(), dto.price(), category);
            Element saved = elementService.save(element);

            return ResponseEntity.ok(new ElementReadDto(
                    saved.getId(), saved.getName(), saved.getPrice(), category.getId()
            ));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete an Element
    @DeleteMapping("/elements/{id}")
    public ResponseEntity<Void> deleteElement(@PathVariable UUID id) {
        // Check if exists first (optional, but good practice)
        try {
            elementService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}