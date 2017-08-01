package studio.brunocasamassa.ajudaquiapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaquiapp.adapters.NotificacoesAdapter;
import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Grupo;
import studio.brunocasamassa.ajudaquiapp.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaquiapp.helper.Preferences;
import studio.brunocasamassa.ajudaquiapp.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaquiapp.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class PerfilActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private int posicao;
    private CircleImageView profileImg;
    private TextView profileName;
    private RecyclerView badges;
    private StorageReference storage;
    private TextView pontosConquistados;
    private TextView pedidosFeitos;
    private TextView pedidosAtendidos;
    private User user = new User();
    private MainActivity main;
    private CadastroActivity cdrst;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaNotificacoes;
    private ArrayList<String> listaMessages;
    private ArrayList<String> listaUserName;
    private ArrayList<String> listaGrupos;
    private ArrayList<String> listaKey;
    private String groupName;
    private String groupKey;
    private String userName;
    private String userKeySolicitante;
    private String message;
    private ImageView premiumTag;
    public static User usuarioPivot = new User();
    private ArrayList<Integer> badgesList = new ArrayList<>();
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());
    private static int premium;
    private String encodedKeySolicitation;
    private ArrayList<String> listaSolicitationKey;
    private ArrayList<String> listaGruposKeys;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        final ListView notificacoes = (ListView) findViewById(R.id.perfil_notificacoes);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        profileImg = (CircleImageView) findViewById(R.id.profileImg);
        profileName = (TextView) findViewById(R.id.profileName);
        premiumTag = (ImageView) findViewById(R.id.premiumTag);
        pedidosAtendidos = (TextView) findViewById(R.id.rankedPedidosAtendidos);
        pedidosFeitos = (TextView) findViewById(R.id.rankedPedidosFeitos);
        pontosConquistados = (TextView) findViewById(R.id.rankedUserPontosConquistados);
        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);

        listaSolicitationKey = new ArrayList<>();
        listaNotificacoes = new ArrayList();
        listaMessages = new ArrayList();
        listaGrupos = new ArrayList();
        listaGruposKeys = new ArrayList();
        listaUserName = new ArrayList();
        listaKey = new ArrayList();

        final String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        storage = FirebaseConfig.getFirebaseStorage().child("userImages");

        final DatabaseReference databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usuario = dataSnapshot.getValue(User.class);
                user.setMedalhas(badgesList); //usuario.getMedalhas()
                System.out.println("recebe usuario NAME: " + usuario.getName());
                System.out.println("recebe usuario DATA: " + dataSnapshot.getValue());
                int respPremium = usuario.getPremiumUser();
                premium = respPremium;

                if (premium == 1) {
                    Glide.with(PerfilActivity.this).load(R.drawable.premium_icon).into(premiumTag);
                }

                System.out.println("Premium user Perfil Activity response " + premium);

                if (usuario.getMsgSolicitacoes() != null) {
                    ArrayList<String> msgSolicita = usuario.getMsgSolicitacoes();
                    for (int i = 0; i < msgSolicita.size(); i++) {
                        String msgCompleta = "";
                        String[] msg = msgSolicita.get(i).split(":");

                        //GRUPO: vamos ver: usuario: dGVzdGVAdGVzdGUuY29t :mensagem: osmanu: hashkey"

                        for (String sentence : msg) {
                            if (sentence.equals("GRUPO")) {
                                groupName = msg[1];
                                //msgCompleta = groupName;
                                System.out.println("GROUP NAME CONCATENADO " + groupName);
                            }
                            if (sentence.equals("USUARIO")) {
                                userName = msg[3];
                                msgCompleta = "O usuario " + userName + " deseja entrar no grupo " + groupName;
                            }
                            if (sentence.equals("MENSAGEM")) {
                                message = msg[5];
                                //msgCompleta = msgCompleta + " (MENSAGEM: "+message+" )";
                            }
                            if (sentence.equals("USERKEY")) {
                                userKeySolicitante = msg[7];
                                System.out.println("USERKEY userkey " + userKeySolicitante);
                                //msgCompleta = msgCompleta + " (MENSAGEM: "+message+" )";
                            }

                            if (sentence.equals("SOLICITATIONKEY")) {
                                encodedKeySolicitation = msg[9];
                                System.out.println("USERKEY userkey " + encodedKeySolicitation);
                                //msgCompleta = msgCompleta + " (MENSAGEM: "+message+" )";
                            }

                            if (sentence.equals("GROUPKEY")) {
                                groupKey = msg[11];
                                System.out.println("GROUPKEY " + groupKey);
                                //msgCompleta = msgCompleta + " (MENSAGEM: "+message+" )";
                            }

                        }

                        System.out.println("CONCATENADO TOTAL " + msgCompleta);
                        listaSolicitationKey.add(listaSolicitationKey.size(), encodedKeySolicitation);
                        listaKey.add(listaKey.size(), userKeySolicitante);
                        listaGrupos.add(listaGrupos.size(), groupName);
                        listaGruposKeys.add(listaGruposKeys.size(), groupKey);
                        listaUserName.add(listaUserName.size(), userName);
                        listaMessages.add(listaMessages.size(), message);
                        listaNotificacoes.add(listaNotificacoes.size(), msgCompleta);
                        adapter = new NotificacoesAdapter(getApplicationContext(), listaNotificacoes);
                        notificacoes.setDivider(null);
                        notificacoes.setAdapter(adapter);

                    }
                }

                profileName.setText(usuario.getName());
                if (usuario.getPedidosAtendidos() != null) {
                    pedidosAtendidos.setText("" + usuario.getPedidosAtendidos().size());
                } else pedidosAtendidos.setText("" + 0);
                if (usuario.getPedidosFeitos() != null) {
                    pedidosFeitos.setText("" + usuario.getPedidosFeitos().size());
                } else pedidosFeitos.setText("" + 0);
                pontosConquistados.setText(String.valueOf(usuario.getPontos()));
                if (dataSnapshot.child("profileImg").exists()) { //todo bug manual register or facebook register
                    Glide.with(PerfilActivity.this).load(usuario.getProfileImg()).into(profileImg);
                } else {
                    storage.child(userKey + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(PerfilActivity.this).load(uri).override(68, 68).into(profileImg);
                            System.out.println("my groups lets seee2 " + uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }
                /*
                usuario.setId(userKey);
                usuario.save();*/
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent intent = new Intent(PerfilActivity.this, CriaPedidoActivity.class);
        intent.putExtra("premium", premium);
        System.out.println("Premium user Perfil Activity response fora" + premium);

        notificacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, final long id) {
                String mensagem = listaMessages.get(position);
                String usuarioSolicitante = listaUserName.get(position);
                final String nomeGrupoSolicitado = listaGrupos.get(position);
                final String grupoSolicitado= listaGruposKeys.get(position);
                final String userKeySolicitante = listaKey.get(position);
                final String encodedKeySolicitation = listaSolicitationKey.get(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);
                alertDialog.setTitle("Solicitação de Grupo");
                alertDialog.setTitle("O usuario " + usuarioSolicitante + " deseja entrar no grupo " + nomeGrupoSolicitado);
                alertDialog.setMessage(mensagem);
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("ACEITAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios").child(userKeySolicitante);
                        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    System.out.println("userkey '" + userKeySolicitante + "'");
                                    User user = dataSnapshot.getValue(User.class);
                                    System.out.println("username " + user.getName());  //ou caminho errado ou preciso declarar usuario como publico (GRUPOS MEUS GRUPOS FRAGMENT)
                                    addGroupIntoUser(user, grupoSolicitado, userKeySolicitante);
                                } catch (Exception e) {
                                    System.out.println("Exception " + e.getLocalizedMessage());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("SOLICITATION ERROR: " + databaseError);

                            }
                        });

                        //ADD USER INTO GROUP
                        DatabaseReference dbGroups = FirebaseConfig.getFireBase().child("grupos").child(grupoSolicitado);
                        dbGroups.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Grupo grupo = dataSnapshot.getValue(Grupo.class);
                                System.out.println("grupo " + grupo.getNome());
                                ArrayList<String> idMembros = new ArrayList<String>();
                                int qtdMembros = grupo.getQtdMembros() + 1;
                                grupo.setQtdMembros(qtdMembros);
                                if (grupo.getIdMembros() != null) {
                                    idMembros.addAll(grupo.getIdMembros());
                                    idMembros.add(idMembros.size(), userKeySolicitante);
                                } else idMembros.add(0, userKeySolicitante);

                                grupo.setIdMembros(idMembros);
                                grupo.save();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("SOLICITATION ERROR: " + databaseError);
                            }
                        });

                        removeSolicitationMessage(grupoSolicitado, encodedKeySolicitation);

                        Toast.makeText(getApplicationContext(), "Solicitação Aceita", Toast.LENGTH_LONG).show();
                        finish();
                        /*ArrayAdapter newAdapter = new NotificacoesAdapter(getApplicationContext(), listaNotificacoes);
                        notificacoes.setDivider(null);
                        notificacoes.setAdapter(newAdapter);
*/
                    }
                });

                alertDialog.setNegativeButton("RECUSAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Solicitação Recusada ", Toast.LENGTH_LONG).show();
                        removeSolicitationMessage(grupoSolicitado, encodedKeySolicitation);


                    }
                }).create().show();

            }
        });

        if (user.getMedalhas() != null) {

            System.out.println("laço de imagens");

            for (int i = 0; i < 10; i++) {
                final ImageView imageView = new ImageView(PerfilActivity.this);
                imageView.setId(i);
                imageView.setPadding(0, 0, 0, 0);
                imageView.setPaddingRelative(View.TEXT_ALIGNMENT_TEXT_START, View.SCROLL_INDICATOR_TOP, 0, 0);
                imageView.isOpaque();
                if (i == 0) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge_back));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);
                }
                if (i == 1) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge2));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);
                }
                if (i == 2) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge3));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                if (i == 3) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge4));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                if (i == 4) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.layout.model_group));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                if (i == 5) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge6));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT);
                        }
                    });

                }
                if (i == 6) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge7));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                if (i == 7) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge3));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                if (i == 8) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.id.import_donation_img));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                if (i == 9) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge5));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                /*
                if (!user.getMedalhas().contains(i) || user.getMedalhas() ==null) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge_back));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);
                }*/

                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.addView(imageView);

            }
        }

        toolbar.setTitle(getResources().getString(R.string.menu_perfil));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        NavigationDrawer navigator = new NavigationDrawer();


        navigator.createDrawer(PerfilActivity.this, toolbar, 7);

    }


    private void refresh() {
        Intent intent = getIntent();
        finish();
        System.out.println("REFRESHED");
        startActivity(intent);
    }

    ;

    private void removeSolicitationMessage(String grupoId, final String keySolicitacao) {

        DatabaseReference dbGroupSolicitaiton = FirebaseConfig.getFireBase().child("grupos").child(grupoId);
        dbGroupSolicitaiton.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Grupo group = dataSnapshot.getValue(Grupo.class);
                ArrayList<String> groupAdmins = group.getIdAdms();
                //loop admins
                for (int i = 0; i < groupAdmins.size(); i++) {
                    System.out.println("admins grupo: " + groupAdmins.get(i));
                    DatabaseReference admins = FirebaseConfig.getFireBase().child("usuarios").child(groupAdmins.get(i));
                    admins.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User userAdmin = dataSnapshot.getValue(User.class);
                            ArrayList<String> userSolicitations = userAdmin.getMsgSolicitacoes();
                            //loop admin messsages
                            for (int j = 0; j < userSolicitations.size(); j++)
                                if (userSolicitations.get(j).contains(keySolicitacao)) {
                                    userSolicitations.remove(j);
                                }

                            userAdmin.setMsgSolicitacoes(userSolicitations);
                            userAdmin.save();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //don't judge me
        int timer = 2000;
        while (timer>=0) {
            if (timer == 0) {
                finish();
            }
            timer--;
        }
    }

    private void addGroupIntoUser(User user, String grupoSolicitado, String userKeySolicitante) {

        System.out.println("USER SOLICITANTE: " + userKeySolicitante);
        ArrayList<String> solicitacoes = new ArrayList<>();
        if (user.getGruposSolicitados() != null) {
            System.out.println("USER SOLICITANTE SOLICITACOES: " + user.getGruposSolicitados());
            solicitacoes.addAll(user.getGruposSolicitados());
            System.out.println("USER SOLICITACOES: " + solicitacoes);
            solicitacoes.remove(grupoSolicitado);
            user.setGruposSolicitados(solicitacoes);
            System.out.println("USER SOLICITANTE SOLICITACOES: " + user.getGruposSolicitados());
            System.out.println("USER SOLICITACOES: " + solicitacoes);
        }
        ArrayList<String> grupos = new ArrayList<String>();
        if (user.getGrupos() != null) {
            grupos.addAll(user.getGrupos());
            grupos.add(grupos.size(), grupoSolicitado);
        } else
            grupos.add(0, grupoSolicitado);
        user.setGrupos(grupos);
        user.setId(userKeySolicitante);
        user.save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                //logoutUser();
                Preferences preferences = new Preferences(PerfilActivity.this);
                preferences.clearSession();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                finish();
                startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                finish();
                startActivity(new Intent(PerfilActivity.this, ConfiguracoesActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
