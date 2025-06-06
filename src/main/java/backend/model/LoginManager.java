package backend.model;

import java.util.ArrayList;
import java.util.List;

public class LoginManager {
    private List<Usuario> usuarios;

    public LoginManager() {
        this.usuarios = new ArrayList<>();
        cadastrarUsuariosIniciais();
    }

    // Método de autenticação
    public Usuario autenticar(String login, String senha) {
        Usuario usuario = buscarPorLogin(login);
        
        if (usuario != null && usuario.verificarSenha(senha)) {
            return usuario;
        }
        return null;
    }

    public Usuario buscarPorLogin(String login) {
        return usuarios.stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst()
                .orElse(null);
    }

    // Validação de permissões (nova adição)
    public void validarPermissao(Usuario usuario, Usuario.Perfil perfilRequerido) throws SecurityException {
        if (usuario == null || usuario.getPerfil() == null) {
            throw new SecurityException("Usuário não autenticado");
        }
        if (perfilRequerido != null && usuario.getPerfil() != perfilRequerido) {
            throw new SecurityException("Perfil " + usuario.getPerfil() + " não tem permissão");
        }
    }

    // Validação de limite para funcionários (nova adição)
    public void validarLimiteFuncionario(Usuario usuario, int quantidade, int limite) throws SecurityException {
        if (usuario.getPerfil() == Usuario.Perfil.FUNCIONARIO && quantidade > limite) {
            throw new SecurityException("Funcionários não podem operar mais que " + limite + " itens");
        }
    }


    private String hashSenha(String senha) {
        return Integer.toString(senha.hashCode());
    }

    //areas de teste
    private void cadastrarUsuariosIniciais() {
        usuarios.add(new Usuario("admin", "admin123", Usuario.Perfil.ADMIN));
        usuarios.add(new Usuario("funcionario", "func123", Usuario.Perfil.FUNCIONARIO));
    }
}