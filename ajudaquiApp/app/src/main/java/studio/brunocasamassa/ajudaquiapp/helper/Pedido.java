package studio.brunocasamassa.ajudaquiapp.helper;

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

    private Double latitude;

    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(Double distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    private Double distanceInMeters;

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    private int naCabine;  //0=false , 1=true

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

    public int getNaCabine() {
        return naCabine;
    }

    public void setNaCabine(int naCabine) {
        this.naCabine = naCabine;
    }


/* STATUS
aberto - 0
em andamento - 1
finalizado - 2
cancelado - 3
*/



}
