package org.oop_project.database_handler.models;

import java.time.LocalDateTime;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Sale {

    // MongoDB document id (ObjectId)
    @BsonId
    private ObjectId _id;

    // Application-level string identifier used for ordering/printing
    @BsonProperty("id")
    private String id;

    @BsonProperty("transactionId")
    private String transactionId;

    private String cashierId;
    private double price;
    private LocalDateTime date;
    @BsonProperty("itemCount")
    private int itemsCount;

    public Sale() {
    }

    public Sale(String id, String cashierId, double price, LocalDateTime date, int itemsCount) {
        this.id = id;
        this.transactionId = id; // reuse id as transaction id if caller provides single id
        this.cashierId = cashierId;
        this.price = price;
        this.date = date;
        this.itemsCount = itemsCount;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getCashierId() {
        return this.cashierId;
    }

    public double getPrice() {
        return this.price;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public int getItemsCount() {
        return this.itemsCount;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    // string id used by application logic (e.g., getLastId)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // access to the raw MongoDB ObjectId if needed
    public ObjectId getObjectId() {
        return this._id;
    }

    public void setObjectId(ObjectId oid) {
        this._id = oid;
    }

}
