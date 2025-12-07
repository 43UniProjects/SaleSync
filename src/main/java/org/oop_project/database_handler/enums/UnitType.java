package org.oop_project.database_handler.enums;

public enum UnitType {
    UNIT("UNIT"),
    KILOS("KILOS"),
    LITERS("LITERS"),
    // backward-compatibility: some DB documents use 'WEIGHT' as the unit type
    WEIGHT("WEIGHT");

    private final String label;

    UnitType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
