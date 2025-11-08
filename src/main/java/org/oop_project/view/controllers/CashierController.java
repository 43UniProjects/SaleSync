package org.oop_project.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.oop_project.DatabaseHandler.models.Product;
import org.oop_project.DatabaseHandler.operations.ProductOperations;

public class CashierController {
    private final static ProductOperations productManager = new ProductOperations();

    @FXML private Button btnLogout;
    @FXML private TextArea scannedStatusField;
    @FXML private Button btnClear;

    @FXML
    protected void backToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 600, 450);
            String css = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SaleSync - Login");
            stage.setWidth(600);
            stage.setHeight(450);
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (Exception e) {
            // ignore for demo
        }
    }

    @FXML
    public void showDetails() {
        String id = productManager.getLastId();
        Product prod = productManager.get(id);
        String text = "ID: " + prod.getId() + "\nName: " + prod.getName() + "\nDescription: " + prod.getDescription()
        + "\nPrice: " + prod.getUnitPrice();
        scannedStatusField.setText(text);
    }

    public void clearField() {
        scannedStatusField.setText("Waiting product to be scanned...");
    }
}
