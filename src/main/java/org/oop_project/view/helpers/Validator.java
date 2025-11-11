package org.oop_project.view.helpers;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class Validator {
    public static boolean isChanged(String oldValue, String newValue) {
        return !oldValue.equals(newValue);
    }

     public static String safeText(TextField tf) {
        return tf != null && tf.getText() != null ? tf.getText().trim() : "";
    }

    public static String safeText(ComboBox<String> cb) {
        return cb != null && cb.getValue() != null ? cb.getValue().trim() : "";
    }
}
