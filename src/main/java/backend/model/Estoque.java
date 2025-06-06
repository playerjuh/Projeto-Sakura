package backend.model;

import java.time.LocalDateTime;
import java.util.List;

import backend.dao.HistoricoDAO;
import backend.dao.ProdutoDAO;

public class Estoque {
    // Atributos
    private final ProdutoDAO produtoDAO;
    private final LoginManager loginManager;
    private final HistoricoDAO historicoDAO;

    // Construtor
    public Estoque(LoginManager loginManager) {
        this.produtoDAO = ProdutoDAO.getInstance();
        this.loginManager = loginManager;
        this.historicoDAO = new HistoricoDAO();
    }

    // Métodos Principais Públicos
    public void addProduto(Produto novoProduto, Usuario usuario) {
        // Verifica permissões
        loginManager.validarPermissao(usuario, null);
        loginManager.validarLimiteFuncionario(usuario, novoProduto.getQuantidade(), 100);

        // Validações
        if (novoProduto.getQuantidade() <= 0 || novoProduto.getPreco() <= 0) {
            throw new IllegalArgumentException("Erro: Quantidade e preço devem ser maiores que 0.");
        }

        // Verifica se o produto já existe
        Produto existente = produtoDAO.buscarPorNome(novoProduto.getNome());
        if (existente != null) {
            existente.addQuantidade(novoProduto.getQuantidade());
            produtoDAO.salvar(existente); // Usa a instância produtoDAO
        } else {
            produtoDAO.salvar(novoProduto); // Usa a instância produtoDAO
        }

        registrarHistorico(usuario, "Adição de Produto", 
            "Nome: " + novoProduto.getNome() + ", Quantidade: " + novoProduto.getQuantidade() + 
            ", Preço: " + novoProduto.getPreco() + ", Categoria: " + novoProduto.getCategoria());
    }

    public void registrarSaida(Produto produto, int quantidade, Usuario usuario) {
        loginManager.validarPermissao(usuario, null);
        loginManager.validarLimiteFuncionario(usuario, 0, 0);

        if (quantidade <= 0) {
            throw new IllegalStateException("Erro: Quantidade deve ser maior que 0.");
        }

        Produto existente = produtoDAO.buscarPorNome(produto.getNome());
        if (existente == null || existente.getQuantidade() < quantidade) {
            throw new IllegalStateException("Produto não encontrado ou estoque insuficiente");
        }

        existente.removerQuantidade(quantidade);
        produtoDAO.salvar(existente);
        registrarHistorico(usuario, "Saída de Produto", 
            "Nome: " + produto.getNome() + ", Quantidade: " + quantidade);
    }

    public void editarProduto(int id, String novaCategoria, double novoPreco, Usuario usuario) {
        loginManager.validarPermissao(usuario, null);

        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            throw new IllegalStateException("Produto não encontrado");
        }
        
        produto.setCategoria(novaCategoria);
        produto.setPreco(novoPreco);
        produtoDAO.salvar(produto);
        
        registrarHistorico(usuario, "Edição de Produto", 
            "ID: " + id + ", Nova Categoria: " + novaCategoria + ", Novo Preço: " + novoPreco);
    }

    public void removerProduto(int id, Usuario usuario) {
        loginManager.validarPermissao(usuario, Usuario.Perfil.ADMIN);
        loginManager.validarLimiteFuncionario(usuario, 0, 0);

        produtoDAO.remover(id);
        registrarHistorico(usuario, "Remoção de Produto", "ID: " + id);
    }

    // Métodos auxiliares
    public List<Produto> listarTodos() {
        return produtoDAO.listarTodos(); // Usa a instância produtoDAO
    }

    public Produto buscarPorId(int id) {
        return produtoDAO.buscarPorId(id); // Usa a instância produtoDAO
    }

    public Produto buscarPorNome(String nome) {
        return produtoDAO.buscarPorNome(nome); // Usa a instância produtoDAO
    }
  
    private void registrarHistorico(Usuario usuario, String acao, String detalhes) {
        // Ajuste os valores conforme necessário para os campos int, String, int, double, LocalDateTime
        // Exemplo: id=0, usuarioLogin, codigoProduto=0, valor=0.0, dataHora
        historicoDAO.registrarEvento(
            new Historico(0, usuario.getLogin(), 0, 0.0, LocalDateTime.now())
        );
    }
}