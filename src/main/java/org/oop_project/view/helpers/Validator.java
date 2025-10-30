package org.oop_project.view.helpers;

public class Validator {
    public static boolean isChanged(String oldValue, String newValue) {
        return !oldValue.equals(newValue);
    }
}
