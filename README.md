## 🛍️ Collectibles Store Web App

A Java Spark-based web application for managing collectible items.
This project is part of a multi-stage development challenge and corresponds to Sprint 2, focusing on exception handling, template views, and form integration.

🚀 Features Implemented (Sprint 2)
Feature	Description
Exception Handling Module	A global handler for runtime errors and 404 responses (ExceptionHandlerModule.java).
Mustache Views and Templates	Implemented HTML templates to display item lists, details, and offer forms.
Web Form	Added a form for submitting new item offers.
JSON Integration	Items are loaded from items.json and rendered dynamically using Spark and Gson.
Partial Peer Review	Conducted a peer review to identify and fix integration or logic issues (see peer-review.md).

🧩 Technologies Used
Java 17+
Maven
Spark Framework
Mustache Template Engine
Gson
Logback

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
