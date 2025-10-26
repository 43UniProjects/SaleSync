package org.oop_project.view.controllers;

import org.oop_project.DatabaseHandler.enums.Role;
import org.oop_project.DatabaseHandler.models.Employee;
import org.oop_project.DatabaseHandler.operations.EmployeeOperations;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginStatus;
    @FXML private Label otpAlert;
    @FXML private Button btnLogin;
    @FXML private Button btnSubmit;
    @FXML private TextField otpField1;
    @FXML private TextField otpField2;
    @FXML private TextField otpField3;
    @FXML private TextField otpField4;
    @FXML private TextField otpField5;
    @FXML private TextField otpField6;

    static EmployeeOperations employeeManager = new EmployeeOperations();

    @FXML
    public void initialize() {
        // Configure OTP fields to accept only a single digit and auto-navigate
        setupOtpField(otpField1, null, otpField2);
        setupOtpField(otpField2, otpField1, otpField3);
        setupOtpField(otpField3, otpField2, otpField4);
        setupOtpField(otpField4, otpField3, otpField5);
        setupOtpField(otpField5, otpField4, otpField6);
        setupOtpField(otpField6, otpField5, null);
    }

    private void setupOtpField(TextField current, TextField previous, TextField next) {
        if (current == null) return;

        // Allow only digits and at most one character
        current.setTextFormatter(new TextFormatter<String>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 1) return null;
            if (!newText.matches("\\d*")) return null;
            return change;
        }));

        // Move to next field when a digit is entered
        current.textProperty().addListener((obs, oldV, newV) -> {
            if (newV != null && newV.length() == 1 && next != null) {
                next.requestFocus();
                next.positionCaret(next.getText() == null ? 0 : next.getText().length());
            }
        });

        // Navigation with Backspace/Delete and arrow keys
        current.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case BACK_SPACE:
                case DELETE:
                    if ((current.getText() == null || current.getText().isEmpty()) && previous != null) {
                        previous.requestFocus();
                        previous.clear();
                        e.consume();
                    }
                    break;
                case LEFT:
                    if (previous != null) { previous.requestFocus(); e.consume(); }
                    break;
                case RIGHT:
                    if (next != null) { next.requestFocus(); e.consume(); }
                    break;
                default:
                    // no-op
            }
        });
    }

    @FXML
    protected void login(){
        // Show OTP UI (demo mode). In real app, trigger OTP send here.
        // loginStatus.setText("Enter the 6-digit OTP sent to your device (Demo mode)");
        // loginStatus.setStyle("-fx-text-fill: #e0e0e0;");

        toggleOtpUI(true);
        if (otpField1 != null) otpField1.requestFocus();

        // Ensure the OTP area is visible in the current window height
        try {
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            if (stage.getHeight() < 650) {
                stage.setHeight(600);
            }
        } catch (Exception ignored) {}

        // Move the OTP alert to the login button position, and OTP fields below it
        try {
            double baseY = btnLogin != null ? btnLogin.getLayoutY() : 316.0;
            double fieldsY = baseY + 50.0;
            if (otpAlert != null) {
                AnchorPane.setLeftAnchor(otpAlert, 0.0);
                AnchorPane.setRightAnchor(otpAlert, 0.0);
                AnchorPane.setTopAnchor(otpAlert, baseY);
            }
            if (otpField1 != null) otpField1.setLayoutY(fieldsY);
            if (otpField2 != null) otpField2.setLayoutY(fieldsY);
            if (otpField3 != null) otpField3.setLayoutY(fieldsY);
            if (otpField4 != null) otpField4.setLayoutY(fieldsY);
            if (otpField5 != null) otpField5.setLayoutY(fieldsY);
            if (otpField6 != null) otpField6.setLayoutY(fieldsY);
            if (btnSubmit != null) btnSubmit.setLayoutY(fieldsY + 66.0);
        } catch (Exception ignored) {}
    }

    @FXML
    protected void submit(){
        // OTP submit - just navigate to admin panel in demo mode
        // loginStatus.setText("OTP verified! (Demo mode)");
        // loginStatus.setStyle("-fx-text-fill: green;"); 

        String user = usernameField.getText();
        String pass = passwordField.getText();

        Employee emp = employeeManager.find(user) ? employeeManager.get(user) : null;
        if (emp == null || !emp.getPassword().equals(pass)) {
            loginStatus.setText("Invalid credentials! (Demo mode)");
            loginStatus.setStyle("-fx-text-fill: red;");
            return;
        }else{
            Role role = emp.getRole();
            if(role == Role.ADMIN) {
                navigateToAdminPanel();
            }else if(role == Role.CASHIER){
                navigateToCashierPortal();
            }else if(role == Role.PRODUCT_MANAGER){
                navigateToProductManagerPanel();
            }else{
                loginStatus.setText("Invalid role! (Demo mode)");
                loginStatus.setStyle("-fx-text-fill: red;");
            }
        }
        
    }

    private void navigateToAdminPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/admin-panel.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 750);
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(1100);
            stage.setHeight(750);
            stage.setTitle("SaleSync - Admin Panel");

            
        } catch (Exception e) {
            loginStatus.setText("Error loading admin panel!");
            loginStatus.setStyle("-fx-text-fill: red;");
        }
    }

    private void navigateToCashierPortal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/cashier-portal.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(900);
            stage.setHeight(600);
            stage.setTitle("SaleSync - Cashier Portal");
        } catch (Exception e) {
            loginStatus.setText("Error loading cashier portal!");
            loginStatus.setStyle("-fx-text-fill: red;");
        }
    }

    private void navigateToProductManagerPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/product-dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 750);
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(1100);
            stage.setHeight(790);
            stage.setTitle("SaleSync - Product Dashboard");
        } catch (Exception e) {
            loginStatus.setText("Error loading product dashboard!");
            loginStatus.setStyle("-fx-text-fill: red;");
        }
    }

    private void toggleOtpUI(boolean show) {
        boolean visible = show;
        boolean managed = show;

        // Show OTP fields and Submit button
        if (otpField1 != null) { otpField1.setVisible(visible); otpField1.setManaged(managed); if (show) otpField1.clear(); }
        if (otpField2 != null) { otpField2.setVisible(visible); otpField2.setManaged(managed); if (show) otpField2.clear(); }
        if (otpField3 != null) { otpField3.setVisible(visible); otpField3.setManaged(managed); if (show) otpField3.clear(); }
        if (otpField4 != null) { otpField4.setVisible(visible); otpField4.setManaged(managed); if (show) otpField4.clear(); }
        if (otpField5 != null) { otpField5.setVisible(visible); otpField5.setManaged(managed); if (show) otpField5.clear(); }
        if (otpField6 != null) { otpField6.setVisible(visible); otpField6.setManaged(managed); if (show) otpField6.clear(); }
        if (btnSubmit != null) { btnSubmit.setVisible(visible); btnSubmit.setManaged(managed); }
        if (otpAlert != null) { otpAlert.setVisible(visible); otpAlert.setManaged(managed); otpAlert.setText("Enter the OTP sent to your device"); }

        // Hide the Login button instead of disabling it
        if (btnLogin != null) { btnLogin.setVisible(!show); btnLogin.setManaged(!show); }

        // Keep username/password enabled to avoid the perception of reset
        if (usernameField != null) usernameField.setDisable(false);
        if (passwordField != null) passwordField.setDisable(false);
    }
}
