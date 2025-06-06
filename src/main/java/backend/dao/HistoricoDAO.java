package backend.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import backend.model.Historico;

public class HistoricoDAO {
    // Nome do arquivo onde os históricos serão persistidos
    private static final String ARQUIVO_HISTORICO = "historico.dat";
    
    // Método principal para registrar um novo evento (recomendado para uso)
    // @param historico O objeto Historico contendo os dados do evento
    public void registrarEvento(Historico historico) {
        List<Historico> historicos = carregarHistorico();
        historicos.add(historico);
        salvarHistorico(historicos);
    }
    
    // Método alternativo mantido para compatibilidade (pode ser depreciado no futuro)
    // @param historico O objeto Historico contendo os dados do evento
    public void salvarRegistro(Historico historico) {
        registrarEvento(historico); // Reutiliza a lógica do novo método
    }
    
    // Salva a lista completa de históricos no arquivo
    // @param historicos Lista de objetos Historico a serem salvos
    private void salvarHistorico(List<Historico> historicos) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_HISTORICO))) {
            oos.writeObject(historicos);
        } catch (IOException e) {
            // Log de erro simplificado - considere usar um framework de logging
            System.err.println("Erro ao salvar histórico: " + e.getMessage());
        }
    }
    
    // Carrega a lista completa de históricos do arquivo
    // @return Lista de objetos Historico recuperados do arquivo
    public List<Historico> carregarHistorico() {
        File arquivo = new File(ARQUIVO_HISTORICO);
        
        // Se o arquivo não existe, retorna uma lista vazia
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARQUIVO_HISTORICO))) {
            // Faz o cast seguro da lista serializada
            @SuppressWarnings("unchecked")
            List<Historico> historicos = (List<Historico>) ois.readObject();
            return historicos;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar histórico: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // Retorna todo o histórico de ações ordenado por data (do mais recente)
    // @return Lista completa de registros históricos
    public List<Historico> obterHistoricoCompleto() {
        List<Historico> historicos = carregarHistorico();
        // Ordena por data (do mais recente para o mais antigo)
        historicos.sort((h1, h2) -> h2.getData().compareTo(h1.getData()));
        return historicos;
    }
    
    // Filtra histórico por usuário específico
    // @param usuario Nome do usuário para filtrar
    // @return Lista filtrada de registros históricos
    public List<Historico> filtrarPorUsuario(String usuario) {
        List<Historico> historicos = carregarHistorico();
        List<Historico> filtrado = new ArrayList<>();
        
        for (Historico h : historicos) {
            if (h.getUsuario().equalsIgnoreCase(usuario)) {
                filtrado.add(h);
            }
        }
        
        // Ordena por data (do mais recente)
        filtrado.sort((h1, h2) -> h2.getData().compareTo(h1.getData()));
        return filtrado;
    }

    public int getProximoId() {
        List <Historico> historicos = obterHistoricoCompleto();
        if (historicos.isEmpty()) {
            return 1;
        }
        return historicos.stream().mapToInt(Historico::getId).max().orElse(0) +1;
    }
    
    // Limpa todo o histórico (útil para testes)
    public void limparHistorico() {
        salvarHistorico(new ArrayList<>());
    }
}