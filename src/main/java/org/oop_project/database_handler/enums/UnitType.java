package org.oop_project.database_handler.enums;

public enum UnitType {
    UNIT("UNIT"),
    KILOS("KILOS"),
    LITERS("LITERS");

    private final String label;

    UnitType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
