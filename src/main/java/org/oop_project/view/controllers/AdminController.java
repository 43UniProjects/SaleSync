package org.oop_project.view.controllers;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.oop_project.DatabaseHandler.models.Employee;
import org.oop_project.DatabaseHandler.enums.Role;
import org.oop_project.DatabaseHandler.operations.EmployeeOperations;
import org.oop_project.view.utils.EmployeeRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    @FXML private TableColumn<EmployeeRow, String> colEmployeeNumber;
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
                    emp.getId(),
                    emp.getFirstName(),
                    emp.getLastName(),
                    emp.getDob() != null ? emp.getDob() : null,
                    emp.getPhoneNumber(),
                    emp.getEmail(),
                    emp.getUsername(),
                    emp.getRole().toString(),
                    emp.getStartDate() != null ? emp.getStartDate(): null
                ));
            }
            
            employeeTable.setItems(employees);

            // When a row is selected, populate form fields
            employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
                if (sel != null) {
                    if (idField != null) idField.setText(String.valueOf(sel.getEmployeeNumber()));
                    if (firstNameField != null) firstNameField.setText(sel.getFirstName());
                    if (lastNameField != null) lastNameField.setText(sel.getLastName());
                    if (phoneNumberField != null) phoneNumberField.setText(sel.getPhone());
                    if (emailField != null) emailField.setText(sel.getEmail());
                    if (usernameField != null) usernameField.setText(sel.getUsername());
                    if (roleComboBox != null) roleComboBox.setValue(sel.getRole());
                    if (dobPicker != null) dobPicker.setValue(LocalDate.parse(sel.getDob()));
                    if (startDatePicker != null) startDatePicker.setValue(LocalDate.parse(sel.getStartDate()));
                    String pass = employeeManager.get(sel.getUsername()).getPassword();
                    if (passwordField != null) passwordField.setText(pass);
                }
            });
        }
    }

    @FXML
    protected void addEmployee() {
        // TODO: Replace this in-memory add with actual DB insert via EmployeeOperations
        int nextId = Integer.parseInt(employeeManager.getLastId()) + 1;
        String id = String.valueOf(nextId);
        String fn = safeText(firstNameField);
        String ln = safeText(lastNameField);
        String phone = safeText(phoneNumberField);
        String email = safeText(emailField);
        String user = safeText(usernameField);
        String role = roleComboBox != null && roleComboBox.getValue() != null ? roleComboBox.getValue() : "";
        LocalDate dob = dobPicker.getValue();
        LocalDate start = startDatePicker.getValue();

        if(!(employeeManager.find(user))){
            Employee emp = new Employee(id, fn, ln, dob, phone, email, user, Role.valueOf(role), start);
            emp.setPassword(safeText(passwordField));
            employeeManager.add(emp);

            employees.add(new EmployeeRow(id, fn, ln, dob, phone, email, user, role, start));
            statusLabel.setText("Employee added! (Demo mode)");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        }else{
            statusLabel.setText("Username already exists (Demo mode)");
            statusLabel.setStyle("-fx-text-fill: red;");
        }



    }

    @FXML
    protected void updateEmployee() {
        // TODO: Replace with DB update by ID
        EmployeeRow sel = employeeTable != null ? employeeTable.getSelectionModel().getSelectedItem() : null;
        if (sel != null) {
            HashMap<String, Object> updatedRecords = new HashMap<>();
            updatedRecords.put("firstName", safeText(firstNameField));
            updatedRecords.put("lastName", safeText(lastNameField));
            updatedRecords.put("phoneNumber", safeText(phoneNumberField));
            updatedRecords.put("email", safeText(emailField));
            updatedRecords.put("username", safeText(usernameField));
            updatedRecords.put("role", roleComboBox != null && roleComboBox.getValue() != null ? roleComboBox.getValue() : "");
            updatedRecords.put("dob", dobPicker.getValue());
            updatedRecords.put("startDate", startDatePicker.getValue());
            updatedRecords.put("password", safeText(passwordField));
            employeeManager.update(sel.getEmployeeNumber(), updatedRecords);


            sel.setFirstName(safeText(firstNameField));
            sel.setLastName(safeText(lastNameField));
            sel.setPhone(safeText(phoneNumberField));
            sel.setEmail(safeText(emailField));
            sel.setUsername(safeText(usernameField));
            sel.setRole(roleComboBox != null && roleComboBox.getValue() != null ? roleComboBox.getValue() : "");
            sel.setDob(dobPicker != null && dobPicker.getValue() != null ? dobPicker.getValue() : null);
            sel.setStartDate(startDatePicker != null && startDatePicker.getValue() != null ? startDatePicker.getValue() : null);
            employeeTable.refresh();
            statusLabel.setText("Employee updated! (Demo mode)");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
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
            String username = safeText(usernameField);
            employeeManager.delete(username);
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
    
}
