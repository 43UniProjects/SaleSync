package org.oop_project.view.controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.oop_project.DatabaseHandler.models.Employee;
import org.oop_project.DatabaseHandler.enums.Role;
import org.oop_project.DatabaseHandler.operations.EmployeeOperations;
import org.oop_project.utils.Generate;
import org.oop_project.view.helpers.EmployeeRow;

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
    @FXML
    private Label headAdminPanel;
    @FXML
    private Label subHeadAdminPanel;
    @FXML
    private TextField employeeNumberField;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
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

    static EmployeeOperations employeeManager = new EmployeeOperations();
    private ObservableList<EmployeeRow> employeeTableRows = FXCollections.observableArrayList();
    List<TableColumn<EmployeeRow, String>> employeeTableColumns = Arrays.asList(colEmployeeNumber, colFirstName,
            colLastName, colDob, colPhone, colEmail, colUsername, colRole, colStartDate);

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll(Role.ADMIN.getLabel(), Role.CASHIER.getLabel(), Role.PRODUCT_MANAGER.getLabel());

        if (employeeTable != null) {
            return;
        }

        // Setup columns
        colEmployeeNumber.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        // fetching employee data
        List<Employee> employeeList = employeeManager.getAll();

        // Convert fetched employee data to EmployeeRow objects for the table
        // employeeTable
        employeeTableRows.clear();
        employeeList.forEach(emp -> employeeTableRows.add(new EmployeeRow(emp)));
        employeeTable.setItems(employeeTableRows);

        // When a row is selected, populate form fields
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, prevSelection, currentSelection) -> {
            
            assert currentSelection == null;

            employeeNumberField.setText(currentSelection.getEmployeeNumber());
            firstNameField.setText(currentSelection.getFirstName());
            lastNameField.setText(currentSelection.getLastName());
            phoneNumberField.setText(currentSelection.getPhone());
            emailField.setText(currentSelection.getEmail());
            usernameField.setText(currentSelection.getUsername());
            roleComboBox.setValue(currentSelection.getRole());
            dobPicker.setValue(LocalDate.parse(currentSelection.getDob()));
            startDatePicker.setValue(LocalDate.parse(currentSelection.getStartDate()));
            passwordField.setText(employeeManager.get(currentSelection.getUsername()).getPassword());

        });

    }.

    @FXML
    protected void addEmployee() {

        String roleInput = roleComboBox.getValue();
        Role roleType = roleInput != null ? Role.valueOf(roleInput) : null;

        if(roleType == null) {
            System.out.println("SYSTEM ERROR: invalid Role submitted admin panel/employee manager");
            return;
        }

        Employee emp = new Employee(
            Generate.generateUserId(employeeManager, roleType),
            safeText(firstNameField),
            safeText(lastNameField),
            dobPicker.getValue(),
            safeText(phoneNumberField),
            safeText(emailField),
            safeText(usernameField),
            roleType,
            startDatePicker.getValue()
        );
        emp.setPassword(safeText(passwordField));
        
        employeeManager.add(emp); // add new Employee to db
        employeeTableRows.add(new EmployeeRow(emp)); // update employeeTable

        statusLabel.setText(String.format("New Employee with ID %s Added Successfully", emp.getId()));
        
        clearFields();
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
            updatedRecords.put("role",
                    roleComboBox != null && roleComboBox.getValue() != null ? roleComboBox.getValue() : "");
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
            sel.setStartDate(
                    startDatePicker != null && startDatePicker.getValue() != null ? startDatePicker.getValue() : null);
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
        if (idField != null)
            idField.clear();
        if (firstNameField != null)
            firstNameField.clear();
        if (lastNameField != null)
            lastNameField.clear();
        if (dobPicker != null)
            dobPicker.setValue(null);
        if (startDatePicker != null)
            startDatePicker.setValue(null);
        if (phoneNumberField != null)
            phoneNumberField.clear();
        if (emailField != null)
            emailField.clear();
        if (usernameField != null)
            usernameField.clear();
        if (passwordField != null)
            passwordField.clear();
        if (roleComboBox != null)
            roleComboBox.setValue(null);
        if (statusLabel != null)
            statusLabel.setText("");
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
    private String safeText(TextField tf) {
        return tf != null && tf.getText() != null ? tf.getText().trim() : "";
    }

    // Lightweight row model for TableView (UI-only)

}
