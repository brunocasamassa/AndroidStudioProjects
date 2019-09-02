package opt2flow.com.br.magolandiaapp.Model;

import android.content.Context;
import android.database.Cursor;

import java.util.List;

import opt2flow.com.br.magolandiaapp.DAO.ExpressaoDAO;

/**
 * Created by Caio on 14/04/2017.
 */

public class Expressao {

    private int id;
    private String nome;
    private String codigo;

    public Expressao() {
    }

    public Expressao(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void inserirExpressao(Context context){
        ExpressaoDAO expressaoDAO = new ExpressaoDAO(context);
        expressaoDAO.inserirExpressao(this);
    }

    public void atualizarExpressao(Context context){
        ExpressaoDAO expressaoDAO = new ExpressaoDAO(context);
        expressaoDAO.atualizarExpressao(this);
    }

    public static List<Expressao> buscarExpressoes(Context context){
        ExpressaoDAO expressaoDAO = new ExpressaoDAO(context);
        return expressaoDAO.buscarExpressoes();
    }
}
