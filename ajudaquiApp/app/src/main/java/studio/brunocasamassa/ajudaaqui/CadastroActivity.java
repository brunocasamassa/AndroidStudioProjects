package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
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

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Preferences;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class CadastroActivity extends AppCompatActivity {
    private Button cadastrar;
    private EditText email;
    private EditText nome;
    private EditText senha;
    private EditText senhaConfirm;
    private FirebaseAuth autenticacao;
    public User usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = (EditText) findViewById(R.id.cadastro_nome);
        email = (EditText) findViewById(R.id.cadastro_email);
        senha = (EditText) findViewById(R.id.cadastro_senha);
        senhaConfirm = (EditText) findViewById(R.id.cadastro_senhaConfirm);

        cadastrar = (Button) findViewById(R.id.buttonValidarCadstro);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new User();
                usuario.setName(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                System.out.println("EMAIL: " + usuario.getEmail() + "SENHA: " + usuario.getSenha());
                cadastrarUsuario();
                startActivity(new Intent(CadastroActivity.this, LoginActivity.class));
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

                            //FirebaseUser usuarioFireBase = task.getResult().getUser();
                            String idUser = Base64Decoder.encoderBase64(usuario.getEmail());
                            usuario.setId(idUser);
                            usuario.save();

                            Preferences preferences = new Preferences(CadastroActivity.this);

                            preferences.saveData(idUser);

                            /*autenticacao.signOut();*/
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
