package org.example;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "elements")
public class Element implements Serializable, Comparable<Element> {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    protected Element() { } // JPA

    public Element(UUID id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    void setCategory(Category category) {
        this.category = category;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Category getCategory() { return category; }

    @Override
    public int compareTo(Element o) {
        int r = Double.compare(this.price, o.price);
        if (r != 0) return r;
        r = this.name.compareTo(o.name);
        if (r != 0) return r;
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return "Category{id='" + id +
                "', name='" + name +
                "', price=" + price +
                "}";
    }
}
