package opt2flow.com.br.magolandiaapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import opt2flow.com.br.magolandiaapp.Model.Expressao;

/**
 * Created by Caio on 14/04/2017.
 */

public class ExpressaoDAO {

    //Tabela Expressoes *************************************************************************
    public static final String TABLE_EXPRESSOES = "expressoes";
    public static final String EXPRESSOES_COLUMN_ID = "_id";
    public static final String EXPRESSOES_COLUMN_NOME = "expressoes_nome";
    public static final String EXPRESSOES_COLUMN_CODIGO = "expressoes_codigo";
    //*************************************************************************************

    private MagolandiaAppOpenHelper openHelper;
    private SQLiteDatabase database;

    public ExpressaoDAO(Context context){
        openHelper = new MagolandiaAppOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public void inserirExpressao(Expressao expressao){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXPRESSOES_COLUMN_NOME, expressao.getNome());
        contentValues.put(EXPRESSOES_COLUMN_CODIGO, expressao.getCodigo());
        database.insert(TABLE_EXPRESSOES, null, contentValues);
    }

    public void atualizarExpressao(Expressao expressao){
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXPRESSOES_COLUMN_NOME, expressao.getNome());
        contentValues.put(EXPRESSOES_COLUMN_CODIGO, expressao.getCodigo());
        database.update(TABLE_EXPRESSOES,contentValues,EXPRESSOES_COLUMN_ID + " = " + expressao.getId(), null);
    }

    public List<Expressao> buscarExpressoes(){
        List<Expressao> expressoes = new ArrayList<Expressao>();
        Cursor cursor = database.query(true, TABLE_EXPRESSOES, new String[]{EXPRESSOES_COLUMN_ID, EXPRESSOES_COLUMN_NOME, EXPRESSOES_COLUMN_CODIGO}, null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                Expressao expressao = new Expressao();
                expressao.setId(cursor.getInt(cursor.getColumnIndex(ExpressaoDAO.EXPRESSOES_COLUMN_ID)));
                expressao.setNome(cursor.getString(cursor.getColumnIndex(ExpressaoDAO.EXPRESSOES_COLUMN_NOME)));
                expressao.setCodigo(cursor.getString(cursor.getColumnIndex(ExpressaoDAO.EXPRESSOES_COLUMN_CODIGO)));
                expressoes.add(expressao);
            }
        } finally {
            cursor.close();
        }
        return expressoes;
    }
}
