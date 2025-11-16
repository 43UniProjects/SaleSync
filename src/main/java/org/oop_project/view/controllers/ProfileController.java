package org.oop_project.view.controllers;

import org.oop_project.database_handler.models.Admin;
import org.oop_project.database_handler.models.Cashier;
import org.oop_project.database_handler.models.ProductManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ProfileController {
    @FXML
    private Label lblDetails;

    @FXML
    private AnchorPane detailsPane;


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

    public void setDetails(Admin admin) {
        profileHead.setText("Hello, " + admin.getFirstName() + "!");
        userIdLabel.setText(admin.getId());
        firstNameLabel.setText(admin.getFirstName());
        lastNameLabel.setText(admin.getLastName());
        dobLabel.setText(admin.getDob().toString());
        phoneNumberLabel.setText(admin.getPhoneNumber());
        emailLabel.setText(admin.getEmail());
        usernameLabel.setText(admin.getUsername());
        roleLabel.setText(admin.getRole().toString());
        startDateLabel.setText(admin.getStartDate().toString());
        salaryLabel.setText(String.valueOf(admin.getSalary()));
        otRateLabel.setText(String.valueOf(admin.getOtRate()));
        dayOffsLabel.setText(String.valueOf(admin.getDayOffsPerMonth()));
    }

    public void setDetails(Cashier cashier) {

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
        salaryLabel.setText(String.valueOf(cashier.getSalary()));
        otRateLabel.setText(String.valueOf(cashier.getOtRate()));
        dayOffsLabel.setText(String.valueOf(cashier.getDayOffsPerMonth()));
    }

    public void setDetails(ProductManager productManager) {

        profileHead.setText("Hello, " + productManager.getFirstName() + "!");
        userIdLabel.setText(productManager.getId());
        firstNameLabel.setText(productManager.getFirstName());
        lastNameLabel.setText(productManager.getLastName());
        dobLabel.setText(productManager.getDob().toString());
        phoneNumberLabel.setText(productManager.getPhoneNumber());
        emailLabel.setText(productManager.getEmail());
        usernameLabel.setText(productManager.getUsername());
        roleLabel.setText(productManager.getRole().toString());
        startDateLabel.setText(productManager.getStartDate().toString());
        salaryLabel.setText(String.valueOf(productManager.getSalary()));
        otRateLabel.setText(String.valueOf(productManager.getOtRate()));
        dayOffsLabel.setText(String.valueOf(productManager.getDayOffsPerMonth()));
    }
}
