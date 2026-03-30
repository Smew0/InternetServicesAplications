package org.example;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "categories")
public class Category implements Serializable, Comparable<Category> {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Element> elements = new ArrayList<>();

    protected Category() { } // JPA

    public Category(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addElement(Element e) {
        elements.add(e);
        e.setCategory(this);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public List<Element> getElements() { return elements; }

    @Override
    public int compareTo(Category o) {
        int r = this.name.compareTo(o.name);
        if (r != 0) return r;
        return this.id.compareTo(o.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category c = (Category) o;
        return id.equals(c.id) && name.equals(c.name);
    }
    @Override
    public String toString() {
        return "Category{id='" + id +
                "', name='" + name +
                "}";
    }

}
