package org.oop_project.DatabaseHandler.Models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.oop_project.DatabaseHandler.Enums.UnitType;

import java.util.Date;
import java.util.HashMap;

/*
 * Represents a Product entity in the MongoDB 'Product' collection.
 * This class is a Plain Old Java Object (POJO) that maps to a BSON document.
 * It contains essential product details such as a unique identifier and other properties.
 * */

public class Product {

    @BsonId
    private ObjectId _id;
    private String id;
    private String name;
    private String description;
    private UnitType productType;
    private String family;
    private String subFamily;
    private double unitPrice;
    private double taxRate;
    private double discountRate;
    private double retailPrice;
    private int supplierId;
    private double availableQuantity;
    private Date lastStockDate;
    private boolean isActive;
    private Date discontinuedDate;
    private HashMap<String, Object> properties;

    // Required for the POJO Codec Provider
    public Product() {
        // left empty intentionally
    }

    public Product(
            String productId,
            String name,
            String description,
            UnitType unitType,
            String family,
            String subFamily,
            double unitPrice,
            double taxRate,
            double discountRate,
            int supplierId,
            double stockQuantity
    ) {
        this.id = productId;
        this.name = name;
        this.description = description;
        this.productType = unitType;
        this.family = family;
        this.subFamily = subFamily;
        this.unitPrice = unitPrice;
        this.taxRate = taxRate;
        this.discountRate = discountRate;
        this.supplierId = supplierId;
        this.availableQuantity = stockQuantity;
        this.setRetailPrice();
        this.changeState();
    }

    // Getters and Setters

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return this.family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getSubFamily() {
        return this.subFamily;
    }

    public void setSubFamily(String subFamily) {
        this.subFamily = subFamily;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getStockQuantity() {
        return this.availableQuantity;
    }

    public void setStockQuantity(double stockQuantity) {
        this.availableQuantity = stockQuantity;
    }

    public double getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
        this.setRetailPrice();
    }

    public double getDiscountRate() {
        return this.discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
        this.setRetailPrice();
    }

    public double getRetailPrice() {
        return this.retailPrice;
    }

    public int getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public double getAvailableQuantity() {
        return this.availableQuantity;
    }

    public void setAvailableQuantity(double availableQuantity) {
        this.availableQuantity = availableQuantity;
        this.lastStockDate = new Date();
    }

    public Date getLastStockDate() {
        return this.lastStockDate;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public Date getDiscontinuedDate() {
        return this.discontinuedDate;
    }

    public UnitType getProductType() {
        return this.productType;
    }

    public void setProductType(UnitType productType) {
        this.productType = productType;
    }

    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    public HashMap<String, Object> getProperties() {
        return this.properties;
    }


    // Util methods

    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    private void setRetailPrice() {
        double tax = this.unitPrice * (this.taxRate / 100);
        double discount = this.unitPrice * (this.discountRate / 100);
        this.retailPrice = this.unitPrice + tax - discount;
    }

    public void addProperty(String key, Object value) {
        Object ret = this.properties.put(key, value);
        if (ret != null) {
            System.out.printf("Property value ( %s ) was over written of Product ID ( %s )", key, this.id);
        }
    }

    public void changeState() {
        this.isActive = !this.isActive;
        if (this.isActive) {
            this.discontinuedDate = null;
        } else {
            this.discontinuedDate = new Date();
        }
    }

}
