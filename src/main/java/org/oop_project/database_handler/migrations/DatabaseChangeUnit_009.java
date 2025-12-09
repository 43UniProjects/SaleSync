package org.oop_project.database_handler.migrations;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;

import java.util.Arrays;

import org.bson.Document;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "ChangeProductValidatorSchema2", order = "009", author = "SaleSync")
public class DatabaseChangeUnit_009 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {

        System.out.println("- Changing Product Validation Schema 2...");

        Document newProductJsonSchema = buildProductSchema();

        Document collModCommand = new Document("collMod", "Product")
                .append("validator", newProductJsonSchema)
                .append("validationLevel", "strict")
                .append("validationAction", "error");

        db.runCommand(collModCommand);
    }

    // Optional: Define rollback logic to undo the migration if it fails or is rolled back
    @RollbackExecution
    public void rollback(MongoDatabase database) {

    }

    private Document buildProductSchema() {
        
        // Changed the schema to accept both double and int data types for unitCost, retailPrice, discountRate and, taxRate

        return new Document("$jsonSchema", new Document()
                .append("required", Arrays.asList(
                                "id", "name", "description", "itemFamily", "itemSubFamily",
                                "availableQuantity", "minimumStock", "maxStock", "stockLocation", "lastOrderDate",
                                "supplierId", "unitCost", "taxRate", "discountRate", "retailPrice", "isActive"
                        )
                )
                .append("properties", new Document()
                        .append("id", new Document()
                                .append("bsonType", "string")
                                .append("description", "unique product identifier")
                        )
                        .append("name", new Document()
                                .append("bsonType", "string")
                                .append("description", "name must be a string")
                        )
                        .append("description", new Document()
                                .append("bsonType", "string")
                                .append("description", "description must be a string")
                        )
                        .append("itemFamily", new Document()
                                .append("bsonType", "string")
                                .append("description", "item family must be a string")
                        )
                        .append("itemSubFamily", new Document()
                                .append("bsonType", "string")
                                .append("description", "item sub family must be a string")
                        )
                        .append("availableQuantity", new Document()
                                .append("bsonType", "int")
                                .append("minimum", 0)
                                .append("description", "available quantity must be a non-negative integer")
                        )
                        .append("minimumStock", new Document()
                                .append("bsonType", "int")
                                .append("description", "minimum stock must be a non-negative integer")
                        )
                        .append("maxStock", new Document()
                                .append("bsonType", "int")
                                .append("description", "maximum stock must be a non-negative integer")
                        )
                        .append("stockLocation", new Document()
                                .append("bsonType", "string")
                                .append("description", "stock location identifier")
                        )
                        .append("supplierId", new Document()
                                .append("bsonType", "string")
                                .append("description", "ID of the primary supplier")
                        )
                        .append("lastOrderDate", new Document()
                                .append("bsonType", "date")
                                .append("description", "date of the arrival of last stock")
                        )
                         .append("unitCost", new Document()
                                .append("bsonType", Arrays.asList("double", "int"))
                                .append("minimum", 0.0)
                                .append("description", "unit cost must be a non-negative double")
                        )
                        .append("taxRate", new Document()
                                .append("bsonType", Arrays.asList("double", "int"))
                                .append("minimum", 0.0)
                                .append("maximum", 1.0)
                                .append("description", "tax rate must be a double between 0 and 1")
                        )
                        .append("discountRate", new Document()
                                .append("bsonType", Arrays.asList("double", "int"))
                                .append("minimum", 0.0)
                                .append("maximum", 1.0)
                                .append("description", "discount must be a non-negative double")
                        )
                        .append("retailPrice", new Document()
                                .append("bsonType", Arrays.asList("double", "int"))
                                .append("minimum", 0.0)
                                .append("description", "retail price must be a non-negative double")
                        )
                        .append("isActive", new Document()
                                .append("bsonType", "bool")
                                .append("description", "product availability status")
                        )
                )
        );

    }

}
