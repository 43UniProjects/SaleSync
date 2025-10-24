# 🛒 SaleSync v2

<div align="center">

[![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](./LICENSE)

**A modern Java application for managing store operations, employees, and inventory.**

[Features](#-features) • [Quick Start](#-quick-start) • [Documentation](#-project-structure) • [Contributing](#-contributing)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Prerequisites](#-prerequisites)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [Database Migrations with Mongock](#-database-migrations-with-mongock)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

## 🎯 Overview

**SaleSync** is an educational Java application built for a university OOP course. It demonstrates object-oriented design principles, data modeling, and database operations using the MongoDB Java driver. The application provides a comprehensive system for managing employees (Admin, Cashier, Product Manager) and product inventory.

### Key Features

- 🏗️ **Object-Oriented Design**: Clean separation of concerns with models, operations, and views
- 👥 **Employee Management**: Support for multiple roles (Admin, Cashier, Product Manager)
- 📦 **Product Management**: Complete CRUD operations for inventory
- 🗄️ **MongoDB Integration**: Seamless database operations with POJO mapping
- 🖥️ **Swing GUI**: Simple graphical interface for user interaction


## 🚀 Features

- ✅ **Role-Based Access**: Admin, Cashier, and Product Manager roles with specific permissions
- ✅ **Employee Operations**: Add, search, list, and delete employees
- ✅ **Product Operations**: Manage product inventory with real-time updates
- ✅ **Database Persistence**: MongoDB integration with POJO codec support
- ✅ **Console & GUI Interface**: Both command-line and Swing-based UI options
- ✅ **Type Safety**: Strongly-typed collections and enums for roles and units
- ✅ **Database Migrations**: Mongock integration for versioned schema management

## 📦 Prerequisites

Before running SaleSync, ensure you have the following installed:

| Requirement | Version | Notes |
|------------|---------|-------|
| ☕ **Java JDK** | 25+ | Can be adjusted in `pom.xml` (e.g., to Java 17) |
| 📦 **Maven** | 3.6+ | For building and dependency management |
| 🗄️ **MongoDB** | 4.0+ | Required for data persistence and migrations |
| 🔄 **Mongock** | 5.x | Included in `pom.xml` dependencies |

> **Note**: If you don't have Java 25, update the `maven-compiler-plugin` source/target in `pom.xml` to match your installed JDK version.

### 📚 Maven Dependencies

The project uses the following key dependencies (managed via `pom.xml`):

```xml
<!-- MongoDB Driver -->
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>4.11.0</version>
</dependency>

<!-- Mongock for Database Migrations -->
<dependency>
    <groupId>io.mongock</groupId>
    <artifactId>mongock-standalone</artifactId>
    <version>5.4.0</version>
</dependency>
```

## ⚡ Quick Start

### 1️⃣ Clone the Repository

```powershell
git clone https://github.com/banuka20431/SaleSync.git
cd SaleSync
```

### 2️⃣ Build the Project

```powershell
mvn clean package
```

### 3️⃣ Run the Application

**Option A: Using Maven exec plugin**
```powershell
mvn exec:java -Dexec.mainClass="org.oop_project.Main"
```

**Option B: Running compiled classes directly**
```powershell
java -cp target\classes org.oop_project.Main
```

### 4️⃣ Configure MongoDB (Optional)

If you want to persist data, ensure MongoDB is running:

```powershell
# Start MongoDB service (Windows)
net start MongoDB

# Or use MongoDB Compass / Atlas for cloud hosting
```

The default connection string is `mongodb://localhost:27017` with database name `SaleSync`.

### 5️⃣ Database Migrations (Automatic)

On first run, Mongock will automatically execute database migrations to set up initial collections, indexes, and seed data. Check the console output for migration logs.

## 📁 Project Structure

```
SaleSync/
├── 📄 pom.xml                          # Maven configuration
├── 📄 README.md                        # This file
├── 📄 LICENSE                          # Project license
└── src/
    └── main/
        └── java/
            └── org/
                └── oop_project/
                    ├── 🎯 Main.java                      # Application entry point
                    ├── 📁 View/
                    │   └── SaleSyncApp.java             # Swing GUI interface
                    ├── 📁 DatabaseHandler/
                    │   ├── DatabaseConnectionManager.java  # MongoDB singleton
                    │   ├── 📁 Enums/
                    │   │   ├── Role.java                # Employee roles
                    │   │   └── UnitType.java            # Product unit types
                    │   ├── 📁 Models/
                    │   │   ├── Employee.java            # Base employee model
                    │   │   ├── Admin.java               # Admin role
                    │   │   ├── Cashier.java             # Cashier role
                    │   │   ├── ProductManager.java      # Product Manager role
					│   │   ├── ItemFamily.java      	 # Product Families
					│   │   ├── ProductManager.java      # Product Sub Families
                    │   │   └── Product.java             # Product model
                    │   ├── 📁 Operations/
                    │   │   ├── Operations.java          # Base operations class
                    │   │   ├── EmployeeOperations.java  # Employee CRUD
                    │   │   └── ProductOperations.java   # Product CRUD
                    │   └── 📁 migration/
                    │       ├── DatabaseChangeUnit_001.java.java  
                    │       ├── DatabaseChangeUnit_002.java.java  
                    │      	├── DatabaseChangeUnit_003.java.java  
                    │       └── DatabaseChangeUnit_004.java.java  
```

### 🔑 Key Components

#### **Main.java**
Entry point that initializes the Swing UI, handles console login, validates credentials via `EmployeeOperations`, and manages database connections.

#### **SaleSyncApp.java**
Minimal Swing-based GUI (JFrame) demonstrating UI wiring with labels and buttons.

#### **DatabaseConnectionManager.java**
Singleton wrapper for MongoDB Java driver:
- Configures POJO codec registry
- Provides typed collection access via `getCollection()`
- Default: `mongodb://localhost:27017`, DB name: `SaleSync`

#### **Operations Layer**
- **Operations.java**: Base class with `DatabaseConnectionManager` instance
- **EmployeeOperations.java**: Add, find, list, and delete employees
- **ProductOperations.java**: Product CRUD and ID generation

#### **Models (POJOs)**
- **Employee.java**: Base POJO with `@BsonId`, username, password, role
- **Admin/Cashier/ProductManager.java**: Role-specific extensions
- **Product.java**: Product model with interactive console helpers

#### **Migration (Mongock)**
- **MongockConfig.java**: Configures Mongock runner with MongoDB connection
- **Changesets**: Versioned database migration scripts
  - **InitialSetupChangelog.java**: Creates initial collections and schema
  - **IndexCreationChangelog.java**: Adds database indexes for performance
  - **SeedDataChangelog.java**: Populates initial data (admin user, sample products)

## 🔄 Database Migrations with Mongock

SaleSync uses **Mongock** for managing database schema evolution and data migrations in a versioned, trackable way.

### What is Mongock?

Mongock is a Java-based migration tool for MongoDB that:
- ✅ Tracks which migrations have been executed
- ✅ Ensures migrations run in order
- ✅ Prevents duplicate executions
- ✅ Provides rollback capabilities
- ✅ Integrates seamlessly with Spring and standalone Java apps

### Migration Structure

Migrations are organized as **Changesets** in the `Migration/changesets/` folder:

```java
@ChangeLog(order = "001")
public class InitialSetupChangelog {
    
    @ChangeSet(order = "001", id = "createEmployeesCollection", author = "salesync")
    public void createEmployeesCollection(MongoDatabase db) {
        db.createCollection("employees");
        // Create unique index on username
        db.getCollection("employees")
          .createIndex(Indexes.ascending("username"), 
                      new IndexOptions().unique(true));
    }
}
```

### How Migrations Work

1. **On Application Startup**: Mongock scans the `changesets` package
2. **Check History**: Compares changesets with `mongockChangeLog` collection
3. **Execute New**: Runs any changesets not yet applied
4. **Record Success**: Logs executed changesets to prevent re-execution

### Adding New Migrations

To create a new migration:

1. Create a new class in `Migration/changesets/`
2. Annotate with `@ChangeLog(order = "XXX")`
3. Add methods with `@ChangeSet` annotation
4. Implement migration logic

**Example:**

```java
@ChangeLog(order = "004")
public class AddProductCategoriesChangelog {
    
    @ChangeSet(order = "001", id = "addCategoryField", author = "developer")
    public void addCategoryToProducts(MongoDatabase db) {
        MongoCollection<Document> products = db.getCollection("products");
        
        // Add category field to all existing products
        products.updateMany(
            Filters.exists("category", false),
            Updates.set("category", "UNCATEGORIZED")
        );
    }
}
```

### Migration Best Practices

- 📝 **Order Matters**: Use sequential order numbers (001, 002, 003...)
- 🆔 **Unique IDs**: Each changeset ID must be unique across the project
- 📅 **Never Modify**: Don't change executed changesets; create new ones
- 🧪 **Test First**: Test migrations on development DB before production
- 📚 **Document**: Add comments explaining complex migrations

## ⚙️ Configuration

### Database Settings

The default MongoDB configuration is set in `DatabaseConnectionManager.java` and `MongockConfig.java`:

| Parameter | Default Value | Description |
|-----------|--------------|-------------|
| **Connection String** | `mongodb://localhost:27017` | MongoDB server URL |
| **Database Name** | `SaleSync` | Database for collections |
| **Migration Lock** | `mongockLock` | Collection for migration locking |
| **Change Log** | `mongockChangeLog` | Collection tracking executed migrations |

**To customize:** Edit `DatabaseConnectionManager.java` or externalize to environment variables:

```java
String connectionString = System.getenv("MONGODB_URI") != null 
    ? System.getenv("MONGODB_URI") 
    : "mongodb://localhost:27017";
```

### Java Version

If you need to target a different Java version:

1. Open `pom.xml`
2. Find the `maven-compiler-plugin` section
3. Update `<source>` and `<target>` values:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>17</source>  <!-- Change from 25 to 17 -->
        <target>17</target>  <!-- Change from 25 to 17 -->
    </configuration>
</plugin>
```

### Viewing Migration Status

To check which migrations have been executed, query the MongoDB `mongockChangeLog` collection:

```javascript
// Connect to MongoDB using mongo shell or Compass
use SaleSync

// View all executed migrations
db.mongockChangeLog.find().pretty()

// Check specific migration
db.mongockChangeLog.find({ changeId: "createEmployeesCollection" })
```

**Output example:**
```json
{
  "_id": "InitialSetupChangelog::createEmployeesCollection",
  "changeId": "createEmployeesCollection",
  "author": "salesync",
  "timestamp": ISODate("2025-10-19T10:30:00.000Z"),
  "changeLogClass": "org.oop_project.DatabaseHandler.Migration.changesets.InitialSetupChangelog",
  "changeSetMethod": "createEmployeesCollection",
  "executionMillis": 45,
  "state": "EXECUTED"
}
```

## 💻 Usage

### Console Interface

When you run the application, you'll see a console login prompt:

```
> Welcome to SaleSync
> Username: admin
> Password: ****
> Login successful! Role: ADMIN
> 
> Menu:
> 1) Manage Employees
> 2) Manage Products
> 3) View Reports
> 4) Exit
```

### GUI Interface

The Swing-based GUI (`SaleSyncApp`) provides a graphical interface for managing store operations with buttons and forms for employee and product management.

## 🧪 Testing

Currently, no automated tests are included. To add tests:

1. Create test classes in `src/test/java`
2. Add JUnit dependency to `pom.xml`
3. Run tests with:

```powershell
mvn test
```

**Recommended test structure:**
```
src/test/java/org/oop_project/
├── DatabaseHandler/
│   ├── EmployeeOperationsTest.java
│   └── ProductOperationsTest.java
└── Models/
    ├── EmployeeTest.java
    └── ProductTest.java
