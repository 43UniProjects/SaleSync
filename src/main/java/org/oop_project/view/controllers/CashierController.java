package org.oop_project.view.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
        } catch (Exception e) {}
    }

    @FXML
    public void showDetails() {
        String id = productManager.getLastId();

        Product prod = productManager.get(id);

        String text = "Id: %s\nName: %s\nDescription: %s\nPrice: %.2f";
        scannedStatusField.setText(text.formatted(prod.getId(), prod.getName(), prod.getDescription(), prod.getUnitPrice()));
    }

    public void clearField() {
        scannedStatusField.setText("Waiting product to be scanned...");
    }

    @FXML
    private void initialize() {
    }
}
