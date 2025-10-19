package org.oop_project.DatabaseHandler.Operations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.conversions.Bson;
import org.oop_project.DatabaseHandler.Enums.UnitType;
import org.oop_project.DatabaseHandler.Models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.PRODUCT_COLLECTION_NAME;


public class ProductOperations extends Operations {

    final MongoCollection<Product> productCollection = dbClient.getCollection(PRODUCT_COLLECTION_NAME, Product.class);


    public void add(
            String productId,
            String name,
            String description,
            UnitType productType,
            String family,
            String subFamily,
            double unitPrice,
            double taxRate,
            double discountRate,
            int supplierId,
            double stockQuantity
    ) {

        Product newProduct = new Product(productId, name, description, productType, family, subFamily, unitPrice, taxRate, discountRate, supplierId, stockQuantity);
        productCollection.insertOne(newProduct);
        System.out.println("\nProduct added: " + newProduct.getName());
    }

    public Product get(BsonId id) {
        return productCollection.find(Filters.eq("_id", id)).first();
    }

    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        productCollection.find().into(products);
        return products.isEmpty() ? null : products;
    }

    public String getLastId() {
        Product lastProduct = productCollection.find().sort(Sorts.descending("id")).first();
        return lastProduct != null ? lastProduct.getId() : "0";
    }

    @SuppressWarnings("unchecked")
    public void update(int id, HashMap<String, Object> updatedRecords) {
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

    public void remove(BsonId id) {
        productCollection.deleteOne(Filters.eq("_id", id));
    }

}
