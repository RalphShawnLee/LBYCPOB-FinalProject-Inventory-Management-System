package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.MainApplication;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.model.StockMovement;

import java.io.IOException;
import java.util.Comparator;

public class StockMovementController {
    @FXML
    private TableView<StockMovement> stockMovementTable;
    @FXML
    private TableColumn<StockMovement, String> productColumn;
    @FXML
    private TableColumn<StockMovement, String> typeColumn;
    @FXML
    private TableColumn<StockMovement, Integer> quantityColumn;
    @FXML
    private TableColumn<StockMovement, String> referenceColumn;
    @FXML
    private TableColumn<StockMovement, String> timestampColumn;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        referenceColumn.setCellValueFactory(new PropertyValueFactory<>("reference"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        var movements = MainApplication.stockMovementService.getAll().stream()
                .sorted(Comparator.comparing(StockMovement::getTimestamp).reversed())
                .toList();
        stockMovementTable.setItems(FXCollections.observableArrayList(movements));
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
