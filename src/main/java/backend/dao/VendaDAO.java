package backend.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import backend.model.Venda;

public class VendaDAO {
    private static final String ARQUIVO = "vendas.dat";
    private static List<Venda> vendasCache = null;
    
    // Padrão Singleton
    private static VendaDAO instance;
    
    private VendaDAO() {}
    
    public static synchronized VendaDAO getInstance() {
        if (instance == null) {
            instance = new VendaDAO();
        }
        return instance;
    }
    
    public void salvar(Venda venda) {
        List<Venda> vendas = carregarTodas();
        vendas.add(venda);
        salvarNoArquivo(vendas);
    }
    
    public List<Venda> carregarTodas() {
        if (vendasCache != null) {
            return new ArrayList<>(vendasCache);
        }
        
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            vendasCache = (List<Venda>) ois.readObject();
            return new ArrayList<>(vendasCache);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar vendas: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    private synchronized void salvarNoArquivo(List<Venda> vendas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            oos.writeObject(vendas);
            vendasCache = new ArrayList<>(vendas); // Atualiza cache
        } catch (IOException e) {
            System.err.println("Erro ao salvar vendas: " + e.getMessage());
        }
    }
    
    // Método adicional para obter próxima ID (se necessário)
    public int getProximoId() {
        List<Venda> vendas = carregarTodas();
        if (vendas.isEmpty()) {
            return 1; // Primeira venda
        }
        return vendas.size() + 1; // Próximo ID
    }
}