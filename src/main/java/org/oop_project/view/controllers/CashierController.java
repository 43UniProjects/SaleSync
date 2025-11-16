package org.oop_project.view.controllers;


import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import org.oop_project.database_handler.models.Cashier;
import org.oop_project.database_handler.models.Product;
import org.oop_project.database_handler.operations.ProductOperations;
import org.oop_project.utils.Text;
import org.oop_project.view.helpers.BillRow;
import org.oop_project.view.helpers.Navigators;

import static org.oop_project.view.helpers.Validator.safeText;
import static org.oop_project.view.helpers.Navigators.navigateToLoginPanel;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CashierController implements Initializable {
    @FXML
    private Label headAdminPanel;

    @FXML
    private Label subHeadAdminPanel;

    @FXML
    private Button btnAccount;

    @FXML
    private AnchorPane detailsPane;

    @FXML
    private Label netTotalMsg;


    @FXML
    private Button btnLogout;
    @FXML
    private TextField scanInputField;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField amountField;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnAdd;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label productQtyLabel;
    @FXML
    private TextArea productDescArea;
    @FXML
    private TableView<BillRow> billItemTable;
    @FXML
    private TableColumn<BillRow, Integer> colNo;
    @FXML
    private TableColumn<BillRow, String> colItem;
    @FXML
    private TableColumn<BillRow, Double> colPrice;
    @FXML
    private TableColumn<BillRow, Double> colQty;
    @FXML
    private TableColumn<BillRow, Double> colTotal;
    @FXML
    private Label lblNetTotal;
    @FXML
    private Button btnCheckout;

    private final static ProductOperations productManager = new ProductOperations();
    private final ObservableList<BillRow> billItemTableRows = FXCollections.observableArrayList();
    private ArrayList<BillRow> productList = new ArrayList<>();

    private double currentItemPrice = 0.0;
    private double currentItemStockQty = 0.0;
    private int itemNumberIter = 1;
    private double totalBillAmount = 0.0;
    private double receivedCashAmount = 0.0;
    private double balance;

    private Cashier cashier;

    
    String TABLE_CELL_SPACE = "    ";
    int billViewLength;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnReset.setVisible(false);
        btnAdd.setVisible(false);
        amountField.setVisible(false);
        lblNetTotal.setText("Rs. 0.00");

        colNo.setCellValueFactory(new PropertyValueFactory<>("itemNo"));
        colItem.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        billItemTable.setItems(billItemTableRows);

        scanInputField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ignored1, String prevValue, String newValue) {
                idAutoSubmit(prevValue, newValue);
            }
        });

        scanInputField.setPromptText("WAITING FOR SCAN..");
        Platform.runLater(() -> scanInputField.requestFocus());
    }

    public void setCashier(Cashier c) {
        cashier = c;
    }

    @FXML
    protected void backToLogin() {
        navigateToLoginPanel((Stage) btnLogout.getScene().getWindow(), statusLabel);
    }

    @FXML
    public void idAutoSubmit(String prevScanValue, String currentScanValue) {

        int dashMarkCount = dashMarkCount(currentScanValue.toCharArray());

        if (dashMarkCount < 2)
            return;

        boolean isPreviousAddonDigit = prevScanValue.isBlank() ? false
                : Character.isDigit(prevScanValue.charAt(prevScanValue.length() - 1));
        boolean isCurrentAddonDigit = Character.isDigit(currentScanValue.charAt(currentScanValue.length() - 1));
        boolean isCurrentAddonLetter = Character.isAlphabetic(currentScanValue.charAt(currentScanValue.length() - 1));

        if (isPreviousAddonDigit && isCurrentAddonLetter) {
            Platform.runLater(() -> scanInputField.clear());
            statusLabel.setText("");
            Platform.runLater(() -> scanInputField.setText(currentScanValue.substring(currentScanValue.length() - 1)));
            scanInputField.requestFocus();
        }

        if (!productManager.find(currentScanValue)) {

            if (!isPreviousAddonDigit && isCurrentAddonDigit) {
                statusLabel.setText("Product not found!");
                statusLabel.setStyle("-fx-text-fill: red;");
            }

            scanInputField.requestFocus();
            return;
        }

        Product prod = productManager.get(currentScanValue);

        productNameLabel.setText(prod.getName());
        productPriceLabel.setText(String.format("%,.2f", prod.getUnitPrice()));
        productQtyLabel.setText(String.format("%s", prod.getAvailableQuantity()));
        currentItemPrice = prod.getUnitPrice();
        currentItemStockQty = prod.getAvailableQuantity();
        productDescArea.setText(prod.getDescription());

        if (prod.getAvailableQuantity() <= 0) {
            statusLabel.setText("Product out of stock!");
            statusLabel.setStyle("-fx-text-fill: red;");
            Platform.runLater(() -> scanInputField.clear());
            scanInputField.requestFocus();
            return;
        }

        statusLabel.setText("Enter the quantity needed");
        statusLabel.setStyle("-fx-text-fill: green;");

        btnReset.setVisible(true);
        btnAdd.setVisible(true);

        amountField.setPromptText("Enter quantity");

        amountField.setVisible(true);
        amountField.requestFocus();

        Platform.runLater(() -> scanInputField.clear());
        Platform.runLater(() -> scanInputField.setPromptText("WAITING FOR SCAN..."));
    }

    public void addProductToBill(ActionEvent actionEvent) {

        double requiredQty;
        String requiredQtyText = safeText(amountField);

        if (requiredQtyText.isEmpty()) {
            statusLabel.setText("Quantity cannot be empty!");
            statusLabel.setStyle("-fx-text-fill: red;");
            amountField.requestFocus();
            return;
        }

        try {
            requiredQty = Double.parseDouble(requiredQtyText);
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid Quantity");
            statusLabel.setStyle("-fx-text-fill: red;");
            amountField.requestFocus();
            return;
        }

        if (requiredQty <= 0) {
            statusLabel.setText("Invalid Quantity");
            statusLabel.setStyle("-fx-text-fill: red;");
            amountField.requestFocus();
            return;
        }

        if (requiredQty > currentItemStockQty) {
            statusLabel.setText("Insufficient stock!");
            statusLabel.setStyle("-fx-text-fill: red;");
            amountField.requestFocus();
            return;
        }

        BillRow newItem = new BillRow(
                itemNumberIter++,
                productNameLabel.getText(),
                currentItemPrice,
                requiredQty,
                requiredQty * currentItemPrice);

        Optional<BillRow> matchedPreviousBillRow = itemAddedBefore(newItem.getItemName());

        if (matchedPreviousBillRow.isPresent()) {

            BillRow matchedPrevItem = matchedPreviousBillRow.get();

            totalBillAmount = totalBillAmount - (matchedPrevItem.getQuantity() * matchedPrevItem.getPrice());

            requiredQty = requiredQty + matchedPrevItem.getQuantity();

            BillRow updatedItem = new BillRow(
                    matchedPrevItem.getItemNo(),
                    matchedPrevItem.getItemName(),
                    currentItemPrice,
                    requiredQty,
                    currentItemPrice * requiredQty);

            newItem = updatedItem;

            int indexOfMatchedItem = productList.indexOf(matchedPrevItem);

            billItemTableRows.add(indexOfMatchedItem, newItem);
            productList.add(indexOfMatchedItem, newItem);

            // remove right shifted matched item from the list
            productList.remove(indexOfMatchedItem + 1);
            billItemTableRows.remove(indexOfMatchedItem + 1);

        } else {

            billItemTableRows.add(newItem);
            productList.add(newItem);

        }

        totalBillAmount += requiredQty * currentItemPrice;
        lblNetTotal.setText(String.format("Rs. %.2f", totalBillAmount));

        billItemTable.scrollTo(newItem);
        billItemTable.getSelectionModel().select(newItem);

        if (requiredQty % 1 == 0) {
            requiredQtyText = String.valueOf(Integer.parseInt(requiredQtyText));
        } else {
            requiredQtyText = String.format("%.2f", requiredQty);
        }

        statusLabel.setText(String.format("%s of %s Added", requiredQtyText, productNameLabel.getText()));
        statusLabel.setStyle("-fx-text-fill: green;");

        clearField();
        scanInputField.requestFocus();
        actionEvent.consume();
    }

    @FXML
    public void checkout(ActionEvent actionEvent) {

        if (productList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No items to checkout.");
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        TextInputDialog cashDialog = new TextInputDialog(String.format("%.2f", totalBillAmount));
        cashDialog.setTitle("Payment");
        cashDialog.setHeaderText("Net total: Rs. %.2f".formatted(totalBillAmount));
        cashDialog.setContentText("Cash Received (Rs)");
        cashDialog.setGraphic(null);

        Optional<String> result = cashDialog.showAndWait();

        if (!result.isPresent()) {
            statusLabel.setText("Checkout Aborted");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        
        try {
            receivedCashAmount = Double.parseDouble(result.get().trim());
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid cash amount.");
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        if (receivedCashAmount < totalBillAmount) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Insufficient cash. Required: Rs. %.2f ".formatted(totalBillAmount));
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        balance = receivedCashAmount - totalBillAmount;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/checkout.fxml"));
            Scene scene = new Scene(loader.load());
            String css = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = new Stage();
            Image icon = new Image(Navigators.class.getResourceAsStream("/org/oop_project/view/images/icon.png"));
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setTitle("SaleSync - Checkout");
            stage.setResizable(false);
            stage.centerOnScreen();

            // Pass the bill text into the checkout controller before showing
            CheckoutController cc = (CheckoutController) loader.getController();
            cc.setBillText(getFormattedBill());
            cc.setBillViewWidth(billViewLength * 7.5);
            
            stage.show();

        } catch (Exception e) {
            System.err.println("Error displaying bill: " + e.getMessage());
        }

        // Reset for next transaction
        billItemTableRows.clear();
        productList.clear();
        itemNumberIter = 1;
        totalBillAmount = 0.0;
        lblNetTotal.setText("Rs. 0.00");
        clearField();
    }

   
    private String getFormattedBill() {

        String HEADER = "SaleSync - Bill";
        String DATE = "DATE: %S".formatted(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        String CASHIER = "CASHIER: %s".formatted(Text.textCapitalize(cashier.getFirstName() + " " + cashier.getLastName()));
        String COUNTER = "COUNTER: 01";
        String NEW_LINE = "\n";
        String[] tableHeaders = { "Item No", "Name", "Price", "QTY", "Total" };
        HashMap<String, Integer> colLengths = calculateCellWidths(tableHeaders);
        billViewLength = 0;
        String currentSpacing;

        for (int len : colLengths.values())
            billViewLength += len;

        String HRULE = "-".repeat(billViewLength);

        StringBuilder tableBuilder = new StringBuilder();

        tableBuilder.append(NEW_LINE + HRULE + NEW_LINE);
        currentSpacing = " ".repeat((billViewLength - HEADER.length()) / 2);
        tableBuilder.append(NEW_LINE + currentSpacing + HEADER + currentSpacing + NEW_LINE);
        tableBuilder.append(NEW_LINE + HRULE + NEW_LINE);
        tableBuilder.append(DATE + NEW_LINE);
        tableBuilder.append(CASHIER + NEW_LINE);
        tableBuilder.append(COUNTER);
        tableBuilder.append(NEW_LINE + HRULE + NEW_LINE);

        for (String tableHead : tableHeaders) {
            tableBuilder.append(("%-" + colLengths.get(tableHead) + "s").formatted(tableHead));
        }
        
        tableBuilder.append(NEW_LINE + HRULE + NEW_LINE);

        for (BillRow item : productList) {

            String itemNoStr = item.getItemNo() < 10 ? "0" + item.getItemNo() : String.valueOf(item.getItemNo());
            String itemPriceStr = "%.2f".formatted(item.getPrice());
            String itemQtyStr;

            if (item.getQuantity() % 1 == 0) {
                itemQtyStr = item.getQuantity() < 10 ? "0" + (int) item.getQuantity()
                        : String.valueOf((int) item.getQuantity());
            } else {
                itemQtyStr = item.getQuantity() < 10 ? "0" + item.getQuantity() : String.valueOf(item.getQuantity());
            }

            String itemTotalStr = "%.2f".formatted(item.getTotal());

            tableBuilder.append(("%-" + colLengths.get(tableHeaders[0]) + "s").formatted( itemNoStr));
            tableBuilder.append(("%-" + colLengths.get(tableHeaders[1]) + "s").formatted(item.getItemName()));
            tableBuilder.append(("%-" + colLengths.get(tableHeaders[2]) + "s").formatted(itemPriceStr));
            tableBuilder.append(("%-" + colLengths.get(tableHeaders[3]) + "s").formatted(itemQtyStr));
            tableBuilder.append(("%-" + colLengths.get(tableHeaders[4]) + "s").formatted(itemTotalStr));
            
            tableBuilder.append(NEW_LINE + HRULE + NEW_LINE);
        }

        int RIGHT_PADDING = 4;

        String receivedCashAmountStr = String.valueOf("%.2f".formatted(receivedCashAmount));

        System.out.println(receivedCashAmountStr);
        System.out.println(receivedCashAmountStr.length());

        String formattedTotalBillAmount = ("%" + receivedCashAmountStr.length() + ".2f").formatted(totalBillAmount);
        String formatterReceivedCashAmount = ("%" + receivedCashAmountStr.length() + ".2f").formatted(receivedCashAmount);
        String formattedBalance = ("%" + receivedCashAmountStr.length() + ".2f").formatted(balance);
        

        tableBuilder.append(("%" +( billViewLength - RIGHT_PADDING) + "s" + NEW_LINE).formatted("Subtotal: Rs. %s".formatted(formattedTotalBillAmount)));
        tableBuilder.append(("%" + (billViewLength - RIGHT_PADDING) + "s" + NEW_LINE).formatted(" Payment: Rs. %s".formatted(formatterReceivedCashAmount)));
        tableBuilder.append(("%" + (billViewLength - RIGHT_PADDING) + "s" + NEW_LINE).formatted(" Balance: Rs. %s".formatted(formattedBalance)));

        
        tableBuilder.append(HRULE + NEW_LINE);

        System.out.println(tableBuilder.toString());

        return tableBuilder.toString();

    }

     private HashMap<String, Integer> calculateCellWidths(String[] colNames) {
        int forItemNumber = colNames[0].length();
        int forItemName = colNames[1].length();
        int forItemPrice = colNames[2].length();
        int forItemQuantity = colNames[3].length();
        int forItemTotal = colNames[4].length();

        HashMap<String, Integer> lengths = new HashMap<>();

        lengths.put(colNames[0], forItemNumber);
        lengths.put(colNames[1], forItemName);
        lengths.put(colNames[2], forItemPrice);
        lengths.put(colNames[3], forItemQuantity);
        lengths.put(colNames[4], forItemTotal);

        for (BillRow item : productList) {

            String itemNoStr = item.getItemNo() < 10 ? "0" + item.getItemNo() : String.valueOf(item.getItemNo());
            String itemPriceStr = "%.2f".formatted(item.getPrice());
            String itemQtyStr;

            if (item.getQuantity() % 1 == 0) {
                itemQtyStr = item.getQuantity() < 10 ? "0" + (int) item.getQuantity()
                        : String.valueOf((int) item.getQuantity());
            } else {
                itemQtyStr = item.getQuantity() < 10 ? "0" + item.getQuantity() : String.valueOf(item.getQuantity());
            }

            String itemTotalStr = "%.2f".formatted(item.getTotal());

            if (itemNoStr.length() > forItemNumber && itemNoStr.length() > lengths.get(colNames[0]))
                forItemNumber = itemNoStr.length();
            if (item.getItemName().length() > forItemName )
                forItemName = item.getItemName().length();
            if (itemPriceStr.length() > forItemPrice)
                forItemPrice = itemPriceStr.length();
            if (itemQtyStr.length() > forItemQuantity)
                forItemQuantity = itemQtyStr.length();
            if (itemTotalStr.length() > forItemTotal)
                forItemTotal = itemTotalStr.length();

            lengths.put(colNames[0], forItemNumber);
            lengths.put(colNames[1], forItemName);
            lengths.put(colNames[2], forItemPrice);
            lengths.put(colNames[3], forItemQuantity);
            lengths.put(colNames[4], forItemTotal);
        }


        for(String key: lengths.keySet()) {
            lengths.put(key, lengths.get(key) + TABLE_CELL_SPACE.length());
        }

        return lengths;

    }


    public void clearField() {

        btnReset.setVisible(false);
        btnAdd.setVisible(false);
        amountField.setVisible(false);

        statusLabel.setText("");
        productNameLabel.setText("-");
        productPriceLabel.setText("-");
        productQtyLabel.setText("-");
        productDescArea.setText("");

        amountField.clear();
        scanInputField.clear();

        scanInputField.setPromptText("WAITING FOR SCAN...");
        scanInputField.requestFocus();

    }

    private int dashMarkCount(char[] idChars) {
        int count = 0;
        for (char c : idChars) {
            if (c == '-') {
                count++;
            }
        }
        return count;
    }

    private Optional<BillRow> itemAddedBefore(String itemName) {
        return productList.stream().filter(p -> p.getItemName().equals(itemName)).findFirst();
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
            pc.setDetails(cashier);

            stage.show();

            scanInputField.requestFocus();

        } catch (Exception e) {
            System.err.println("Error displaying profile: " + e.getMessage());
        }

        actionEvent.consume();
    }

}
