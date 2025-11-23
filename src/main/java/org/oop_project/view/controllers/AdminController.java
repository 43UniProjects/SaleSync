package org.oop_project.view.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.oop_project.database_handler.enums.Role;
import org.oop_project.database_handler.models.Admin;
import org.oop_project.database_handler.models.Employee;
import org.oop_project.database_handler.operations.EmployeeOperations;
import org.oop_project.database_handler.operations.Operations;
import org.oop_project.utils.Generate;
import org.oop_project.view.helpers.EmployeeRow;
import org.oop_project.view.helpers.Navigators;
import static org.oop_project.view.helpers.Navigators.navigateToLoginPanel;
import static org.oop_project.view.helpers.Validator.safeText;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AdminController {
    @FXML
    private Label headAdminPanel;

    @FXML
    private Label subHeadAdminPanel;

    @FXML
    private Button btnAccount;
    

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;


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

    @FXML
    private Button btnAnalytics;   

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

    private Admin admin;

    @FXML
    public void initialize() {

        roleComboBox.getItems().addAll(
                Role.ADMIN.getLabel(),
                Role.PRODUCT_MANAGER.getLabel(),
                Role.CASHIER.getLabel());

        if (employeeTable == null) {
            return;
        }

        // Table column setup
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

            if (sel == null) {
                return;
            }

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

            } catch (Exception e) {
                return;
            }

        });

        List<Employee> employeeList = employeeManager.getAll();

        employeeTableRows.clear();

        if (employeeList != null && !employeeList.isEmpty()) {
            employeeTableRows.addAll(employeeList.stream().map(Employee::mapEmployeeRow).toList());
        }

        employeeTable.setItems(employeeTableRows);

    }

    public void setAdmin(Admin a) {
        admin = a;
    }

    @FXML
    protected void addEmployee() {

        String newId = Generate.generateUserId(employeeManager, Role.valueOf(roleComboBox.getValue()));
        String firstName = safeText(firstNameField);
        String lastName = safeText(lastNameField);
        String phoneNumber = safeText(phoneNumberField);
        String email = safeText(emailField);
        String uname = safeText(usernameField);
        String role = roleComboBox.getValue();
        LocalDate dob = dobPicker.getValue();
        LocalDate startDate = startDatePicker.getValue();
        String pwd = safeText(passwordField);

        // checks whether all fields are filled to prevent exceptions

        if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || uname.isEmpty() || role == null
                || dob == null || startDate == null) {

            statusLabel.setText("Please fill all the fileds");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if ((employeeManager.find(uname))) {
            statusLabel.setText("Username already exists");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        Employee emp = new Employee(newId, firstName, lastName, dob, phoneNumber, email, uname, Role.valueOf(role), startDate);
        emp.setPassword(pwd);
        employeeManager.add(emp);
        employeeTableRows.add(Employee.mapEmployeeRow(emp));

        statusLabel.setText("Employee added!");
        statusLabel.setStyle("-fx-text-fill: green;");

        clearFields();

    }

    @FXML
    protected void updateEmployee() {

        if (employeeTable == null)
            return;

        String firstName = safeText(firstNameField);
        String lastName = safeText(lastNameField);
        String phoneNumber = safeText(phoneNumberField);
        String email = safeText(emailField);
        String uname = safeText(usernameField);
        String role = safeText(roleComboBox);
        LocalDate dob = dobPicker.getValue();
        LocalDate startDate = startDatePicker.getValue();
        String pwd = safeText(passwordField);

        if (!(employeeManager.find(uname))) {
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

        if (selectedEmployee.getFirstName().equals(firstName)){
            updatedRecords.put("firstName", firstName);
            selectedEmployee.setFirstName(firstName);
        }
        
        if (selectedEmployee.getLastName().equals(lastName)) {
            updatedRecords.put("lastName", lastName);
            selectedEmployee.setLastName(lastName);
        }
        
        if (selectedEmployee.getPhone().equals(phoneNumber)) {
            updatedRecords.put("phoneNumber", phoneNumber);
            selectedEmployee.setPhone(phoneNumber);
        }
        
        if (selectedEmployee.getEmail().equals(email)) {
            updatedRecords.put("email", email);
            selectedEmployee.setEmail(email);
        }
        
        if (selectedEmployee.getUsername().equals(uname)) {
            updatedRecords.put("username", uname);
            selectedEmployee.setUsername(uname);
        }
        
        if (selectedEmployee.getRole().equals(role)) {
            updatedRecords.put("role", role);
            selectedEmployee.setRole(role);
        }
        
        if (dob != null && selectedEmployee.getDob().equals(dobPicker.getValue().toString())) {
            updatedRecords.put("dob", dob);
            selectedEmployee.setDob(dob);
        }
        
        if (startDate != null
                && selectedEmployee.getStartDate().equals(startDatePicker.getValue().toString())) {
            updatedRecords.put("startDate", startDate);
            selectedEmployee.setStartDate(startDate);
        }

        String prevPwd = employeeManager.get(selectedEmployee.getUsername()).getPassword();
        if (prevPwd.equals(pwd)) {
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

        clearFields();
    }

    @FXML
    protected void deleteEmployee() {

        if (employeeTable == null){return;}

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
        navigateToLoginPanel((Stage) btnLogout.getScene().getWindow(), statusLabel);
    }

    @FXML
    public void showProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/profile.fxml"));
            Scene scene = new Scene(loader.load());
            String css = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            Image icon = new Image(Navigators.class.getResourceAsStream("/org/oop_project/view/images/icon.png"));
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setTitle("SaleSync - Profile");
            stage.setResizable(false);
            stage.centerOnScreen();

            ProfileController pc = (ProfileController) loader.getController();
            pc.setDetails(admin);

            stage.show();

        } catch (Exception e) {
            System.err.println("Error displaying profile: " + e.getMessage());
        }
        actionEvent.consume();
    }

    @FXML
    public void showAnalytics(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/analytics.fxml"));
            Scene scene = new Scene(loader.load());
            String css = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("SaleSync - Analytics");
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            System.err.println("Error displaying analytics: " + e.getMessage());
        }
        actionEvent.consume();
    }
}