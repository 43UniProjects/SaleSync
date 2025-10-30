package org.oop_project.view.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.oop_project.DatabaseHandler.enums.UnitType;
import org.oop_project.DatabaseHandler.models.Product;
import org.oop_project.DatabaseHandler.operations.ProductOperations;
import org.oop_project.view.helpers.ProductRow;

import java.io.IOException;

import static org.oop_project.utils.Generate.generateProductId;



public class ProductController {

    @FXML private TextField productIdField;
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> unitTypeCombo;
    @FXML private TextField familyField;
    @FXML private TextField subFamilyField;
    @FXML private TextField unitPriceField;
    @FXML private TextField taxRateField;
    @FXML private TextField discountRateField;
    @FXML private TextField quantityField;
    @FXML private Label statusLabel;

    @FXML private TextField searchField;

    @FXML private TableView<ProductRow> productTable;
    @FXML private TableColumn<ProductRow, String> colId;
    @FXML private TableColumn<ProductRow, String> colName;
    @FXML private TableColumn<ProductRow, String> colType;
    @FXML private TableColumn<ProductRow, String> colFamily;
    @FXML private TableColumn<ProductRow, String> colSubFamily;
    @FXML private TableColumn<ProductRow, String> colUnitPrice;
    @FXML private TableColumn<ProductRow, String> colTax;
    @FXML private TableColumn<ProductRow, String> colDiscount;
    @FXML private TableColumn<ProductRow, String> colRetail;
    @FXML private TableColumn<ProductRow, String> colQty;

    private final ObservableList<ProductRow> products = FXCollections.observableArrayList();

    private final static ProductOperations productManager = new ProductOperations();

    @FXML
    public void initialize() {
        unitTypeCombo.getItems().addAll("UNIT", "KILOS", "LITERS");

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colFamily.setCellValueFactory(new PropertyValueFactory<>("family"));
        colSubFamily.setCellValueFactory(new PropertyValueFactory<>("subFamily"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTax.setCellValueFactory(new PropertyValueFactory<>("taxRate"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discountRate"));
        colRetail.setCellValueFactory(new PropertyValueFactory<>("retailPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productTable.setItems(products);

        // Populate form when row selected
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel != null) {
                productIdField.setText(sel.getId());
                nameField.setText(sel.getName());
                descriptionField.setText(sel.getDescription());
                unitTypeCombo.setValue(sel.getType());
                familyField.setText(sel.getFamily());
                subFamilyField.setText(sel.getSubFamily());
                unitPriceField.setText(sel.getUnitPrice());
                taxRateField.setText(sel.getTaxRate());
                discountRateField.setText(sel.getDiscountRate());
                quantityField.setText(sel.getQuantity());
            }
        });
    }

    @FXML
    protected void addProduct() {

        String id = generateProductId(productManager, safe(familyField), safe(subFamilyField));
        String name = safe(nameField);
        String desc = safe(descriptionField);
        String type = unitTypeCombo.getValue() != null ? unitTypeCombo.getValue() : "";
        String family = safe(familyField);
        String subFamily = safe(subFamilyField);
        double unitPrice = parseDouble(unitPriceField);
        double tax = parseDouble(taxRateField);
        double discount = parseDouble(discountRateField);
        double qty = parseDouble(quantityField);
        double retail = calcRetail(unitPrice, tax, discount);

        Product product = new Product(id, name, desc, UnitType.valueOf(type), family, subFamily, unitPrice, tax, discount, qty);
        productManager.add(product);

        products.add(new ProductRow(id, name, desc, type, family, subFamily, unitPrice, tax, discount, retail, qty));


        status("Product added! (Demo mode)", true);
        clearFields();
    }

    @FXML
    protected void updateProduct() {
        // TODO: Replace with DB update by ID
        ProductRow sel = productTable.getSelectionModel().getSelectedItem();
        if (sel == null) { status("Select a row to update", false); return; }
        sel.setName(safe(nameField));
        sel.setDescription(safe(descriptionField));
        sel.setType(unitTypeCombo.getValue() != null ? unitTypeCombo.getValue() : "");
        sel.setFamily(safe(familyField));
        sel.setSubFamily(safe(subFamilyField));
        double unitPrice = parseDouble(unitPriceField);
        double tax = parseDouble(taxRateField);
        double discount = parseDouble(discountRateField);
        sel.setUnitPrice(unitPrice);
        sel.setTaxRate(tax);
        sel.setDiscountRate(discount);
        sel.setRetailPrice(calcRetail(unitPrice, tax, discount));
        sel.setQuantity(parseDouble(quantityField));
        productTable.refresh();
        status("Product updated! (Demo mode)", true);
    }

    @FXML
    protected void removeProduct() {
        // TODO: Replace with DB delete by ID
        ProductRow sel = productTable.getSelectionModel().getSelectedItem();
        if (sel == null) { status("Select a row to remove", false); return; }
        products.remove(sel);
        status("Product removed! (Demo mode)", true);
        clearFields();
    }

    @FXML
    protected void clearFields() {
        nameField.clear();
        descriptionField.clear();
        unitTypeCombo.setValue(null);
        familyField.clear();
        subFamilyField.clear();
        unitPriceField.clear();
        taxRateField.clear();
        discountRateField.clear();
        quantityField.clear();
        productTable.getSelectionModel().clearSelection();
    }

    @FXML
    protected void searchProduct() {
        // TODO: Implement server-side or DB-backed search
        status("Search not implemented yet (Demo)", false);
    }

    @FXML
    protected void resetSearch() {
        searchField.clear();
        productTable.setItems(products);
    }

    @FXML
    protected void backToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/oop_project/view/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 600, 450);
            String css = getClass().getResource("/org/oop_project/view/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);
            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("SaleSync - Login");
            stage.setWidth(600);
            stage.setHeight(450);
            stage.setResizable(false);
        } catch (IOException e) {
            if (statusLabel != null) { statusLabel.setText("Error loading login!"); }
        }
    }

    private void status(String msg, boolean ok) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
            statusLabel.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: orange;");
        }
    }

    private String safe(TextField tf) { return tf != null && tf.getText() != null ? tf.getText().trim() : ""; }
    private double parseDouble(TextField tf) { try { return Double.parseDouble(safe(tf)); } catch (NumberFormatException e) { return 0.0; } }
    private int parseInt(TextField tf) { try { return Integer.parseInt(safe(tf)); } catch (NumberFormatException e) { return 0; } }
    private double calcRetail(double unitPrice, double taxRate, double discountRate) {
        double tax = unitPrice * (taxRate / 100.0);
        double discount = unitPrice * (discountRate / 100.0);
        return unitPrice + tax - discount;
    }


}
