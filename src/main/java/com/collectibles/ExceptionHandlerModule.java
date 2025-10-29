package com.collectibles;

import static spark.Spark.*;

public class ExceptionHandlerModule {
    public static void register() {
        exception(Exception.class, (e, req, res) -> {
            res.status(500);
            res.body("Internal Server Error: " + e.getMessage());
            e.printStackTrace();
        });

        notFound((req, res) -> {
            res.type("text/html");
            return "<h1>404 - Not Found</h1><p>The requested resource was not found.</p>";
        });
    }
}
