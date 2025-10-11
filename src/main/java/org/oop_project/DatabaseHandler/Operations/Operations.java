package org.oop_project.DatabaseHandler.Operations;

import com.mongodb.client.MongoCollection;
import org.oop_project.DatabaseHandler.DatabaseConnectionManager;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Models.Product;

/*
* Base class for the collection operations in the database
* */

public class Operations {

    public DatabaseConnectionManager dbClient = DatabaseConnectionManager.getInstance();

    MongoCollection<Employee> employeeCollection = dbClient.getCollection("Employee", Employee.class);
    MongoCollection<Product> productCollection = dbClient.getCollection("Product", Product.class);

    public void closeConnection() {
        dbClient.close();
    };
}
