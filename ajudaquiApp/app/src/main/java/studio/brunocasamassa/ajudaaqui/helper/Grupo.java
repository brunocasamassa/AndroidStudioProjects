package studio.brunocasamassa.ajudaaqui.helper;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.util.List;

import studio.brunocasamassa.ajudaaqui.R;

/**
 * Created by bruno on 26/04/2017.
 */

public class Grupo {


    public String nome;
    public ImageView grupoImg;
    public int qtdMembros;
    public String descricao;
    public List<Pedidos> trocas;

    public Grupo(){

    }

    public List<Pedidos> getTrocas() {
        return trocas;
    }

    public void setTrocas(List<Pedidos> trocas) {
        this.trocas = trocas;
    }

    public List<Pedidos> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Pedidos> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public List<Pedidos> getServicos() {
        return servicos;
    }

    public void setServicos(List<Pedidos> servicos) {
        this.servicos = servicos;
    }

    public List<Pedidos> getDoacoes() {
        return doacoes;
    }

    public void setDoacoes(List<Pedidos> doacoes) {
        this.doacoes = doacoes;
    }

    public List<Pedidos> emprestimos;
    public List<Pedidos> servicos;
    public List<Pedidos> doacoes;


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





}
