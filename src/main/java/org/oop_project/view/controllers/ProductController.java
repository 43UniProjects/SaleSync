package org.oop_project.view.controllers;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


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
    @FXML private TextField supplierIdField;
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
    @FXML private TableColumn<ProductRow, String> colSupplier;
    @FXML private TableColumn<ProductRow, String> colQty;

    private final ObservableList<ProductRow> products = FXCollections.observableArrayList();
    private int nextId = 1; // Auto-generated product id number (demo)

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
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
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
                supplierIdField.setText(sel.getSupplierId());
                quantityField.setText(sel.getQuantity());
            }
        });

        generateProductId();
    }

    private void generateProductId() {
        productIdField.setText(String.format("P%04d", nextId));
    }

    @FXML
    protected void addProduct() {
        // TODO: Replace with DB insert via ProductOperations
        String id = String.format("P%04d", nextId++);
        String name = safe(nameField);
        String desc = safe(descriptionField);
        String type = unitTypeCombo.getValue() != null ? unitTypeCombo.getValue() : "";
        String family = safe(familyField);
        String subFamily = safe(subFamilyField);
        double unitPrice = parseDouble(unitPriceField);
        double tax = parseDouble(taxRateField);
        double discount = parseDouble(discountRateField);
        int supplierId = parseInt(supplierIdField);
        double qty = parseDouble(quantityField);
        double retail = calcRetail(unitPrice, tax, discount);

        products.add(new ProductRow(id, name, desc, type, family, subFamily, unitPrice, tax, discount, retail, supplierId, qty));
        status("Product added! (Demo mode)", true);
        clearFields();
        generateProductId();
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
        sel.setSupplierId(parseInt(supplierIdField));
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
        generateProductId();
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
        supplierIdField.clear();
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

    // Lightweight row model for the TableView (UI-only)
    public static class ProductRow {
    private final String id;
        private String name;
        private String description;
        private String type;
        private String family;
        private String subFamily;
        private double unitPrice;
        private double taxRate;
        private double discountRate;
        private double retailPrice;
        private int supplierId;
        private double quantity;

        public ProductRow(String id, String name, String description, String type, String family, String subFamily, double unitPrice, double taxRate, double discountRate, double retailPrice, int supplierId, double quantity) {
            this.id = id; this.name = name; this.description = description; this.type = type; this.family = family; this.subFamily = subFamily; this.unitPrice = unitPrice; this.taxRate = taxRate; this.discountRate = discountRate; this.retailPrice = retailPrice; this.supplierId = supplierId; this.quantity = quantity;
        }
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getType() { return type; }
        public String getFamily() { return family; }
        public String getSubFamily() { return subFamily; }
        public String getUnitPrice() { return String.format("%.2f", unitPrice); }
        public String getTaxRate() { return String.format("%.2f", taxRate); }
        public String getDiscountRate() { return String.format("%.2f", discountRate); }
        public String getRetailPrice() { return String.format("%.2f", retailPrice); }
        public String getSupplierId() { return String.valueOf(supplierId); }
        public String getQuantity() { return String.format("%.2f", quantity); }
        public void setName(String v) { this.name = v; }
        public void setDescription(String v) { this.description = v; }
        public void setType(String v) { this.type = v; }
        public void setFamily(String v) { this.family = v; }
        public void setSubFamily(String v) { this.subFamily = v; }
        public void setUnitPrice(double v) { this.unitPrice = v; }
        public void setTaxRate(double v) { this.taxRate = v; }
        public void setDiscountRate(double v) { this.discountRate = v; }
        public void setRetailPrice(double v) { this.retailPrice = v; }
        public void setSupplierId(int v) { this.supplierId = v; }
    public void setQuantity(double v) { this.quantity = v; }
    }
}
