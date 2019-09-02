package opt2flow.com.br.magolandiaapp.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import opt2flow.com.br.magolandiaapp.R;

public class MainActivity extends AppCompatActivity {

    private ListView configListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configListView = (ListView)findViewById(R.id.configListView);

        ArrayList<String> list = new ArrayList<>();
        list.add("Configurar Expressões");
        list.add("Exportar arquivo de configurações");
        list.add("Gerenciar/Alterar Senhas");
        list.add("Configurar Controle Manual");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        configListView.setAdapter(adapter);
        configListView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        Intent i = new Intent(getApplicationContext(), BotoesActivity.class);
                        startActivity(i);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "um", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "dois", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Intent i4 = new Intent(getApplicationContext(), ConfigurarControleManualActivity.class);
                        startActivity(i4);
                        break;
                    default:
                        break;
                }
            }
        });

    }
}
