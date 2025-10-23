package com.collectibles;

import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.*;

public class App {
    static Gson gson = new Gson();
    static Map<Integer, Map<String, String>> users = new HashMap<>();

    public static void main(String[] args) {

        port(4567);

        // GET /users — Retrieve all users
        get("/users", (req, res) -> {
            res.type("application/json");
            return gson.toJson(users.values());
        });

        // GET /users/:id — Retrieve user by ID
        get("/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Map<String, String> user = users.get(id);
            if (user == null) {
                res.status(404);
                return gson.toJson(Map.of("error", "User not found"));
            }
            return gson.toJson(user);
        });

        // POST /users/:id — Add user
        post("/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Map<String, String> user = gson.fromJson(req.body(), Map.class);
            users.put(id, user);
            res.status(201);
            return gson.toJson(Map.of("message", "User added successfully"));
        });

        // PUT /users/:id — Edit user
        put("/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            if (!users.containsKey(id)) {
                res.status(404);
                return gson.toJson(Map.of("error", "User not found"));
            }
            Map<String, String> updatedUser = gson.fromJson(req.body(), Map.class);
            users.put(id, updatedUser);
            return gson.toJson(Map.of("message", "User updated successfully"));
        });

        // OPTIONS /users/:id — Check existence
        options("/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            boolean exists = users.containsKey(id);
            return gson.toJson(Map.of("exists", exists));
        });

        // DELETE /users/:id — Delete user
        delete("/users/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            if (users.remove(id) == null) {
                res.status(404);
                return gson.toJson(Map.of("error", "User not found"));
            }
            return gson.toJson(Map.of("message", "User deleted successfully"));
        });
    }
}
