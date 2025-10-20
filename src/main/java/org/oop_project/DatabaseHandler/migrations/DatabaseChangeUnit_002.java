package org.oop_project.DatabaseHandler.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import java.util.Arrays;

import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.PRODUCT_COLLECTION_NAME;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "ProductSetup", order = "002", author = "SaleSync")
public class DatabaseChangeUnit_002 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {


        System.out.println("- Creating 'Product' Collection...");

        Document productJsonSchema = buildProductSchema();

        ValidationOptions productValidationOptions = new ValidationOptions()
                .validator(productJsonSchema)
                .validationLevel(ValidationLevel.STRICT)
                .validationAction(ValidationAction.ERROR);

        CreateCollectionOptions productCollectionOptions = new CreateCollectionOptions()
                .validationOptions(productValidationOptions);

        db.createCollection(PRODUCT_COLLECTION_NAME, productCollectionOptions);
        db.getCollection(PRODUCT_COLLECTION_NAME).createIndex(Indexes.ascending("id"), new IndexOptions().unique(true));
        db.getCollection(PRODUCT_COLLECTION_NAME).createIndex(Indexes.ascending("itemFamily"));
        db.getCollection(PRODUCT_COLLECTION_NAME).createIndex(Indexes.ascending("itemSubFamily"));

    }

    // Optional: Define rollback logic to undo the migration if it fails or is rolled back
    @RollbackExecution
    public void rollback(MongoDatabase database) {
        System.out.println("Rollback ProductSetup: Dropping collection 'Product'");
        database.getCollection(PRODUCT_COLLECTION_NAME).drop();
    }


    private Document buildProductSchema() {

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
                        .append("unitCost", new Document()
                                .append("bsonType", "decimal")
                                .append("minimum", 0.0)
                                .append("description", "unit cost must be a non-negative decimal")
                        )
                        .append("lastOrderDate", new Document()
                                .append("bsonType", "date")
                                .append("description", "date of the arrival of last stock")
                        )
                        .append("taxRate", new Document()
                                .append("bsonType", "decimal")
                                .append("minimum", 0.0)
                                .append("maximum", 1.0)
                                .append("description", "tax rate must be a decimal between 0 and 1")
                        )
                        .append("discountRate", new Document()
                                .append("bsonType", "decimal")
                                .append("minimum", 0.0)
                                .append("maximum", 1.0)
                                .append("description", "discount must be a non-negative decimal")
                        )
                        .append("retailPrice", new Document()
                                .append("bsonType", "decimal")
                                .append("minimum", 0.0)
                                .append("description", "retail price must be a non-negative decimal")
                        )
                        .append("isActive", new Document()
                                .append("bsonType", "bool")
                                .append("description", "product availability status")
                        )
                )
        );

    }

}
