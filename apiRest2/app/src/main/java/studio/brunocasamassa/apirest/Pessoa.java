package studio.brunocasamassa.apirest;

import java.io.Serializable;


public class Pessoa implements Serializable {

    /**
     * POJO
     */
    public String nome;
    public String url;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return nome;
    }

}