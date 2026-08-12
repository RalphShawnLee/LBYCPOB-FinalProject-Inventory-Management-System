# Inventory Management System

A JavaFX desktop application for tracking products, suppliers, purchase orders, sales orders, and stock movements, built as a final project for LBYCPOB (Object-Oriented Programming Laboratory) at De La Salle University.

**Group 3**
- Lee, Ralph Shawn Christopher
- Neri, John Sean Nicole
- Ramin, Harry Yvan

## Overview

- Work in Progress

## Features

- **Login** — single admin account, SHA-256 hashed password
- **Dashboard** — live low-stock count and total inventory value
- **Products** — add and edit products (SKU, category, price, quantity, reorder threshold)
- **Suppliers** — add and edit supplier records
- **Purchase Orders** — multi-line orders; receiving an order increases product stock
- **Sales Orders** — multi-line orders; fulfilling an order decreases product stock, with a low-stock popup alert the moment a product crosses its reorder threshold
- **Stock Movement audit log** — every quantity change recorded with type (in/out), reference order, and timestamp
- **Reports** — products ranked by total quantity sold (fast/slow movers)
- **Persistence** — all data is saved to `.json` files under `data/`, so nothing is lost when the app closes

> Note: Purchase Orders, Sales Orders, Stock Movements, and Reports are fully implemented but not currently linked from the Dashboard's navigation buttons — only Products and Suppliers are reachable from there right now.

## Tech stack

- Java 21
- JavaFX 21 (Controls + FXML)
- Maven
- JUnit 5

No database and no JSON library dependency, because persistence is hand-rolled against `.json` files using `java.nio.file.Files`.

## Project structure

```
src/main/java/.../inventorymanagementsystemlbycpob/
├── MainApplication.java     entry point, composition root, screen navigation
├── Launcher.java             non-modular launch workaround
├── model/                    domain classes and enums (Product, Supplier, PurchaseOrder, ...)
├── repository/                data access — one interface + InMemory/Json implementation per entity
├── service/                   business logic (validation, stock updates, reporting)
└── controller/                JavaFX controllers, one per screen
```


## Getting started

Requires JDK 21+ and Maven (or use the bundled wrapper).

```bash
./mvnw javafx:run
```

**Default login:** `admin` / `admin123`.

## Running tests

```bash
./mvnw test
```

## Data

Application data lives under `data/` at the project root — `products.json`, `suppliers.json`, `users.json`, `purchase-orders.json`, `sales-orders.json`, and `stock-movements.json`. This folder is generated at runtime and should never be committed.
