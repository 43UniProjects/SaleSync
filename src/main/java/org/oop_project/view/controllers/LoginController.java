package org.oop_project.view.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static org.oop_project.view.helpers.Navigators.navigateToAdminPanel;
import static org.oop_project.view.helpers.Navigators.navigateToCashierPortal;
import static org.oop_project.view.helpers.Navigators.navigateToProductManagerPanel;

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
            return;
        }

        switch (emp.getRole()) {
            case ADMIN:
                navigateToAdminPanel((Stage) btnSubmit.getScene().getWindow(), loginStatus);
                break;
            case PRODUCT_MANAGER:
                navigateToProductManagerPanel((Stage) btnSubmit.getScene().getWindow(), loginStatus);
                break;
            case CASHIER:
                navigateToCashierPortal((Stage) btnSubmit.getScene().getWindow(), loginStatus);
                break;
            default:
                loginStatus.setText("Invalid role!");
                loginStatus.setStyle("-fx-text-fill: red;");
                break;
        }
    }
}
