package studio.brunocasamassa.ajudaquiapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Mail;
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
    private MyFirebaseInstanceIdService md = new MyFirebaseInstanceIdService();
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios");
    public static String idUser;
    private ValueEventListener valueEventListenerUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final String ajudaquimail = Base64Decoder.encoderBase64("ajudaquisuporte@gmail.com");
        final String ajudaquipass = Base64Decoder.encoderBase64("ajudaqui931931931");

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




        lostPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);

                alertDialog.setTitle("Esqueci minha senha");
                alertDialog.setMessage("Digite seu email abaixo, enviaremos sua senha para resgate");
                alertDialog.setCancelable(false);
                final EditText editText = new EditText(LoginActivity.this);
                alertDialog.setView(editText);

                alertDialog.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbUser.child(Base64Decoder.encoderBase64(editText.getText().toString())).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                try {
                                    if (user.getSenha() != null) {
                                        String password = user.getSenha();
                                        String[] recipients = {editText.getText().toString()};
                                        SendEmailAsyncTask email = new SendEmailAsyncTask();
                                        email.m = new Mail(Base64Decoder.decoderBase64(ajudaquimail), Base64Decoder.decoderBase64(ajudaquipass));
                                        email.m.set_from("ajudaquisuporte@gmail.com");
                                        email.m.setBody("Voce recebeu este e-mail porque foi requisitado um resgate de senha para o AJUDAQUIAPP, SUA SENHA É: " + password + "\n acesse o app para validar seu login... \n\n Um forte abraço da equipe Ajudaqui e por favor, nos mantenha atualizado com sua satisfação" + "\n\n Cordialmente,\n Equipe Ajudaqui ");
                                        email.m.set_to(recipients);
                                        email.m.set_subject("AJUDAQUI - RESGATE DE SENHA");
                                        email.execute();
                                        Toast.makeText(getApplicationContext(), "Mensagem enviada", Toast.LENGTH_SHORT).show();
                                    } else Toast.makeText(getApplicationContext(), "Falha ao enviar mensagem, Verifique o email digitado", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    System.out.println("exception " + e);
                                    Toast.makeText(getApplicationContext(), "Erro ao enviar mensagem", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();

            }
        });


    }


    private void validarLogin() {

        System.out.println("USER EMAIL: " + usuario.getEmail() + " USER SENHA: " + usuario.getSenha());
        try {
            autenticacao = FirebaseConfig.getFirebaseAuthentication();
            autenticacao.signInWithEmailAndPassword(
                    usuario.getEmail(),
                    usuario.getSenha()
            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();

                        System.out.println("111USER EMAIL: " + usuario.getEmail() + "USER SENHA: " + usuario.getSenha());

                        idUser = Base64Decoder.encoderBase64(usuario.getEmail());

                        System.out.println("LA: decoder 64 " + idUser);
                        firebase = FirebaseConfig.getFireBase()
                                .child("usuarios")
                                .child(idUser);

                        System.out.println("LA: FIREBASE " + firebase);

                        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override

                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User usuarioRecuperado = dataSnapshot.getValue(User.class);
                                System.out.println("LA: Data Changed " + usuarioRecuperado);
                                Preferences preferencias = new Preferences(LoginActivity.this);
                                preferencias.saveData(idUser, usuarioRecuperado.getName());

                                ArrayList<String> entradas = new ArrayList<String>();
                                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");
                                format.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
                                String currentTime = format.format(new Date());
                                //Toast.makeText(getApplicationContext(), currentTime, Toast.LENGTH_SHORT).show();

                                if (currentTime != null) {
                                    if (usuarioRecuperado.getEntradas() != null) {
                                        entradas.addAll(usuarioRecuperado.getEntradas());
                                        entradas.add(entradas.size(), currentTime);
                                        usuarioRecuperado.setEntradas(entradas);
                                    } else {
                                        entradas.add(0, currentTime);
                                        usuarioRecuperado.setEntradas(entradas);
                                    }

                                }
                                md.onTokenRefresh();
                                usuarioRecuperado.save();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("CANCELADO " + databaseError);
                            }
                        });


                        abrirTelaPrincipal();
                        Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(LoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_LONG).show();
                        System.out.println("TASK EXCEPTION" + task.getException());

                    }

                }
            });
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e);
        }
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, PedidosActivity.class);
        Preferences preferences = new Preferences(LoginActivity.this);
        preferences.saveLogin(email.getText().toString(), senha.getText().toString());

        startActivity(intent);
        finish();
    }

    public void displayMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}

class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    Mail m;
    LoginActivity activity;

    public SendEmailAsyncTask() {
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (m.send()) {
               // activity.displayMessage("Email sent.");
            } else {
               // activity.displayMessage("Email failed to send.");
            }

            return true;
        } catch (AuthenticationFailedException e) {
            System.out.println(SendEmailAsyncTask.class.getName() + "Bad account details");
            e.printStackTrace();
           // activity.displayMessage("Authentication failed.");
            return false;
        } catch (MessagingException e) {
            System.out.println(SendEmailAsyncTask.class.getName() + "Email failed");
            e.printStackTrace();
          //  activity.displayMessage("Email failed to send.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
           // activity.displayMessage("Unexpected error occured.");
            return false;
        }
    }
}
