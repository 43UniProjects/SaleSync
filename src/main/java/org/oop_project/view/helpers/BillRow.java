package org.oop_project.view.helpers;

public class BillRow {
    private final int itemNo;
    private final String itemName;
    private final double price;
    private final double quantity;
    private final double total;

    public BillRow(int itemNo, String itemName, double price, double quantity, double total) {
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public int getItemNo() {
        return itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }
}
