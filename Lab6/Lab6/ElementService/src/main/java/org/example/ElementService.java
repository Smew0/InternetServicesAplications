package org.example;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ElementService {

    private final ElementRepository repo;

    public ElementService(ElementRepository repo) {
        this.repo = repo;
    }

    public List<Element> findAll() {
        return repo.findAll();
    }

    public List<Element> findByCategory(Category category) {
        return repo.findByCategory(category);
    }

    public Element save(Element e) {
        return repo.save(e);
    }

    public void deleteById(UUID id) {
        repo.deleteById(id);
    }
}
