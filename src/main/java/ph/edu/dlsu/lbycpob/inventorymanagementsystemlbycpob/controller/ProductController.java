package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.MainApplication;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;

import java.io.IOException;

public class ProductController {
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> skuColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, Integer> reorderThresholdColumn;

    @FXML
    private TextField skuField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField reorderThresholdField;

    @FXML
    private Button addProductButton;
    @FXML
    private Button updateProductButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        reorderThresholdColumn.setCellValueFactory(new PropertyValueFactory<>("reorderThreshold"));

        productTable.setItems(FXCollections.observableArrayList(MainApplication.productService.getAll()));
        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selected) -> {
            if (selected != null) {
                skuField.setText(selected.getSku());
                nameField.setText(selected.getName());
                categoryField.setText(selected.getCategory());
                priceField.setText(String.valueOf(selected.getPrice()));
                quantityField.setText(String.valueOf(selected.getQuantity()));
                reorderThresholdField.setText(String.valueOf(selected.getReorderThreshold()));
            }
        });
    }

    @FXML
    protected void onAddProductButtonClick() {
        try {
            Product product = MainApplication.productService.addProduct(
                    skuField.getText(),
                    nameField.getText(),
                    categoryField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(quantityField.getText()),
                    Integer.parseInt(reorderThresholdField.getText())
            );
            productTable.getItems().add(product);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Price, quantity, and reorder threshold must be numbers").showAndWait();
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void onUpdateProductButtonClick() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a product to update").showAndWait();
            return;
        }
        try {
            selected.setSku(skuField.getText());
            selected.setName(nameField.getText());
            selected.setCategory(categoryField.getText());
            selected.setPrice(Double.parseDouble(priceField.getText()));
            selected.setQuantity(Integer.parseInt(quantityField.getText()));
            selected.setReorderThreshold(Integer.parseInt(reorderThresholdField.getText()));
            productTable.refresh();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Price, quantity, and reorder threshold must be numbers").showAndWait();
        }
    }

    @FXML
    protected void onBackButtonClick() {
        try {
            MainApplication.setRoot("dashboard-view.fxml");
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load dashboard: " + e.getMessage()).showAndWait();
        }
    }
}
