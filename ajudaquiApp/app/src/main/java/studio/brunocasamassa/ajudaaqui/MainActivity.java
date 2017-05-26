package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Preferences;
import studio.brunocasamassa.ajudaaqui.helper.User;

public class MainActivity extends AppCompatActivity {

    private ProfileTracker mProfileTracker;
    private Button cadastrar;
    private Button login;
    private LoginButton btnLogin;
    public static User user = new User();
    private CallbackManager callbackManager;
    public static LoginResult lr;
    private static String userId;
    private static FirebaseAuth autenticacao;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference firebaseDatabase;


    @Override
    public void onStart() {
        super.onStart();
        autenticacao.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            autenticacao.removeAuthStateListener(mAuthListener);
        }
    }
    // ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseConfig.getFireBase().child("usuarios");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                System.out.println("usuario conectado: " + firebaseAuth.getCurrentUser());
                if (user != null) {
                    // User is signed in
                    startActivity(new Intent(MainActivity.this, PerfilActivity.class));
                    Log.d("IN", "onAuthStateChanged:signed_in:  " + user.getUid());
                } else {
                    // User is signed out
                    Log.d("OUT", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        autenticacao = FirebaseConfig.getFirebaseAuthentication();


        cadastrar = (Button) findViewById(R.id.entrar);
        login = (Button) findViewById(R.id.loginButton);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });


        callbackManager = CallbackManager.Factory.create();


        btnLogin = (LoginButton) findViewById(R.id.login_button);

        btnLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));


        //FACEBOOK INTEGRATION

        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                System.out.println("MESSAGE Sucesso no callback, integrando com o firebase, login result >>>>  " + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                System.out.println("erro no callback" + error);
            }


        })


        ;
    }


    private void handleFacebookAccessToken(AccessToken token) {
        System.out.println("handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken()); //firebase-facebook bound-line
        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("login no firebase " + task.isSuccessful());
                        Toast.makeText(MainActivity.this, "Sucesso em fazer login, ola " + task.getResult().getUser().getDisplayName().toString(), Toast.LENGTH_LONG).show();
                        String encodedFacebookEmailUser = Base64Decoder.encoderBase64(task.getResult().getUser().getEmail());
                        User usuario = new User();
                        usuario.setName(task.getResult().getUser().getDisplayName());
                        usuario.setProfileImageURL(task.getResult().getUser().getPhotoUrl());
                        usuario.setEmail(task.getResult().getUser().getEmail());
                        usuario.setId(encodedFacebookEmailUser);
                        ArrayList<Integer> badgesList = new ArrayList<Integer>();
                        usuario.setMedalhas(badgesList);
                        usuario.save();

                        Preferences preferences = new Preferences(MainActivity.this);
                        preferences.saveData(encodedFacebookEmailUser, usuario.getName());
                        //verifyLoggedUser(task);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            System.out.println("erro login firebase" + task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void verifyLoggedUser(final Task<AuthResult> task) {



    /*    firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("datasnapshot 2"+ dataSnapshot);

                if(!dataSnapshot.child(encodedFacebookEmailUser).exists()){
                    System.out.println("CRIANDO USUARIO NO DATABASSE");
                    // FirebaseUser usuarioFireBase = task.getResult().getUser();
                    usuario.save();

                    Preferences preferences = new Preferences(MainActivity.this);
                    preferences.saveData(encodedFacebookEmailUser, usuario.getName() );

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

