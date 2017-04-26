package studio.brunocasamassa.ajudaaqui.helper;

import android.net.Uri;

import java.util.List;

/**
 * Created by bruno on 26/04/2017.
 */

public class User {

    public List<Grupo> grupos;


    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }


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
