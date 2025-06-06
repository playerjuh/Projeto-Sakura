package backend.model;

import java.io.Serializable;

public class ItemVenda implements Serializable {
    private static final long serialVersionUID = 1L;

    // Atributos
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    // Construtor

    public ItemVenda(Produto produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = produto.getPreco();
    }

    // Métodos

    public double getSubtotal() {
        return quantidade * precoUnitario;
    }

    // Getters

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

}
