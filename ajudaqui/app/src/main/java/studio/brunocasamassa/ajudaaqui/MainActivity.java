package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import studio.brunocasamassa.ajudaaqui.helper.User;

public class MainActivity extends AppCompatActivity {

    private ProfileTracker mProfileTracker;
    private Button cadastrar;
    private Button login;
    private LoginButton btnLogin;
    public static User user;
    public CallbackManager callbackManager;
    public static LoginResult lr;
    private static String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cadastrar = (Button) findViewById(R.id.entrar);
        login = (Button) findViewById(R.id.loginButton);
        btnLogin = (LoginButton) findViewById(R.id.login_button);

        btnLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));


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


        //FACEBOOK INTEGRATION

        callbackManager = CallbackManager.Factory.create();

        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {

                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            //profile2 is the new profile
                            //System.out.println("facebook - profile" + profile2.getLastName());
                            user = new User();
                            user.setName(profile2.getFirstName()+" "+profile2.getLastName());
                            user.setProfileImageURL(profile2.getProfilePictureUri(50, 50));
                            mProfileTracker.stopTracking();
                            lr = loginResult;
                            String userId = loginResult.getAccessToken().getUserId();
                            System.out.println("LOGIN RESULT: "+ userId);
                            String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";
                            user.setProfileImg(profileImgUrl);
                            returnLoginResult(lr);
                            startActivity(new Intent(MainActivity.this, PedidosActivity.class));


                        }
                    };
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    System.out.println(("facebook - profile" + profile.getMiddleName()));
                    System.out.println("LOGIN COM SUCESSO");
                    Toast.makeText(getApplicationContext(), "Logado com Sucesso", Toast.LENGTH_SHORT).show();
                    lr = loginResult;
                    startActivity(new Intent(MainActivity.this, PedidosActivity.class));



                }


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }


        })


        ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static LoginResult returnLoginResult(LoginResult result) {

        result = lr;
        return result;
    }


}

