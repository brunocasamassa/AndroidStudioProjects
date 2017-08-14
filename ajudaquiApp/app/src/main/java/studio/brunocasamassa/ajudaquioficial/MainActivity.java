package studio.brunocasamassa.ajudaquioficial;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import studio.brunocasamassa.ajudaquioficial.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquioficial.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquioficial.helper.Preferences;
import studio.brunocasamassa.ajudaquioficial.helper.User;

public class MainActivity extends AppCompatActivity {

    private ProfileTracker mProfileTracker;
    private ImageButton cadastrar;
    private ImageButton login;
    private LoginButton btnLogin;
    private CallbackManager callbackManager;
    private static String userId;
    private static FirebaseAuth autenticacao;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference firebaseDatabase;
    private StorageReference storage;
    private MyFirebaseInstanceIdService md = new MyFirebaseInstanceIdService();
    private static String userName;
    private User pivotUsuario = new User();
    public static LoginResult lr;
    public static User usuario = new User();
    public String facebookImg;
    private ProgressDialog progress = null;
    private boolean trigger = false;

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

    @Override
    protected void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ConnectivityManager conMgr2 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            //verify if user db structure really exist
            progress = ProgressDialog.show(this, "Aguarde...",
                    "Verificando dados do usuario", true);

            if (conMgr2.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                    || conMgr2.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                Toast.makeText(getApplicationContext(), "Sem conexao com a internet", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }

    }


    // ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  //Determine screen size
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();
        } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            Toast.makeText(this, "Normal sized screen", Toast.LENGTH_LONG).show();
        } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            Toast.makeText(this, "Small sized screen", Toast.LENGTH_LONG).show();
        } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            Toast.makeText(this, "Xlarge sized screen", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Screen size is neither large, normal or small", Toast.LENGTH_LONG).show();
        }*/


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //verify connection and check version
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                && conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(getApplicationContext(), "Sem conexao com a internet", Toast.LENGTH_SHORT).show();
        }

        autenticacao = FirebaseConfig.getFirebaseAuthentication();

        storage = FirebaseConfig.getFirebaseStorage().child("userImages");

        firebaseDatabase = FirebaseConfig.getFireBase().child("usuarios");
        final Preferences preferencias = new Preferences(MainActivity.this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser authUser = firebaseAuth.getCurrentUser();
                System.out.println("usuario conectado: " + firebaseAuth.getCurrentUser());
                try {
                    if (authUser != null) {
                        DatabaseReference firebase = FirebaseConfig.getFireBase().child("usuarios").child(Base64Decoder.encoderBase64(authUser.getEmail()));
                        System.out.println("main email " + authUser.getEmail());
                        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if (user == null) {

                                    authUser.delete();

                                    if (progress != null) {
                                        progress.dismiss();
                                    }

                                    LoginManager.getInstance().logOut();
                                    Toast.makeText(getApplicationContext(), "Falha no login, por favor entre novamente ", Toast.LENGTH_SHORT).show();
                                    //refresh();

                                } else {

                                    pivotUsuario.setName(user.getName());
                                    pivotUsuario.setEmail(user.getEmail());
                                    System.out.println("usernAME " + userName);
                                    preferencias.saveData(Base64Decoder.encoderBase64(pivotUsuario.getEmail()), pivotUsuario.getName());

                                    if (user.getSenha() != null) {
                                        preferencias.saveLogin(user.getEmail(), user.getSenha());
                                    }

                                    Toast.makeText(getApplicationContext(), "Bem vindo " + preferencias.getNome(), Toast.LENGTH_SHORT).show();

                                    String savedToken = preferencias.getToken();

                                    String token = FirebaseInstanceId.getInstance().getToken();

                                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");
                                    format.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
                                    String currentTime = format.format(new Date());

                                    ArrayList<String> entradas = new ArrayList<String>();

                                    if (currentTime != null) {
                                        if (user.getEntradas() != null) {
                                            entradas.addAll(user.getEntradas());
                                            entradas.add(entradas.size(), currentTime);
                                            user.setEntradas(entradas);
                                        } else {
                                            entradas.add(0, currentTime);
                                            user.setEntradas(entradas);
                                        }
                                        user.save();
                                    }
                                    md.onTokenRefresh();
                                /*if (token != savedToken) {
                                    md.sendRegistrationToServer(token);
                                } else md.sendRegistrationToServer(savedToken);*/

                                    System.out.println("usuario name " + usuario.getName());
                                    if (progress != null) {
                                        progress.dismiss();
                                    }

                                    //Toast.makeText(getApplicationContext(), currentTime, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, PedidosActivity.class);
                                    startActivity(intent);
                                    //Log.d("IN", "onAuthStateChanged:signed_in:  " + user.getUid());
                                }
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
                preferencias.saveAccessToken(loginResult.getAccessToken());
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
      /*  progress = ProgressDialog.show(this, "Aguarde...",
          "Verificando dados do usuario", true);
        System.out.println("handleFacebookAccessToken:" + token);*/

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        //firebase-facebook bound-line
        autenticacao.removeAuthStateListener(mAuthListener);

        autenticacao.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        final DatabaseReference users = FirebaseConfig.getFireBase().child("usuarios");
                        users.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println("login no firebase " + task);
                                System.out.println("login no firebase " + task.getResult().getUser().getEmail());
                                System.out.println("login no firebase " + task.getResult().getUser().getDisplayName());
                                Toast.makeText(MainActivity.this, "Sucesso em fazer login, ola " + task.getResult().getUser().getDisplayName().toString(), Toast.LENGTH_LONG).show();
                                final String encodedFacebookEmailUser;
                                final String email;
                                if (task.getResult().getUser().getEmail() != null) {
                                    encodedFacebookEmailUser = Base64Decoder.encoderBase64(task.getResult().getUser().getEmail());
                                    email = task.getResult().getUser().getEmail();
                                } else {
                                    encodedFacebookEmailUser = Base64Decoder.encoderBase64(task.getResult().getUser().getDisplayName() + "@facebook.com");
                                    email = task.getResult().getUser().getDisplayName() + "@facebook.com";
                                }

                                final String name = task.getResult().getUser().getDisplayName();
                                final Uri photo = task.getResult().getUser().getPhotoUrl();

                                System.out.println("datasnapshot 2" + dataSnapshot);
                                if (!dataSnapshot.child(encodedFacebookEmailUser).exists()) {
                                   try {
                                       System.out.println("CRIANDO USUARIO NO DATABASSE");
                                       // FirebaseUser usuarioFireBase = task.getResult().getUser();
                                       usuario.setName(name);
                                       usuario.setPremiumUser(1);
                                       usuario.setMaxDistance(10);
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
                                   }catch (Exception e){
                                       Toast.makeText(getApplicationContext(), "Falha no Registro, por favor, valide seu e-mail facebook", Toast.LENGTH_SHORT).show();
                                   }
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
                            System.out.println("erro login firebase "+ task.getException().toString() );
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });

    }

    private void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    ;

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

