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

    @GetMapping("/elements")
    public List<ElementCollectionDto> getAllElements() {
        return elementService.findAll().stream()
                .map(e -> new ElementCollectionDto(e.getId(), e.getName()))
                .toList();
    }

    @GetMapping("/categories/{categoryId}/elements")
    public ResponseEntity<List<ElementCollectionDto>> getElementsByCategory(@PathVariable UUID categoryId) {
        return categoryService.findById(categoryId)
                .map(category -> {
                    List<Element> elements = elementService.findByCategory(category);
                    List<ElementCollectionDto> dtos = elements.stream()
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
            Element element = new Element(UUID.randomUUID(), dto.name(), dto.price(), category);
            Element saved = elementService.save(element);
            return ResponseEntity.ok(new ElementReadDto(
                    saved.getId(), saved.getName(), saved.getPrice(), category.getId()
            ));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/elements/{id}")
    public ResponseEntity<Void> deleteElement(@PathVariable UUID id) {
        elementService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}