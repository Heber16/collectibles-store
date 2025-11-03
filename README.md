## 🎨 Collectibles Store

A web application for managing collectible items with real-time price updates, built with Java Spark Framework and WebSockets.

## 📋 Features

### Sprint 2
- **Exception Handling Module**: Centralized error management for 404, 500, and custom exceptions
- **Views & Templates**: Mustache templates for all pages with embedded CSS
- **Web Forms**: Complete offer management system with form validation

### Sprint 3
- **Advanced Filters**: Search items by text, minimum price, and maximum price
- **Real-time Updates**: WebSocket integration for live price modifications
- **Quality Assurance**: Comprehensive checklist and testing procedures

## 🛠️ Technology Stack

- **Backend**: Java with Spark Framework (v2.9.4)
- **Template Engine**: Mustache
- **WebSocket**: Jetty WebSocket Server (v9.4.51)
- **Data Serialization**: Gson (v2.10.1)
- **Data Storage**: JSON files

## 📁 Project Structure

```
collectibles-store/
├── src/main/
│   ├── java/com/collectibles/
│   │   ├── App.java                          # Main application
│   │   ├── ExceptionHandlerModule.java       # Exception handling
│   │   ├── models/
│   │   │   ├── Item.java                     # Item model
│   │   │   └── Offer.java                    # Offer model
│   │   ├── services/
│   │   │   └── OfferService.java             # Business logic for offers
│   │   └── websocket/
│   │       └── PriceWebSocketServer.java     # WebSocket server
│   └── resources/
│       ├── items.json                         # Items database
│       ├── offers.json                        # Offers database
│       └── templates/
│           ├── items.mustache                 # Items list with filters
│           ├── itemDetail.mustache            # Item detail view
│           ├── priceForm.mustache             # Price update form
│           ├── offer.mustache                 # Create offer form
│           ├── offers.mustache                # Offers list
│           └── error.mustache                 # Error page
└── pom.xml                                    # Maven dependencies
```

## 🚀 Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd collectibles-store
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn exec:java -Dexec.mainClass="com.collectibles.App"
```

4. Access the application:
- **Web Interface**: http://localhost:4567
- **WebSocket**: ws://localhost:8081

## 📡 API Endpoints

### Items Endpoints

#### Web Views (HTML)
- `GET /items` - List all items with filters
- `GET /items/:id` - View item details
- `GET /items/:id/price` - Price update form
- `POST /items/:id/price` - Submit price update

#### REST API (JSON)
- `GET /api/items?q=&minPrice=&maxPrice=` - List items with filters
  - **Query Parameters**:
    - `q`: Search text (searches in name and description)
    - `minPrice`: Minimum price filter
    - `maxPrice`: Maximum price filter
- `GET /api/items/:id` - Get item by ID
- `PUT /api/items/:id/price` - Update item price
  - **Body**: `{"price": 750.00}`

### Offers Endpoints

#### Web Views (HTML)
- `GET /offer` - Create offer form
- `POST /offer` - Submit new offer
- `GET /offers` - View all offers

#### REST API (JSON)
- `GET /api/offers?itemId=` - List all offers (optionally filter by item)
- `POST /api/offers` - Create new offer
  - **Body**:
  ```json
  {
    "itemId": "1",
    "itemName": "Item Name",
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "offerAmount": 500.00,
    "message": "Optional message"
  }
  ```

### WebSocket

- `ws://localhost:8081` - Real-time price updates
  - **Message Format**:
  ```json
  {
    "type": "price_update",
    "id": "1",
    "price": "$750.00 USD",
    "numericPrice": 750.00,
    "timestamp": "2025-11-03T10:30:00Z"
  }
  ```

## 🔍 Features in Detail

### 1. Item Filtering
Search and filter items using multiple criteria:
- **Text Search**: Search by item name or description
- **Price Range**: Filter by minimum and maximum price
- **Combined Filters**: Use all filters simultaneously

