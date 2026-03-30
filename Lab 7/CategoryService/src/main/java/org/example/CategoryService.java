package org.example;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repo;
    private final RestTemplate restTemplate;

    // Hardcoded URL for the Element Service (Port 8081)
    private final String ELEMENT_SERVICE_URL = "http://localhost:8081/api/sync/categories";

    public CategoryService(CategoryRepository repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public List<Category> findAll() {
        return repo.findAll();
    }
    public Optional<Category> findById(UUID id) {
        return repo.findById(id);
    }

    public Category save(Category c) {
        Category saved = repo.save(c);

        // Notify Element Service of Create/Update
        try {
            String url = ELEMENT_SERVICE_URL + "/" + saved.getId();
            // We send a simple DTO body with the name
            restTemplate.put(url, new CategorySyncDto(saved.getName()));
        } catch (Exception e) {
            // Log error
            System.err.println("Failed to sync category create/update: " + e.getMessage());
        }

        return saved;
    }

    public void deleteById(UUID id) {
        // Notify Element Service of Delete
        try {
            String url = ELEMENT_SERVICE_URL + "/" + id;
            restTemplate.delete(url);
        } catch (Exception e) {
            System.err.println("Failed to sync category deletion: " + e.getMessage());
        }

        repo.deleteById(id);
    }

    // DTO for the outgoing request
    public record CategorySyncDto(String name) {}
}