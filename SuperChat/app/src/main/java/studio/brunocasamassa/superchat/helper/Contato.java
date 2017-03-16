package studio.brunocasamassa.superchat.helper;

/**
 * Created by bruno on 10/03/2017.
 */

public class Contato {

    private String idUser;
    private String nome;
    private String email;

    public String getidUser() {
        return idUser;
    }

    public void setidUser(String identificadorUsuario) {
        this.idUser = identificadorUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contato(){

    }
}
