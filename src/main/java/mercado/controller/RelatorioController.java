package mercado.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import backend.dao.HistoricoDAO;
import backend.model.Historico;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import mercado.service.NavigationService;



public class RelatorioController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        historicoDAO = new HistoricoDAO();
        configurarTabela();
        carregarHistoricoNaTabela(); 
    }

    @FXML
    private TableView<Historico> tabelaHistorico;

    @FXML
    private TableColumn<Historico, Integer> colunaId;

    @FXML
    private TableColumn<Historico, String> colunaProduto;

    @FXML
    private TableColumn<Historico, Integer> colunaQuantidade;

    @FXML
    private TableColumn<Historico, Double> colunaPreco;

    @FXML
    private TableColumn<Historico, LocalDateTime> colunaData;

    private HistoricoDAO historicoDAO;
    private ObservableList<Historico> historicos;


        private void configurarTabela() {
            colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colunaProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
            colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
            colunaPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
            colunaData.setCellValueFactory(new PropertyValueFactory<>("data"));
            
            // Formatar preço para exibir como moeda
            colunaPreco.setCellFactory(column -> new TableCell<Historico, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("R$ %.2f", item));
                    }
                }
            });
            
            // Formatação da data
            colunaData.setCellFactory(column -> {
                return new TableCell<Historico, LocalDateTime>() {
                    @Override
                    protected void updateItem(LocalDateTime item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        }
                    }
                };
            });
        }

        @FXML
        private void retornar() { // Método chamado ao clicar no botão de cancelar
            NavigationService.navigateTo("dashboard");
        }

        @FXML
        private void atualizarHistorico() {
            carregarHistoricoNaTabela();
        }

        private void carregarHistoricoNaTabela() {
            historicos = javafx.collections.FXCollections.observableArrayList(historicoDAO.carregarHistorico());
            tabelaHistorico.setItems(historicos);
        }

}
