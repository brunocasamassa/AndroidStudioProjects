package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ProfileTracker;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Preferences;
import studio.brunocasamassa.ajudaaqui.helper.User;

public class MainActivity extends AppCompatActivity {

    private ProfileTracker mProfileTracker;
    private ImageButton cadastrar;
    private ImageButton login;
    private LoginButton btnLogin;
    private CallbackManager callbackManager;
    public static LoginResult lr;
    private static String userId;
    private static FirebaseAuth autenticacao;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference firebaseDatabase;
    public static User usuario = new User();
    private StorageReference storage;
    public String facebookImg;
    private static String userName;
    private User pivotUsuario = new User();

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

        autenticacao = FirebaseConfig.getFirebaseAuthentication();

        storage = FirebaseConfig.getFirebaseStorage().child("userImages");

        firebaseDatabase = FirebaseConfig.getFireBase().child("usuarios");
        final Preferences preferencias = new Preferences(MainActivity.this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                System.out.println("usuario conectado: " + firebaseAuth.getCurrentUser());
                try {
                    if (user != null) {
                        DatabaseReference firebase = FirebaseConfig.getFireBase().child("usuarios").child(Base64Decoder.encoderBase64(user.getEmail()));
                        System.out.println("main email " + user.getEmail());
                        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                pivotUsuario.setName(user.getName());
                                pivotUsuario.setEmail(user.getEmail());
                                System.out.println("usernAME " + userName);

                                preferencias.saveData(Base64Decoder.encoderBase64(pivotUsuario.getEmail()), pivotUsuario.getName());
                                Toast.makeText(getApplicationContext(), "signed in " + preferencias.getNome(), Toast.LENGTH_LONG).show();

                                System.out.println("usuario name " + usuario.getName());
                                startActivity(new Intent(MainActivity.this, PedidosActivity.class));
                                //Log.d("IN", "onAuthStateChanged:signed_in:  " + user.getUid());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else {
                        // User is signed out
                        Log.d("OUT", "onAuthStateChanged:signed_out");
                    }
                } catch (Exception e) {
                    System.out.println("EXCEPTION " + e);
                }
                // ...
            }
        };


        cadastrar = (ImageButton) findViewById(R.id.entrar);
        login = (ImageButton) findViewById(R.id.loginButton);

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
                System.out.println("cancelede");
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
        autenticacao.removeAuthStateListener(mAuthListener);

        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DatabaseReference users = FirebaseConfig.getFireBase().child("usuarios");
                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println("datasnapshot 1" + dataSnapshot);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                            System.out.println("login no firebase " + task);
                            Toast.makeText(MainActivity.this, "Sucesso em fazer login, ola " + task.getResult().getUser().getDisplayName().toString(), Toast.LENGTH_LONG).show();
                            final String encodedFacebookEmailUser = Base64Decoder.encoderBase64(task.getResult().getUser().getEmail());
                            final String email = task.getResult().getUser().getEmail();
                            final String name = task.getResult().getUser().getDisplayName();
                            final Uri photo = task.getResult().getUser().getPhotoUrl();
                            firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    System.out.println("datasnapshot 2" + dataSnapshot);
                                    if (!dataSnapshot.child(encodedFacebookEmailUser).exists()) {
                                        System.out.println("CRIANDO USUARIO NO DATABASSE");
                                        // FirebaseUser usuarioFireBase = task.getResult().getUser();
                                        usuario.setName(name);
                                        usuario.setProfileImg(photo.toString());
                                        usuario.setEmail(email);
                                        usuario.setId(encodedFacebookEmailUser.toString());
                                        ArrayList<Integer> badgesList = new ArrayList<Integer>();
                                        usuario.setMedalhas(badgesList);
                                        System.out.println("user name1 " + usuario.getName());
                                        Preferences preferences = new Preferences(MainActivity.this);
                                        preferences.saveDataImgFacebook(usuario.getId(), usuario.getName(), usuario.getProfileImg());

                                        facebookImg = usuario.getProfileImg();

                                        usuario.save();
                                        autenticacao.addAuthStateListener(mAuthListener);

                                        //refresh();

                                    } else {

                                        Preferences preferences = new Preferences(MainActivity.this);
                                        preferences.saveDataImgFacebook(encodedFacebookEmailUser, name, photo.toString());
                                        preferences.saveData(encodedFacebookEmailUser, name);
                                        System.out.println("username " + name);
                                        autenticacao.addAuthStateListener(mAuthListener);

                                    }
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        /*Preferences preferences = new Preferences(MainActivity.this);
                        preferences.saveData(encodedFacebookEmailUser, user.getName());*/
                        //verifyLoggedUser(task);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            System.out.println("erro login firebase");
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

    }
    private void refresh(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    };
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

