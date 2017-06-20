package studio.brunocasamassa.ajudaaqui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class GrupoFechadoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private TextView qtdMembros;
    private TextView groupName;
    private TextView descricao;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private Grupo grupo;
    private Bundle itens;
    private Button botaoSolicitar;
    private DatabaseReference firebase;
    private ValueEventListener valueEventSolicita;
    private CircleImageView groupImg;
    private DatabaseReference userData;
    private ValueEventListener valueEventListenerUser;
    private User user = new User();
    private String idAdmin;
    private ArrayList<String> solicitacoesUser = new ArrayList<>();
    private String userName = new String();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onStart() {
        super.onStart();
        //userData.addListenerForSingleValueEvent(valueEventListenerUser);
        //dbGroups.addListenerForSingleValueEvent(valueEventListenerAllGroups);

    }

    @Override
    public void onStop() {
        super.onStop();
        //firebase.removeEventListener(valueEventListenerUser);
        //dbGroups.removeEventListener(valueEventListenerAllGroups);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);

        qtdMembros = (TextView) findViewById(R.id.qtdMembros);
        groupName = (TextView) findViewById(R.id.groupName);
        descricao = (TextView) findViewById(R.id.grupoDescricao);
        groupImg = (CircleImageView) findViewById(R.id.groupImg);
        botaoSolicitar = (Button) findViewById(R.id.botaoSolicitar);


        grupo = new Grupo();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            userName = extra.getString("userName");

            System.out.println("userName " + userName);

            grupo.setIdAdms(extra.getStringArrayList("idAdmins"));
            grupo.setDescricao(extra.getString("descricao"));
            grupo.setNome(extra.getString("nome"));
            grupo.setQtdMembros(Integer.valueOf(extra.getString("qtdmembros")));
            if(extra.getStringArrayList("gruposSolicitados")!= null){
                solicitacoesUser.addAll(extra.getStringArrayList("gruposSolicitados"));
            }
        }

        botaoSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Solicitacao user "+solicitacoesUser);
                if(!solicitacoesUser.contains(grupo.getId())) {

                    geraSolicitacao();
                } else Toast.makeText(GrupoFechadoActivity.this, "Pedido de solicitação para este grupo já enviado, aguarde resposta dos administradores ", Toast.LENGTH_LONG).show();

            }
        });


        final File[] imgFile = new File[1];
        StorageReference storage = FirebaseConfig.getFirebaseStorage().child("groupImage");
        Task<Uri> uri2 = storage.child(grupo.getNome() + ".jpg").getDownloadUrl();/*.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //imgFile[0] = new File(uri.toString());
                grupo.setGrupoImg(uri.toString());
                System.out.println("my groups lets seee2"+ uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });*/

        grupo.setId(Base64Decoder.encoderBase64(grupo.getNome()));
        groupName.setText(grupo.getNome());
        qtdMembros.setText(String.valueOf(grupo.getQtdMembros()));
        // groupImg.setImageURI();
        storage = FirebaseConfig.getFirebaseStorage().child("groupImages");

        storage.child(grupo.getNome() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(GrupoFechadoActivity.this).load(uri).override(68, 68).into(groupImg);
                System.out.println("group image chat " + uri);
            }
        });


        System.out.println("group URI " + grupo.getGrupoImg());
        descricao.setText(grupo.getDescricao());
        //grupo.save();

        //grupo.setGrupoImg(groupImg);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(R.string.menu_grupos);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));


        NavigationDrawer navigator = new NavigationDrawer();
        navigator.createDrawer(GrupoFechadoActivity.this, toolbar, 5);

    }

    private void refresh(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    };

    private void geraSolicitacao() {


        Log.i("Gera Solicitação", "entrei");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GrupoFechadoActivity.this);

        alertDialog.setTitle("Solicitar Participação");
        alertDialog.setMessage("Escreva uma mensagem para os administradores");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(GrupoFechadoActivity.this);
        alertDialog.setView(editText);

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        });

        alertDialog.setPositiveButton("Solicitar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String mensagemSolicitacao = editText.getText().toString();
                String grupoNome = groupName.getText().toString();
                
                //MESSAGE TO ADMINS
                if (mensagemSolicitacao.isEmpty()) {
                    Toast.makeText(GrupoFechadoActivity.this, "Preencha o campo de mensagem", Toast.LENGTH_LONG).show();
                }  else {

                //insert solicitacao into user(para nao solicitar de novo)
                DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
                dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        ArrayList<String> gruposSolicitados = new ArrayList<String>();
                        user.setGruposSolicitados(user.getGruposSolicitados());
                        if (user.getGruposSolicitados() != null) {
                            gruposSolicitados.addAll(user.getGruposSolicitados());
                            gruposSolicitados.add(gruposSolicitados.size(), grupo.getId());
                            user.setGruposSolicitados(gruposSolicitados);
                        } else {
                            gruposSolicitados.add(0, grupo.getId());
                            user.setGruposSolicitados(gruposSolicitados);
                        }
                        user.setId(userKey);
                        user.save();
                        Intent intent = new Intent(GrupoFechadoActivity.this, ListaAdmins.class);
                        intent.putExtra("MESSAGE",mensagemSolicitacao );
                        intent.putExtra("GROUP ID", grupo.getId());
                        startActivity(intent);
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
                startActivity(new Intent(GrupoFechadoActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

