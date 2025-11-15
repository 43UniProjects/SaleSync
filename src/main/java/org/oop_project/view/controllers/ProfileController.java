package org.oop_project.view.controllers;

import org.oop_project.database_handler.models.Employee;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {

    @FXML
    private Label profileHead;

    @FXML
    private Label userIdLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label dobLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label startDateLabel;
    @FXML
    private Label salaryLabel;
    @FXML
    private Label otRateLabel;
    @FXML
    private Label dayOffsLabel;

    public void setDetails(Employee emp) {
        profileHead.setText("Hello, " + emp.getFirstName() + "!");
        userIdLabel.setText(emp.getId());
        firstNameLabel.setText(emp.getFirstName());
        lastNameLabel.setText(emp.getLastName());
        dobLabel.setText(emp.getDob().toString());
        phoneNumberLabel.setText(emp.getPhoneNumber());
        emailLabel.setText(emp.getEmail());
        usernameLabel.setText(emp.getUsername());
        roleLabel.setText(emp.getRole().toString());
        startDateLabel.setText(emp.getStartDate().toString());
        //salaryLabel.setText(String.valueOf(emp.getSalary()));
        //otRateLabel.setText(String.valueOf(emp.getOtRate()));
        //dayOffsLabel.setText(String.valueOf(emp.getDayOffsPerMonth()));
    }
}
