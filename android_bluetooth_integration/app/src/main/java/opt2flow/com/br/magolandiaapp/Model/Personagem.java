package opt2flow.com.br.magolandiaapp.Model;

import android.content.Context;
import android.database.Cursor;

import opt2flow.com.br.magolandiaapp.DAO.PersonagemDAO;

/**
 * Created by Caio on 22/02/2017.
 */

public class Personagem {

    private int idPersonagem;
    private String nomePersonagem;
    private String nomeArduinoPersonagem;
    private int idArduinoPersonagem;

    public Personagem(String nomePersonagem, String nomeArduinoPersonagem, int idArduinoPersonagem) {
        this.nomePersonagem = nomePersonagem;
        this.nomeArduinoPersonagem = nomeArduinoPersonagem;
        this.idArduinoPersonagem = idArduinoPersonagem;
    }

    public Personagem(int idPersonagem, String nomePersonagem, String nomeArduinoPersonagem, int idArduinoPersonagem) {
        this.idPersonagem = idPersonagem;
        this.nomePersonagem = nomePersonagem;
        this.nomeArduinoPersonagem = nomeArduinoPersonagem;
        this.idArduinoPersonagem = idArduinoPersonagem;
    }

    public int getIdPersonagem() {
        return idPersonagem;
    }

    public void setIdPersonagem(int idPersonagem) {
        this.idPersonagem = idPersonagem;
    }

    public String getNomePersonagem() {
        return nomePersonagem;
    }

    public void setNomePersonagem(String nomePersonagem) {
        this.nomePersonagem = nomePersonagem;
    }

    public String getNomeArduinoPersonagem() {
        return nomeArduinoPersonagem;
    }

    public void setNomeArduinoPersonagem(String nomeArduinoPersonagem) {
        this.nomeArduinoPersonagem = nomeArduinoPersonagem;
    }

    public int getIdArduinoPersonagem() {
        return idArduinoPersonagem;
    }

    public void setIdArduinoPersonagem(int idArduinoPersonagem) {
        this.idArduinoPersonagem = idArduinoPersonagem;
    }

    public void inserirPersonagem(Context context){
        PersonagemDAO personagemDAO = new PersonagemDAO(context);
        personagemDAO.inserirPersonagem(this);
    }

    public void atualizarPersonagem(Context context){
        PersonagemDAO personagemDAO = new PersonagemDAO(context);
        personagemDAO.atualizarPersonagem(this);
    }

    public Cursor buscarPersonagens(Context context){
        PersonagemDAO personagemDAO = new PersonagemDAO(context);
        return personagemDAO.buscarPersonagens();
    }
}
