package com.collectibles;

import static spark.Spark.*;
import com.collectibles.models.Item;
import com.collectibles.models.Offer;
import com.collectibles.services.OfferService;
import com.collectibles.websocket.PriceWebSocketServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.FileReader;
import java.io.Reader;
import java.time.Instant;
import java.util.*;

public class App {

    static Gson gson = new Gson();
    static PriceWebSocketServer wsServer;
    static OfferService offerService;

    public static void main(String[] args) {
        port(4567);

        ExceptionHandlerModule.register();

        offerService = new OfferService();

        wsServer = new PriceWebSocketServer(8081);
        wsServer.start();

        // Load items
        List<Item> items = loadItems();
        items.forEach(i -> i.setNumericPrice(i.getNumericPrice()));

        // ================ API ENDPOINTS ================

        path("/api", () -> {

            // GET /api/items - With filters (q, minPrice, maxPrice)
            get("/items", (req, res) -> {
                res.type("application/json");

                String q = Optional.ofNullable(req.queryParams("q")).orElse("").trim().toLowerCase();
                String minStr = req.queryParams("minPrice");
                String maxStr = req.queryParams("maxPrice");

                Double minPrice = null;
                Double maxPrice = null;

                try {
                    if (minStr != null && !minStr.isEmpty()) minPrice = Double.parseDouble(minStr);
                    if (maxStr != null && !maxStr.isEmpty()) maxPrice = Double.parseDouble(maxStr);
                } catch (NumberFormatException ex) {
                    res.status(400);
                    return gson.toJson(Map.of("error", "minPrice/maxPrice must be numeric"));
                }

                List<Map<String, Object>> result = new ArrayList<>();
                for (Item it : items) {
                    boolean matches = true;

                    // Filter by text
                    if (!q.isEmpty()) {
                        String combined = (it.getName() + " " + it.getDescription()).toLowerCase();
                        if (!combined.contains(q)) matches = false;
                    }

                    // Filter by price
                    if (minPrice != null && it.getNumericPrice() < minPrice) matches = false;
                    if (maxPrice != null && it.getNumericPrice() > maxPrice) matches = false;

                    if (matches) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", it.getId());
                        map.put("name", it.getName());
                        map.put("price", it.getPrice());
                        map.put("numericPrice", it.getNumericPrice());
                        result.add(map);
                    }
                }
                return gson.toJson(result);
            });

            // GET /api/items/:id
            get("/items/:id", (req, res) -> {
                res.type("application/json");
                String id = req.params(":id");
                Optional<Item> item = findItemById(items, id);
                if (item.isPresent()) {
                    return gson.toJson(item.get());
                }
                res.status(404);
                return gson.toJson(Map.of("error", "Item not found"));
            });

            // PUT /api/items/:id/price - Update price
            put("/items/:id/price", (req, res) -> {
                res.type("application/json");
                String id = req.params(":id");

                Map<String, Object> payload;
                try {
                    payload = gson.fromJson(req.body(), new TypeToken<Map<String, Object>>(){}.getType());
                } catch (Exception e) {
                    res.status(400);
                    return gson.toJson(Map.of("error", "Invalid JSON"));
                }

                if (payload == null || !payload.containsKey("price")) {
                    res.status(400);
                    return gson.toJson(Map.of("error", "Missing price field"));
                }

                double newPrice;
                try {
                    Object p = payload.get("price");
                    newPrice = Double.parseDouble(String.valueOf(p));
                } catch (Exception e) {
                    res.status(400);
                    return gson.toJson(Map.of("error", "price must be numeric"));
                }

                Optional<Item> itemOpt = findItemById(items, id);
                if (itemOpt.isPresent()) {
                    Item item = itemOpt.get();
                    item.setNumericPrice(newPrice);

                    // Notify change via WebSocket
                    Map<String, Object> updateMsg = Map.of(
                            "type", "price_update",
                            "id", item.getId(),
                            "price", item.getPrice(),
                            "numericPrice", item.getNumericPrice(),
                            "timestamp", Instant.now().toString()
                    );
                    wsServer.broadcastPriceUpdate(updateMsg);

                    return gson.toJson(Map.of("message", "Price updated", "item", item));
                }

                res.status(404);
                return gson.toJson(Map.of("error", "Item not found"));
            });

            // ===== OFFERS API =====

            // GET /api/offers
            get("/offers", (req, res) -> {
                res.type("application/json");
                String itemId = req.queryParams("itemId");

                List<Offer> offers = (itemId != null)
                        ? offerService.getOffersByItemId(itemId)
                        : offerService.getAllOffers();

                return gson.toJson(offers);
            });

            // POST /api/offers - Create offer
            post("/offers", (req, res) -> {
                res.type("application/json");
                try {
                    Offer offer = gson.fromJson(req.body(), Offer.class);
                    if (offer.getCustomerName() == null || offer.getCustomerEmail() == null) {
                        res.status(400);
                        return gson.toJson(Map.of("error", "Missing required fields"));
                    }
                    Offer created = offerService.createOffer(offer);
                    return gson.toJson(created);
                } catch (Exception e) {
                    res.status(400);
                    return gson.toJson(Map.of("error", "Invalid offer data"));
                }
            });

        }); // end path /api

        // ================ WEB VIEWS ================

        // Homepage - List of items with filters
        get("/", (req, res) -> {
            res.redirect("/items");
            return null;
        });

