package org.oop_project.DatabaseHandler.operations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.oop_project.DatabaseHandler.models.ItemFamily;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.ITEM_FAMILY_COLLECTION_NAME;


public class ItemFamilyOperations implements Operations<ItemFamily>{

    final MongoCollection<ItemFamily> itemFamilyCollection = dbClient.getCollection(ITEM_FAMILY_COLLECTION_NAME, ItemFamily.class);

    public void add(ItemFamily newItemFamily) {
        itemFamilyCollection.insertOne(newItemFamily);
        System.out.println("New Item Family Added '" + newItemFamily.getLabel() + "'");
    }

    @Override
    public boolean find(String id) {
        return itemFamilyCollection.find(eq("username", id)).first() != null;
    }

    @Override
    public ItemFamily get(String id) {
        return itemFamilyCollection.find(eq("username", id)).first();
    }

    @Override
    public List<ItemFamily> getAll() {
        List<ItemFamily> itemFamilies = new ArrayList<>();
        itemFamilyCollection.find().into(itemFamilies);
        return itemFamilies.isEmpty() ? null : itemFamilies;

    }

    @Override
    public String getLastId() {
        ItemFamily lastProduct = itemFamilyCollection.find().sort(Sorts.descending("id")).first();
        return lastProduct != null ? lastProduct.getId() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(String id, HashMap<String, Object> updatedRecords) {
        Bson filter = Filters.eq("id", id);

        if (updatedRecords.containsKey("subFamilies") && updatedRecords.get("subFamilies") instanceof Map) {
            HashMap<String, Object> updatedProperties = (HashMap<String, Object>) updatedRecords.get("properties");
            for (String key : updatedProperties.keySet()) {
                updatedRecords.put(String.format("subFamilies.%s", key), updatedProperties.get(key));
            }
            updatedRecords.remove("subFamilies");
        }

        Bson updateOperation = new Document("$set", new Document(updatedRecords));
        itemFamilyCollection.updateOne(filter, updateOperation);
    }

    @Override
    public boolean delete(String id) {
        return itemFamilyCollection.deleteOne(eq("id", id)).getDeletedCount() > 0;
    }

}
