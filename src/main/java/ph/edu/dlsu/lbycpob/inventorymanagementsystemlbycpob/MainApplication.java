package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryProductRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemorySupplierRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.repository.InMemoryUserRepository;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.AuthService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.ProductService;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.service.SupplierService;

import java.io.IOException;

public class MainApplication extends Application {
    public static AuthService authService;
    public static ProductService productService;
    public static SupplierService supplierService;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        authService = new AuthService(new InMemoryUserRepository());
        productService = new ProductService(new InMemoryProductRepository());
        supplierService = new SupplierService(new InMemorySupplierRepository());

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
