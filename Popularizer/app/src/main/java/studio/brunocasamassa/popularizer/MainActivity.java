package studio.brunocasamassa.popularizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;


//TODO: quem é vc na fila do pao, vamos descobrir

public class MainActivity extends AppCompatActivity {

    private LoginButton btnLogin;
    public CallbackManager callbackManager;
    public static LoginResult lr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (LoginButton) findViewById(R.id.login_button);
        btnLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));

        callbackManager = CallbackManager.Factory.create();

        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("LOGIN COM SUCESSO");
                Toast.makeText(getApplicationContext(), "Logado com Sucesso", Toast.LENGTH_SHORT).show();
                lr = loginResult;
                startActivity(new Intent(MainActivity.this, LoggedActivity.class));
                returnLoginResult(lr);
            }

            @Override
            public void onCancel() {
                System.out.println("LOGIN CANCELADO");
                Toast.makeText(getApplicationContext(), "Falha no login, tente novamente", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public static LoginResult returnLoginResult(LoginResult result){

            return result;
    }

}



