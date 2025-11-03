package com.collectibles.models;

public class Item {
    private String id;
    private String name;
    private String description;
    private String price;
    private double numericPrice;

    public Item() {}

    public Item(String id, String name, String description, String price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.numericPrice = parsePrice(price);
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPrice() { return price; }
    public void setPrice(String price) {
        this.price = price;
        this.numericPrice = parsePrice(price);
    }

    public double getNumericPrice() { return numericPrice; }
    public void setNumericPrice(double numericPrice) {
        this.numericPrice = numericPrice;
        this.price = String.format("$%.2f USD", numericPrice);
    }

    private double parsePrice(String priceStr) {
        if (priceStr == null) return 0.0;
        String cleaned = priceStr.replaceAll("[^0-9.,]", "");
        cleaned = cleaned.replace(",", ".");
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}