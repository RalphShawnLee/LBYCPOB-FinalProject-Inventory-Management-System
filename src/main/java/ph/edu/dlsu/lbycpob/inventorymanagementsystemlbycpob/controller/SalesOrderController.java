package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.MainApplication;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Product;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.SalesOrderLine;

import java.io.IOException;
import java.util.ArrayList;

public class SalesOrderController {
    @FXML
    private TableView<SalesOrder> salesOrderTable;
    @FXML
    private TableColumn<SalesOrder, Integer> idColumn;
    @FXML
    private TableColumn<SalesOrder, String> statusColumn;
    @FXML
    private TableColumn<SalesOrder, String> createdAtColumn;
    @FXML
    private Button fulfillOrderButton;

    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private Button addLineButton;

    @FXML
    private TableView<SalesOrderLine> pendingLinesTable;
    @FXML
    private TableColumn<SalesOrderLine, Product> lineProductColumn;
    @FXML
    private TableColumn<SalesOrderLine, Integer> lineQuantityColumn;

    @FXML
    private Button createOrderButton;
    @FXML
    private Button backButton;

    private final ObservableList<SalesOrderLine> pendingLines = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        salesOrderTable.setItems(FXCollections.observableArrayList(MainApplication.salesOrderService.getAll()));

        lineProductColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        lineQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        pendingLinesTable.setItems(pendingLines);

        productComboBox.setItems(FXCollections.observableArrayList(MainApplication.productService.getAll()));
    }

    @FXML
    protected void onAddLineButtonClick() {
        Product product = productComboBox.getValue();
        if (product == null) {
            new Alert(Alert.AlertType.WARNING, "Select a product").showAndWait();
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            pendingLines.add(new SalesOrderLine(product, quantity));
            quantityField.clear();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Quantity must be a number").showAndWait();
        }
    }

    @FXML
    protected void onCreateOrderButtonClick() {
        try {
            SalesOrder order = MainApplication.salesOrderService.createOrder(new ArrayList<>(pendingLines));
            salesOrderTable.getItems().add(order);
            pendingLines.clear();
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void onFulfillOrderButtonClick() {
        SalesOrder selected = salesOrderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select an order to fulfill").showAndWait();
            return;
        }
        try {
            MainApplication.salesOrderService.fulfillOrder(selected);
            salesOrderTable.refresh();
            for (SalesOrderLine line : selected.getLines()) {
                if (MainApplication.notificationService.isLowStock(line.getProduct())) {
                    new Alert(Alert.AlertType.WARNING, line.getProduct().getName() + " is now low on stock").showAndWait();
                }
            }
        } catch (IllegalStateException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
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