        get("/items", (req, res) -> {
            String q = Optional.ofNullable(req.queryParams("q")).orElse("");
            String minStr = req.queryParams("minPrice");
            String maxStr = req.queryParams("maxPrice");

            Double minPrice = null;
            Double maxPrice = null;

            try {
                if (minStr != null && !minStr.isEmpty()) minPrice = Double.parseDouble(minStr);
                if (maxStr != null && !maxStr.isEmpty()) maxPrice = Double.parseDouble(maxStr);
            } catch (NumberFormatException e) {
                // Ignore invalid filters
            }

            List<Item> filtered = filterItems(items, q, minPrice, maxPrice);

            Map<String, Object> model = new HashMap<>();
            model.put("items", filtered);
            model.put("searchQuery", q);
            model.put("minPrice", minStr != null ? minStr : "");
            model.put("maxPrice", maxStr != null ? maxStr : "");
            model.put("hasFilters", !q.isEmpty() || minPrice != null || maxPrice != null);

            return new ModelAndView(model, "items.mustache");
        }, new MustacheTemplateEngine());

        // Item detail
        get("/items/:id", (req, res) -> {
            String id = req.params(":id");
            Optional<Item> itemOpt = findItemById(items, id);

            if (itemOpt.isPresent()) {
                Map<String, Object> model = new HashMap<>();
                model.put("item", itemOpt.get());
                return new ModelAndView(model, "itemDetail.mustache");
            }

            halt(404, "Item not found");
            return null;
        }, new MustacheTemplateEngine());

        // Price update form
        get("/items/:id/price", (req, res) -> {
            String id = req.params(":id");
            Optional<Item> itemOpt = findItemById(items, id);

            if (itemOpt.isPresent()) {
                Map<String, Object> model = new HashMap<>();
                model.put("item", itemOpt.get());
                return new ModelAndView(model, "priceForm.mustache");
            }

            halt(404, "Item not found");
            return null;
        }, new MustacheTemplateEngine());

        // Update price (POST from form)
        post("/items/:id/price", (req, res) -> {
            String id = req.params(":id");
            String priceStr = req.queryParams("price");

            if (priceStr == null || priceStr.isEmpty()) {
                halt(400, "Missing price");
            }

            double newPrice = Double.parseDouble(priceStr);
            Optional<Item> itemOpt = findItemById(items, id);

            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();
                item.setNumericPrice(newPrice);

                // Notify via WebSocket
                Map<String, Object> updateMsg = Map.of(
                        "type", "price_update",
                        "id", item.getId(),
                        "price", item.getPrice(),
                        "numericPrice", item.getNumericPrice(),
                        "timestamp", Instant.now().toString()
                );
                wsServer.broadcastPriceUpdate(updateMsg);

                res.redirect("/items/" + id);
                return null;
            }

            halt(404, "Item not found");
            return null;
        });

        // ===== OFFERS =====

        //Form to create offer
        get("/offer", (req, res) -> {
            String itemId = req.queryParams("itemId");
            Map<String, Object> model = new HashMap<>();

            if (itemId != null) {
                Optional<Item> itemOpt = findItemById(items, itemId);
                if (itemOpt.isPresent()) {
                    model.put("item", itemOpt.get());
                    model.put("preselected", true);
                }
            }

            model.put("items", items);
            return new ModelAndView(model, "offer.mustache");
        }, new MustacheTemplateEngine());

        // Process offer form
        post("/offer", (req, res) -> {
            String itemId = req.queryParams("itemId");
            String customerName = req.queryParams("customerName");
            String customerEmail = req.queryParams("customerEmail");
            String offerAmountStr = req.queryParams("offerAmount");
            String message = req.queryParams("message");

            // Validations
            if (itemId == null || customerName == null || customerEmail == null || offerAmountStr == null) {
                halt(400, "Missing required fields");
            }

            double offerAmount = Double.parseDouble(offerAmountStr);
            Optional<Item> itemOpt = findItemById(items, itemId);

            if (!itemOpt.isPresent()) {
                halt(404, "Item not found");
            }

            Item item = itemOpt.get();
            Offer offer = new Offer(itemId, item.getName(), customerName, customerEmail, offerAmount, message);
            offerService.createOffer(offer);

            res.redirect("/offers?success=true");
            return null;
        });

        // See all offers
        get("/offers", (req, res) -> {
            List<Offer> allOffers = offerService.getAllOffers();
            Map<String, Object> model = new HashMap<>();
            model.put("offers", allOffers);
            model.put("success", req.queryParams("success") != null);
            return new ModelAndView(model, "offers.mustache");
        }, new MustacheTemplateEngine());

        System.out.println("🚀 Server started at http://localhost:4567");
        System.out.println("📡 WebSocket running at ws://localhost:8081");
    }

    // ============ AUXILIARY METHODS ============

    private static List<Item> loadItems() {
        try (Reader reader = new FileReader("src/main/resources/items.json")) {
            List<Item> list = gson.fromJson(reader, new TypeToken<List<Item>>(){}.getType());
            return (list != null) ? list : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("❌ Error loading items: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static Optional<Item> findItemById(List<Item> items, String id) {
        return items.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    private static List<Item> filterItems(List<Item> items, String query, Double minPrice, Double maxPrice) {
        List<Item> result = new ArrayList<>();
        String q = query.toLowerCase();

        for (Item item : items) {
            boolean matches = true;

            if (!q.isEmpty()) {
                String combined = (item.getName() + " " + item.getDescription()).toLowerCase();
                if (!combined.contains(q)) matches = false;
            }

            if (minPrice != null && item.getNumericPrice() < minPrice) matches = false;
            if (maxPrice != null && item.getNumericPrice() > maxPrice) matches = false;

            if (matches) result.add(item);
        }

        return result;
    }
}