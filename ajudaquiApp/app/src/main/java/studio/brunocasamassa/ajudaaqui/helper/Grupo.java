package studio.brunocasamassa.ajudaaqui.helper;

import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Created by bruno on 26/04/2017.
 */

public class Grupo {


    public String nome;
    public ImageView grupoImg;
    public int qtdMembros;
    public String descricao;
    public List<Pedido> trocas;
    public List<Pedido> emprestimos;
    public List<Pedido> servicos;
    public List<Pedido> doacoes;

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

    public ImageView getGrupoImg() {
        return grupoImg;
    }

    public void setGrupoImg(ImageView grupoImg) {
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
        referenciaFirebase.child("grupos").child(getNome()).setValue(this);
    }
}
