package studio.brunocasamassa.superchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import studio.brunocasamassa.superchat.R;
import studio.brunocasamassa.superchat.helper.FirebaseConfig;
import studio.brunocasamassa.superchat.helper.User;

/**
 * Created by bruno on 24/02/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private TextView cadastro;
    private EditText email;
    private EditText senha;
    private Button entrar;
    private DatabaseReference referenciaFirebase;
    private FirebaseAuth autenticator;
    private User usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verifyLoggedUser();

        cadastro = (TextView) findViewById(R.id.textView3);
        entrar = (Button) findViewById(R.id.entrar);
        email = (EditText) findViewById(R.id.login_email);
        senha = (EditText) findViewById(R.id.login_senha);

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity(v);
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new User();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                login();
            }
        });
    }

    /*methods*/

    private void login() {
        autenticator = FirebaseConfig.getFirebaseAuthentication();
        autenticator.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login", Toast.LENGTH_LONG).show();
                    openLoggedActivity();
                } else {
                    Toast.makeText(LoginActivity.this, "3rr0 a0 f4z3r l0g1n!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void verifyLoggedUser() {
        autenticator = FirebaseConfig.getFirebaseAuthentication();
        if (autenticator.getCurrentUser() != null) {
            openLoggedActivity();
        }
    }

    private void openLoggedActivity() {
        Intent intent = new Intent(LoginActivity.this, HelloActivity.class);
        startActivity(intent);
        finish();
    }

    public void openRegisterActivity(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);

        startActivity(intent);
    }


}


