package org.oop_project.view.controllers;

import java.util.List;

import org.oop_project.database_handler.enums.UnitType;
import org.oop_project.database_handler.models.Product;
import org.oop_project.database_handler.operations.Operations;
import org.oop_project.database_handler.operations.ProductOperations;
import org.oop_project.view.helpers.ProductRow;

import static org.oop_project.utils.Generate.generateProductId;
import static org.oop_project.view.helpers.Navigators.navigateToLoginPanel;
import static org.oop_project.view.helpers.Validator.safeText;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

        if (productList != null && !productList.isEmpty())
            productTableRows.addAll(productList.stream().map(Product::mapProductRow).toList());

        productTable.setItems(productTableRows);

        searchField.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            String query = newValue.toLowerCase();
            ObservableList<ProductRow> filtered = productTableRows
                    .filtered(row -> {

                        boolean queryContainsName = row.getName().toLowerCase().contains(query);
                        boolean queryContainsId = row.getId().toLowerCase().contains(query);
                        boolean queryContainsFamily = row.getFamily().toLowerCase().contains(query);
                        boolean queryContainsSubFamily = row.getSubFamily().toLowerCase().contains(query);

                        return queryContainsName || queryContainsId || queryContainsFamily || queryContainsSubFamily;

                    });

            productTable.setItems(filtered);
        }
    });

    }

    @FXML
    protected void addProduct() {

        String name = safeText(nameField);
        String desc = safeText(descriptionField);
        String type = unitTypeCombo.getValue() != null ? unitTypeCombo.getValue() : "";
        String family = safeText(familyField);
        String subFamily = safeText(subFamilyField);
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
                id, name, desc, UnitType.valueOf(type), family, subFamily, unitPrice, tax, discount, qty);

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

        sel.setName(safeText(nameField));
        sel.setDescription(safeText(descriptionField));
        sel.setType(unitTypeCombo.getValue() != null ? unitTypeCombo.getValue() : "");
        sel.setFamily(safeText(familyField));
        sel.setSubFamily(safeText(subFamilyField));
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
    protected void resetSearch() {
        searchField.clear();
        productTable.setItems(productTableRows);
    }

    @FXML
    protected void backToLogin() {
        navigateToLoginPanel((Stage) productTable.getScene().getWindow(), statusLabel);
    }

    private void status(String msg, boolean ok) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
            statusLabel.setStyle(ok ? "-fx-text-fill: green;" : "-fx-text-fill: orange;");
        }
    }

    private double parseDouble(TextField tf) {
        try {
            return Double.parseDouble(safeText(tf));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int parseInt(TextField tf) {
        try {
            return Integer.parseInt(safeText(tf));
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
