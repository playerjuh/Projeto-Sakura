package backend.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import backend.model.Produto;

public class ProdutoDAO {
    private static final String ARQUIVO_PRODUTOS = "produtos.dat";
    private static ProdutoDAO instance;
    // Padrão Singleton
    private int proximoId = 1;

    private ProdutoDAO() {
        // Construtor privado para Singleton
    }
    public static synchronized ProdutoDAO getInstance() {
        if (instance == null) {
            instance = new ProdutoDAO();
        }
        return instance; 
    }

    //Salva um novo produto ou atualiza existente
    public void salvar(Produto produto) {
        List<Produto> produtos = listarTodos();

        if (produto.getId() == 0) {
            // Novo produto, atribui ID e adiciona à lista
            produto.setId(proximoId++);
            produtos.add(produto);
        } else {
            // Atualiza produto existente
            produtos.removeIf(p -> p.getId() == produto.getId());
            produtos.add(produto);
        }
        salvarLista(produtos);
    }

    // Remove produto por ID
    public void remover(int id) {
        List<Produto> produtos = listarTodos();
        produtos.removeIf(p -> p.getId() == id);
        salvarLista(produtos);
    }

    // Busca produto por ID
    public Produto buscarPorId(int id) {
        List<Produto> produtos = listarTodos();
        return produtos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado com ID: " + id));
    }

    public Produto buscarPorNome(String nome) {
        List<Produto> produtos = listarTodos();
        return produtos.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null); // Retorna null se não encontrar
    }

    // Lista todos os produtos
    public List<Produto> listarTodos() {
        File arquivo = new File(ARQUIVO_PRODUTOS);
        
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_PRODUTOS))) {
            @SuppressWarnings("unchecked")
            List<Produto> produtos = (List<Produto>) ois.readObject();
            return new ArrayList<>(produtos);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar produtos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Métodos auxiliares

    // Salva a lista de produtos no arquivo
    private void salvarLista(List<Produto> produtos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_PRODUTOS))) {
            oos.writeObject(produtos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
    // Carrega o próximo ID a ser atribuído
    private void carregarProximoId() {
        List<Produto> produtos = listarTodos();
        this.proximoId = produtos.stream()
                .mapToInt(Produto::getId)
                .max()
                .orElse(0) + 1; // Se não houver produtos, começa do 1
    }

}
