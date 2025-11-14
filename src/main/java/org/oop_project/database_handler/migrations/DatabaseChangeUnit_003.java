package org.oop_project.database_handler.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

import static org.oop_project.database_handler.DatabaseConnectionManager.SUPPLIER_COLLECTION_NAME;

import java.util.Arrays;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "SupplierSetup", order = "003", author = "SaleSync")
public class DatabaseChangeUnit_003 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {


        System.out.println("- Creating 'Supplier' Collection...");

        Document supplierJsonSchema = buildSupplierSchema();

        ValidationOptions supplierValidationOptions = new ValidationOptions()
                .validator(supplierJsonSchema)
                .validationLevel(ValidationLevel.STRICT)
                .validationAction(ValidationAction.ERROR);

        CreateCollectionOptions supplierCollectionOptions = new CreateCollectionOptions()
                .validationOptions(supplierValidationOptions);

        db.createCollection(SUPPLIER_COLLECTION_NAME, supplierCollectionOptions);
        db.getCollection(SUPPLIER_COLLECTION_NAME).createIndex(Indexes.ascending("id"), new IndexOptions().unique(true));


    }

    // Optional: Define rollback logic to undo the migration if it fails or is rolled back
    @RollbackExecution
    public void rollback(MongoDatabase database) {
        System.out.println("Rollback SupplierSetup: Dropping collection 'Supplier'");
        database.getCollection(SUPPLIER_COLLECTION_NAME).drop();
    }


    private Document buildSupplierSchema() {

        return new Document("$jsonSchema", new Document()
                .append("required", Arrays.asList("id", "name", "phoneNumber", "email", "joinedDate", "active")
                )
                .append("properties", new Document()
                        .append("id", new Document()
                                .append("bsonType", "string")
                                .append("description", "unique supplier identifier")
                        )
                        .append("name", new Document()
                                .append("bsonType", "string")
                                .append("description", "name must be a string")
                        )
                        .append("phoneNumber", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 10)
                                .append("description", "description must be a string")
                        )
                        .append("email", new Document()
                                .append("bsonType", "string")
                                .append("pattern", "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
                                .append("description", "Invalid Email")
                        )
                        .append("joinedDate", new Document()
                                .append("bsonType", "date")
                        )

                        .append("isActive", new Document()
                                .append("bsonType", "bool")
                                .append("description", "supplier status")
                        )
                )
        );

    }

}
