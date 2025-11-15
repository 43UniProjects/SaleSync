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

    public void setCashierDetails(Employee cashier) {
        profileHead.setText("Hello, " + cashier.getFirstName() + "!");
        userIdLabel.setText(cashier.getId());
        firstNameLabel.setText(cashier.getFirstName());
        lastNameLabel.setText(cashier.getLastName());
        dobLabel.setText(cashier.getDob().toString());
        phoneNumberLabel.setText(cashier.getPhoneNumber());
        emailLabel.setText(cashier.getEmail());
        usernameLabel.setText(cashier.getUsername());
        roleLabel.setText(cashier.getRole().toString());
        startDateLabel.setText(cashier.getStartDate().toString());
        //salaryLabel.setText(String.valueOf(cashier.getSalary()));
        //otRateLabel.setText(String.valueOf(cashier.getOtRate()));
        //dayOffsLabel.setText(String.valueOf(cashier.getDayOffsPerMonth()));
    }
}
