package mercado.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import mercado.App;

public class DashboardController {

    @FXML
    private Button btnProdutos; // Botão para produtos

    @FXML
    private Button btnVendas; // Botão para vendas

    @FXML
    private Button btnRelatorio; // Botão para relatorio

    @FXML
    private void handleLogout() {
        App.setRoot("login");
    }

    @FXML
    private void handleProdutos() {
        // Verifique se o caminho está exatamente como sua estrutura de pastas
        App.setRoot("produtos"); // ou "/views/produtos" dependendo da sua estrutura
    }

    @FXML
    private void handleVendas() {
        App.setRoot("vendas");
    }

    @FXML
    private void handleRelatorio() {
        App.setRoot("relatorio");
    }

}