```


## 🤝 Contributing

Contributions are welcome! Here's how you can help improve SaleSync:

### Development Workflow

1. **Fork** the repository
2. **Create** a feature branch:
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Commit** your changes:
   ```bash
   git commit -m "Add amazing feature"
   ```
4. **Push** to the branch:
   ```bash
   git push origin feature/amazing-feature
   ```
5. **Open** a Pull Request

### Coding Standards

- Follow existing Java conventions and code style
- Add JavaDoc comments for public methods
- Update this README if you change functionality
- Include unit tests for new features
- Ensure the project builds successfully before submitting PR

### Areas for Contribution

- 🧪 Add unit tests for operations and models
- 🎨 Improve the Swing UI with modern components
- 🔐 Implement authentication and authorization
- 📊 Add reporting and analytics features
- 🌐 Internationalization (i18n) support
- 📝 Improve documentation and code comments

## 📄 License

This project is licensed under the terms specified in the [LICENSE](./LICENSE) file. Please review and respect the license terms when using or contributing to this project.

---

<div align="center">

**Made with ❤️ for OOP learning**

⭐ Star this repo if you find it helpful! ⭐

[Report Bug](https://github.com/banuka20431/SaleSync/issues) • [Request Feature](https://github.com/banuka20431/SaleSync/issues)

</div>

