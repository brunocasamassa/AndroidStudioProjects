package studio.brunocasamassa.popularizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private LoginButton btnLogin;
    private CallbackManager callbackManager;
    private ProfilePictureView profileImage;
    private TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (AccessToken.getCurrentAccessToken() == null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
            parameters.putString("fields", "id, name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();

        }

    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
//            email.setText(jsonObject.getString("email"));
//            gender.setText(jsonObject.getString("gender"));

            profileName.setText(jsonObject.getString("name"));
            profileImage.setPresetSize(ProfilePictureView.NORMAL);
            profileImage.setProfileId(jsonObject.getString("id"));
//            infoLayout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
