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

import com.facebook.login.LoginManager;
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

        botaoSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geraSolicitacao();

            }
        });

        grupo = new Grupo();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            userName = extra.getString("userName");

            System.out.println("userName "+userName);

            grupo.setIdAdms(extra.getStringArrayList("idAdmins"));
            grupo.setDescricao(extra.getString("descricao"));
            grupo.setNome(extra.getString("nome"));
            grupo.setQtdMembros(Integer.valueOf(extra.getString("qtdmembros")));

        }

        final File[] imgFile = new File[1];
        StorageReference storage = FirebaseConfig.getFirebaseStorage().child("groupImage");
        Task<Uri> uri2 = storage.child(grupo.getNome()+".jpg").getDownloadUrl();/*.addOnSuccessListener(new OnSuccessListener<Uri>() {
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
       // Glide.with(GrupoFechadoActivity.this).load(uri2.getResult()).override(68,68).into(groupImg);
        System.out.println("group URI "+grupo.getGrupoImg());
        descricao.setText(grupo.getDescricao());
        //grupo.save();

        //grupo.setGrupoImg(groupImg);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(R.string.menu_grupos);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        NavigationDrawer navigator = new NavigationDrawer();
        navigator.createDrawer(GrupoFechadoActivity.this, toolbar, 5);

    }

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

                //Validate message to contact
                if (mensagemSolicitacao.isEmpty()) {
                    Toast.makeText(GrupoFechadoActivity.this, "Preencha o campo de mensagem", Toast.LENGTH_LONG).show();
                } else {

                    firebase = FirebaseConfig.getFireBase().child("grupos").child(grupo.getId());

                    Log.i("Gera Solicitação", "antes do evento de leitura");

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("Gera Solicitação", " admins" + dataSnapshot.getValue());
                            Grupo dataGrupo = dataSnapshot.getValue(Grupo.class);
                            grupo.setNome(dataGrupo.getNome());
                            Log.i("Gera Solicitação", " grupo populado" + dataGrupo.getIdAdms());
                            ArrayList idAdms = (ArrayList) dataGrupo.getIdAdms();
                            for (int i = 0; i < idAdms.size(); i++) {
                                String dataIdAdm = (String) idAdms.get(i);
                                idAdmin = dataIdAdm;
                                userData = FirebaseConfig.getFireBase().child("usuarios").child(idAdmin);
                                userData.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User dataUser = dataSnapshot.getValue(User.class);
                                        ArrayList<String> msgSolicitacoes = new ArrayList();
                                        if(dataUser.getMsgSolicitacoes() != null){
                                            System.out.println("mensagens solicitação usuario: "+ dataUser.getMsgSolicitacoes());
                                         user.setMsgSolicitacoes(dataUser.getMsgSolicitacoes());
                                            System.out.println("mensagens solicitação usuario: "+ user.getMsgSolicitacoes());
                                        msgSolicitacoes.addAll(user.getMsgSolicitacoes());
                                            //padrao de mensagem na db
                                        msgSolicitacoes.add(msgSolicitacoes.size(), "GRUPO: "+grupo.getNome() + ":USUARIO: "+ userName + " :MENSAGEM: "+ mensagemSolicitacao +":USERKEY:"+userKey);

                                        }else{
                                            //padrao de mensagem na db
                                            msgSolicitacoes.add(msgSolicitacoes.size(), "GRUPO: "+grupo.getNome() + ":USUARIO: "+ userName + " :MENSAGEM: "+ mensagemSolicitacao +":USERKEY: "+userKey);
                                        }

                                        if(dataUser.getMedalhas() != null){
                                            user.setMedalhas(dataUser.getMedalhas());}
                                            user.setMsgSolicitacoes(msgSolicitacoes);
                                        if(dataUser.getGrupos() != null){
                                            user.setGrupos(dataUser.getGrupos());}
                                        user.setCreditos(dataUser.getCreditos());
                                        if(dataUser.getEmail() != null){
                                            user.setEmail(dataUser.getEmail());}
                                        if(dataUser.getName() != null){
                                            user.setName(dataUser.getName());}
                                        user.setPremiumUser(dataUser.getPremiumUser());
                                        if(dataUser.getProfileImageURL() != null){
                                            user.setProfileImageURL(dataUser.getProfileImageURL());}
                                        if(dataUser.getProfileImg() != null){
                                            user.setProfileImg(dataUser.getProfileImg());}
                                        if(dataUser.getPedidosFeitos() != null){
                                            user.setPedidosFeitos(dataUser.getPedidosFeitos());}
                                        if(dataUser.getPedidosAtendidos() != null){
                                            user.setPedidosAtendidos(dataUser.getPedidosAtendidos());}
                                        if(dataUser.getPontos() != null){
                                            user.setPontos(dataUser.getPontos());}
                                        System.out.println("userName "+userName);
                                        user.setId(Base64Decoder.encoderBase64(user.getEmail()));
                                        user.save();

                                        Toast.makeText(GrupoFechadoActivity.this, "Solicitação enviada", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

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
                startActivity(new Intent(GrupoFechadoActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}

