package org.oop_project.database_handler.models;

import java.time.LocalDate;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Sale {

    @BsonId
    private String _id;

    @BsonProperty("transactionId")
    private int transactionId;

    private String cashierId;
    private double price;
    private LocalDate date;

    // Constructors, getters, and setters

    public Sale(int transactionId, String cashierId, double price, LocalDate date) {
        this.transactionId = transactionId;
        this.cashierId = cashierId;
        this.price = price;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
