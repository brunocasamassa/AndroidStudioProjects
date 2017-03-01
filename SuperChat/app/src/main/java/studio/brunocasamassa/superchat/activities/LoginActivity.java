package studio.brunocasamassa.superchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import studio.brunocasamassa.superchat.R;
import studio.brunocasamassa.superchat.helper.FirebaseConfig;

/**
 * Created by bruno on 24/02/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private TextView cadastro;
    private Button entrar;
    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        referenciaFirebase = FirebaseConfig.getFireBase();
        referenciaFirebase.child("pontos").setValue("800");
        cadastro = (TextView) findViewById(R.id.textView3);
        entrar = (Button) findViewById(R.id.entrar);


         //cadastro.callOnClick();

    }

    public void abrirCadastro(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);

        startActivity(intent);
    }


}


