package org.oop_project.view.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CashierController implements Initializable {
    private final static ProductOperations productManager = new ProductOperations();

    private final ObservableList<BillRow> billRows = FXCollections.observableArrayList();

    private ArrayList<BillRow> totalProductsList = new ArrayList<>();  // array list to hold all products added to the bill

    private int itemNo = 1;  // item number for the bill order
    private double netTotal = 0.0;

    @FXML private Button btnLogout;
    @FXML private TextArea scannedStatusField;
    @FXML private TextField scanInputField;

    @FXML private Label statusLabel;
    @FXML private TextField amountField;

    @FXML private Button btnReset;
    @FXML private Button btnAdd;
    
    // Product summary UI
    @FXML private Label productNameLabel;
    @FXML private Label productPriceLabel;
    @FXML private Label productQtyLabel;
    @FXML private TextArea productDescArea;

    // current numeric values for the scanned product (avoid parsing UI text)
    private double currentPrice = 0.0;
    private double currentAvailable = 0.0;
    
    // Billing table UI
    @FXML private TableView<org.oop_project.view.helpers.BillRow> itemsList;
    @FXML private TableColumn<org.oop_project.view.helpers.BillRow, Integer> colNo;
    @FXML private TableColumn<org.oop_project.view.helpers.BillRow, String> colItem;
    @FXML private TableColumn<org.oop_project.view.helpers.BillRow, Double> colPrice;
    @FXML private TableColumn<org.oop_project.view.helpers.BillRow, Double> colQty;
    @FXML private TableColumn<org.oop_project.view.helpers.BillRow, Double> colTotal;


    @FXML private Label lblNetTotal;

    @FXML private Button btnCheckout;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This ensures the TextField is always ready to accept
        // "keyboard" input from the scanner without clicking.
        Platform.runLater(() -> scanInputField.requestFocus());
        statusLabel.setVisible(false);
        btnReset.setVisible(false);
        btnAdd.setVisible(false);
        amountField.setVisible(false);
        lblNetTotal.setText("Rs. 0.00");

        // Setup billing table columns and bind to billRows
        if (colNo != null) colNo.setCellValueFactory(new PropertyValueFactory<>("itemNo"));
        if (colItem != null) colItem.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        if (colPrice != null) colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        if (colQty != null) colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        if (colTotal != null) colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        if (itemsList != null) itemsList.setItems(billRows);
    }

    @FXML
    protected void backToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 600, 450);
            String css = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SaleSync - Login");
            stage.setWidth(600);
            stage.setHeight(450);
            stage.setResizable(false);
            stage.centerOnScreen();
        } catch (Exception e) {}
    }

    @FXML
    public void showDetails() {
        String id = productManager.getLastId();

        Product prod = productManager.get(id);

        String text = "Id: %s\nName: %s\nDescription: %s\nPrice: %.2f";
        scannedStatusField.setText(text.formatted(prod.getId(), prod.getName(), prod.getDescription(), prod.getUnitPrice()));
    }

    public void clearField() {
        scannedStatusField.setText("Waiting product to be scanned...");
    }

    @FXML
    private void initialize() {
    }
}
