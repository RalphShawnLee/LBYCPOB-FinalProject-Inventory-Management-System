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
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.Supplier;

import java.io.IOException;

public class SupplierController {
    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, String> nameColumn;
    @FXML
    private TableColumn<Supplier, String> emailColumn;
    @FXML
    private TableColumn<Supplier, String> phoneColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;

    @FXML
    private Button addSupplierButton;
    @FXML
    private Button updateSupplierButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        supplierTable.setItems(FXCollections.observableArrayList(MainApplication.supplierService.getAll()));
        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selected) -> {
            if (selected != null) {
                nameField.setText(selected.getName());
                emailField.setText(selected.getEmail());
                phoneField.setText(selected.getPhone());
            }
        });
    }

    @FXML
    protected void onAddSupplierButtonClick() {
        try {
            Supplier supplier = MainApplication.supplierService.addSupplier(
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText()
            );
            supplierTable.getItems().add(supplier);
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void onUpdateSupplierButtonClick() {
        Supplier selected = supplierTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select a supplier to update").showAndWait();
            return;
        }
        try {
            MainApplication.supplierService.updateSupplier(
                    selected,
                    nameField.getText(),
                    emailField.getText(),
                    phoneField.getText()
            );
            supplierTable.refresh();
        } catch (IllegalArgumentException e) {
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
