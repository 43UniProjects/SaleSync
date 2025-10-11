package org.oop_project.DatabaseHandler.Enums;

public enum Role {
    ADMIN("admin"),
    CASHIER("cashier"),
    PRODUCT_MANAGER("productManager");

    private final String label;

    // Constructor (runs once for each constant above)
    Role(String label) {
        this.label = label;
    }

    // Getter method to read the description
    public String getDescription() {
        return label;
    }
}
