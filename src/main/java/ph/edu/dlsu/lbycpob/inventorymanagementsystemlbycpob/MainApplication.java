package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.JsonProductRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.JsonPurchaseOrderRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.JsonSalesOrderRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.JsonStockMovementRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.JsonSupplierRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.JsonUserRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.ProductRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.SupplierRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.AuthService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.NotificationService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.ProductService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.PurchaseOrderService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.ReportService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.SalesOrderService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.StockMovementService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.SupplierService;

import java.io.IOException;

public class MainApplication extends Application {
    public static AuthService authService;
    public static ProductService productService;
    public static SupplierService supplierService;
    public static StockMovementService stockMovementService;
    public static PurchaseOrderService purchaseOrderService;
    public static SalesOrderService salesOrderService;
    public static ReportService reportService;
    public static NotificationService notificationService;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        ProductRepository productRepository = new JsonProductRepository("data/products.json");
        SupplierRepository supplierRepository = new JsonSupplierRepository("data/suppliers.json");

        authService = new AuthService(new JsonUserRepository("data/users.json"));
        productService = new ProductService(productRepository);
        supplierService = new SupplierService(supplierRepository);
        stockMovementService = new StockMovementService(new JsonStockMovementRepository("data/stock-movements.json", productRepository));
        purchaseOrderService = new PurchaseOrderService(
                new JsonPurchaseOrderRepository("data/purchase-orders.json", productRepository, supplierRepository),
                stockMovementService);
        salesOrderService = new SalesOrderService(
                new JsonSalesOrderRepository("data/sales-orders.json", productRepository),
                stockMovementService);
        reportService = new ReportService(productService, stockMovementService);
        notificationService = new NotificationService();

        stage.setTitle("Inventory Management System");
        setRoot("login-view.fxml");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 480, 360);
        primaryStage.setScene(scene);
    }
}
