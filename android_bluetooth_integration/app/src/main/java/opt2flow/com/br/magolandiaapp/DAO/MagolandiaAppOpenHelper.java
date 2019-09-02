package opt2flow.com.br.magolandiaapp.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Caio on 22/02/2017.
 */

public class MagolandiaAppOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "magolandiaapp.db";

    MagolandiaAppOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //TABELA EXPRESSOES ********************************************************************
        sqLiteDatabase.execSQL("CREATE TABLE " + ExpressaoDAO.TABLE_EXPRESSOES + "( "
                + ExpressaoDAO.EXPRESSOES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExpressaoDAO.EXPRESSOES_COLUMN_NOME + " TEXT, "
                + ExpressaoDAO.EXPRESSOES_COLUMN_CODIGO  + " TEXT )"
        );
        //************************************************************************************
        /*
        //TABELA PERSONAGEM ********************************************************************
        sqLiteDatabase.execSQL("CREATE TABLE " + PersonagemDAO.TABLE_PERSONAGEM + "( "
                + PersonagemDAO.PERSONAGEM_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PersonagemDAO.PERSONAGEM_COLUMN_NOME + " TEXT, "
                + PersonagemDAO.PERSONAGEM_COLUMN_ID_ARDUINO + " INTEGER UNIQUE, "
                + PersonagemDAO.PERSONAGEM_COLUMN_NOME_ARDUINO  + " TEXT )"
        );
        //************************************************************************************
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PersonagemDAO.TABLE_PERSONAGEM);
        onCreate(sqLiteDatabase);
    }
}
