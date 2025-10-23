package org.oop_project.DatabaseHandler.operations;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.oop_project.DatabaseHandler.models.Supplier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.oop_project.DatabaseHandler.DatabaseConnectionManager.SUPPLIER_COLLECTION_NAME;

public class SupplierOperations implements Operations<Supplier> {


    private final MongoCollection<Supplier> supplierCollection = dbClient.getCollection(SUPPLIER_COLLECTION_NAME, Supplier.class);

    @Override
    public void add(Supplier newSupplier) {
        supplierCollection.insertOne(newSupplier);
        System.out.printf("\nEmployee saved: %s\n", newSupplier.getName());
    }

    @Override
    public boolean find(String id) {
        return supplierCollection.find(eq("id", id)).first() != null;
    }

    @Override
    public Supplier get(String id) {
        return supplierCollection.find(eq("id", id)).first();
    }

    @Override
    public List<Supplier> getAll() {
        List<Supplier> suppliers = new ArrayList<>();
        supplierCollection.find().into(suppliers);
        return suppliers.isEmpty() ? null : suppliers;
    }

    @Override
    public String getLastId() {
        Supplier s = supplierCollection.find().sort(Sorts.descending("id")).first();
        return s != null ? s.getId() : null;
    }

    @Override
    public void update(String id, HashMap<String, Object> updatedRecords) {
        Bson filter = Filters.eq("id", id);

        Bson updateOperation = new Document("$set", new Document(updatedRecords));
        supplierCollection.updateOne(filter, updateOperation);
    }

    @Override
    public boolean delete(String id) {
        return supplierCollection.deleteOne(eq("id", id)).getDeletedCount() > 0;
    }

}
