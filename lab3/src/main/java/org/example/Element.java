package org.example;
import java.io.Serializable;
import java.util.Objects;


class Element implements Serializable, Comparable<Element> {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final double price;
    private Category category;

    private Element(String id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getId() { return id; }
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
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Element)) return false;
        Element e = (Element) o;
        return id.equals(e.id) && name.equals(e.name);
    }

    @Override
    public String toString() {
        return "Element{id='" + id +
                "', name='" + name +
                "', price=" + price +
                ", category=" +
                (category == null ? "null" : category.getName()) + "}";
    }

    // Builder
    public static class Builder {
        private String id;
        private String name;
        private double price;
        private Category category;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder price(double price) { this.price = price; return this; }
        public Builder category(Category category) { this.category = category; return this; }

        public Element build() {
            return new Element(id, name, price, category);
        }
    }
}