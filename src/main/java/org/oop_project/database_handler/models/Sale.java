package org.oop_project.database_handler.models;

import java.time.LocalDateTime;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Sale {

    @BsonId
    private String _id;

    @BsonProperty("transactionId")
    private int transactionId;

    private String cashierId;
    private double price;
    private LocalDateTime date;
    private int itemsCount; // total number of items in this sale

    // Constructors, getters, and setters

    // Required by the MongoDB PojoCodec to instantiate the POJO
    public Sale() {
    }

    public Sale(int transactionId, String cashierId, double price, LocalDateTime date) {
        this.transactionId = transactionId;
        this.cashierId = cashierId;
        this.price = price;
        this.date = date;
        this.itemsCount = 0;
    }

    public Sale(int transactionId, String cashierId, double price, LocalDateTime date, int itemsCount) {
        this.transactionId = transactionId;
        this.cashierId = cashierId;
        this.price = price;
        this.date = date;
        this.itemsCount = itemsCount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getCashierId() {
        return cashierId;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setTransactionId(int transactionId) {
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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
