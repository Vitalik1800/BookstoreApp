# 📚 Bookstore Management System

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)
![JDBC](https://img.shields.io/badge/JDBC-Database-red)
![Swing](https://img.shields.io/badge/Java%20Swing-GUI-green)
![OOP](https://img.shields.io/badge/OOP-Principles-yellow)
![Status](https://img.shields.io/badge/Status-Completed-success)

Desktop application for managing a bookstore, developed in Java using Swing and MySQL.

The system provides management of sellers, buyers and purchases, supports CRUD operations and generates sales statistics.

---

## 🚀 Features

### 👨‍💼 Seller Management

- Add seller
- Edit seller
- Delete seller
- View seller list

### 🛒 Buyer Management

- Add buyer
- Edit buyer
- Delete buyer
- View buyer list

### 📖 Purchase Management

- Add purchase
- Edit purchase
- Delete purchase
- View purchase history

### 📊 Statistics

- Total number of sellers
- Total number of buyers
- Total sales amount
- Most popular book
- Biggest spender
- Books sold by genre

---

## 🛠️ Tech Stack

| Technology | Description |
|------------|------------|
| Java | Core application logic |
| Swing | Desktop graphical interface |
| JDBC | Database connectivity |
| MySQL | Data storage |
| SQL | Data manipulation |
| OOP | Object-oriented design |
| Git | Version control |

---

## 📂 Project Structure

```text
BookstoreManagementSystem
│
├── src
│   ├── Main.java
│   ├── Database.java
│   ├── Seller.java
│   ├── Buyer.java
│   └── Purchase.java
│
├── database
│   └── bookstore.sql
│
├── screenshots
│   ├── AddPurchaseScreen.png
│   ├── AddSellerScreen.png
│   ├── BuyersScreen.png
│   ├── MainMenu.png
│   ├── PurchasesScreen.png
│   ├── SellersScreen.png
│   ├── SellersScreenAfterAddingData.png
│   └── StatisticsScreen.png
│
└── README.md
```

---

## 🗄️ Database Structure

The application uses a MySQL database named:

```sql
bookstore
```

Main tables:

### sellers

| Field | Type |
|---------|---------|
| id | INT |
| store_name | VARCHAR |
| full_name | VARCHAR |
| address | VARCHAR |
| phone | VARCHAR |
| shift | VARCHAR |
| track_number | VARCHAR |

### buyer

| Field | Type |
|---------|---------|
| id | INT |
| storeName | VARCHAR |
| fullName | VARCHAR |
| phoneNumber | VARCHAR |
| purchaseId | INT |

### purchase

| Field | Type |
|---------|---------|
| purchaseId | INT |
| bookTitle | VARCHAR |
| quantity | INT |
| cost | DOUBLE |
| genre | VARCHAR |

---

## 📸 Screenshots

All screenshots can be found in:

screenshots/

Recommended screenshots:

### 🏠 Main Window

Shows the main application interface.

### 👨‍💼 Add Seller

Form for creating a new seller.

### 🛒 Add Buyer

Form for creating a new buyer.

### 📖 Add Purchase

Form for creating a new purchase.

### 📋 Sellers Table

List of all sellers.

### 📋 Buyers Table

List of all buyers.

### 📋 Purchases Table

List of all purchases.

### 📊 Statistics Dashboard

Displays:

- Number of sellers
- Number of buyers
- Most popular book
- Biggest spender
- Total sales amount

---

## ⚙️ Installation

### 1. Clone repository

```bash
git clone https://github.com/Vitalik1800/BookstoreApp
```

### 2. Create database

```sql
CREATE DATABASE bookstore;
```

### 3. Import SQL script

```sql
SOURCE bookstore.sql;
```

### 4. Configure database connection

Open:

```java
Database.java
```

Update credentials:

```java
private static final String URL =
        "jdbc:mysql://localhost:3306/bookstore";

private static final String USER =
        "root";

private static final String PASSWORD =
        "your_password";
```

### 5. Run project

Run:

```java
Main.java
```

---

## 🎯 Learning Objectives

This project was developed to practice:

- Java programming
- Object-Oriented Programming
- JDBC
- SQL
- MySQL integration
- Desktop application development
- CRUD operations
- Data analysis and reporting

---

## 📈 Implemented Statistics

The system calculates:

### 💰 Total Sales Amount

```sql
SELECT SUM(cost * quantity)
FROM purchase;
```

### 📖 Most Popular Book

```sql
SELECT bookTitle,
SUM(quantity)
FROM purchase
GROUP BY bookTitle;
```

### 🏆 Biggest Spender

```sql
SELECT buyer.fullName,
SUM(purchase.cost * purchase.quantity)
FROM buyer
JOIN purchase
ON buyer.purchaseId = purchase.purchaseId;
```

---

## 🔒 Security

The application uses:

✅ JDBC PreparedStatement

```java
PreparedStatement stmt =
    conn.prepareStatement(query);
```

to protect against SQL Injection attacks.

---

## 👨‍💻 Author

**Vitalii Semchyshyn**

Junior Java Developer

GitHub:
https://github.com/Vitalik1800

LinkedIn:
https://linkedin.com/in/віталій-семчишин-1557292a8

---

## ⭐ Project Highlights

- Java Desktop Application
- Swing GUI
- JDBC Integration
- MySQL Database
- Full CRUD Functionality
- Sales Analytics
- OOP Design
- Portfolio Project

---
⭐ If you found this project interesting, feel free to star the repository.
