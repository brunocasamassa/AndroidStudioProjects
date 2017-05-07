package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.User;

public class MainActivity extends AppCompatActivity {

    private ProfileTracker mProfileTracker;
    private Button cadastrar;
    private Button login;
    private LoginButton btnLogin;
    public static User user = new User();
    public CallbackManager callbackManager;
    public static LoginResult lr;
    private static String userId;
    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseConfig.getFireBase();
        firebaseDatabase.child("pontos").setValue("300");

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

        Profile facebookProfile ;

        if ( Profile.getCurrentProfile() != null ){
            facebookProfile = Profile.getCurrentProfile();

            user.setName(facebookProfile.getFirstName()+" "+facebookProfile.getLastName());
            user.setProfileImageURL(facebookProfile.getProfilePictureUri(50, 50));
            startActivity(new Intent(MainActivity.this, PerfilActivity.class));
        }

        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {

                if (Profile.getCurrentProfile() == null) {

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    System.out.println("LoginActivity"+ response.toString());

                                    // Application code
                                    try {
                                        String email = object.getString("email");
                                        System.out.println("email: "+ email);
                                        String birthday = object.getString("birthday");
                                        System.out.println("birthday: "+ birthday);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }}});

                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            autenticacao = FirebaseConfig.getFirebaseAuthentication();
                            //profile2 is the new profile
                            user.setName(profile2.getFirstName()+" "+profile2.getLastName());
                            user.setProfileImageURL(profile2.getProfilePictureUri(50, 50));
                            Profile.setCurrentProfile(profile2);
                            mProfileTracker.stopTracking();
                            lr = loginResult;
                            String userId = loginResult.getAccessToken().getUserId();
                            System.out.println("LOGIN RESULT: "+ userId);
                            String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";
                            user.setProfileImg(profileImgUrl);
                            returnLoginResult(lr);
                            startActivity(new Intent(MainActivity.this, PerfilActivity.class));

                        }
                    };
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

