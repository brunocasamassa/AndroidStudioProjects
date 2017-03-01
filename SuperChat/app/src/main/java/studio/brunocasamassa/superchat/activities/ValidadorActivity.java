package studio.brunocasamassa.superchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import studio.brunocasamassa.superchat.R;
import studio.brunocasamassa.superchat.helper.Preferences;

/**
 * Created by bruno on 20/02/2017.
 */

public class ValidadorActivity extends AppCompatActivity {

        private EditText token;
        private Button botaoValidar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        token = (EditText) findViewById(R.id.token);
        botaoValidar = (Button) findViewById(R.id.buttonValidate);

        SimpleMaskFormatter simpleMaskCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher(token, simpleMaskCodigoValidacao);

        token.addTextChangedListener(mascaraCodigoValidacao);

        botaoValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences preferences = new Preferences(ValidadorActivity.this);
                HashMap<String, String> usuario = preferences.getUserData();
                String tokenGerado = usuario.get("token");
                String tokenDigitado = token.getText().toString();
                if(tokenDigitado.equals(tokenGerado)){
                    Toast.makeText(ValidadorActivity.this,"TOKEN VALIDADO", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ValidadorActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else Toast.makeText(ValidadorActivity.this,"TOKEN NAO VALIDADO OU JA DESATIVADO", Toast.LENGTH_LONG).show();
            }
        });
    }
}



