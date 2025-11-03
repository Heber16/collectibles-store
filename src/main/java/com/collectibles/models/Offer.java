package com.collectibles.models;

import java.time.Instant;

public class Offer {
    private String id;
    private String itemId;
    private String itemName;
    private String customerName;
    private String customerEmail;
    private double offerAmount;
    private String message;
    private String status; // "pending", "accepted", "rejected"
    private String timestamp;

    public Offer() {
        this.timestamp = Instant.now().toString();
        this.status = "pending";
    }

    public Offer(String itemId, String itemName, String customerName, String customerEmail,
                 double offerAmount, String message) {
        this();
        this.id = generateId();
        this.itemId = itemId;
        this.itemName = itemName;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.offerAmount = offerAmount;
        this.message = message;
    }

    private String generateId() {
        return "offer_" + System.currentTimeMillis();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public double getOfferAmount() { return offerAmount; }
    public void setOfferAmount(double offerAmount) { this.offerAmount = offerAmount; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getFormattedAmount() {
        return String.format("$%.2f USD", offerAmount);
    }
}