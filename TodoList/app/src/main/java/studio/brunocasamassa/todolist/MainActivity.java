package studio.brunocasamassa.todolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.facebook.FacebookActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText textoTarefa;
    private Button botaoAdicionar;
    private ListView listaTarefas;
    private SQLiteDatabase bancoDados;
    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;
    private ArrayList<Integer> ids;
    private ImageView disquete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            disquete= (ImageView) findViewById(R.id.disqueteid);
            textoTarefa = (EditText) findViewById(R.id.adicionar);
            botaoAdicionar = (Button) findViewById(R.id.addButton);
            listaTarefas = (ListView) findViewById(R.id.listviewid);

            bancoDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);

            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR)");


            botaoAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textoDigitado = textoTarefa.getText().toString();
                    salvarTarefa(textoDigitado);
                    Random randomizer = new Random();
                    int imageTarget = randomizer.nextInt(5);
                    randomImages(imageTarget);

                }
            });


            listaTarefas.setLongClickable(true);
            listaTarefas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    removerTarefa( ids.get( position ) );
                    return true;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void salvarTarefa(String texto) {
        try {
            if (texto.equals("")) {
                Toast.makeText(MainActivity.this, "Digite uma tarefa", Toast.LENGTH_SHORT).show();
            } else {
                bancoDados.execSQL("INSERT INTO tarefas (tarefa) VALUES('" + texto + "')");
                Toast.makeText(MainActivity.this, "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show();
            }
            //GET DATA
            refreshtables();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refreshtables() {

        try {
            //GET DATA
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);
            int indiceColunaid = cursor.getColumnIndex("id");
            int indiceColunatarefa = cursor.getColumnIndex("tarefa");

            itens = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                    android.R.layout.simple_list_item_2,
                    android.R.id.text2,
                    itens);

            cursor.moveToFirst();

            try {
                while (cursor != null) {
                    System.out.println("Resultado - id Tarefa: " + cursor.getString(indiceColunaid)+ "/ " + cursor.getString(indiceColunatarefa));
                    itens.add(cursor.getString(indiceColunatarefa));
                    ids.add(Integer.parseInt(cursor.getString(indiceColunaid)));
                    cursor.moveToNext();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            listaTarefas.setAdapter(itensAdaptador);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void removerTarefa(Integer id) {
        try {



            bancoDados.execSQL("DELETE FROM tarefas WHERE ID=" + id);

            refreshtables();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void randomImages(int x){

        if (x==0){
            disquete.setImageDrawable( ContextCompat.getDrawable(MainActivity.this,R.drawable.disquete_verde ) );
        }
        if (x==1){
            disquete.setImageDrawable( ContextCompat.getDrawable(MainActivity.this,R.drawable.disquete) );
        }
        if (x==2){
            disquete.setImageDrawable( ContextCompat.getDrawable(MainActivity.this,R.drawable.disquete_azul) );
        }
        if (x==3){
            disquete.setImageDrawable( ContextCompat.getDrawable(MainActivity.this,R.drawable.disquete_amarelo) );
        }
        if (x==4){
            disquete.setImageDrawable( ContextCompat.getDrawable(MainActivity.this,R.drawable.disquete_rosa) );
        }
    }


}
