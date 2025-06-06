package mercado.controller;

import java.time.LocalDateTime;

import backend.dao.HistoricoDAO;
import backend.dao.ProdutoDAO;
import backend.dao.VendaDAO;
import backend.model.Historico;
import backend.model.ItemVenda;
import backend.model.Produto;
import backend.model.Venda;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mercado.service.NavigationService;

public class VendaController {
    // Controlador para gerenciar vendas no sistema de mercado
    @FXML private ComboBox<Produto> produtoCombo;
    @FXML private TextField quantidadeField;
    @FXML private TableView<ItemVenda> itensTable;
    @FXML private Label totalLabel;
    @FXML private Label mensagemLabel;
    @FXML private TableColumn<ItemVenda, String> colunaProduto;
    @FXML private TableColumn<ItemVenda, Integer> colunaQuantidade;
    @FXML private TableColumn<ItemVenda, Double> colunaPreco;
    @FXML private TableColumn<ItemVenda, Double> colunaSubtotal;


    // Variáveis para armazenar a venda atual e os DAOs
    private Venda vendaAtual;
    private ProdutoDAO produtoDAO;
    private VendaDAO vendaDAO;
    
    @FXML
    public void initialize() {
        produtoDAO = ProdutoDAO.getInstance(); // Usando Singleton
        vendaDAO = VendaDAO.getInstance();
        
        vendaAtual = new Venda(); 
        vendaAtual.setUsuario("Operador"); // Define o usuário

        colunaProduto.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getProduto().getNome()));
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colunaSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        
        produtoCombo.setItems(FXCollections.observableList(produtoDAO.listarTodos()));

        // Configura a tabela de itens
        itensTable.setItems(FXCollections.observableList(vendaAtual.getItens()));
        
        // Atualiza o total quando a tabela muda
        itensTable.getItems().addListener((ListChangeListener<ItemVenda>) change -> {
            atualizarTabela();
        });
    }
    
    @FXML
    private void adicionarItem() { // Método chamado ao clicar no botão de adicionar item
        try {
            Produto produto = produtoCombo.getSelectionModel().getSelectedItem();
            if (produto == null) {
                mensagemLabel.setText("Selecione um produto!");
                return;
            }
            
            int quantidade = Integer.parseInt(quantidadeField.getText());
            if (quantidade <= 0) {
                mensagemLabel.setText("Quantidade deve ser positiva!");
                return;
            }
            
            ItemVenda item = new ItemVenda(produto, quantidade, produto.getPreco());
            vendaAtual.adicionarItem(item); // Método agora existe
            
            atualizarTabela();
            quantidadeField.clear();
            mensagemLabel.setText("");
        } catch (NumberFormatException e) {
            mensagemLabel.setText("Quantidade deve ser um número válido!");
        }
    }
    
    @FXML
    private void finalizarVenda() { // Método chamado ao clicar no botão de finalizar venda
        if (vendaAtual.getItens().isEmpty()) {
            mensagemLabel.setText("Adicione itens à venda!");
            return;
        }

        try {
            // Atualiza o estoque de cada produto vendido
            for (ItemVenda item : vendaAtual.getItens()) {
                Produto produto = item.getProduto();
                int novaQuantidade = produto.getQuantidade() - item.getQuantidade();
                if (novaQuantidade < 0) {
                    mensagemLabel.setText("Estoque insuficiente para o produto: " + produto.getNome());
                    return;
                }
                produto.setQuantidade(novaQuantidade);
                produtoDAO.salvar(produto);
            }

            vendaDAO.salvar(vendaAtual);

            for (ItemVenda item : vendaAtual.getItens()){
                registrarHistoricoVenda(item);
            }

            mensagemLabel.setText("Venda registrada com sucesso! Nº " + vendaAtual.getId());
            produtoCombo.setItems(FXCollections.observableList(produtoDAO.listarTodos()));

            // Prepara para nova venda
            vendaAtual = new Venda();
            itensTable.getItems().clear();
            totalLabel.setText("R$ 0.00");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao salvar venda: " + e.getMessage());
        }
    }

    @FXML
    private void removerItem() { // Método chamado ao clicar no botão de remover item
        ItemVenda item = itensTable.getSelectionModel().getSelectedItem();
        if (item != null) {
            vendaAtual.getItens().remove(item);
            atualizarTabela();
        }
    }
    
    @FXML
    private void cancelar() { // Método chamado ao clicar no botão de cancelar
        NavigationService.navigateTo("dashboard");
    }

    @FXML
    private void handleFinalizarVenda() { // Método chamado ao clicar no botão de finalizar venda
        if (vendaAtual.getItens().isEmpty()) {
            showAlert("Adicione itens antes de finalizar a venda.");
            return;
        }

        vendaDAO.salvar(vendaAtual);
        for (ItemVenda item : vendaAtual.getItens()) {
            registrarHistoricoVenda(item);
        }
        limparVenda();
        showAlert("Venda finalizada com sucesso! ID: " + vendaAtual.getId());
    } 

    private void registrarHistoricoVenda(ItemVenda item){
        HistoricoDAO historicoDAO = new HistoricoDAO();
        Historico historico = new Historico(historicoDAO.getProximoId(), item.getProduto().getNome(), item.getQuantidade(), item.getProduto().getPreco(), LocalDateTime.now());
        historicoDAO.salvarRegistro(historico);
    }

    // Método para limpar a venda atual e a tabela de itens
    private void limparVenda() {
        vendaAtual = new Venda();
        itensTable.getItems().clear();
        atualizarTabela();
    }
    
    // Método para atualizar a tabela de itens e o total da venda
    private void atualizarTabela() {
        itensTable.setItems(FXCollections.observableList(vendaAtual.getItens()));
        totalLabel.setText(String.format("R$ %.2f", vendaAtual.getTotal()));
    }

    // Método para exibir mensagens de alerta na interface
    private void showAlert(String message) {
        mensagemLabel.setText(message);
    }
}