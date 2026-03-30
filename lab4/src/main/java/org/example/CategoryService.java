package org.example;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> findAll() {
        return repo.findAll();
    }

    public Optional<Category> findById(UUID id) {
        return repo.findById(id);
    }

    public Optional<Category> findByName(String name) {
        return repo.findByName(name);
    }

    public Category save(Category c) {
        return repo.save(c);
    }
}

