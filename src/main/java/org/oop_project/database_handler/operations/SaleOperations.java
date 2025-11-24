package org.oop_project.database_handler.operations;

import static org.oop_project.database_handler.DatabaseConnectionManager.SALE_COLLECTION_NAME;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.oop_project.database_handler.models.Sale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mongodb.client.MongoCollection;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

public class SaleOperations implements Operations<Sale> {

    private final MongoCollection<Sale> saleCollection = dbClient.getCollection(SALE_COLLECTION_NAME, Sale.class);

    @Override    
    public void add(Sale newSale) {
        saleCollection.insertOne(newSale);
        System.out.println("\nProduct added: " + newSale.getId());
    }

    @Override
    public boolean find(String id) {
        return saleCollection.find(Filters.eq("id", id)).first() != null;
    }

    @Override
    public Sale get(String id) {
        return saleCollection.find(Filters.eq("id", id)).first();
    }

    @Override
    public List<Sale> getAll() {
        List<Sale> sales = new ArrayList<>();
        saleCollection.find().into(sales);
        return sales.isEmpty() ? null : sales;
    }

    @Override
    public String getLastId() {
        Sale lastSale = saleCollection.find().sort(Sorts.descending("_id")).first();
        return lastSale != null ? lastSale.getId() : null;
    }

    @Override
    public void update(String id, HashMap<String, Object> updatedRecords) {
        Bson filter = Filters.eq("id", id);

        Bson updateOperation = new Document("$set", new Document(updatedRecords));
        saleCollection.updateOne(filter, updateOperation);
    }

    @Override
    public boolean delete(String id) {
        // this can return whether record is deleted or not as a boolean value
        return saleCollection.deleteOne(Filters.eq("id", id)).getDeletedCount() > 0;
    }

}