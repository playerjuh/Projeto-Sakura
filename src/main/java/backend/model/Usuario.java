package backend.model;

import org.mindrot.jbcrypt.BCrypt;

public class Usuario {

    private String login;
    private String senhaHash;
    private Perfil perfil;

    public enum Perfil {
        ADMIN,
        FUNCIONARIO
    }

    // Construtor
    public Usuario(String login, String senha, Perfil perfil) {
        this.login = login;
        this.senhaHash = gerarHashSenha(senha);
        this.perfil = perfil;
    }
    // Método para criptografar a senha
    private String gerarHashSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    // Método para verificar a senha
    public boolean verificarSenha(String senha) {
        return BCrypt.checkpw(senha, this.senhaHash);
    }


    // Getters

    public String getLogin() {return login;}
    public String getSenhaHash() {return senhaHash;}
    public Perfil getPerfil() {return perfil;}

    // Setters

    public void setLogin(String login) {this.login = login;}
    public void setPerfil(Perfil perfil) {this.perfil = perfil;}
}