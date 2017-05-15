package studio.brunocasamassa.ajudaaqui.helper;

import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 26/04/2017.
 */

public class Grupo {


    public String id;
    public String nome;
    public String grupoImg;
    public int qtdMembros;
    public String descricao;
    public ArrayList<String> idAdms;
    public List<Pedido> trocas;
    public List<Pedido> emprestimos;
    public List<Pedido> servicos;
    public List<Pedido> doacoes;


    public ArrayList<String> getIdAdms() {
        return idAdms;
    }

    public void setIdAdms(ArrayList<String> idAdms) {
        this.idAdms = idAdms;
    }

    public List<Pedido> getTrocas() {
        return trocas;
    }

    public void setTrocas(List<Pedido> trocas) {
        this.trocas = trocas;
    }

    public List<Pedido> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Pedido> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public List<Pedido> getServicos() {
        return servicos;
    }

    public void setServicos(List<Pedido> servicos) {
        this.servicos = servicos;
    }

    public List<Pedido> getDoacoes() {
        return doacoes;
    }

    public void setDoacoes(List<Pedido> doacoes) {
        this.doacoes = doacoes;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGrupoImg() {
        return grupoImg;
    }

    public void setGrupoImg(String grupoImg) {
        this.grupoImg = grupoImg;
    }

    public int getQtdMembros() {
        return qtdMembros;
    }

    public void setQtdMembros(int qtdMembros) {
        this.qtdMembros = qtdMembros;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void save() {
        DatabaseReference referenciaFirebase = FirebaseConfig.getFireBase();
       // referenciaFirebase.child("gruposId").setValue(getId());
        referenciaFirebase.child("grupos").child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id = Id;
    }
}
