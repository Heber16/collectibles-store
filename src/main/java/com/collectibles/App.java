package com.collectibles;

import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class App {

    static Gson gson = new Gson();

    static class Item {
        String id;
        String name;
        String description;
        String price;
    }

    public static void main(String[] args) {

        ExceptionHandlerModule.register();

        port(4567);

        List<Item> items = loadItems();

        path("/api/items", () -> {

            // GET /api/items
            get("", (req, res) -> {
                res.type("application/json");

                List<Map<String, Object>> basicInfo = new ArrayList<>();
                for (Item i : items) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", i.id);
                    data.put("name", i.name);
                    data.put("price", i.price);
                    basicInfo.add(data);
                }
                return gson.toJson(basicInfo);
            });

            // GET /api/items/:id
            get("/:id", (req, res) -> {
                res.type("application/json");
                String id = req.params(":id");

                for (Item i : items) {
                    if (i.id.equals(id)) {
                        return gson.toJson(Map.of(
                                "id", i.id,
                                "name", i.name,
                                "description", i.description,
                                "price", i.price
                        ));
                    }
                }
                res.status(404);
                return gson.toJson(Map.of("error", "Item not found"));
            });
        });

        path("/items", () -> {

            get("", (req, res) -> {
                Map<String, Object> model = new HashMap<>();
                model.put("items", items);
                return new ModelAndView(model, "items.mustache");
            }, new MustacheTemplateEngine());

            get("/:id", (req, res) -> {
                String id = req.params(":id");
                for (Item i : items) {
                    if (i.id.equals(id)) {
                        return new ModelAndView(i, "itemDetail.mustache");
                    }
                }
                res.status(404);
                return new ModelAndView(Map.of("error", "Item not found"), "error.mustache");
            }, new MustacheTemplateEngine());
        });

        get("/offer", (req, res) -> new ModelAndView(null, "offer.mustache"), new MustacheTemplateEngine());

        post("/offer", (req, res) -> {
            String name = req.queryParams("name");
            String itemId = req.queryParams("itemId");
            String amount = req.queryParams("amount");

            Map<String, Object> model = Map.of("name", name, "itemId", itemId, "amount", amount);
            return new ModelAndView(model, "offerSuccess.mustache");
        }, new MustacheTemplateEngine());
    }

    private static List<Item> loadItems() {
        try (Reader reader = new FileReader("src/main/resources/items.json")) {
            return gson.fromJson(reader, new TypeToken<List<Item>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
