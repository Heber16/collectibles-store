# Collectibles Store API

A Java Spark-based REST API that manages users for an online collectibles store.

## Technologies
- Java
- Maven
- Spark Framework
- Gson
- Logback

## Endpoints
| Method | Route | Description |
|--------|--------|-------------|
| GET | /users | Retrieve all users |
| GET | /users/:id | Retrieve user by ID |
| POST | /users/:id | Add user |
| PUT | /users/:id | Edit user |
| OPTIONS | /users/:id | Check user existence |
| DELETE | /users/:id | Delete user |

## Run Instructions
1. Clone this repository  
2. Run `mvn clean install`  
3. Execute the `App` class  
4. Open your browser and visit `http://localhost:4567/users`
