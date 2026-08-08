package ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ph.edu.dlsu.lbycpob.inventorymanagementsystemlbycpob.MainApplication;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;

    @FXML
    protected void onLoginButtonClick() {
        boolean success = MainApplication.authService.login(username.getText(), password.getText()).isPresent();
        if (!success) {
            new Alert(Alert.AlertType.ERROR, "Invalid username or password").showAndWait();
            return;
        }
        try {
            MainApplication.setRoot("dashboard-view.fxml");
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load dashboard: " + e.getMessage()).showAndWait();
        }
    }
}
