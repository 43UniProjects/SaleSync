# SaleSync

 Application for managing store day-to-day work load. Built with Java and MongoDB driver.

## Project overview

 SaleSync is a small object-oriented project intended for a university OOP course. It demonstrates basic application structure, data models, and operations for employees (Admin/Cashier/ProductManager) and products. The project uses the MongoDB Java driver as a dependency (configured in `pom.xml`)

 Main features
 - Models for Admin, Cashier, Employee, Product and ProductManager
 - Operations packages for employee and product related actions
 - A simple console/view entrypoint in `org.oop_project.Main` / `org.oop_project.View.SaleSyncApp`

## Prerequisites

 - Java 25 JDK installed and available on PATH
 - Maven installed (or use the Maven wrapper if you add one)
 - (Optional) MongoDB instance if you want to test database integration

 Note: The `pom.xml` sets maven compiler target/source to Java 25. If you don't have JDK 25, change the values in `pom.xml` to match your installed JDK (for example `17`).

## Build and run (Windows PowerShell)

 Open PowerShell in the project root (where `pom.xml` is located) and run:

 ```powershell
 # Compile
 mvn clean package

 # Run the application (executes the main class configured in your IDE/build)
 mvn exec:java -Dexec.mainClass="org.oop_project.Main"
 ```

 If you prefer to run the compiled JAR (after `mvn package`) and your build produces an executable JAR, run:

 ```powershell
 java -cp target\classes org.oop_project.Main
 ```

 Adjust the main class name if your project uses `org.oop_project.View.SaleSyncApp` as the entrypoint.

## Project structure

This project follows a simple package layout under `src/main/java/org/oop_project`.

Top-level packages

- `org.oop_project` — application entrypoint and orchestration:
	- `Main.java` — the program main class. Starts the Swing UI (`SaleSyncApp`) on the EDT, shows a console login prompt, uses `UserOperations` to validate credentials, performs a role switch, and closes DB connections on exit.

- `org.oop_project.View` — employee interface code:
	- `SaleSyncApp.java` — a minimal Swing-based GUI (JFrame) used as the application's window. Contains a demo label and button and demonstrates how a simple view would be wired up.

- `org.oop_project.DatabaseHandler` — database integration and domain models:
	- `DatabaseConnectionManager.java` — a singleton wrapper around the MongoDB Java driver. It configures a POJO codec registry, exposes `getCollection(...)` for typed collections, and provides `getInstance()` and `close()` methods. Default connection string is `mongodb://localhost:27017` and DB name is `SaleSync`.

Sub-packages and important classes

- `org.oop_project.DatabaseHandler.Operations` — database operation helpers:
	- `Operations.java` — base class that holds a `DatabaseConnectionManager` instance and references to typed `MongoCollection<User>` and `MongoCollection<Product>`. Provides `closeConnection()` to close the DB client.
	- `EmployeeOperations.java` — employee-specific operations: add a employee, find by username, get a employee, list all employees, and delete by name. Uses MongoDB filters to query collections.
	- `ProductOperations.java` — product-specific operations: add a product and obtain the last product id (helper used by `Product` when creating new records).

- `org.oop_project.DatabaseHandler.Models` — domain model POJOs (persisted as MongoDB documents):
	- `Employee.java` — main employee POJO with fields: `ObjectId id` (annotated with `@BsonId`), `username`, `password`, `role`. Includes a no-arg constructor and convenience constructor, plus getters/setters — ready to be used with MongoDB POJO codec.
	- `Admin.java`, `Cashier.java`, `ProductManager.java` — role-specific classes that extend `Employee`. They currently re-declare fields and constructors (they can be simplified by inheriting fields directly from `User`), but are included to model different employee roles.
	- `Product.java` — product model with `productId`, `name`, `price`, and `stockQuantity`. Includes static console helper methods `productPortal(User)` and `add()` that interactively collect data and call `ProductOperations` to insert products.

Other notes

- The `pom.xml` declares the MongoDB synchronous driver. Compiler level is set to Java 25; update `pom.xml` if you target a different JDK.

## How to use

 1. Build the project using Maven as shown above.
 2. Run the application. It should present a console UI (if `SaleSyncApp` is the active view class) where you can interact with employees and products.
 3. For database-backed tests, ensure `DatabaseConnectionManager` is configured to connect to a running MongoDB instance and update any connection strings in code or via environment variables.

## Testing

 There are no automated tests included in this repository by default. You can add JUnit tests under `src/test/java` and run them with `mvn test`.

## Contributing

 Contributions are welcome. Suggested workflow:

 1. Fork the repository
 2. Create a feature branch: `git checkout -b feature/your-feature`
 3. Make changes and add tests
 4. Create a pull request with a clear description of the change

 Coding style: Follow the existing code conventions in the repository. If you update the Java version in `pom.xml`, document it in this README.

## License

 This project includes a top-level `LICENSE` file. Respect the license terms when using or contributing to this project.

