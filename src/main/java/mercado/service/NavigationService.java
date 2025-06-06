package mercado.service;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import mercado.App;

public class NavigationService {
    public static final String LOGIN_FXML = "/fxml/login.fxml";
    public static final String DASHBOARD_FXML = "/fxml/dashboard.fxml";
    public static final String PRODUTO_FXML = "/fxml/produto.fxml";
    public static final String VENDA_FXML = "/fxml/venda.fxml";
    public static final String HISTORICO_FXML = "/fxml/historico.fxml";
    
    public static void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Adicione tratamento de erro mais informativo
            showErrorAlert("Erro ao carregar a tela: " + fxmlPath);
        }
    }
    
    private static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Navegação");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Navegação para uma tela nova substituindo a atual
    public static void navigateTo(String fxmlName) {
        App.setRoot(fxmlName);
    }

    public static void openModal(String fxmlPath, String title){
        try {
            Parent root = FXMLLoader.load(App.class.getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao abrir modal: " + fxmlPath);
            e.printStackTrace();
        }
    }

}
