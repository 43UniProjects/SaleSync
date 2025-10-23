package org.oop_project.View.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;

import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Models.Employee;
import org.oop_project.DatabaseHandler.Operations.EmployeeOperations;

public class AdminController {

    static EmployeeOperations employeeManager = new EmployeeOperations();

    @FXML private TextField idField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField phoneNumberField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Button btnLogout;
    @FXML private Label statusLabel;
    @FXML private DatePicker startDatePicker;

    // TableView and columns
    @FXML private TableView<EmployeeRow> employeeTable;
    @FXML private TableColumn<EmployeeRow, Integer> colEmployeeNumber;
    @FXML private TableColumn<EmployeeRow, String> colFirstName;
    @FXML private TableColumn<EmployeeRow, String> colLastName;
    @FXML private TableColumn<EmployeeRow, String> colDob;
    @FXML private TableColumn<EmployeeRow, String> colPhone;
    @FXML private TableColumn<EmployeeRow, String> colEmail;
    @FXML private TableColumn<EmployeeRow, String> colUsername;
    @FXML private TableColumn<EmployeeRow, String> colRole;
    @FXML private TableColumn<EmployeeRow, String> colStartDate;

    // In-memory list to simulate DB (demo mode)
    private final ObservableList<EmployeeRow> employees = FXCollections.observableArrayList();
    private int nextId = 1; // auto-generated starting from 1

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("ADMIN", "PRODUCT_MANAGER", "CASHIER");

        // Setup table columns
        if (employeeTable != null) {
            colEmployeeNumber.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
            colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
            colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

            List<Employee> dbEmployees = employeeManager.getAll();

            // Convert database employees to EmployeeRow objects for the table
            employees.clear();
            for (Employee emp : dbEmployees) {
                employees.add(new EmployeeRow(
                    Integer.parseInt(emp.getId()),
                    emp.getFirstName(),
                    emp.getLastName(),
                    emp.getDob() != null ? emp.getDob().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null,
                    emp.getPhoneNumber(),
                    emp.getEmail(),
                    emp.getUsername(),
                    emp.getRole().toString(),
                    emp.getStartDate() != null ? emp.getStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null
                ));
            }
            
            employeeTable.setItems(employees);

            // When a row is selected, populate form fields
            employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
                if (sel != null) {
                    idField.setText(String.valueOf(sel.getEmployeeNumber()));
                    firstNameField.setText(sel.getFirstName());
                    lastNameField.setText(sel.getLastName());
                    phoneNumberField.setText(sel.getPhone());
                    emailField.setText(sel.getEmail());
                    usernameField.setText(sel.getUsername());
                    roleComboBox.setValue(sel.getRole());
                    // Parse LocalDate if needed is out of scope in demo
                }
            });
        }
    }

    @FXML
    protected void addEmployee() {
        // TODO: Replace this in-memory add with actual DB insert via EmployeeOperations
        int id = nextId++;
        String fn = safeText(firstNameField);
        String ln = safeText(lastNameField);
        String phone = safeText(phoneNumberField);
        String email = safeText(emailField);
        String user = safeText(usernameField);
        String role = roleComboBox != null && roleComboBox.getValue() != null ? roleComboBox.getValue() : "";
        LocalDate dob = dobPicker != null ? dobPicker.getValue() : null;
        LocalDate start = startDatePicker != null ? startDatePicker.getValue() : null;

        employees.add(new EmployeeRow(id, fn, ln, dob, phone, email, user, role, start));
        statusLabel.setText("Employee added! (Demo mode)");
        statusLabel.setStyle("-fx-text-fill: green;");
        clearFields();
    }

    @FXML
    protected void updateEmployee() {
        // TODO: Replace with DB update by ID
        EmployeeRow sel = employeeTable != null ? employeeTable.getSelectionModel().getSelectedItem() : null;
        if (sel != null) {
            sel.setFirstName(safeText(firstNameField));
            sel.setLastName(safeText(lastNameField));
            sel.setPhone(safeText(phoneNumberField));
            sel.setEmail(safeText(emailField));
            sel.setUsername(safeText(usernameField));
            sel.setRole(roleComboBox != null && roleComboBox.getValue() != null ? roleComboBox.getValue() : "");
            sel.setDob(dobPicker != null ? dobPicker.getValue() : null);
            sel.setStartDate(startDatePicker != null ? startDatePicker.getValue() : null);
            employeeTable.refresh();
            statusLabel.setText("Employee updated! (Demo mode)");
            statusLabel.setStyle("-fx-text-fill: green;");
        } else {
            statusLabel.setText("Select a row to update (Demo mode)");
            statusLabel.setStyle("-fx-text-fill: orange;");
        }
    }

    @FXML
    protected void deleteEmployee() {
        // TODO: Replace with DB delete by ID
        EmployeeRow sel = employeeTable != null ? employeeTable.getSelectionModel().getSelectedItem() : null;
        if (sel != null) {
            employees.remove(sel);
            statusLabel.setText("Employee deleted! (Demo mode)");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        } else {
            statusLabel.setText("Select a row to delete (Demo mode)");
            statusLabel.setStyle("-fx-text-fill: orange;");
        }
    }

    @FXML
    protected void clearFields() {
        if (idField != null) idField.clear();
        if (firstNameField != null) firstNameField.clear();
        if (lastNameField != null) lastNameField.clear();
        if (dobPicker != null) dobPicker.setValue(null);
        if (startDatePicker != null) startDatePicker.setValue(null);
        if (phoneNumberField != null) phoneNumberField.clear();
        if (emailField != null) emailField.clear();
        if (usernameField != null) usernameField.clear();
        if (passwordField != null) passwordField.clear();
        if (roleComboBox != null) roleComboBox.setValue(null);
        if (statusLabel != null) statusLabel.setText("");
    }

    @FXML
    protected void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 600, 450);
            // Apply dark mode stylesheet
            try {
                String cssPath = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (Exception e) {
                System.out.println("Warning: style.css not found, continuing without styling.");
            }
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SaleSync - Login");
            stage.setWidth(600);
            stage.setHeight(450);
            stage.setResizable(false);
        } catch (Exception e) {
            statusLabel.setText("Error loading login!");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // Helpers
    private String safeText(TextField tf) { return tf != null && tf.getText() != null ? tf.getText().trim() : ""; }

    // Lightweight row model for TableView (UI-only)
    public static class EmployeeRow {
        private int employeeNumber;
        private String firstName;
        private String lastName;
        private LocalDate dob;
        private String phone;
        private String email;
        private String username;
        private String role;
        private LocalDate startDate;

        public EmployeeRow(int employeeNumber, String firstName, String lastName, LocalDate dob, String phone, String email, String username, String role, LocalDate startDate) {
            this.employeeNumber = employeeNumber;
            this.firstName = firstName;
            this.lastName = lastName;
            this.dob = dob;
            this.phone = phone;
            this.email = email;
            this.username = username;
            this.role = role;
            this.startDate = startDate;
        }
        public int getEmployeeNumber() { return employeeNumber; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getDob() { return dob != null ? dob.toString() : ""; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getStartDate() { return startDate != null ? startDate.toString() : ""; }

        public void setFirstName(String v) { this.firstName = v; }
        public void setLastName(String v) { this.lastName = v; }
        public void setDob(LocalDate v) { this.dob = v; }
        public void setPhone(String v) { this.phone = v; }
        public void setEmail(String v) { this.email = v; }
        public void setUsername(String v) { this.username = v; }
        public void setRole(String v) { this.role = v; }
        public void setStartDate(LocalDate v) { this.startDate = v; }
    }
}
