package studio.brunocasamassa.ajudaaqui.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bruno on 21/02/2017.
 */

public class Preferences {

    private Context contexto;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "ajudaaqui.preferences";
    private int MODE = 0;
    private SharedPreferences.Editor editor;


    private String CHAVE_ID = "idUser";
    private String CHAVE_NOME = "nameUser";
    private String FACEBOOK_PHOTO = "facebook_photo";


    public Preferences(Context contextoParametro){
        contexto =contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = preferences.edit();
    }

    public void saveData(String idUser, String nameUser) {
        editor.putString(CHAVE_ID, idUser);
        editor.putString(CHAVE_NOME, nameUser);
        editor.commit();

    }

    public void saveDataImgFacebook(String idUser, String nameUser, String facebookPhoto) {
        editor.putString(FACEBOOK_PHOTO , facebookPhoto);
        editor.putString(CHAVE_ID, idUser);
        editor.putString(CHAVE_NOME, nameUser);
        editor.commit();

    }

    public String getFACEBOOK_PHOTO(){
        return preferences.getString(FACEBOOK_PHOTO,null);
    }



}
