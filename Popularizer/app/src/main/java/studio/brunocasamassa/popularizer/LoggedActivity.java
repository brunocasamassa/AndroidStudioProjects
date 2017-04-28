package studio.brunocasamassa.popularizer;

import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class LoggedActivity extends AppCompatActivity {

    private LoginButton btnLogin;
    private CallbackManager callbackManager;
    private ImageView profileImage;
    private TextView profileName;
    private LoginResult loginResult;
    private Preference prefUtil;
    private ImageRequest glide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginResult = MainActivity.lr;
        System.out.println("LOGIN RESULT: "+ loginResult);

        profileImage = (ImageView) findViewById(R.id.profileImage);

        profileName = (TextView) findViewById(R.id.nome);

        Profile profile = Profile.getCurrentProfile();

        String userid = loginResult.getAccessToken().getUserId();
        String namee = message(profile);
        profileName.setText(namee);
        System.out.println("MENSAGEM DO FRONT: "+ message(profile));

        String userId = loginResult.getAccessToken().getUserId();
        System.out.println("ID USER: "+userId);
        String accessToken = loginResult.getAccessToken().getToken();

            String profileImgUrl = "https://graph.facebook.com/" + userId + "/picture?type=large";

        Glide.with(LoggedActivity.this).load(profileImgUrl).into(profileImage);
    }



    private String message(Profile profile) {
        StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(profile.getName());
            System.out.println(stringBuffer);

        return stringBuffer.toString();
    }



    // save accessToken to SharedPreference

    // loginResult.saveAccessToken(accessToken);


}



/*
        if (AccessToken.getCurrentAccessToken() == null) {
            startActivity(new Intent(LoggedActivity.this, MainActivity.class)); //comeback
        } else {
            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.v("Home", response.toString());
                            setProfileToView(object);
                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, nome,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();

        }

    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
//            email.setText(jsonObject.getString("email"));
//            gender.setText(jsonObject.getString("gender"));

            profileName.setText(jsonObject.getString("nome"));
            profileImage.setPresetSize(ProfilePictureView.NORMAL);
            profileImage.setProfileId(jsonObject.getString("id"));
//            infoLayout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

*/