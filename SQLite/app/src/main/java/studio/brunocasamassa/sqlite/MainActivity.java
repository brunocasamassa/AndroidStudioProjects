
package studio.brunocasamassa.sqlite;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    try{
        SQLiteDatabase bancodedados = openOrCreateDatabase("app", MODE_PRIVATE, null);

        //tabela
        bancodedados.execSQL("CREATE TABLE IF NOT EXISTS pessoas (nome VARCHAR , idade INT(3) )");

        //inserir dados

        bancodedados.execSQL("INSERT INTO pessoas(nome,idade) VALUES ('Marcos, 30)");
        bancodedados.execSQL("INSERT INTO pessoas(nome,idade) VALUES ('Ana, 20)");
        bancodedados.execSQL("INSERT INTO pessoas(nome,idade) VALUES ('Bruna, 15)");

        Cursor cursor = bancodedados.rawQuery("SELECT nome, idade FROM pessoas", null);

        int indiceColunaNome = cursor.getColumnIndex("nome");
        int indiceColunaIdade = cursor.getColumnIndex("idade");

        cursor.moveToFirst();

        while (cursor != null){
            Log.i("RESULTADO - idade: ",cursor.getString(indiceColunaIdade));
            Log.i("RESULTADO - nome: ", cursor.getString(indiceColunaNome));
            cursor.moveToNext();

    }}
    catch(Exception e){
     e.printStackTrace();
}
}}
