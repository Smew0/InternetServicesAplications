package org.example;

import java.io.*;
import java.util.*;

/*
 * Category: business class that has one-to-many relationship to Element.
 */

public class Category implements Serializable, Comparable<Category> {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final List<Element> elements = new ArrayList<>();

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Element> getElements() { return elements; }

    public void addElement(Element e) {
        if (e == null) return;
        if (!elements.contains(e)) {
            elements.add(e);
        }
    }

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
                "', elementCount=" + elements.size() +
                "}";
    }
}

