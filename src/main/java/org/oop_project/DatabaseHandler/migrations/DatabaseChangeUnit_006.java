package org.oop_project.DatabaseHandler.migrations;

import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "ChangeProductValidatorSchema", order = "006", author = "SaleSync")
public class DatabaseChangeUnit_006 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {


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

        return new Document("$jsonSchema", new Document()
                .append("properties", new Document()
                        .append("unitCost", new Document()
                                .append("bsonType", "double")
                                .append("minimum", 0.0)
                                .append("description", "unit cost must be a non-negative double")
                        )
                        .append("taxRate", new Document()
                                .append("bsonType", "double")
                                .append("minimum", 0.0)
                                .append("maximum", 1.0)
                                .append("description", "tax rate must be a double between 0 and 1")
                        )
                        .append("discountRate", new Document()
                                .append("bsonType", "double")
                                .append("minimum", 0.0)
                                .append("maximum", 1.0)
                                .append("description", "discount must be a non-negative double")
                        )
                        .append("retailPrice", new Document()
                                .append("bsonType", "double")
                                .append("minimum", 0.0)
                                .append("description", "retail price must be a non-negative double")
                        )
                )
        );

    }

}
