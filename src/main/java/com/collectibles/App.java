package com.collectibles;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import com.google.gson.reflect.TypeToken;

public class App {

    static Gson gson = new Gson();

    static class Item {
        String id;
        String name;
        String description;
        String price;
    }

    public static void main(String[] args) {

        port(4567);

        List<Item> items = loadItems();

        path("/items", () -> {

            // GET /items
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

            // GET /items/:id
            get("/:id", (req, res) -> {
                res.type("application/json");
                String id = req.params(":id");

                for (Item i : items) {
                    if (i.id.equals(id)) {
                        return gson.toJson(Map.of(
                                "id", i.id,
                                "name", i.name,
                                "description", i.description
                        ));
                    }
                }
                res.status(404);
                return gson.toJson(Map.of("error", "Item not found"));
            });

        });
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

