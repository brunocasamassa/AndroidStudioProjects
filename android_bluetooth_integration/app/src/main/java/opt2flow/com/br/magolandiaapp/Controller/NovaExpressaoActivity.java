package opt2flow.com.br.magolandiaapp.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import opt2flow.com.br.magolandiaapp.Model.Expressao;
import opt2flow.com.br.magolandiaapp.R;

public class NovaExpressaoActivity extends AppCompatActivity {

    private EditText nomeExpressaoEditText;
    private EditText codigoExpressaoEditText;
    private Button cadastraExpressaoButton;
    private Button alterarButton;
    private Button removerButton;
    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_expressao);

        nomeExpressaoEditText = (EditText) findViewById(R.id.nomeExpressaoEditText);
        codigoExpressaoEditText = (EditText) findViewById(R.id.codigoExpressaoEditText);
        cadastraExpressaoButton = (Button) findViewById(R.id.cadastraExpressaoButton);
        alterarButton = (Button) findViewById(R.id.alterarButton);
        removerButton = (Button) findViewById(R.id.removerButton);

        Intent origem = this.getIntent();
        b = origem.getExtras();
        if(b.getBoolean("edit")){
            cadastraExpressaoButton.setVisibility(View.GONE);
            alterarButton.setVisibility(View.VISIBLE);
            removerButton.setVisibility(View.VISIBLE);
            nomeExpressaoEditText.setText(b.getString("nome"));
            codigoExpressaoEditText.setText(b.getString("codigo"));
        } else {
            cadastraExpressaoButton.setVisibility(View.VISIBLE);
            alterarButton.setVisibility(View.GONE);
            removerButton.setVisibility(View.GONE);
        }

        cadastraExpressaoButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Expressao expressao = new Expressao();
                expressao.setNome(nomeExpressaoEditText.getEditableText().toString());
                expressao.setCodigo(codigoExpressaoEditText.getEditableText().toString());
                expressao.inserirExpressao(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Expressão Cadastrada com Sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alterarButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Expressao expressao = new Expressao();
                expressao.setId(b.getInt("id"));
                expressao.setNome(nomeExpressaoEditText.getEditableText().toString());
                expressao.setCodigo(codigoExpressaoEditText.getEditableText().toString());
                expressao.atualizarExpressao(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Expressão Alterada com Sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        removerButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Expressao expressao = new Expressao();
                expressao.setNome(nomeExpressaoEditText.getEditableText().toString());
                expressao.setCodigo(codigoExpressaoEditText.getEditableText().toString());
                expressao.inserirExpressao(getApplicationContext());
                Toast.makeText(getApplicationContext(), "Expressão Alterada com Sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
