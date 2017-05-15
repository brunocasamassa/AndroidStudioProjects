package studio.brunocasamassa.ajudaaqui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.prefs.Preferences;

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class GrupoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private TextView qtdMembros;
    private TextView groupName;
    private TextView descricao;
    private ImageView groupImg;
    private Grupo grupo;
    private Bundle itens;
    private Button botaoSolicitar;
    private DatabaseReference firebase;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);


        qtdMembros = (TextView) findViewById(R.id.qtdMembros);
        groupName = (TextView) findViewById(R.id.groupName);
        descricao = (TextView) findViewById(R.id.grupoDescricao);
        groupImg = (ImageView) findViewById(R.id.groupImg);
        botaoSolicitar = (Button) findViewById(R.id.botaoSolicitar);


        botaoSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geraSolicitacao();

            }
        });

        grupo = new Grupo();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {

            grupo.setNome(extra.getString("nome"));
            grupo.setQtdMembros(Integer.valueOf(extra.getString("qtdmembros")));

        }
        grupo.setId(Base64Decoder.encoderBase64(grupo.getNome()));
        groupName.setText(grupo.getNome());
        qtdMembros.setText(String.valueOf(grupo.getQtdMembros()));
        // groupImg.setImageURI();
        // groupImg.setImageURI();
        descricao.setText(grupo.getDescricao());
        grupo.save();

        //grupo.setGrupoImg(groupImg);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(R.string.menu_grupos);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        NavigationDrawer navigator = new NavigationDrawer();
        navigator.createDrawer(GrupoActivity.this, toolbar);

    }

    private void geraSolicitacao() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GrupoActivity.this);

        alertDialog.setTitle("Solicitar Participação");
        alertDialog.setMessage("Escreva uma mensagem para os administradores");
        alertDialog.setCancelable(false);


        final EditText editText = new EditText(GrupoActivity.this);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Solicitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String mensagemSolicitacao = editText.getText().toString();
                String grupoNome = groupName.getText().toString();

                //Validate e-mail contact
                if (mensagemSolicitacao.isEmpty()) {
                    Toast.makeText(GrupoActivity.this, "Preencha o campo de mensagem", Toast.LENGTH_LONG).show();
                } else {

                    firebase = FirebaseConfig.getFireBase();
                    firebase.child("grupos").child(grupo.getId()).child("idAdms");

                    ValueEventListener valueEventSolicita = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dados : dataSnapshot.getChildren()){

                                System.out.println("id adms: "+ dados.getValue());
                               String idAdm = dados.getValue().toString();

                                User user = new User();

                                DatabaseReference userData = FirebaseConfig.getFireBase();
                                userData.child("usuarios").child(idAdm).child("solicitacoes").setValue(mensagemSolicitacao);


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };



                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                //logoutUser();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(GrupoActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
