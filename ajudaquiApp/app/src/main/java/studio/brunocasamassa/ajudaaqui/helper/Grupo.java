package studio.brunocasamassa.ajudaaqui.helper;

import android.widget.ImageView;

/**
 * Created by bruno on 26/04/2017.
 */

public class  Grupo {


    public String nome;
    public ImageView grupoImg;
    public int qtdMembros;
    public String descricao;

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