Example:
```
GET /items?q=autograph&minPrice=500&maxPrice=800
```

### 2. Real-time Price Updates
- WebSocket connection established automatically on page load
- Price changes broadcast to all connected clients instantly
- Updates reflected without page refresh
- Works on both item list and detail pages

### 3. Offer Management
- Create offers for any item
- Required fields: customer name, email, and offer amount
- Optional message field for additional context
- All offers stored persistently in JSON
- View all submitted offers with status tracking

### 4. Exception Handling
Centralized error management with custom handlers:
- **404 Not Found**: Missing pages or items
- **500 Internal Server Error**: Server-side issues
- **400 Bad Request**: Invalid input (e.g., non-numeric prices)
- **Custom Exceptions**: NumberFormatException, NullPointerException
- User-friendly error pages with details

## 🧪 Testing

### Manual Testing Checklist

#### Filters
- [ ] Test text search functionality
- [ ] Test minimum price filter
- [ ] Test maximum price filter
- [ ] Test combined filters
- [ ] Test clearing filters

#### Price Updates
- [ ] Update price via web form
- [ ] Update price via API
- [ ] Verify real-time update in another browser tab
- [ ] Test invalid price inputs

#### Offers
- [ ] Submit offer with all fields
- [ ] Submit offer without required fields (should fail)
- [ ] View all offers
- [ ] Verify persistence in offers.json

#### Error Handling
- [ ] Access non-existent item (404)
- [ ] Submit invalid price format
- [ ] Test server error scenarios

### API Testing with curl

**Get items with filters:**
```bash
curl "http://localhost:4567/api/items?q=gorra&minPrice=500&maxPrice=700"
```

**Update item price:**
```bash
curl -X PUT http://localhost:4567/api/items/1/price \
  -H "Content-Type: application/json" \
  -d '{"price": 750.00}'
```

**Create offer:**
```bash
curl -X POST http://localhost:4567/api/offers \
  -H "Content-Type: application/json" \
  -d '{
    "itemId": "1",
    "itemName": "Item Name",
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "offerAmount": 600.00,
    "message": "Great item!"
  }'
```

## 📊 Data Format

### items.json
```json
[
  {
    "id": "1",
    "name": "Signed Cap",
    "description": "A cap signed by a famous artist",
    "price": "$621.34 USD"
  }
]
```

### offers.json
```json
[
  {
    "id": "offer_1730635200000",
    "itemId": "1",
    "itemName": "Signed Cap",
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "offerAmount": 600.00,
    "message": "Great item!",
    "status": "pending",
    "timestamp": "2025-11-03T10:30:00Z"
  }
]
```

## 🔧 Configuration

### Ports
- **HTTP Server**: 4567 (configurable in `App.java`)
- **WebSocket Server**: 8081 (configurable in `App.java`)

### File Locations
- **Items Database**: `src/main/resources/items.json`
- **Offers Database**: `src/main/resources/offers.json`
- **Templates**: `src/main/resources/templates/`

## 🐛 Troubleshooting

### WebSocket Connection Issues
- Ensure port 8081 is not blocked by firewall
- Check browser console for connection errors
- Verify WebSocket server starts successfully (check logs)

### Template Not Found
- Verify templates are in `src/main/resources/templates/`
- Check template names match exactly (case-sensitive)
- Ensure Mustache dependency is in pom.xml

### Items Not Loading
- Verify `items.json` exists in `src/main/resources/`
- Check JSON format is valid
- Review console logs for parsing errors

## 🔮 Future Enhancements

- User authentication and authorization
- Database integration (PostgreSQL/MySQL)
- Image upload for items
- Email notifications for offers
- Admin dashboard for offer management
- Search result pagination
- Sorting options (by price, name, date)
- Offer status updates (accept/reject)
- Item categories and tags
- Shopping cart functionality

---

**Note**: This application uses in-memory data storage with JSON files. For production use, consider implementing a proper database solution.
