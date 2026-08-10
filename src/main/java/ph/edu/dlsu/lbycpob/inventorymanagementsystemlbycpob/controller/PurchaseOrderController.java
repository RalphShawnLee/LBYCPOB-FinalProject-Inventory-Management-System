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
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrder;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.PurchaseOrderLine;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;

import java.io.IOException;
import java.util.ArrayList;

public class PurchaseOrderController {
    @FXML
    private TableView<PurchaseOrder> purchaseOrderTable;
    @FXML
    private TableColumn<PurchaseOrder, Integer> idColumn;
    @FXML
    private TableColumn<PurchaseOrder, Supplier> supplierColumn;
    @FXML
    private TableColumn<PurchaseOrder, String> statusColumn;
    @FXML
    private TableColumn<PurchaseOrder, String> createdAtColumn;
    @FXML
    private Button receiveOrderButton;

    @FXML
    private ComboBox<Supplier> supplierComboBox;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private Button addLineButton;

    @FXML
    private TableView<PurchaseOrderLine> pendingLinesTable;
    @FXML
    private TableColumn<PurchaseOrderLine, Product> lineProductColumn;
    @FXML
    private TableColumn<PurchaseOrderLine, Integer> lineQuantityColumn;

    @FXML
    private Button createOrderButton;
    @FXML
    private Button backButton;

    private final ObservableList<PurchaseOrderLine> pendingLines = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        purchaseOrderTable.setItems(FXCollections.observableArrayList(MainApplication.purchaseOrderService.getAll()));

        lineProductColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        lineQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        pendingLinesTable.setItems(pendingLines);

        supplierComboBox.setItems(FXCollections.observableArrayList(MainApplication.supplierService.getAll()));
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
            pendingLines.add(new PurchaseOrderLine(product, quantity));
            quantityField.clear();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Quantity must be a number").showAndWait();
        }
    }

    @FXML
    protected void onCreateOrderButtonClick() {
        Supplier supplier = supplierComboBox.getValue();
        if (supplier == null) {
            new Alert(Alert.AlertType.WARNING, "Select a supplier").showAndWait();
            return;
        }
        try {
            PurchaseOrder order = MainApplication.purchaseOrderService.createOrder(supplier, new ArrayList<>(pendingLines));
            purchaseOrderTable.getItems().add(order);
            pendingLines.clear();
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void onReceiveOrderButtonClick() {
        PurchaseOrder selected = purchaseOrderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select an order to receive").showAndWait();
            return;
        }
        try {
            MainApplication.purchaseOrderService.receiveOrder(selected);
            purchaseOrderTable.refresh();
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
