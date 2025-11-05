package org.oop_project.view.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.oop_project.DatabaseHandler.enums.Role;
import org.oop_project.DatabaseHandler.models.Employee;
import org.oop_project.DatabaseHandler.operations.EmployeeOperations;
import org.oop_project.utils.Generate;
import org.oop_project.view.helpers.EmployeeRow;
import org.oop_project.view.helpers.Validator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class AdminController {

    private final static EmployeeOperations employeeManager = new EmployeeOperations();
    // In-memory list to simulate DB (demo mode)
    private final ObservableList<EmployeeRow> employees = FXCollections.observableArrayList();
    @FXML
    private TextField employeeNumberField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker dobPicker;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button btnLogout;
    @FXML
    private Label statusLabel;
    @FXML
    private DatePicker startDatePicker;
    // TableView and columns
    @FXML
    private TableView<EmployeeRow> employeeTable;
    @FXML
    private TableColumn<EmployeeRow, String> colEmployeeNumber;
    @FXML
    private TableColumn<EmployeeRow, String> colFirstName;
    @FXML
    private TableColumn<EmployeeRow, String> colLastName;
    @FXML
    private TableColumn<EmployeeRow, String> colDob;
    @FXML
    private TableColumn<EmployeeRow, String> colPhone;
    @FXML
    private TableColumn<EmployeeRow, String> colEmail;
    @FXML
    private TableColumn<EmployeeRow, String> colUsername;
    @FXML
    private TableColumn<EmployeeRow, String> colRole;
    @FXML
    private TableColumn<EmployeeRow, String> colStartDate;

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

            // When a row is selected, populate form fields
            employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
                if (sel != null) {
                    if (employeeNumberField != null)
                        employeeNumberField.setText(String.valueOf(sel.getEmployeeNumber()));
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
                        emp.getStartDate() != null ? emp.getStartDate() : null
                ));
            }

            employeeTable.setItems(employees);

        }
    }

    @FXML
    protected void addEmployee() {

        String id = Generate.generateUserId(employeeManager, Role.valueOf(roleComboBox.getValue()));

        // checks whether all fields are filled to prevent exceptions
        if (safeText(firstNameField).isEmpty() ||
                safeText(lastNameField).isEmpty() ||
                safeText(phoneNumberField).isEmpty() ||
                safeText(emailField).isEmpty() ||
                safeText(usernameField).isEmpty() ||
                roleComboBox.getValue() == null ||
                dobPicker.getValue() == null ||
                startDatePicker.getValue() == null) {
            statusLabel.setText("Please fill all the fileds");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        String fn = safeText(firstNameField);
        String ln = safeText(lastNameField);
        String phone = safeText(phoneNumberField);
        String email = safeText(emailField);
        String user = safeText(usernameField);
        String role = roleComboBox.getValue();
        LocalDate dob = dobPicker.getValue();
        LocalDate start = startDatePicker.getValue();

        if (!(employeeManager.find(user))) {
            Employee emp = new Employee(id, fn, ln, dob, phone, email, user, Role.valueOf(role), start);
            emp.setPassword(safeText(passwordField));
            employeeManager.add(emp);

            employees.add(new EmployeeRow(id, fn, ln, dob, phone, email, user, role, start));
            statusLabel.setText("Employee added!");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        } else {
            statusLabel.setText("Username already exists");
            statusLabel.setStyle("-fx-text-fill: red;");
        }


    }


    @FXML
    protected void updateEmployee() {

        EmployeeRow sel = employeeTable != null ? employeeTable.getSelectionModel().getSelectedItem() : null;
        if (sel != null) {

            HashMap<String, Object> updatedRecords = new HashMap<>();
            if(Validator.isChanged(sel.getFirstName(), safeText(firstNameField)))
                updatedRecords.put("firstName", safeText(firstNameField));
            if(Validator.isChanged(sel.getLastName(), safeText(lastNameField)))
                updatedRecords.put("lastName", safeText(lastNameField));
            if(Validator.isChanged(sel.getPhone(), safeText(phoneNumberField)))
                updatedRecords.put("phoneNumber", safeText(phoneNumberField));
            if(Validator.isChanged(sel.getEmail(), safeText(emailField)))
                updatedRecords.put("email", safeText(emailField));
            if(Validator.isChanged(sel.getUsername(), safeText(usernameField)))
                updatedRecords.put("username", safeText(usernameField));
            String roleComboBOxVal = roleComboBox != null && roleComboBox.getValue() != null ? roleComboBox.getValue() : "";
            if(Validator.isChanged(sel.getRole(), roleComboBOxVal))
                updatedRecords.put("role", roleComboBOxVal);
            if(Validator.isChanged(sel.getDob(), dobPicker.getValue().toString()))
                updatedRecords.put("dob", dobPicker.getValue());
            if(Validator.isChanged(sel.getStartDate(), startDatePicker.getValue().toString()))
                updatedRecords.put("startDate", startDatePicker.getValue());
            if(Validator.isChanged(employeeManager.get(sel.getUsername()).getPassword(), safeText(passwordField)))
                updatedRecords.put("password", safeText(passwordField));

            if(!updatedRecords.isEmpty()) {
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
                statusLabel.setText("Employee updated!");
                statusLabel.setStyle("-fx-text-fill: green;");
                clearFields();
            } else {

                statusLabel.setText("No changes were made");
                statusLabel.setStyle("-fx-text-fill: blue;");
            }
        } else {
            statusLabel.setText("Select a row to update");
            statusLabel.setStyle("-fx-text-fill: orange;");
        }
    }

    @FXML
    protected void deleteEmployee() {

        EmployeeRow sel = employeeTable != null ? employeeTable.getSelectionModel().getSelectedItem() : null;
        if (sel != null) {
            employees.remove(sel);
            String username = safeText(usernameField);
            employeeManager.delete(username);
            statusLabel.setText("Employee deleted!");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        } else {
            statusLabel.setText("Select a row to delete");
            statusLabel.setStyle("-fx-text-fill: orange;");
        }
    }

    @FXML
    protected void clearFields() {
        if (employeeNumberField != null) employeeNumberField.clear();
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
            Scene scene = new Scene(loader.load());
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
            stage.setWidth(588);
            stage.setHeight(441);
            stage.setResizable(false);
        } catch (Exception e) {
            statusLabel.setText("Error loading login!");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // Helpers
    private String safeText(TextField tf) {
        return tf != null && tf.getText() != null ? tf.getText().trim() : "";
    }


}