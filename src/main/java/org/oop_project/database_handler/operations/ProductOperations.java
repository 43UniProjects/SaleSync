package org.oop_project.database_handler.operations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.oop_project.database_handler.models.Product;

import static org.oop_project.database_handler.DatabaseConnectionManager.PRODUCT_COLLECTION_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductOperations implements Operations<Product> {

    final MongoCollection<Product> productCollection = dbClient.getCollection(PRODUCT_COLLECTION_NAME, Product.class);

    @Override    
    public void add(Product newProduct) {
        productCollection.insertOne(newProduct);
        System.out.println("\nProduct added: " + newProduct.getName());
    }

    @Override
    public boolean find(String id) {
        return productCollection.find(Filters.eq("id", id)).first() != null;
    }

    @Override
    public Product get(String id) {
        return productCollection.find(Filters.eq("id", id)).first();
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        productCollection.find().into(products);
        return products.isEmpty() ? null : products;
    }

    @Override
    public String getLastId() {
        Product lastProduct = productCollection.find().sort(Sorts.descending("_id")).first();
        return lastProduct != null ? lastProduct.getId() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(String id, HashMap<String, Object> updatedRecords) {
        Bson filter = Filters.eq("id", id);

        if (updatedRecords.containsKey("properties") && updatedRecords.get("properties") instanceof Map) {
            HashMap<String, Object> updatedProperties = (HashMap<String, Object>) updatedRecords.get("properties");
            for (String key : updatedProperties.keySet()) {
                updatedRecords.put(String.format("properties.%s", key), updatedProperties.get(key));
            }
            updatedRecords.remove("properties");
        }

        Bson updateOperation = new Document("$set", new Document(updatedRecords));
        productCollection.updateOne(filter, updateOperation);
    }

    @Override
    public boolean delete(String id) {
        // this can return whether record is deleted or not as a boolean value
        return productCollection.deleteOne(Filters.eq("id", id)).getDeletedCount() > 0;
    }

}
