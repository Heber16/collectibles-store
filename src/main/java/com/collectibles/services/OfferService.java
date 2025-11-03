package com.collectibles.services;

import com.collectibles.models.Offer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.*;

public class OfferService {
    private static final String OFFERS_FILE = "src/main/resources/offers.json";
    private static final Gson gson = new Gson();
    private List<Offer> offers;

    public OfferService() {
        this.offers = loadOffers();
    }

    private List<Offer> loadOffers() {
        File file = new File(OFFERS_FILE);

        // If the file does not exist, create it with an empty list
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                saveOffers(new ArrayList<>());
                return new ArrayList<>();
            } catch (IOException e) {
                System.err.println("❌ Error creating offers.json: " + e.getMessage());
                return new ArrayList<>();
            }
        }

        try (Reader reader = new FileReader(file)) {
            List<Offer> list = gson.fromJson(reader, new TypeToken<List<Offer>>(){}.getType());
            return (list != null) ? list : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("❌ Error loading offers: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveOffers(List<Offer> offers) {
        try (Writer writer = new FileWriter(OFFERS_FILE)) {
            gson.toJson(offers, writer);
        } catch (IOException e) {
            System.err.println("❌ Error saving offers: " + e.getMessage());
        }
    }

    public List<Offer> getAllOffers() {
        return new ArrayList<>(offers);
    }

    public List<Offer> getOffersByItemId(String itemId) {
        List<Offer> result = new ArrayList<>();
        for (Offer offer : offers) {
            if (offer.getItemId().equals(itemId)) {
                result.add(offer);
            }
        }
        return result;
    }

    public Optional<Offer> getOfferById(String id) {
        return offers.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    public Offer createOffer(Offer offer) {
        offers.add(offer);
        saveOffers(offers);
        System.out.println("✅ Offer created: " + offer.getId());
        return offer;
    }

    public boolean updateOfferStatus(String id, String status) {
        Optional<Offer> offerOpt = getOfferById(id);
        if (offerOpt.isPresent()) {
            Offer offer = offerOpt.get();
            offer.setStatus(status);
            saveOffers(offers);
            System.out.println("✅ Offer " + id + " status updated to: " + status);
            return true;
        }
        return false;
    }

    public boolean deleteOffer(String id) {
        boolean removed = offers.removeIf(o -> o.getId().equals(id));
        if (removed) {
            saveOffers(offers);
            System.out.println("✅ Offer deleted: " + id);
        }
        return removed;
    }
}