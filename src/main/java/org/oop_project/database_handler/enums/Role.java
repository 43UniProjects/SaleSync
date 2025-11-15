package org.oop_project.database_handler.enums;

public enum Role {
    ADMIN("ADMIN"),
    CASHIER("CASHIER"),
    PRODUCT_MANAGER("PRODUCT_MANAGER");

    private final String label;

    // Constructor (runs once for each constant above)
    Role(String label) {
        this.label = label;
    }

    // Getter method to read the description
    public String getLabel() {
        return label;
    }
}
