package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.MainApplication;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.ReportService.ProductMovementSummary;

import java.io.IOException;

public class ReportController {
    @FXML
    private Label summaryLabel;
    @FXML
    private TableView<ProductMovementSummary> movementSummaryTable;
    @FXML
    private TableColumn<ProductMovementSummary, String> productColumn;
    @FXML
    private TableColumn<ProductMovementSummary, Integer> totalOutQuantityColumn;
    @FXML
    private Button generateReportButton;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        totalOutQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalOutQuantity"));
        onGenerateReportButtonClick();
    }

    @FXML
    protected void onGenerateReportButtonClick() {
        movementSummaryTable.setItems(FXCollections.observableArrayList(MainApplication.reportService.getMovementSummary()));
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
