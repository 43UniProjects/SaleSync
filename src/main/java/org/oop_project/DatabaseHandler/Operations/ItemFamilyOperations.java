package org.oop_project.DatabaseHandler.Operations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Models.ItemFamily;
import org.oop_project.DatabaseHandler.Models.ItemSubFamily;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.ITEM_FAMILY_COLLECTION_NAME;


public class ItemFamilyOperations extends Operations{

    final MongoCollection<ItemFamily> itemFamilyCollection = dbClient.getCollection(ITEM_FAMILY_COLLECTION_NAME, ItemFamily.class);

    public void add(String id, String label, List<ItemSubFamily> itemSubFamilies) {
        ItemFamily newItemFamily = new ItemFamily(id, label, itemSubFamilies);
    }


    public boolean find(String id) {
        return itemFamilyCollection.find(eq("username", id)).first() != null;
    }

    public ItemFamily get(String id) {
        return itemFamilyCollection.find(eq("username", id)).first();
    }

    public List<ItemFamily> getAll() {
        List<ItemFamily> itemFamilies = new ArrayList<>();
        itemFamilyCollection.find().into(itemFamilies);
        return itemFamilies.isEmpty() ? null : itemFamilies;

    }

    public void delete(String id) {
        itemFamilyCollection.deleteOne(eq("id", id));
    }

}
