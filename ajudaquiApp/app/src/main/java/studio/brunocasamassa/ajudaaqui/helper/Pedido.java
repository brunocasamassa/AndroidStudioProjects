package studio.brunocasamassa.ajudaaqui.helper;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by bruno on 08/05/2017.
 */

public class Pedido {

    private String atendenteId;

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    private String idPedido;

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public ArrayList<String> getTagsCategoria() {
        return tagsCategoria;
    }

    public void setTagsCategoria(ArrayList<String> tagsCategoria) {
        this.tagsCategoria = tagsCategoria;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    private String Titulo;
    private String Descricao;
    private ArrayList<String> tagsCategoria = new ArrayList<String>();

    public String getCriadorId() {
        return criadorId;
    }

    public void setCriadorId(String criadorId) {
        this.criadorId = criadorId;
    }

    private String criadorId;


     int status;
    private String grupo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private String tipo;


    public void save() {
        DatabaseReference referenciaFirebase = FirebaseConfig.getFireBase();
        referenciaFirebase.child("Pedidos").child(getIdPedido()).setValue(this);
    }

    public String getAtendenteId() {
        return atendenteId;
    }

    public void setAtendenteId(String atendenteId) {
        this.atendenteId = atendenteId;
    }


/* STATUS
aguardando avaliação - 1
em andamento - 2
finalizado - 3
cancelado - 4
*/



}
