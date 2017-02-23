package studio.brunocasamassa.superchat.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v4.content.ContextCompat;

import java.util.HashMap;

/**
 * Created by bruno on 21/02/2017.
 */

public class Preferences {

    private Context contexto;
    private SharedPreferences preferences;
    private String NOME_ARQUIVO = "superchat.preferences";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";



    public Preferences (Context contextoParametro){
        contexto =contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = preferences.edit();
    }

    public void saveUserPreferences(String nome , String telefone , String token) {
        editor.putString(CHAVE_NOME, nome);
        editor.putString(CHAVE_TELEFONE, telefone);
        editor.putString(CHAVE_TOKEN, token);
        editor.commit();

    }

    public HashMap <String, String> getUserData(){
        HashMap<String, String> userData = new HashMap<>();

        userData.put(CHAVE_NOME,preferences.getString(CHAVE_NOME,null));
        userData.put(CHAVE_TELEFONE,preferences.getString(CHAVE_TELEFONE,null));
        userData.put(CHAVE_TOKEN,preferences.getString(CHAVE_TOKEN,null));

        return userData;

    }


}
