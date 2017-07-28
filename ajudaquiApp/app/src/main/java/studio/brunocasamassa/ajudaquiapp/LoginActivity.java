package studio.brunocasamassa.ajudaquiapp;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Preferences;
import studio.brunocasamassa.ajudaquiapp.helper.User;

/**
 * Created by bruno on 24/04/   2017.
 */

public class LoginActivity extends AppCompatActivity {

    private Button entrar;
    private TextView lostPassword;
    private User usuario;
    private EditText email;
    private EditText senha;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    public static String idUser;
    private ValueEventListener valueEventListenerUsuario;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        entrar = (Button) findViewById(R.id.entrar);
        lostPassword = (TextView) findViewById(R.id.lostPassword);

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new User();
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                validarLogin();
            }

        });


/*
        Intent itSend = new Intent(android.content.Intent.ACTION_SEND);
// vamos a enviar texto plano a menos que el checkbox esté marcado
        itSend.setType("plain/text");
        itSend.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ etEmail.getText().toString()});
        itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, etSubject.getText().toString());
        itSend.putExtra(android.content.Intent.EXTRA_TEXT, etBody.getText());
        startActivity(itSend);*/

    }

    private void validarLogin() {

        System.out.println("USER EMAIL: " + usuario.getEmail() + " USER SENHA: " + usuario.getSenha());
try{
        autenticacao = FirebaseConfig.getFirebaseAuthentication();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();

                    System.out.println("111USER EMAIL: " + usuario.getEmail() + "USER SENHA: " + usuario.getSenha());

                    idUser = Base64Decoder.encoderBase64(usuario.getEmail());

                    System.out.println("LA: decoder 64 "+ idUser);
                    firebase = FirebaseConfig.getFireBase()
                            .child("usuarios")
                            .child(idUser);

                    System.out.println("LA: FIREBASE "+ firebase);

                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User usuarioRecuperado = dataSnapshot.getValue(User.class);
                            System.out.println("LA: Data Changed " + usuarioRecuperado);
                            Preferences preferencias = new Preferences(LoginActivity.this);
                            preferencias.saveData(idUser, usuarioRecuperado.getName());

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("CANCELADO " + databaseError);
                        }
                    });


                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                }


                else{

                    Toast.makeText(LoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();
                    System.out.println("TASK EXCEPTION"+ task.getException());

                }

            }
        });
    } catch (Exception e){
            System.out.println("EXCEPTION "+ e);
    }}

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, PerfilActivity.class);
        startActivity(intent);
        finish();
    }
}
