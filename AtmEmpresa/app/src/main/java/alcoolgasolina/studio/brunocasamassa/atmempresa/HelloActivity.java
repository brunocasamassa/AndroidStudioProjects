package alcoolgasolina.studio.brunocasamassa.atmempresa;

/**
 * Created by bruno on 22/01/2017.
 */
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class HelloActivity  extends  AppCompatActivity{

    private Button botao;
    private EditText name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        botao = (Button) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.nome);

        botao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Editable nome = name.getText();
                System.out.println(nome);
                Intent intent = new Intent(HelloActivity.this, MainActivity.class);
                intent.putExtra("keyName",nome.toString());
                System.out.println(intent.getExtras());
                startActivity(intent);
            }
        });
    }}
