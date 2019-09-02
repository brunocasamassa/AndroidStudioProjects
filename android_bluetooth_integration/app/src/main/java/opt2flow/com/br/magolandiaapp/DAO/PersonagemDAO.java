package opt2flow.com.br.magolandiaapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import opt2flow.com.br.magolandiaapp.Model.Personagem;

/**
 * Created by Caio on 22/02/2017.
 */

public class PersonagemDAO {

    //Tabela Personagem *************************************************************************
    public static final String TABLE_PERSONAGEM = "personagens";
    public static final String PERSONAGEM_COLUMN_ID = "_id";
    public static final String PERSONAGEM_COLUMN_NOME = "personagens_nome";
    public static final String PERSONAGEM_COLUMN_ID_ARDUINO = "personagens_id_arduino";
    public static final String PERSONAGEM_COLUMN_NOME_ARDUINO = "personagens_nome_arduino";
    //*************************************************************************************

    private MagolandiaAppOpenHelper openHelper;
    private SQLiteDatabase database;

    public PersonagemDAO(Context context){
        openHelper = new MagolandiaAppOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public void inserirPersonagem(Personagem personagem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSONAGEM_COLUMN_NOME, personagem.getNomePersonagem());
        contentValues.put(PERSONAGEM_COLUMN_ID_ARDUINO, personagem.getIdArduinoPersonagem());
        contentValues.put(PERSONAGEM_COLUMN_NOME_ARDUINO, personagem.getNomeArduinoPersonagem());
        database.insert(TABLE_PERSONAGEM, null, contentValues);
    }

    public void atualizarPersonagem(Personagem personagem){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSONAGEM_COLUMN_NOME, personagem.getNomePersonagem());
        contentValues.put(PERSONAGEM_COLUMN_ID_ARDUINO, personagem.getIdArduinoPersonagem());
        contentValues.put(PERSONAGEM_COLUMN_NOME_ARDUINO, personagem.getNomeArduinoPersonagem());
        database.update(TABLE_PERSONAGEM,contentValues,PERSONAGEM_COLUMN_ID + " = " + personagem.getIdPersonagem(), null);
    }

    public Cursor buscarPersonagens(){
        return database.query(true, TABLE_PERSONAGEM, new String[]{PERSONAGEM_COLUMN_ID, PERSONAGEM_COLUMN_NOME, PERSONAGEM_COLUMN_ID_ARDUINO, PERSONAGEM_COLUMN_NOME_ARDUINO}, null, null, null, null, null, null);
    }
}
