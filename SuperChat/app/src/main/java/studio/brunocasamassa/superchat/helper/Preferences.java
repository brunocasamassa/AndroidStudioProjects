package studio.brunocasamassa.superchat.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bruno on 21/02/2017.
 */

public class Preferences {

    private Context contexto;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "superchat.preferences";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_ID = "idUser";


    public Preferences (Context contextoParametro){
        contexto =contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = preferences.edit();
    }

    public void saveData(String idUser) {
        editor.putString(CHAVE_ID, idUser);
        editor.commit();

    }


    public String getIdentifier(){
        return preferences.getString(CHAVE_ID,null);
    }



}
