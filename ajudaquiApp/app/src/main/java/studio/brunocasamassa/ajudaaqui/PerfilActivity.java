package studio.brunocasamassa.ajudaaqui;

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
import studio.brunocasamassa.ajudaaqui.adapters.NotificacoesAdapter;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaaqui.helper.User;

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
    private String userName;
    private String encodedKeyRequestedUser;
    private String message;
    public static User usuarioPivot = new User();
    private ArrayList<Integer> badgesList = new ArrayList<>();
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        final ListView notificacoes = (ListView) findViewById(R.id.perfil_notificacoes);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        profileImg = (CircleImageView) findViewById(R.id.profileImg);
        profileName = (TextView) findViewById(R.id.profileName);
        pedidosAtendidos = (TextView) findViewById(R.id.perfilPedidosAtendidos);
        pedidosFeitos = (TextView) findViewById(R.id.perfilPedidosFeitos);
        pontosConquistados = (TextView) findViewById(R.id.perfilPontosConquistados);
        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);

        listaNotificacoes = new ArrayList();
        listaMessages = new ArrayList();
        listaGrupos = new ArrayList();
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

                if (usuario.getMsgSolicitacoes() != null) {
                    ArrayList<String> msgSolicita = usuario.getMsgSolicitacoes();
                    for (int i = 0; i < msgSolicita.size(); i++) {
                        String msgCompleta = "";
                        String[] msg = msgSolicita.get(i).split(":");

                        //GRUPO: vamos ver: usuario: dGVzdGVAdGVzdGUuY29t :mensagem: osmanu"
                        for (String sentence : msg) {
                            if (sentence.equals("GRUPO")) { //TODO alterar padrao de mensagem  (FULANO desej entrar no grupo TAL)
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
                                encodedKeyRequestedUser = msg[7];
                                System.out.println("SOLICITATION userkey " + encodedKeyRequestedUser);
                                //msgCompleta = msgCompleta + " (MENSAGEM: "+message+" )";
                            }

                        }

                        System.out.println("CONCATENADO TOTAL " + msgCompleta);
                        listaKey.add(listaKey.size(), encodedKeyRequestedUser);
                        listaGrupos.add(listaGrupos.size(), groupName);
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
                //pontosConquistados.setText(usuario.getPontos());
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

        notificacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, final long id) {
                String mensagem = listaMessages.get(position);
                String usuarioSolicitante = listaUserName.get(position);
                final String grupoSolicitado = listaGrupos.get(position);
                final String userKeySolicitante = listaKey.get(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PerfilActivity.this);

                alertDialog.setTitle("Solicitação de Grupo");
                alertDialog.setTitle("O usuario " + usuarioSolicitante + " deseja entrar no grupo " + grupoSolicitado);
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
                                    System.out.println("userkey '"+userKeySolicitante+"'");
                                    User user = dataSnapshot.getValue(User.class);
                                    System.out.println("username "+ user.getName());  //ou caminho errado ou preciso declarar usuario como publico (GRUPOS MEUS GRUPOS FRAGMENT)
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

                        DatabaseReference dbGroups = FirebaseConfig.getFireBase().child("grupos").child(Base64Decoder.encoderBase64(grupoSolicitado));
                        dbGroups.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Grupo grupo = dataSnapshot.getValue(Grupo.class);
                                System.out.println("grupo " + grupo.getNome());
                                ArrayList<String> idMembros =  new ArrayList<String>();
                                int qtdMembros = grupo.getQtdMembros() + 1;
                                grupo.setQtdMembros(qtdMembros);
                                if (grupo.getIdMembros() != null) {
                                    idMembros.addAll(user.getGrupos());
                                    idMembros.add(idMembros.size(), Base64Decoder.encoderBase64(grupoSolicitado));
                                } else idMembros.add(0, Base64Decoder.encoderBase64(grupoSolicitado));

                                grupo.save();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("SOLICITATION ERROR: " + databaseError);
                            }
                        });

                        removeSolicitationMessage(position);
                        Toast.makeText(getApplicationContext(), "Solicitação Aceita", Toast.LENGTH_LONG).show();
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
                        listaNotificacoes.remove(position);


                    }
                }).create().show();


            }
        });

        if (user.getMedalhas() != null) {

            System.out.println("laço de imagens");
            for (int i = 0; i < 10; i++) {
                ImageView imageView = new ImageView(PerfilActivity.this);
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
                            getResources(), R.drawable.badge4));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                if (i == 9) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge5));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }
                if (!user.getMedalhas().contains(i)) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge_back));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);
                }
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

    private void removeSolicitationMessage(final int position) {

        DatabaseReference dbUserRemover = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
        dbUserRemover.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                ArrayList<String> lista = new ArrayList<String>();
                lista.addAll(user.getMsgSolicitacoes());
                lista.remove(position);
                user.setMsgSolicitacoes(lista);

                user.save();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addGroupIntoUser(User user, String grupoSolicitado, String userKeySolicitante) {

        ArrayList<String> grupos = new ArrayList<String>();
        if (user.getGrupos() != null) {
            grupos.addAll(user.getGrupos());
            grupos.add(grupos.size(), Base64Decoder.encoderBase64(grupoSolicitado));
        } else
            grupos.add(0, Base64Decoder.encoderBase64(grupoSolicitado));
        user.setGrupos(grupos);
        user.setId(userKeySolicitante);
        user.save();
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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                Toast.makeText(PerfilActivity.this, "Em Produção", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
