package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.MainApplication;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Label summaryLabel;
    @FXML
    private Button refreshButton;
    @FXML
    private Button manageProductsButton;
    @FXML
    private Button manageSuppliersButton;

    @FXML
    public void initialize() {
        onRefreshButtonClick();
    }

    @FXML
    protected void onRefreshButtonClick() {
        long lowStockCount = MainApplication.productService.getLowStockCount();
        double totalValue = MainApplication.productService.getTotalInventoryValue();
        // ponytail: recent stock movements needs StockMovementService, a later feature
        summaryLabel.setText(String.format("Low stock items: %d | Total inventory value: %.2f", lowStockCount, totalValue));
    }

    @FXML
    protected void onManageProductsClick() {
        navigateTo("product-view.fxml");
    }

    @FXML
    protected void onManageSuppliersClick() {
        navigateTo("supplier-view.fxml");
    }

    private void navigateTo(String fxml) {
        try {
            MainApplication.setRoot(fxml);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load screen: " + e.getMessage()).showAndWait();
        }
    }
}
