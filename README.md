# 🏆 Sports Matches API

This is a RESTful API built with **Spring Boot** and **Kotlin** designed to provide match data, betting odds, and analytical insights for sports events.

---

## ✅ 1. Features

- 📅 Get today's matches
- 📊 Access match odds 
- 🔍 Retrieve match insights
- 🏆 Browse available leagues

---

## ✅ 2. Tech Stack

- **Kotlin** (JVM)
- **Spring Boot** (Web, Actuator)
- **Gradle**
- **Docker** (optional)

---

## ✅ 3. Getting Started

### Prerequisites

- Java 
- Gradle 
- Docker 

## Backend Endpoints
| Endpoint                 | Method | Description            |
| ------------------------ | ------ | ---------------------- |
| `/matches/today`         | GET    | Get today's matches    |
| `/matches/{id}`          | GET    | Get match metadata     |
| `/matches/{id}/odds`     | GET    | Get 1X2 and O/U odds   |
| `/matches/{id}/insights` | GET    | Get all 20 insights    |
| `/leagues`               | GET    | List available leagues |
