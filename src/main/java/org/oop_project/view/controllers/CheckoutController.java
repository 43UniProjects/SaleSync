package org.oop_project.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

/**
 * Controller for the checkout dialog. Keeps responsibilities small: display the bill text and
 * respond to print action (print not implemented).
 */
public class CheckoutController {

    @FXML
    private TextArea billArea;

    public void setBillText(String text) {
        if (billArea != null) billArea.setText(text);
    }

     @FXML
    public void printBill(ActionEvent actionEvent) {
        // Printing functionality can be implemented here
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Print functionality is not implemented yet.");
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
