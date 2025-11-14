# 🛒 SaleSync

<div align="center">

**Simple Store Manager Application**

This project was developed to fulfill the requirements of the **Object Oriented Programming** course module **(IIC 1153)** at the University of Sri Jayewardenepura, Faculty of Technology

[Assignment Requirements](#-assignment-requirements) • [Features](#-features) • [Quick Start](#-quick-start) • [Project Structure](#-project-structure)

</div>

---

## 🎯 Overview

**SaleSync** is a Java application designed as a simple store manager system. It serves as a practical demonstration of **Object-Oriented Programming (OOP) principles** and related Java features as required by the **IIC 1153 - Object Oriented Programming** 

The system addresses a practical business or operational problem faced by a local startup company by providing solutions for:
- **Employee Management**
- **Product Inventory Management**
- **Billing System**

### Key Demonstrations

* 🏗️ **OOP Principles**: Application of **Encapsulation**, **Abstraction**, **Inheritance**, and **Polymorphism**
* 📚 **Java Concepts**: Use of **Interfaces & Packages**, **Collections & Generics**, **File Handling**, and **Exception Handling**
* 🗄️ **Data Persistence**: Integration with **MongoDB** for data storage (implied file handling for data).
* 🖥️ **User Interface**: Implementation of a **JavaFx GUI**.
* 👥 **Version Control**: Effective use of **Git/GitHub** for collaborative development

---

## 📄 Assignment Requirements

This project addresses the core requirements of the IIC 1153 Group Assignment[cite: 7, 8]:

* **Problem Identification**: Identify a current operational or management issue faced by a local startup company and propose a software solution
* **OOP Implementation**: Design and implement the solution in Java, applying sound OOP design principles
    * Encapsulation 
    * Abstraction
    * Inheritance 
    * Polymorphism
    * Exception Handling 
    * Collections & Generics 
    * File Handling 
    * Interfaces & Packages
* **Teamwork & VCS**: Demonstrate effective teamwork and use of a Version Control System (Git/GitHub)
    * Commit and push regularly
    * Use descriptive commit messages
    * Demonstrate branch usage and merging.

---

## 🚀 Features

The **SaleSync** application supports the following key functionalities:

* ✅ **Role-Based Access**: Distinguishes between Admin, Cashier, and Product Manager roles.
* ✅ **Employee Management**: CRUD (Create, Read, Update, Delete) operations for employee records.
* ✅ **Product Inventory**: Full CRUD operations to manage product stock and details.
* ✅ **Data Persistence**: Uses a NoSQL database (MongoDB) to store all application data.
* ✅ **GUI Interface**: Provides a user-friendly graphical interface using JavaFx.
* ✅ **Robustness**: Utilizes **exception handling** techniques to build a robust, error-tolerant application[cite: 15].

---

## 📦 Prerequisites

Before running the application, ensure you have the following installed:

| Requirement | Notes |
| :--- | :--- |
| ☕ **Java JDK** | Version 17 or higher. |
| 📦 **Maven** | Version 3.6+ for building and dependency management. |
| 🗄️ **MongoDB** | Running locally or accessible via a connection string. |

> **Note**: If your installed JDK version differs from the project's default, update the `<source>` and `<target>` values in the `maven-compiler-plugin` section of `pom.xml`.

---

## ⚡ Quick Start

Follow these steps to set up and run the SaleSync application:

### 1️⃣ Clone the Repository

```powershell
git clone [https://github.com/banuka20431/SaleSync.git](https://github.com/banuka20431/SaleSync.git)

cd SaleSync
```

### 2️⃣ Build the Project
Use Maven to compile the code and package the application:

```PowerShell
mvn clean package
```
### 3️⃣ Run the Application
You can run the application using the Maven exec plugin:

```PowerShell
mvn exec:java -Dexec.mainClass="org.oop_project.Main"
```

### 4️⃣ Database Setup

Ensure your MongoDB server is running. The application is configured by default to connect to mongodb://localhost:27017 with the database name SaleSync.

### 📁 Project Structure

The project follows a standard package structure demonstrating the separation of concerns (MVC pattern elements) and the application of OOP principles:
```
SaleSync/
├── 📄 LICENSE
├── 📄 pom.xml                               # Maven build configuration
├── 📄 README.md
├── 📁 lib/
│   └── 📄 javafx.properties                 # JavaFX runtime configuration
└── 📁 src/
    └── 📁 main/
        ├── 📁 java/
        │   └── 📁 org/
        │       └── 📁 oop_project/
        │           ├── 🎯 Main.java                    # Launches JavaFX application
        │           ├── 📁 database_handler/            # Data access and business logic
        │           │   ├── DatabaseConnectionManager.java
        │           │   ├── 📁 enums/
        │           │   │   ├── Role.java
        │           │   │   └── UnitType.java
        │           │   ├── 📁 migrations/              # Database versioning
        │           │   │   ├── DatabaseChangeUnit_001.java
        │           │   │   ├── DatabaseChangeUnit_002.java
        │           │   │   ├── DatabaseChangeUnit_003.java
        │           │   │   ├── DatabaseChangeUnit_004.java
        │           │   │   ├── DatabaseChangeUnit_006.java
        │           │   │   └── DatabaseChangeUnit_007.java
        │           │   ├── 📁 models/                  # Domain models (inheritance)
        │           │   │   ├── Admin.java
        │           │   │   ├── Cashier.java
        │           │   │   ├── Employee.java           # Base class
        │           │   │   ├── Product.java
        │           │   │   └── ProductManager.java
        │           │   └── 📁 operations/              # Business operations
        │           │       ├── EmployeeOperations.java
        │           │       ├── Operations.java
        │           │       └── ProductOperations.java
        │           ├── 📁 utils/                       # General utilities
        │           │   ├── Generate.java
        │           │   ├── JsonReader.java
        │           │   └── Text.java
        │           └── 📁 view/                        # JavaFX presentation layer
        │               ├── SaleSyncApp.java            # JavaFX Application subclass
        │               ├── 📁 controllers/             # UI event handlers
        │               │   ├── AdminController.java
        │               │   ├── CashierController.java
        │               │   ├── CheckoutController.java
        │               │   ├── LoginController.java
        │               │   └── ProductController.java
        │               ├── 📁 helpers/                 # UI helper classes
        │               │   ├── BillRow.java
        │               │   ├── EmployeeRow.java
        │               │   ├── Navigators.java
        │               │   ├── ProductRow.java
        │               │   └── Validator.java
        │               └── 📁 view/                    # Nested resources accessors
        │                   └── (see resources section)
        └── 📁 resources/
            └── 📁 org/
                └── 📁 oop_project/
                    └── 📁 view/
                        ├── 📁 css/
                        │   └── style.css
                        ├── 📁 fxml/
                        │   ├── admin-panel.fxml
                        │   ├── cashier-portal.fxml
                        │   ├── checkout.fxml
                        │   ├── login.fxml
                        │   └── product-dashboard.fxml
                        └── 📁 images/                  # Image assets (filenames omitted)
```

### 💻 Usage

The application will launch the JavaFx GUI upon execution. You will first be prompted to log in. A default Admin user is created on the first run for initial access.

**Features Access**

- Admin: Full access to Employee and Product Management.
- Product Manager: Access to Product Inventory Management.
- Cashier: Access to the Billing System.

### 🤝 Contributing (VCS Demo)

As a collaborative group assignment, effective use of Git and GitHub is mandatory to demonstrate teamwork.

Follow these steps to ensure proper Version Control System (VCS) usage:

Work on a Feature Branch: Create a new branch for every task to isolate changes and allow for merging demonstrations.

```Bash
git checkout -b feature/implement-login
```

Commit Regularly: Commit your changes frequently with descriptive commit messages.

```Bash
git commit -m "FEAT: Added basic validation logic to LoginController"
```

Push to GitHub: Keep the collaborative project repository updated.

```Bash
git push origin feature/implement-login
```

Open and Merge a PR: Use Pull Requests (PRs) on GitHub to merge your branch back into main once the feature is complete and reviewed.
