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
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    private Grupo grupo;
    private Bundle itens;
    private Button botaoSolicitar;
    private DatabaseReference firebase;
    private ValueEventListener valueEventSolicita;


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

            grupo.setIdAdms(extra.getStringArrayList("idAdmins"));
            grupo.setDescricao(extra.getString("descricao"));
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
        navigator.createDrawer(GrupoActivity.this, toolbar, 5);

    }

    private void geraSolicitacao() {

        Log.i("Gera Solicitação", "entrei");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GrupoActivity.this);

        alertDialog.setTitle("Solicitar Participação");
        alertDialog.setMessage("Escreva uma mensagem para os administradores");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(GrupoActivity.this);
        alertDialog.setView(editText);

        alertDialog.setNegativeButton("Cancelar:", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setPositiveButton("Solicitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String mensagemSolicitacao = editText.getText().toString();
                String grupoNome = groupName.getText().toString();

                //Validate message to contact
                if (mensagemSolicitacao.isEmpty()) {
                    Toast.makeText(GrupoActivity.this, "Preencha o campo de mensagem", Toast.LENGTH_LONG).show();
                } else {

                    firebase = FirebaseConfig.getFireBase().child("grupos").child(grupo.getId());

                    Log.i("Gera Solicitação", "antes do evento de leitura");

                    firebase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.i("Gera Solicitação", " admins" + dataSnapshot.getValue());
                            Grupo grupo = dataSnapshot.getValue(Grupo.class);
                            Log.i("Gera Solicitação", " rgupo populado" + grupo.getIdAdms());
                            ArrayList idAdms = (ArrayList) grupo.getIdAdms();
                            for (int i = 0; i < idAdms.size(); i++) {
                                String idAdm = (String) idAdms.get(i);
                                User user = new User();
                                DatabaseReference userData = FirebaseConfig.getFireBase();
                                //salva hashmaps para o admin, um com o grupo e outro com a mensagem
                                ArrayList<String> msgSolicitacoes =new ArrayList();
                                msgSolicitacoes.add(msgSolicitacoes.size(), "GRUPO: "+grupo.getNome() + ":USUARIO: "+ userKey + " :MENSAGEM: "+ mensagemSolicitacao );
                                userData.child("usuarios").child(idAdm).child("msgSolicitacoes").setValue(msgSolicitacoes);
                                Toast.makeText(GrupoActivity.this, "Solicitação enviada", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        }).create().show();
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

