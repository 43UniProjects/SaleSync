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
import org.oop_project.DatabaseHandler.operations.Operations;
import org.oop_project.DatabaseHandler.operations.EmployeeOperations;
import org.oop_project.utils.Generate;
import org.oop_project.view.helpers.EmployeeRow;
import org.oop_project.view.helpers.Validator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AdminController {

    private final static Operations<Employee> employeeManager = new EmployeeOperations();

    private final ObservableList<EmployeeRow> employeeTableRows = FXCollections.observableArrayList();

    @FXML
    private TableView<EmployeeRow> employeeTable;

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

    // Table columns
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

        roleComboBox.getItems().addAll(
                Role.ADMIN.getLabel(),
                Role.PRODUCT_MANAGER.getLabel(),
                Role.CASHIER.getLabel());

        if (employeeTable == null)
            return;

        // Setup table columns

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

            if (sel == null)
                return;

            try {
                employeeNumberField.setText(String.valueOf(sel.getEmployeeNumber()));
                firstNameField.setText(sel.getFirstName());
                lastNameField.setText(sel.getLastName());
                phoneNumberField.setText(sel.getPhone());
                emailField.setText(sel.getEmail());
                usernameField.setText(sel.getUsername());
                roleComboBox.setValue(sel.getRole());
                dobPicker.setValue(LocalDate.parse(sel.getDob()));
                startDatePicker.setValue(LocalDate.parse(sel.getStartDate()));
                String pass = employeeManager.get(sel.getUsername()).getPassword();
                passwordField.setText(pass);

            } catch (NullPointerException e) {
                return;
            }

        });

        List<Employee> employeeList = employeeManager.getAll();

        employeeTableRows.clear();

        employeeTableRows.addAll(employeeList.stream().map(Employee::mapEmployeeRow).toList());

        employeeTable.setItems(employeeTableRows);

    }

    @FXML
    protected void addEmployee() {

        String id = Generate.generateUserId(employeeManager, Role.valueOf(roleComboBox.getValue()));
        String fn = safeText(firstNameField);
        String ln = safeText(lastNameField);
        String phone = safeText(phoneNumberField);
        String email = safeText(emailField);
        String user = safeText(usernameField);
        String role = roleComboBox.getValue();
        LocalDate dob = dobPicker.getValue();
        LocalDate start = startDatePicker.getValue();
        String pwd = safeText(passwordField);

        // checks whether all fields are filled to prevent exceptions

        if (fn.isEmpty() || ln.isEmpty() || phone.isEmpty() || email.isEmpty() || user.isEmpty() || role == null
                || dob == null || start == null) {

            statusLabel.setText("Please fill all the fileds");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if ((employeeManager.find(user))) {
            statusLabel.setText("Username already exists");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Employee emp = new Employee(id, fn, ln, dob, phone, email, user, Role.valueOf(role), start);
        emp.setPassword(pwd);
        employeeManager.add(emp);
        employeeTableRows.add(Employee.mapEmployeeRow(emp));

        statusLabel.setText("Employee added!");
        statusLabel.setStyle("-fx-text-fill: green;");

        clearFields();

    }

    @FXML
    protected void updateEmployee() {

        if(employeeTable == null) return;

        String fn = safeText(firstNameField);
        String ln = safeText(lastNameField);
        String phone = safeText(phoneNumberField);
        String email = safeText(emailField);
        String user = safeText(usernameField);
        String role = safeText(roleComboBox);
        LocalDate dob = dobPicker.getValue();
        LocalDate start = startDatePicker.getValue();
        String pwd = safeText(passwordField);

        if (!(employeeManager.find(user))) {
            statusLabel.setText("Username already exists");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        EmployeeRow selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            statusLabel.setText("Select a row to update");
            statusLabel.setStyle("-fx-text-fill: orange;");
            return;
        }

        HashMap<String, Object> updatedRecords = new HashMap<>();

        if (Validator.isChanged(selectedEmployee.getFirstName(), fn)) {
            updatedRecords.put("firstName", fn);
            selectedEmployee.setFirstName(fn);
        }

        if (Validator.isChanged(selectedEmployee.getLastName(), ln)) {
            updatedRecords.put("lastName", ln);
            selectedEmployee.setLastName(ln);
        }
        if (Validator.isChanged(selectedEmployee.getPhone(), phone)) {
            updatedRecords.put("phoneNumber", phone);
            selectedEmployee.setPhone(phone);
        }
        if (Validator.isChanged(selectedEmployee.getEmail(), email)) {
            updatedRecords.put("email", email);
            selectedEmployee.setEmail(email);
        }
        if (Validator.isChanged(selectedEmployee.getUsername(), user)) {
            updatedRecords.put("username", user);
            selectedEmployee.setUsername(user);
        }

        if (Validator.isChanged(selectedEmployee.getRole(), role)) {
            updatedRecords.put("role", role);
            selectedEmployee.setRole(role);
        }
        if (dob != null && Validator.isChanged(selectedEmployee.getDob(), dobPicker.getValue().toString())) {
            updatedRecords.put("dob", dob);
            selectedEmployee.setDob(dob);
        }
        if (start != null && Validator.isChanged(selectedEmployee.getStartDate(), startDatePicker.getValue().toString())) {
            updatedRecords.put("startDate", start);
            selectedEmployee.setStartDate(start);
        }
        String prevPwd = employeeManager.get(selectedEmployee.getUsername()).getPassword();
        if (Validator.isChanged(prevPwd, pwd)){   
             updatedRecords.put("password", pwd);
            }


        if (updatedRecords.isEmpty()) {
            statusLabel.setText("No changes were made");
            statusLabel.setStyle("-fx-text-fill: blue;");
            return;
        }

        employeeManager.update(selectedEmployee.getEmployeeNumber(), updatedRecords);
        employeeTable.refresh();
        statusLabel.setText("Employee updated!");
        statusLabel.setStyle("-fx-text-fill: green;");

        // clearing employee form data fields
        clearFields();

    }

    @FXML
    protected void deleteEmployee() {

        if (employeeTable == null)
            return;

        EmployeeRow selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();

        if (selectedEmployee == null) {
            statusLabel.setText("Select a row to delete");
            statusLabel.setStyle("-fx-text-fill: orange;");
        }

        employeeTableRows.remove(selectedEmployee);

        String username = safeText(usernameField);
        employeeManager.delete(username);
        statusLabel.setText("Employee deleted!");
        statusLabel.setStyle("-fx-text-fill: green;");
        clearFields();

    }

    @FXML
    protected void clearFields() {
        employeeNumberField.clear();
        firstNameField.clear();
        lastNameField.clear();
        dobPicker.setValue(null);
        startDatePicker.setValue(null);
        phoneNumberField.clear();
        emailField.clear();
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue(null);
        statusLabel.setText("");
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
            stage.setWidth(600);
            stage.setHeight(450);
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (Exception e) {
            statusLabel.setText("Error loading login!");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // Helpers
    private String safeText(TextField tf) {
        return tf != null && tf.getText() != null ? tf.getText().trim() : "";
    }

    private String safeText(ComboBox<String> cb) {
        return cb != null && cb.getValue() != null ? cb.getValue().trim() : "";
    }

}