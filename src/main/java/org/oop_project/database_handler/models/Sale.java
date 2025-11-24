package org.oop_project.database_handler.models;

import java.time.LocalDateTime;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Sale {

    @BsonId
    private String _id;

    @BsonProperty("transactionId")
    private String transactionId;

    private String cashierId;
    private double price;
    private LocalDateTime date;
    private int itemsCount; 

    public Sale() {
    }

    public Sale(String transactionId, String cashierId, double price, LocalDateTime date) {
        this.transactionId = transactionId;
        this.cashierId = cashierId;
        this.price = price;
        this.date = date;
        this.itemsCount = 0;
    }

    public Sale(String transactionId, String cashierId, double price, LocalDateTime date, int itemsCount) {
        this.transactionId = transactionId;
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

    public String getId() {
        return this._id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

}
