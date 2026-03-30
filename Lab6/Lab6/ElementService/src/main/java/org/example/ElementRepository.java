package org.example;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ElementRepository extends JpaRepository<Element, UUID> {
    List<Element> findByCategory(Category category);

    // New method for cascading delete
    void deleteByCategory(Category category);
}
