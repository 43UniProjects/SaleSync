package org.oop_project.database_handler.operations;

import static org.oop_project.database_handler.DatabaseConnectionManager.SALE_COLLECTION_NAME;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;

import org.oop_project.database_handler.models.Employee;
import org.oop_project.database_handler.models.Sale;
import static org.oop_project.database_handler.operations.Operations.dbClient;

import com.mongodb.client.model.Sorts;

public class SaleOperations {

    private final MongoCollection<Sale> saleCollection = dbClient.getCollection(SALE_COLLECTION_NAME, Sale.class);

    public int getLastId() {
        Sale s = saleCollection.find().sort(Sorts.descending("transactionId")).first();
        return s != null ? s.getTransactionId() : 0;
    }

    public void add(Sale newSale) {
        saleCollection.insertOne(newSale);
        //System.out.printf("\nSale saved: %d\n", newSale.getTransactionId());
    }

    public boolean find(int transactionId) {
        return saleCollection.find(eq("transactionId", transactionId)).first() != null;
    }

    public Sale get(int transactionId) {
        return saleCollection.find(eq("transactionId", transactionId)).first();
    }
}