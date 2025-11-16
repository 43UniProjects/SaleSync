package org.oop_project.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the checkout dialog. Keeps responsibilities small: display the
 * bill text and
 * respond to print action (print not implemented).
 */
public class CheckoutController {
    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private Button btnPrint;


    @FXML
    private TextArea billArea;

    public void setBillText(String text) {
        billArea.setText(text);
        billArea.setStyle("-fx-font-family: monospace");
    }

    @FXML
    public void printBill(ActionEvent actionEvent) {
        // Printing functionality can be implemented here
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Print functionality is not implemented yet.");
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public void setBillViewWidth(double w) {
        billArea.setPrefWidth(w);
    }
}
