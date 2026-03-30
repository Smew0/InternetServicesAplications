package org.example;

import java.io.Serializable;

class ElementDTO implements Serializable, Comparable<ElementDTO> {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final double price;
    private final String categoryId;

    private ElementDTO(String id, String name, double price, String categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategoryId() { return categoryId; }

    @Override
    public int compareTo(ElementDTO o) {
        int r = this.id.compareTo(o.id);
        if (r != 0) return r;
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return "ElementDTO{id='" + id +
                "', name='" + name +
                "', price=" + price +
                ", categoryId='" + categoryId +
                "'}";
    }

    public static class Builder {
        private String id;
        private String name;
        private double price;
        private String categoryId;

        public Builder id(String id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder price(double price) { this.price = price; return this; }
        public Builder categoryId(String categoryId) { this.categoryId = categoryId; return this; }

        public ElementDTO build() {
            return new ElementDTO(id, name, price, categoryId);
        }
    }
}
