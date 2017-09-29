package alcoolgasolina.studio.brunocasamassa.atmempresa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static alcoolgasolina.studio.brunocasamassa.atmempresa.R.id.nome;
import static alcoolgasolina.studio.brunocasamassa.atmempresa.R.id.surfaceName;

public class MainActivity extends AppCompatActivity {

    private ImageView empresa;
    private ImageView servico;
    private ImageView cliente;
    private ImageView contato;
    private TextView helloName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloName = (TextView) findViewById(surfaceName);
        empresa = (ImageView) findViewById(R.id.empresa);
        servico = (ImageView) findViewById(R.id.servicos);
        cliente = (ImageView) findViewById(R.id.clientes);
        contato = (ImageView) findViewById(R.id.contato);

        Bundle extra = getIntent().getExtras();
        String nome = extra.getString("keyName");
        System.out.println(nome) ;
        helloName.setText("Olá "+ nome);

        empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EmpresaActivity.class));
            }
        });

        servico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServicoActivity.class));
            }
        });

        cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClientesActivity.class));
            }
        });

        contato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContatoActivity.class));
            }
        });

    }
}
