package backend.model;

import java.io.Serializable;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contador = 1;
    
    private int id;         
    private String nome;    
    private String categoria;
    private int quantidade;
    private double preco;

    public Produto(int id, String nome, String categoria, int quantidade, double preco) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    // Getters (sem setters para 'id', 'nome' e 'quantidade')
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public int getQuantidade() { return quantidade; }
    public double getPreco() { return preco; }

    // Setters apenas para campos mutáveis
    public void setId(int id) {this.id = id;}
    public void setNome(String nome) {this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setQuantidade(int quantidade){this.quantidade = quantidade; }

    public static void updateContador(int maiorId) {
        if (maiorId >= contador) {
            contador = maiorId + 1;
        }
    }

    public void addQuantidade(int qtd) {
        if (qtd > 0) this.quantidade += qtd;
        else System.out.println("Erro: Quantidade inválida.");
    }

    public boolean removerQuantidade(int qtd) {
        if (qtd > 0 && this.quantidade >= qtd) {
            this.quantidade -= qtd;
            return true;
        }
        System.out.println("Erro: Quantidade insuficiente ou inválida.");
        return false;
    }

    @Override
    public String toString() {
        return String.format(
            "ID: %d | Nome: %-15s | Categoria: %-20s | Preço: R$%.2f | Qtd: %d",
            id, nome, categoria, preco, quantidade
        );
    }
}