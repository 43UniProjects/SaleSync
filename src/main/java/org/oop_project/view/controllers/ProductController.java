package org.oop_project.view.controllers;

import java.io.IOException;
import java.util.List;

import org.oop_project.DatabaseHandler.enums.UnitType;
import org.oop_project.DatabaseHandler.models.Product;
import org.oop_project.DatabaseHandler.operations.Operations;
import org.oop_project.DatabaseHandler.operations.ProductOperations;
import static org.oop_project.utils.Generate.generateProductId;
import org.oop_project.view.helpers.ProductRow;

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

    @FXML
    private TextField productIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<String> unitTypeCombo;
    @FXML
    private TextField familyField;
    @FXML
    private TextField subFamilyField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private TextField taxRateField;
    @FXML
    private TextField discountRateField;
    @FXML
    private TextField quantityField;
    @FXML
    private Label statusLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<ProductRow> productTable;
    @FXML
    private TableColumn<ProductRow, String> colId;
    @FXML
    private TableColumn<ProductRow, String> colName;
    @FXML
    private TableColumn<ProductRow, String> colType;
    @FXML
    private TableColumn<ProductRow, String> colFamily;
    @FXML
    private TableColumn<ProductRow, String> colSubFamily;
    @FXML
    private TableColumn<ProductRow, String> colUnitPrice;
    @FXML
    private TableColumn<ProductRow, String> colTax;
    @FXML
    private TableColumn<ProductRow, String> colDiscount;
    @FXML
    private TableColumn<ProductRow, String> colRetail;
    @FXML
    private TableColumn<ProductRow, String> colQty;

    private final ObservableList<ProductRow> productTableRows = FXCollections.observableArrayList();

    private final static Operations<Product> productManager = new ProductOperations();

    @FXML
    public void initialize() {

        unitTypeCombo.getItems().addAll(
                UnitType.UNIT.getLabel(),
                UnitType.KILOS.getLabel(),
                UnitType.LITERS.getLabel());

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

        // Populate form when row selected
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {

            if (sel == null)
                return;

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

        });

        List<Product> productList = productManager.getAll();

        productTableRows.clear();

        productTableRows.addAll(productList.stream().map(Product::mapProductRow).toList());

        productTable.setItems(productTableRows);

    }

    @FXML
    protected void addProduct() {

        String name = safe(nameField);
        String desc = safe(descriptionField);
        String type = unitTypeCombo.getValue() != null ? unitTypeCombo.getValue() : "";
        String family = safe(familyField);
        String subFamily = safe(subFamilyField);
        double unitPrice = parseDouble(unitPriceField);
        String unitType = unitTypeCombo.getValue();
        double tax = parseDouble(taxRateField);
        double discount = parseDouble(discountRateField);
        double qty = parseDouble(quantityField);

        String id = generateProductId(productManager, family, subFamily);

        // checks whether all fields are filled to prevent exceptions


        if (unitPrice <= 0) {
            status("Invalid Unit Price", false);
            return;
        }

        if (qty <= 0) {
            status("Invalid Quantity", false);
            return;
        }

        if (name.isEmpty() || desc.isEmpty() || unitType == null || family.isEmpty() || subFamily.isEmpty()) {
            status("Please fill in all required fields!", false);
            return;
        }

        Product product = new Product(
            id, name, desc, UnitType.valueOf(type), family, subFamily, unitPrice, tax, discount, qty
        );

        productManager.add(product);

        productTableRows.add(Product.mapProductRow(product));

        status("Product added!", true);

        clearFields();
    }

    @FXML
    protected void updateProduct() {

        ProductRow sel = productTable.getSelectionModel().getSelectedItem();

        if (sel == null) {
            status("Select a row to update", false);
            return;
        }

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

        status("Product updated!", true);
    }

    @FXML
    protected void removeProduct() {

        ProductRow sel = productTable.getSelectionModel().getSelectedItem();

        if (sel == null) {
            status("Select a row to remove", false);
            return;
        }
        productManager.delete(sel.getId());
        productTableRows.remove(sel);
        status("Product removed!", true);
        clearFields();
    }

    @FXML
    protected void clearFields() {
        productIdField.clear();
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
        // Search done through filtering the table observable list

        String query = safe(searchField).toLowerCase();
        if (query.isEmpty()) {
            productTable.setItems(productTableRows);
            return;
        }
        // Implement search filtering logic here
        ObservableList<ProductRow> filtered = productTableRows
                .filtered(p -> p.getName().toLowerCase().contains(query) ||
                        p.getId().toLowerCase().contains(query) ||
                        p.getFamily().toLowerCase().contains(query) ||
                        p.getSubFamily().toLowerCase().contains(query));
        productTable.setItems(filtered);

    }

    @FXML
    protected void resetSearch() {
        searchField.clear();
        productTable.setItems(productTableRows);
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
            stage.centerOnScreen();
        } catch (IOException e) {
            if (statusLabel != null) {
                statusLabel.setText("Error loading login!");
            }
        }
    }

    private void status(String msg, boolean ok) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
            statusLabel.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: orange;");
        }
    }

    private String safe(TextField tf) {
        return tf != null && tf.getText() != null ? tf.getText().trim() : "";
    }

    private double parseDouble(TextField tf) {
        try {
            return Double.parseDouble(safe(tf));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseInt(TextField tf) {
        try {
            return Integer.parseInt(safe(tf));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double calcRetail(double unitPrice, double taxRate, double discountRate) {
        double tax = unitPrice * (taxRate / 100.0);
        double discount = unitPrice * (discountRate / 100.0);
        return unitPrice + tax - discount;
    }

}
