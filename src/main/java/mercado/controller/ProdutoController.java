package mercado.controller;

import backend.dao.ProdutoDAO;
import backend.model.Produto;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import mercado.service.NavigationService;


public class ProdutoController {

    // Controlador para gerenciar produtos no sistema de mercado
    @FXML private TextField nomeField;
    @FXML private TextField precoField;
    @FXML private TextField estoqueField;
    @FXML private TextField categoriaField;
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, String> colunaNome;
    @FXML private TableColumn<Produto, String> colunaCategoria;
    @FXML private TableColumn<Produto, Integer> colunaEstoque;
    @FXML private TableColumn<Produto, Double> colunaPreco;
    @FXML private Label mensagemLabel;
    
    // Instância do DAO para acessar os dados dos produtos
    private ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
    private Produto produtoSelecionado;

    @FXML
    public void initialize() {
        System.out.println("Inicializando ProdutoController");
        // Configuração das colunas da tabela
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colunaEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        
        // Adiciona as colunas na tabela (caso não estejam no FXML)
    if (tabelaProdutos.getColumns().isEmpty()) {
        tabelaProdutos.getColumns().addAll(colunaNome, colunaCategoria, colunaEstoque, colunaPreco);
    }

        carregarProdutos();
        
        // Listener para seleção na tabela
        tabelaProdutos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                produtoSelecionado = newSelection;
            });
    }

    // Método para carregar os produtos na tabela
    private void carregarProdutos() {
        tabelaProdutos.setItems(FXCollections.observableList(produtoDAO.listarTodos()));
    }

    @FXML
    private void salvarProduto(ActionEvent event) { // Método chamado ao clicar no botão de salvar produto
        try {
            if (validarCampos()) {
                Produto produto;
                
                if (produtoSelecionado == null) {
                    produto = new Produto(
                        0, // ID temporário
                        nomeField.getText(),
                        categoriaField.getText(),
                        Integer.parseInt(estoqueField.getText()),
                        Double.parseDouble(precoField.getText())
                    );
                } else {
                    produto = produtoSelecionado;
                    produto.setNome(nomeField.getText());
                    produto.setCategoria(categoriaField.getText());
                    produto.setQuantidade(Integer.parseInt(estoqueField.getText()));
                    produto.setPreco(Double.parseDouble(precoField.getText()));
                }

                produtoDAO.salvar(produto);
                mensagemLabel.setText("Produto salvo com sucesso!");
                carregarProdutos();
                limparCampos();
                produtoSelecionado = null;
            }
        } catch (NumberFormatException e) {
            mensagemLabel.setText("Preço e estoque devem ser números válidos.");
        } catch (Exception e) {
            mensagemLabel.setText("Erro ao salvar produto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para validar os campos de entrada
    private boolean validarCampos() {
        if (nomeField.getText().isEmpty() || categoriaField.getText().isEmpty()) {
            mensagemLabel.setText("Nome e categoria são obrigatórios!");
            return false;
        }
        return true;
    }

    @FXML 
    private void editarProduto() { // Método chamado ao clicar no botão de editar produto
        if (produtoSelecionado != null) {
            nomeField.setText(produtoSelecionado.getNome());
            categoriaField.setText(produtoSelecionado.getCategoria());
            estoqueField.setText(String.valueOf(produtoSelecionado.getQuantidade()));
            precoField.setText(String.valueOf(produtoSelecionado.getPreco()));
        } else {
            mensagemLabel.setText("Selecione um produto para editar.");
        }
    }

    @FXML
    private void excluirProduto() { // Método chamado ao clicar no botão de excluir produto
        if (produtoSelecionado != null) {
            produtoDAO.remover(produtoSelecionado.getId());
            mensagemLabel.setText("Produto excluído com sucesso!");
            carregarProdutos();
            limparCampos();
            produtoSelecionado = null;
        } else {
            mensagemLabel.setText("Selecione um produto para excluir.");
        }
    }

    @FXML
    private void cancelar() { // Método chamado ao clicar no botão de cancelar
        NavigationService.navigateTo("dashboard");
    }

    // Método para limpar os campos de entrada
    private void limparCampos() {
        nomeField.clear();
        categoriaField.clear();
        estoqueField.clear();
        precoField.clear();
    }
}