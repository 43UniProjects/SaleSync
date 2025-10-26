package org.oop_project.view.helpers;

// Lightweight row model for the TableView (UI-only)
public class ProductRow {
    private final String id;
    private String name;
    private String description;
    private String type;
    private String family;
    private String subFamily;
    private double unitPrice;
    private double taxRate;
    private double discountRate;
    private double retailPrice;
    private String supplierId;
    private double quantity;

    public ProductRow(String id, String name, String description, String type, String family, String subFamily, double unitPrice, double taxRate, double discountRate, double retailPrice, String supplierId, double quantity) {
        this.id = id; this.name = name; this.description = description; this.type = type; this.family = family; this.subFamily = subFamily; this.unitPrice = unitPrice; this.taxRate = taxRate; this.discountRate = discountRate; this.retailPrice = retailPrice; this.supplierId = supplierId; this.quantity = quantity;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getFamily() { return family; }
    public String getSubFamily() { return subFamily; }
    public String getUnitPrice() { return String.format("%.2f", unitPrice); }
    public String getTaxRate() { return String.format("%.2f", taxRate); }
    public String getDiscountRate() { return String.format("%.2f", discountRate); }
    public String getRetailPrice() { return String.format("%.2f", retailPrice); }
    public String getSupplierId() { return String.valueOf(supplierId); }
    public String getQuantity() { return String.format("%.2f", quantity); }
    public void setName(String v) { this.name = v; }
    public void setDescription(String v) { this.description = v; }
    public void setType(String v) { this.type = v; }
    public void setFamily(String v) { this.family = v; }
    public void setSubFamily(String v) { this.subFamily = v; }
    public void setUnitPrice(double v) { this.unitPrice = v; }
    public void setTaxRate(double v) { this.taxRate = v; }
    public void setDiscountRate(double v) { this.discountRate = v; }
    public void setRetailPrice(double v) { this.retailPrice = v; }
    public void setSupplierId(String v) { this.supplierId = v; }
    public void setQuantity(double v) { this.quantity = v; }
}