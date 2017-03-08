package studio.brunocasamassa.superchat.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import studio.brunocasamassa.superchat.R;
import studio.brunocasamassa.superchat.helper.FirebaseConfig;
import studio.brunocasamassa.superchat.helper.User;

/**
 * Created by bruno on 24/02/2017.
 */

public class CadastroActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private User usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = (EditText) findViewById(R.id.cadastro_nome);
        email = (EditText) findViewById(R.id.cadastro_email);
        senha = (EditText) findViewById(R.id.cadastro_senha);
        botaoCadastrar = (Button) findViewById(R.id.buttonValidate);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new User();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                System.out.println("EMAIL: " + usuario.getEmail() + "SENHA: " + usuario.getSenha());
                cadastrarUsuario();

            }
        });

    }


    private void cadastrarUsuario() {

        autenticacao = FirebaseConfig.getFirebaseAuthentication();
        System.out.println("EMAIL: " + usuario.getEmail() + "  SENHA: " + usuario.getSenha());
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastroActivity.this, "Usuario cadastrado com sucesso", Toast.LENGTH_LONG).show();

                            FirebaseUser usuarioFireBase = task.getResult().getUser();
                            usuario.setId(usuarioFireBase.getUid());
                            usuario.save();

                            autenticacao.signOut(); 
                            finish();

                        } else {

                            try {
                                throw task.getException();

                            } catch (FirebaseAuthWeakPasswordException e) {
                                Toast.makeText(CadastroActivity.this,"Senha invalida, favor escolher outra senha para autenticacao", Toast.LENGTH_LONG).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Toast.makeText(CadastroActivity.this, "e-mail invalido, verifique os valores digitados", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                }

        );

    }
}


