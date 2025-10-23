# Technical Decisions and Changes

## 1. Framework Selection
Chose **Spark Java** for lightweight API routing instead of Spring Boot, since it’s simpler for small web apps.

## 2. Data Structure
Used an in-memory **HashMap** for user storage to simulate CRUD operations without a database.

## 3. JSON Processing
Implemented **Gson** for serialization/deserialization between Java objects and JSON.

## 4. Logging
Configured **Logback** for future logging purposes (not yet integrated in Sprint 1).
