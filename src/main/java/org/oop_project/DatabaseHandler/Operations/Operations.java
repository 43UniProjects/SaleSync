package org.oop_project.DatabaseHandler.Operations;

import com.mongodb.client.MongoCollection;
import org.oop_project.DatabaseHandler.DatabaseConnectionManager;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Models.Product;

import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.EMPLOYEE_COLLECTION_NAME;
import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.PRODUCT_COLLECTION_NAME;

/*
* Base class for the collection operations in the database
* */

public class Operations {

    public final DatabaseConnectionManager dbClient = DatabaseConnectionManager.getInstance();

    public void closeConnection() {
        dbClient.close();
    }
}
