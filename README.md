## 🛍️ Collectibles Store Web App

<<<<<<< HEAD
A Java Spark-based REST API that manages collectible items for an online store.
This project demonstrates route grouping, request handling, and JSON data responses using Spark Framework and Gson.

🛠️ Technologies Used

Java

=======
A Java Spark-based web application for managing collectible items.
This project is part of a multi-stage development challenge and corresponds to Sprint 2, focusing on exception handling, template views, and form integration.

🚀 Features Implemented (Sprint 2)
Feature	Description
A global handler for runtime errors and 404 responses (ExceptionHandlerModule.java).
Implemented HTML templates to display item lists, details, and offer forms.
Added a form for submitting new item offers.
Items are loaded from items.json and rendered dynamically using Spark and Gson.
Conducted a peer review to identify and fix integration or logic issues (see peer-review.md).

🧩 Technologies Used

Java 17+

>>>>>>> c862488 (Sprint 2 - added exception handling, Mustache views, and offer form)
Maven

Spark Framework

<<<<<<< HEAD
=======
Mustache Template Engine

>>>>>>> c862488 (Sprint 2 - added exception handling, Mustache views, and offer form)
Gson

Logback

<<<<<<< HEAD
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
=======
🗂️ Project Structure
src/
└── main/
├── java/
│    └── com/collectibles/
│         ├── App.java
│         └── ExceptionHandlerModule.java
└── resources/
├── items.json
└── templates/
├── items.mustache
├── itemDetail.mustache
├── offer.mustache
└── offerSuccess.mustache
peer-review.md

🌐 Endpoints Overview
Method	Route	Description
GET	/items	Displays the full list of collectible items
GET	/items/:id	Displays details of a specific item
GET	/items/new or /offer	Displays a form to add a new item offer
POST	/items or /offer	Processes a new item offer submission
404	Custom error page for missing routes
500	Custom error response for internal exceptions

🧰 Exception Handling
All application exceptions are managed through the ExceptionHandlerModule.java class:
404 Not Found → returns a simple HTML message
500 Internal Server Error → displays error message and logs details to the console

🖼️ Mustache Templates
Template	Purpose
items.mustache	Displays all available collectibles
itemDetail.mustache	Shows details of a selected item
offer.mustache	Web form for submitting new offers
offerSuccess.mustache	Confirmation page after submitting an offer

🧪 How to Run
Clone this repository
Run the following commands:

mvn clean install
mvn exec:java -Dexec.mainClass="com.collectibles.App"


Open your browser and navigate to:

http://localhost:4567/items
→ item list

http://localhost:4567/items/1
→ item details


🧾 Peer Review Summary

See peer-review.md
for details on:

Issues detected during testing

Proposed fixes

Final review comments
>>>>>>> c862488 (Sprint 2 - added exception handling, Mustache views, and offer form)
