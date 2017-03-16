package studio.brunocasamassa.superchat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import studio.brunocasamassa.superchat.R;
import studio.brunocasamassa.superchat.helper.FirebaseConfig;
import studio.brunocasamassa.superchat.helper.Permissao;
import studio.brunocasamassa.superchat.helper.Preferences;


public class MainActivity extends AppCompatActivity {


    private EditText telefone;
    private EditText ddi;
    private EditText ddd;
    private EditText nome;
    private Button botao;
    private String[] permissoesNecessarias = new String[]{
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.SEND_SMS

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Permissao.validaPermissoes(1, this, permissoesNecessarias);



        nome = (EditText) findViewById(R.id.login_email);
        telefone = (EditText) findViewById(R.id.edit_telefone);
        ddi = (EditText) findViewById(R.id.DDI);
        ddd = (EditText) findViewById(R.id.DDD);
        botao = (Button) findViewById(R.id.entrar);

        /*UI MASKS*/
        SimpleMaskFormatter simpleMaskTelephone = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher maskTelefone = new MaskTextWatcher(telefone, simpleMaskTelephone);


        SimpleMaskFormatter simpleMaskddd = new SimpleMaskFormatter("NN");
        MaskTextWatcher maskddd = new MaskTextWatcher(ddd, simpleMaskddd);


        SimpleMaskFormatter simpleMaskddi = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskddi = new MaskTextWatcher(ddi, simpleMaskddi);

        telefone.addTextChangedListener(maskTelefone);
        ddd.addTextChangedListener(maskddd);
        ddi.addTextChangedListener(maskddi);

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = nome.getText().toString();
                String telefoneCompleto = ddi.getText().toString() +
                        ddd.getText().toString() +
                        telefone.getText().toString();
                String telefoneFormatado = telefoneCompleto.replace("+", "");
                telefoneFormatado = telefoneFormatado.replace("-", "");

                //System.out.println("USUARIO: "+user+" Telefone Completo: "+ telefoneFormatado);

                //TOKEN GENERATOR
                Random randomizer = new Random();
                int randomicNumber = randomizer.nextInt(9999 - 1000) + 1000;
                String token = String.valueOf(randomicNumber);
                String message = "SuperChat confirmation TOKEN" + token;



                String phoneNumber = "+5511994896047";
                //SENDING SMS
                // telefoneFormatado = "5554";
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                intent.putExtra("huehuebr", message);
                startActivity(intent);*/
                boolean smsIsValid = sendSMS("+" + telefoneFormatado, message);

                if (smsIsValid) {
                    Intent intent = new Intent(MainActivity.this, ValidadorActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this , "Problema ao enviar o SMS, tente novamente!!", Toast.LENGTH_LONG).show();
                }

                //HashMap<String, String> usuario = preferencias.getUserData();
                //System.out.println("TOKEN " + "T: " + usuario.get("token"));


            }
        });


    }

    private boolean sendSMS(String telefone, String mensagem) {


        try {
            SmsManager smsManager = SmsManager.getDefault();
            System.out.println(smsManager);
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);
            System.out.println("FONE : " + telefone + " MESSAGE: " + mensagem);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    //Permission Negated
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultado : grantResults) {

            if (resultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }

        }

    }

    private void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app, é necessário aceitar as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
