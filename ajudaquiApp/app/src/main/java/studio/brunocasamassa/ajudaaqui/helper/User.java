package studio.brunocasamassa.ajudaaqui.helper;

import android.net.Uri;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 26/04/2017.
 */

public class User implements DatabaseReference.CompletionListener {

    private String id;

    private ArrayList<Integer> medalhas;

    private String pontos;

    private String pedidosAtendidos;

    private String pedidosFeitos;

    private String senha;

    private ArrayList<String> grupos;

    private String profileImg;

    private Uri profileImageURL;

    public String name;

    private String email;

    public ArrayList<String> getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(ArrayList<String> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    private ArrayList<String> solicitacoes;


    public User() {

    }

    public ArrayList<Integer> getMedalhas() {
        return medalhas;
    }

    public void setMedalhas(ArrayList<Integer> medalhas) {
        this.medalhas = medalhas;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
    }


    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getPedidosAtendidos() {

        return pedidosAtendidos;
    }

    public void setPedidosAtendidos(String pedidosAtendidos) {
        this.pedidosAtendidos = pedidosAtendidos;
    }



    public String getPedidosFeitos() {
        return pedidosFeitos;
    }

    public void setPedidosFeitos(String pedidosFeitos) {
        this.pedidosFeitos = pedidosFeitos;
    }



    public ArrayList<String> getGrupos() {
        return grupos;
    }

    public void setGrupos(ArrayList<String> grupos) {
        this.grupos = grupos;
    }



    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }



    public Uri getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(Uri profileImageURL) {
        this.profileImageURL = profileImageURL;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public void save() {
        DatabaseReference referenciaFirebase = FirebaseConfig.getFireBase();
        referenciaFirebase.child("usuarios").child(getId()).setValue(this);
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

    }
}
