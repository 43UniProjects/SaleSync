package org.oop_project.DatabaseHandler.migrations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.oop_project.utils.JsonReader;

import java.util.ArrayList;
import java.util.List;

import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.ITEM_FAMILY_COLLECTION_NAME;
import static org.oop_project.Main.INITIAL_ITEM_FAMILIES_DATA_PATH;

// @ChangeUnit ensures this class runs once and its execution is recorded.
@ChangeUnit(id = "ItemFamilyInsert", order = "005", author = "SaleSync")
public class DatabaseChangeUnit_005 {

    // Mongock automatically injects the MongoDatabase instance.
    @Execution
    public void execute(MongoDatabase db) {

        System.out.println("- Inserting initial data to the 'ItemFamily' Collection...");

        JSONObject data = JsonReader.read(INITIAL_ITEM_FAMILIES_DATA_PATH);

        MongoCollection<Document> itemFamilyCollection = db.getCollection(ITEM_FAMILY_COLLECTION_NAME);
        JSONArray itemFamiliesArray = data.getJSONArray("data");

        for (Object itemFamilyObj : itemFamiliesArray) {

            JSONObject itemFamily = (JSONObject) itemFamilyObj;


            String id = itemFamily.getString("id");
            String label = itemFamily.getString("label");
            List<Document> itemSubFamilies = new ArrayList<>();
            JSONArray itemSubFamiliesArray = itemFamily.getJSONArray("subFamilies");

            for (Object itemSubFamilyObj : itemSubFamiliesArray) {
                JSONObject itemSubFamily = (JSONObject) itemSubFamilyObj;
                itemSubFamilies.add(new Document()
                        .append("id", itemSubFamily.getString("id"))
                        .append("label", itemSubFamily.getString("label"))
                );
            }


            itemFamilyCollection.insertOne(
                    new Document()
                            .append("id", id)
                            .append("label", label)
                            .append("subFamilies", itemSubFamilies)
            );
        }

    }

    // Optional: Define rollback logic to undo the migration if it fails or is rolled back
    @RollbackExecution
    public void rollback(MongoDatabase database) {
        System.out.println("Rollback ItemFamilyInsert: Deleting  'ItemFamily' collection data");
        database.getCollection(ITEM_FAMILY_COLLECTION_NAME).deleteMany(new Document());
    }

}