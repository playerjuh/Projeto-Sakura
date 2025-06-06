package mercado.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import mercado.App;

public class LoginController {
    
    // Campos de entrada e rótulos
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() { // Método chamado ao clicar no botão de login
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validar se os campos estão preenchidos
        if (username.isBlank() || password.isBlank()) {
            showError("Usuário e senha não podem estar vazios.");
            return;
        }

        if (username.equals("admin") && password.equals("admin")) {
            App.setRoot("dashboard"); // Troque para usar o método centralizado
        } else {
            errorLabel.setText("Credenciais inválidas");
            errorLabel.setVisible(true);
        }

        }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

}
