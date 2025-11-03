package com.collectibles;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;

/**
 * Centralized module for exception handling in the application
 */
public class ExceptionHandlerModule {

    private static final MustacheTemplateEngine templateEngine = new MustacheTemplateEngine();

    public static void register() {
        // Handler for 404 - Resource not found
        notFound((req, res) -> {
            res.type("text/html");
            Map<String, Object> model = new HashMap<>();
            model.put("error", "404 - Page not found");
            model.put("message", "The page you are looking for does not exist.");
            model.put("path", req.pathInfo());
            return templateEngine.render(new ModelAndView(model, "error.mustache"));
        });

        // Handler for 500 - Internal Server Error
        internalServerError((req, res) -> {
            res.type("text/html");
            Map<String, Object> model = new HashMap<>();
            model.put("error", "500 - Internal server error");
            model.put("message", "Something went wrong. Please try again later.");
            return templateEngine.render(new ModelAndView(model, "error.mustache"));
        });

        // Generic exception handler
        exception(Exception.class, (e, req, res) -> {
            System.err.println("⚠️ Exception caught:");
            e.printStackTrace();

            res.status(500);
            res.type("text/html");
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Application error");
            model.put("message", "An unexpected error occurred: " + e.getMessage());
            model.put("details", e.getClass().getSimpleName());
            res.body(templateEngine.render(new ModelAndView(model, "error.mustache")));
        });

        // Specific Handler for NumberFormatException
        exception(NumberFormatException.class, (e, req, res) -> {
            System.err.println("⚠️ NumberFormatException: " + e.getMessage());
            res.status(400);
            res.type("text/html");
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Invalid format number");
            model.put("message", "Please, enter a valid number.");
            res.body(templateEngine.render(new ModelAndView(model, "error.mustache")));
        });

        // Handler para NullPointerException
        exception(NullPointerException.class, (e, req, res) -> {
            System.err.println("⚠️ NullPointerException:");
            e.printStackTrace();
            res.status(500);
            res.type("text/html");
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Data error");
            model.put("message", "Missing required information.");
            res.body(templateEngine.render(new ModelAndView(model, "error.mustache")));
        });

        System.out.println("✅ Exception handlers registered");
    }
}