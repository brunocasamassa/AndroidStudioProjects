package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Preferences;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class CadastroActivity extends AppCompatActivity {
    private Button cadastrar;
    private ImageButton backButton;
    private EditText email;
    private EditText nome;
    private EditText senha;
    private EditText senhaConfirm;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebaseDatabase;
    public User usuario;
    public static String idUser;
    private Base64Decoder decoder;
    private ArrayList<Integer> badgesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //badgesList.add(0,2);


        LoginManager.getInstance().logOut();

        backButton = (ImageButton) findViewById(R.id.backButton);
        nome = (EditText) findViewById(R.id.cadastro_nome);
        email = (EditText) findViewById(R.id.cadastro_email);
        senha = (EditText) findViewById(R.id.cadastro_senha);
        senhaConfirm = (EditText) findViewById(R.id.cadastro_senhaConfirm);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CadastroActivity.this, MainActivity.class));
            }
        });

        cadastrar = (Button) findViewById(R.id.buttonValidarCadstro);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(senha.getText().toString().equals(senhaConfirm.getText().toString())){
                    usuario = new User();
                    usuario.setName(nome.getText().toString());
                    usuario.setEmail(email.getText().toString());
                    usuario.setSenha(senha.getText().toString());
                    cadastrarUsuario();
                } else Toast.makeText(getApplicationContext(),"Senha e confirmação devem ser iguais", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void cadastrarUsuario() {

        autenticacao = FirebaseConfig.getFirebaseAuthentication();

        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CadastroActivity.this, "Usuario cadastrado com sucesso", Toast.LENGTH_LONG).show();

                            idUser = Base64Decoder.encoderBase64(usuario.getEmail());
                            System.out.println("BASE64 ENCODER: " + idUser);
                            usuario.setId(idUser);
                            usuario.setMedalhas(badgesList);
                            // FirebaseUser usuarioFireBase = task.getResult().getUser();
                            usuario.save();

                            Preferences preferences = new Preferences(CadastroActivity.this);
                            preferences.saveData(idUser, usuario.getName() );

                            openProfieUser();

                        } else {

                            String erro = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erro = "Escolha uma senha que contenha, letras e números.";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Email indicado não é válido.";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erro = "Já existe uma conta com esse e-mail.";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário: " + erro, Toast.LENGTH_LONG).show();
                        }

                    }
                }


        );

    }



    private void openProfieUser() {
        String nomeUser = usuario.getName().toString();
        Toast.makeText(CadastroActivity.this, "Olá "+nomeUser+", bem vindo ao app Ajudaqui " , Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CadastroActivity.this, PerfilActivity.class);
        startActivity(intent);
        finish();
    }


}
