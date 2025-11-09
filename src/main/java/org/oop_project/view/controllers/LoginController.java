package org.oop_project.view.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.oop_project.DatabaseHandler.enums.Role;
import org.oop_project.DatabaseHandler.models.Employee;
import org.oop_project.DatabaseHandler.operations.EmployeeOperations;
import org.oop_project.DatabaseHandler.operations.Operations;

public class LoginController {

    static Operations<Employee> employeeManager = new EmployeeOperations();
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginStatus;
    @FXML
    private Button btnSubmit;

    @FXML
    public void initialize() {
    }


    @FXML
    protected void submit() {

        String user = usernameField.getText();
        String pass = passwordField.getText();

        Employee emp = employeeManager.find(user) ? employeeManager.get(user) : null;

        if (emp == null || !emp.getPassword().equals(pass)) {
            loginStatus.setText("Invalid credentials!");
            loginStatus.setStyle("-fx-text-fill: red;");
        } else {
            Role role = emp.getRole();
            if (role == Role.ADMIN) {
                navigateToAdminPanel();
            } else if (role == Role.CASHIER) {
                navigateToCashierPortal();
            } else if (role == Role.PRODUCT_MANAGER) {
                navigateToProductManagerPanel();
            } else {
                loginStatus.setText("Invalid role!");
                loginStatus.setStyle("-fx-text-fill: red;");
            }
        }
    }

    private void navigateToAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/admin-panel.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 750);
            Stage stage = (Stage) btnSubmit.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(1100);
            stage.setHeight(750);
            stage.centerOnScreen();
            stage.setTitle("SaleSync - Admin Panel");


        } catch (Exception e) {
            e.printStackTrace();
            loginStatus.setText("Error loading admin panel!");
            loginStatus.setStyle("-fx-text-fill: red;");
        }
    }

    private void navigateToCashierPortal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/cashier-portal.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) btnSubmit.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(930);

            stage.setHeight(900);
            stage.centerOnScreen();

            stage.setTitle("SaleSync - Cashier Portal");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            loginStatus.setText("Error loading cashier portal!");
            loginStatus.setStyle("-fx-text-fill: red;");
        }
    }

    private void navigateToProductManagerPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/product-dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 750);
            Stage stage = (Stage) btnSubmit.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(1100);
            stage.setHeight(790);
            stage.setTitle("SaleSync - Product Dashboard");
        } catch (Exception e) {
            loginStatus.setText("Error loading product dashboard!");
            loginStatus.setStyle("-fx-text-fill: red;");
        }
    }

}
