# Collectibles Store API

A Java Spark-based REST API that manages collectible items for an online store.
This project demonstrates route grouping, request handling, and JSON data responses using Spark Framework and Gson.

🛠️ Technologies Used

Java

Maven

Spark Framework

Gson

Logback

🚀 Features

Load collectible items from a JSON file (items.json).

Return a list of all collectibles with their name, price, and ID.

Retrieve detailed information (including description) for a specific collectible by ID.

Organized route grouping under /items.

📡 API Endpoints
Method	Route	Description
GET	/items	Returns a list of all collectibles (ID, name, and price)
GET	/items/:id	Returns details (name and description) of a specific collectible
🧠 Project Explanation

This API uses Spark’s path() function to group routes logically under /items.
Each route returns JSON responses generated with Gson.
The data source (items.json) is stored in the src/main/resources folder and is automatically loaded when the application starts.

Example responses:

GET /items
[
  {"id": "1", "name": "Gorra autografiada por Peso Pluma", "price": "$621.34 USD"},
  {"id": "2", "name": "Casco autografiado por Rosalía", "price": "$734.57 USD"}
]

GET /items/3
{
  "id": "3",
  "name": "Chamarra de Bad Bunny",
  "description": "Una chamarra de la marca favorita de Bad Bunny, autografiada por el propio artista."
}

▶️ Run Instructions

Clone this repository

Run:

mvn clean install


Execute the App class (e.g., from IntelliJ)

Open your browser or Postman and visit:

http://localhost:4567/items


or

http://localhost:4567/items/1

📁 Project Structure
src/
 ├─ main/
 │   ├─ java/com/collectibles/App.java
 │   └─ resources/items.json
 └─ test/
pom.xml
README.md
