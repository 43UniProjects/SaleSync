package org.oop_project.database_handler.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import static org.oop_project.database_handler.DatabaseConnectionManager.SALE_COLLECTION_NAME;

import java.util.Arrays;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "SaleSetup", order = "008", author = "SaleSync")
public class DatabaseChangeUnit_008 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {

        System.out.println("- Creating 'Sale' Collection...");

        Document employeeJsonSchema = buildSaleSchema();

        ValidationOptions employeeValidationOptions = new ValidationOptions()
                .validator(employeeJsonSchema)
                .validationLevel(ValidationLevel.STRICT)
                .validationAction(ValidationAction.ERROR);

        CreateCollectionOptions employeeCollectionOptions = new CreateCollectionOptions()
                .validationOptions(employeeValidationOptions);

        db.createCollection(SALE_COLLECTION_NAME, employeeCollectionOptions);
        db.getCollection(SALE_COLLECTION_NAME).createIndex(Indexes.ascending("transactionId"), new IndexOptions().unique(true));
    }

    // Optional: Define rollback logic to undo the migration if it fails or is rolled back
    @RollbackExecution
    public void rollback(MongoDatabase database) {
        System.out.println("Rollback V001: Dropping collection 'Sale");
        database.getCollection(SALE_COLLECTION_NAME).drop();
    }

    private Document buildSaleSchema() {
        return new Document("$jsonSchema", new Document()
                .append("required", Arrays.asList("transactionId", "cashierId", "price", "date", "itemCount"))
                .append("properties", new Document()
                        .append("transactionId", new Document()
                                .append("bsonType", "string")
                        )
                        .append("cashierId", new Document()
                                .append("bsonType", "string")
                                .append("description", "cashierId must be a string")
                        )
                        .append("price", new Document()
                                .append("bsonType", "double")
                        )
                        .append("date", new Document()
                                .append("bsonType", "date")
                        )
                        .append("itemCount", new Document()
                                .append("bsonType", "int")
                        )       
                )
        );

    }

}