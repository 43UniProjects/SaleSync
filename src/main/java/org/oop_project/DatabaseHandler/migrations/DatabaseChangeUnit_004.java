package org.oop_project.DatabaseHandler.migrations;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.json.JSONObject;
import org.oop_project.Utils.JsonReader;

import java.util.Arrays;

import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.ITEM_FAMILY_COLLECTION_NAME;
import static org.oop_project.Main.INITIAL_ITEM_FAMILIES_DATA_PATH;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "ItemFamilySetup", order = "004", author = "SaleSync")
public class DatabaseChangeUnit_004 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {


        System.out.println("- Creating 'ItemFamily' Collection...");

        Document itemFamilyJsonSchema = buildItemFamilySchema();

        ValidationOptions itemFamilyValidationOptions = new ValidationOptions()
                .validator(itemFamilyJsonSchema)
                .validationLevel(ValidationLevel.STRICT)
                .validationAction(ValidationAction.ERROR);

        CreateCollectionOptions itemFamilyCollectionOptions = new CreateCollectionOptions()
                .validationOptions(itemFamilyValidationOptions);

        db.createCollection(ITEM_FAMILY_COLLECTION_NAME, itemFamilyCollectionOptions);
        db.getCollection(ITEM_FAMILY_COLLECTION_NAME).createIndex(Indexes.ascending("id"), new IndexOptions().unique(true));

    }

    // Optional: Define rollback logic to undo the migration if it fails or is rolled back
    @RollbackExecution
    public void rollback(MongoDatabase database) {
        System.out.println("Rollback ItemFamilySetup: Dropping collection 'ItemFamily'");
        database.getCollection(ITEM_FAMILY_COLLECTION_NAME).drop();
    }


    private Document buildItemFamilySchema() {

        return new Document("$jsonSchema", new Document()
                .append("required", Arrays.asList("id", "label", "subFamilies")
                )
                .append("properties", new Document()
                        .append("id", new Document()
                                .append("bsonType", "string")
                                .append("minLength", 3)
                                .append("maxLength", 5)
                        )
                        .append("label", new Document()
                                .append("bsonType", "string")
                        )
                        .append("subFamilies", new Document()
                                .append("bsonType", "array")
                                .append("description", "An array of embedded sub-family documents (one-to-few relationship)")
                                .append("minItems", 0)
                                .append("items", new Document()
                                        .append("bsonType", "object")
                                        .append("required", Arrays.asList("id", "label"))
                                        .append("properties", new Document()
                                                .append("id", new Document()
                                                        .append("bsonType", "string")
                                                )
                                                .append("label", new Document()
                                                        .append("bsonType", "string")
                                                )
                                                .append("item_count", new Document()
                                                        .append("bsonType", "int")
                                                        .append("minimum", 0)
                                                )
                                        )
                                )

                        )
                )
        );
    }

}
