package org.oop_project.view.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.oop_project.DatabaseHandler.models.Product;
import org.oop_project.DatabaseHandler.operations.ProductOperations;
import org.oop_project.view.helpers.BillRow;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn; // Import URL
import javafx.scene.control.TableView; // Import ResourceBundle
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
        } catch (Exception e) {
            // ignore for demo
        }
    }

    @FXML
    public void onScanComplete(ActionEvent event) {
        String id = scanInputField.getText();

        if(!productManager.find(id)) {
            statusLabel.setVisible(true);
            statusLabel.setText("Product not found!");
            statusLabel.setStyle("-fx-text-fill: red;");
            scanInputField.clear();

            // clear product summary
            productNameLabel.setText("-");
            productPriceLabel.setText("-");
            productQtyLabel.setText("-");
            productDescArea.setText("");

        }else {
            Product prod = productManager.get(id);
            // populate readable summary labels
            productNameLabel.setText(prod.getName() != null ? prod.getName() : "-");
            productPriceLabel.setText(String.format("%,.2f", prod.getUnitPrice()));
            productQtyLabel.setText(String.format("%s", prod.getAvailableQuantity()));
            // store numeric values
            currentPrice = prod.getUnitPrice();
            currentAvailable = prod.getAvailableQuantity();
            productDescArea.setText(prod.getDescription() != null ? prod.getDescription() : "");

            if(prod.getAvailableQuantity() <= 0) {
                statusLabel.setText("Product out of stock!");
                statusLabel.setStyle("-fx-text-fill: red;");

                scanInputField.clear();

                scanInputField.requestFocus();
            } else {
                statusLabel.setText("Enter the quantity needed");
                statusLabel.setStyle("-fx-text-fill: green;");

                statusLabel.setVisible(true);
                btnReset.setVisible(true);
                btnAdd.setVisible(true);

                amountField.setPromptText("Enter quantity");
                amountField.setVisible(true);
                amountField.requestFocus();

                scanInputField.clear();
                scanInputField.setPromptText("Scan completed!");
            }
        }

        
        
    }

    public void clearField() {
        statusLabel.setVisible(false);
        btnReset.setVisible(false);
        btnAdd.setVisible(false);
        amountField.setVisible(false);
        amountField.clear();
        statusLabel.setText("");
        productNameLabel.setText("-");
        productPriceLabel.setText("-");
        productQtyLabel.setText("-");
        productDescArea.setText("");
        scanInputField.clear();
        scanInputField.setPromptText("Waiting product to be scanned...");
        scanInputField.requestFocus();



    }

    private String safe(TextField tf) { return tf != null && tf.getText() != null ? tf.getText().trim() : ""; }

    public void addProductToBill(ActionEvent actionEvent) {
        String qtyText = safe(amountField);
        if(qtyText.isEmpty()) {
            statusLabel.setText("Quantity cannot be empty!");
            statusLabel.setStyle("-fx-text-fill: red;");
            amountField.requestFocus();
            return;
        }

        double qty;
        try {
            qty = Double.parseDouble(qtyText);
            System.out.println("Parsed quantity: " + qty);
            if(qty <= 0) {
                statusLabel.setText("Quantity must be greater than zero!");
                statusLabel.setStyle("-fx-text-fill: red;");
                amountField.requestFocus();
                return;
            }

            if(qty > currentAvailable) {
                statusLabel.setText("Insufficient stock available!");
                statusLabel.setStyle("-fx-text-fill: red;");
                amountField.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid quantity format!");
            statusLabel.setStyle("-fx-text-fill: red;");
            amountField.requestFocus();
            return;
        }

        // Create a bill row for the product
        BillRow billRow = new BillRow(
                    itemNo++,
                    productNameLabel.getText(),
                    currentPrice,
                    qty,
                    qty * currentPrice
            );

        netTotal += qty * currentPrice;
        lblNetTotal.setText(String.format("Rs. %.2f", netTotal));

        billRows.add(billRow);

        totalProductsList.add(billRow); // adds product object to total products list

        // ensure the new row is visible and selected in the table
        if (itemsList != null) {
            itemsList.scrollTo(billRow);
            itemsList.getSelectionModel().select(billRow);
        }

        
        String qtyStr;
        if (qty == (long) qty) {
            qtyStr = Long.toString((long) qty);
        } else {
            qtyStr = String.format("%,.2f", qty);
        }
        statusLabel.setText(String.format("Added %s of %s to bill.", qtyStr, productNameLabel.getText()));
        statusLabel.setStyle("-fx-text-fill: green;");

        netTotal += qty * currentPrice;

        // Reset fields for next scan
        clearField();
        scanInputField.requestFocus();
    }


    @FXML
    public void checkout(ActionEvent actionEvent) {
        if (totalProductsList == null || totalProductsList.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "No items to checkout.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Calculate total from items to avoid any prior accumulation errors
        double total = 0.0;
        for (BillRow r : totalProductsList) {
            total += r.getTotal();
        }

        // Ask for cash received
        javafx.scene.control.TextInputDialog cashDialog =
                new javafx.scene.control.TextInputDialog(String.format("%.2f", total));
        cashDialog.setTitle("Cash Received");
        cashDialog.setHeaderText("Enter cash amount received");
        cashDialog.setContentText("Cash (Rs.):");
        java.util.Optional<String> result = cashDialog.showAndWait();
        if (!result.isPresent()) return;

        double cash;
        try {
            cash = Double.parseDouble(result.get().trim());
        } catch (NumberFormatException ex) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Invalid cash amount.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        if (cash < total) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR,
                    "Insufficient cash. Required: Rs. " + String.format("%.2f", total));
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        double balance = cash - total;

        // Build simple bill text
        StringBuilder sb = new StringBuilder();
        sb.append("SaleSync - Bill\n");
        sb.append("------------------------------------------------------------\n");
        sb.append("Date: ")
          .append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
          .append("\n\n");
        sb.append(String.format("%-4s %-22s %8s %6s %10s%n", "No", "Item", "Price", "Qty", "Total"));
        sb.append("------------------------------------------------------------\n");
        for (BillRow r : totalProductsList) {
            sb.append(String.format("%-4d %-22.22s %8.2f %6.2f %10.2f%n",
                    r.getItemNo(), r.getItemName(), r.getPrice(), r.getQuantity(), r.getTotal()));
        }
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("%-30s %20s%n", "Subtotal:", String.format("Rs. %.2f", total)));
        sb.append(String.format("%-30s %20s%n", "Cash:", String.format("Rs. %.2f", cash)));
        sb.append(String.format("%-30s %20s%n", "Balance:", String.format("Rs. %.2f", balance)));

        // Show minimal bill interface
        javafx.scene.control.TextArea billArea = new javafx.scene.control.TextArea(sb.toString());
        billArea.setEditable(false);
        billArea.setWrapText(false);
        billArea.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 12px;");

        javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(10, billArea);
        root.setStyle("-fx-padding: 10;");

        javafx.scene.Scene scene = new javafx.scene.Scene(root, 640, 480);
        javafx.stage.Stage billStage = new javafx.stage.Stage();
        billStage.setTitle("Bill");
        billStage.setScene(scene);
        billStage.initOwner(btnCheckout.getScene().getWindow());
        billStage.show();

        // Reset for next transaction
        billRows.clear();
        totalProductsList.clear();
        itemNo = 1;
        netTotal = 0.0;
        lblNetTotal.setText("Rs. 0.00");
        clearField();
    }
}
