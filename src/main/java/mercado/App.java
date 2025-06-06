package mercado;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage primaryStage;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        
        // Configuração inicial da cena principal
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        mainScene = new Scene(root);
        mainScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        // Configurações da janela principal
        primaryStage.setTitle("Sakura");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);
        primaryStage.setMinWidth(1366 );
        primaryStage.setMinHeight(768);
        primaryStage.show();
    }

   public static void setRoot(String fxml) {
        try {
            URL fxmlUrl = App.class.getResource("/fxml/" + fxml + ".fxml");
            System.out.println("Carregando FXML: " + fxmlUrl);
            if (fxmlUrl == null) {
                throw new IOException("Arquivo FXML não encontrado: " + fxml);
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            
            mainScene.setRoot(root);
            if (!mainScene.getStylesheets().contains(App.class.getResource("/css/styles.css").toExternalForm())) {
                mainScene.getStylesheets().add(App.class.getResource("/css/styles.css").toExternalForm());
            }
            
        } catch (IOException e) {
            System.err.println("FALHA CRÍTICA ao carregar " + fxml);
            e.printStackTrace();
            
            // Fallback para login
            try {
                Parent root = FXMLLoader.load(App.class.getResource("login"));
                mainScene.setRoot(root);
            } catch (IOException ex) {
                System.err.println("FALHA NO FALLBACK para tela de login");
                ex.printStackTrace();
            }
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}