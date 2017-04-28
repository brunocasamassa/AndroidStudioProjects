package studio.brunocasamassa.ajudaaqui.helper;

import android.net.Uri;

import java.util.List;

/**
 * Created by bruno on 26/04/2017.
 */

public class User {

    public String pontos ;

    public String pedidosAtendidos ;

    public String pedidosFeitos ;

    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
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

    public List<Grupo> grupos;

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String profileImg;

    private Uri profileImageURL;

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

    private String name;

    private String email;

}
