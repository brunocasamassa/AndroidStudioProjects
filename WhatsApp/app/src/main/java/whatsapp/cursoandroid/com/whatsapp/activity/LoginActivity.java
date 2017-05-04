package whatsapp.cursoandroid.com.whatsapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Random;

import javax.security.auth.login.LoginException;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;
import whatsapp.cursoandroid.com.whatsapp.helper.Permissao;
import whatsapp.cursoandroid.com.whatsapp.helper.Preferencias;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("pontos").setValue("800");

    }

    public void abrirCadastroUsuario(View view){

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity( intent );

    }

}
