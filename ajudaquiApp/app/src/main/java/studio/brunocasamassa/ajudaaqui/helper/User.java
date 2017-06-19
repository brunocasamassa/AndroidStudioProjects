package studio.brunocasamassa.ajudaaqui.helper;

import android.net.Uri;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bruno on 26/04/2017.
 */

public class User implements DatabaseReference.CompletionListener {

    private String id;

    public int getPremiumUser() {
        return premiumUser;
    }

    public void setPremiumUser(int premiumUser) {
        this.premiumUser = premiumUser;
    }

    private int premiumUser;  //0==free , 1==premium

    private ArrayList<Integer> medalhas;

    private int creditos =3;

    private String pontos;

    private ArrayList<String> pedidosAtendidos;

    private ArrayList<String> pedidosFeitos;

    public String getMessageNotification() {
        return MessageNotification;
    }

    public void setMessageNotification(String messageNotification) {
        MessageNotification = messageNotification;
    }

    private String MessageNotification;
    private String senha;

    private ArrayList<String> grupos;

    private String profileImg;

    private Uri profileImageURL;

    public String name;

    private String email;


    private ArrayList<String> msgSolicitacoes;

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public void setCpf_cnpj(String cpf_cnpj) {
        this.cpf_cnpj = cpf_cnpj;
    }

    public String cpf_cnpj;


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


    public ArrayList<String> getPedidosAtendidos() {

        return pedidosAtendidos;
    }

    public void setPedidosAtendidos(ArrayList<String> pedidosAtendidos) {
        this.pedidosAtendidos = pedidosAtendidos;
    }


    public ArrayList<String> getPedidosFeitos() {
        return pedidosFeitos;
    }

    public void setPedidosFeitos(ArrayList<String> pedidosFeitos) {
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

    public void update() {
        DatabaseReference referenciaFirebase = FirebaseConfig.getFireBase();
        referenciaFirebase.child("usuarios").child(getId()).setValue(this);
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }


    public ArrayList<String> getMsgSolicitacoes() {
        return msgSolicitacoes;
    }

    public void setMsgSolicitacoes(ArrayList<String> msgSolicitacoes) {
        this.msgSolicitacoes = msgSolicitacoes;
    }

}
