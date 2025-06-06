package backend.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Historico implements Serializable {
    private static final long serialVersionUID = 1L;

    // Inicializadores de atributos
    private LocalDateTime data;
    private String usuario;
    private int id;
    private String nomeProduto;
    private int quantidade;
    private double precoUnitario;
    
    // Construtor
    public Historico(int id, String nomeProduto, int quantidade, double precoUnitario, LocalDateTime data) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.data = data;
    }

    // Getters e Setters
    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setNomeProduto(String nomeProduto){
        this.nomeProduto = nomeProduto;
    }

    public String getNomeProduto(){
        return nomeProduto;
    }

    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public void setPrecoUnitario(double precoUnitario){
        this.precoUnitario = precoUnitario;
    }

    public double getPrecoUnitario(){
        return precoUnitario;
    }


    @Override
    public String toString() {
        return id + "|" + nomeProduto + "|" + quantidade + "|" + precoUnitario + "|" + data;
    }
}
