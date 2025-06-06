package backend.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Venda implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int proximoId = 1; // ID inicial para vendas
    
    private final int id; // ID único da venda
    private List<ItemVenda> itens;
    private String usuario;
    private LocalDateTime data;
    private double total;

    // Construtor vazio para inicialização
    public Venda() {
        this.id = proximoId++;
        this.itens = new ArrayList<>();
        this.usuario = "Sistema"; // Usuário padrão
        this.data = LocalDateTime.now();
        this.total = 0.0;
    }

    public Venda(List<ItemVenda> itens, String usuario, LocalDateTime data) {
        this();
        this.itens = new ArrayList<>(Objects.requireNonNull(itens));
        this.usuario = Objects.requireNonNull(usuario);
        this.data = Objects.requireNonNull(data);
        this.total = calcularTotal();
    }

    public Venda(List<ItemVenda> itens, String usuario) {
        this(itens, usuario, LocalDateTime.now());
    }


    // Método para adicionar itens dinamicamente
    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        this.total = calcularTotal(); // Recalcula o total
    }

    private double calcularTotal() {
        return itens.stream()
                .mapToDouble(ItemVenda::getSubtotal)
                .sum();
    }

    public void exibirDetalhes() {
        System.out.println("Detalhes da Venda:");
        System.out.println("===================================");
        System.out.println("Venda realizada por: " + usuario);
        System.out.println("Data da venda: " + data);
        System.out.println("Itens vendidos:");
        for (ItemVenda item : itens) {
            System.out.println("- " + item.getProduto().getNome() + 
                ": " + item.getQuantidade() + 
                " x R$" + item.getPrecoUnitario() +
                " = R$" + item.getSubtotal());
        }
        System.out.println("Total: R$" + total);
        System.out.println("===================================");
    }

    // Getters
    public List<ItemVenda> getItens() {
        return new ArrayList<>(itens); // Retorna cópia defensiva
    }

    public String getUsuario() {
        return usuario;
    }

    public LocalDateTime getData() {
        return data;
    }

    public double getTotal() {
        return total;
    }

    public int getId() {
        return this.id;
    }

    // Setters adicionados
    public void setUsuario(String usuario) {
        this.usuario = Objects.requireNonNull(usuario);
    }

    public void setData(LocalDateTime data) {
        this.data = Objects.requireNonNull(data);
    }

    // Métodos adicionais para melhor funcionamento
    @Override
    public String toString() {
        return "Venda{" +
                "usuario='" + usuario + '\'' +
                ", data=" + data +
                ", total=" + total +
                ", itens=" + itens.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return Double.compare(venda.total, total) == 0 &&
                Objects.equals(itens, venda.itens) &&
                Objects.equals(usuario, venda.usuario) &&
                Objects.equals(data, venda.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itens, usuario, data, total);
    }
}