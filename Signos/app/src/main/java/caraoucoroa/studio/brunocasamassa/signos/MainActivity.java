package caraoucoroa.studio.brunocasamassa.signos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] signos = {"Aries","Touro","Virgem","Capricornio","Sagitario","Peixes","Libra","Aquario","Leão","Escorpião","Cancer","Gêmeos"};

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (ListView) findViewById(R.id.listaid);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                signos
        );

        lista.setAdapter(adapter);

    }
}
