package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaaqui.adapters.NotificacoesAdapter;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
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
    private MainActivity main;
    private static User user = new User();
    private CadastroActivity cdrst;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaNotificacoes;
    private ArrayList<Integer> badgesList = new ArrayList<>();

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

        final String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        storage = FirebaseConfig.getFirebaseStorage().child("userImages");

        DatabaseReference databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usuario = dataSnapshot.getValue(User.class);
                user.setMedalhas(badgesList); //usuario.getMedalhas()
                System.out.println("recebe usuario NAME: " + usuario.getName());
                System.out.println("recebe usuario DATA: " + dataSnapshot.getValue());
                
                if(usuario.getMsgSolicitacoes() != null){
                    ArrayList<String> msgSolicita = usuario.getMsgSolicitacoes();
                    for(int i=0; i< msgSolicita.size();i++) {
                        String msgCompleta = null;
                        String[] msg = msgSolicita.get(i).split(":");

                        //GRUPO: vamos ver: usuario: dGVzdGVAdGVzdGUuY29t :mensagem: osmanu"
                          for(String sentence : msg){
                                if (sentence.equals("GRUPO")){ //TODO alterar padrao de mensagem  (FULANO desej entrar no grupo TAL)
                                    String groupName = msg[1];
                                    msgCompleta = groupName;
                                    System.out.println("GROUP NAME CONCATENADO "+ groupName);
                                }

                                if(sentence.equals("USUARIO")){
                                    String userName = Base64Decoder.decoderBase64(msg[3]);
                                    msgCompleta = msgCompleta+": O usuario "+userName+ " deseja entrar neste grupo ";
                                }
                                if(sentence.equals("MENSAGEM")){
                                  String message = msg[5];
                                    msgCompleta = msgCompleta + " (MENSAGEM: "+message+" )";
                              }

                          }
                          System.out.println("CONCATENADO TOTAL "+msgCompleta);
                        listaNotificacoes.add(listaNotificacoes.size(), msgCompleta );
                        adapter = new NotificacoesAdapter(getApplicationContext(),listaNotificacoes);
                        notificacoes.setAdapter(adapter);
                    }
                }

                profileName.setText(usuario.getName());
                if (usuario.getPedidosAtendidos() != null) {
                    pedidosAtendidos.setText(""+usuario.getPedidosAtendidos().size());
                } else pedidosAtendidos.setText(""+0);
                if (usuario.getPedidosFeitos() != null) {
                    pedidosFeitos.setText(""+usuario.getPedidosFeitos().size());
                } else pedidosFeitos.setText(""+0);
                //pontosConquistados.setText(usuario.getPontos());
                if (dataSnapshot.child("profileImg").exists()) { //todo bug manual register or facebook register
                    Glide.with(PerfilActivity.this).load(usuario.getProfileImg()).into(profileImg);
                } else{ storage.child(userKey+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(PerfilActivity.this).load(uri).override(68,68).into(profileImg);
                        System.out.println("my groups lets seee2 "+ uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });}
                usuario.setId(userKey);
                usuario.save();
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(user.getMedalhas() != null){

            System.out.println("laço de imagens");
            for (int i = 0; i < 10; i++) {
                ImageView imageView = new ImageView(PerfilActivity.this);
                imageView.setId(i);
                imageView.setPadding(0,0,0,0);
                imageView.setPaddingRelative(View.TEXT_ALIGNMENT_TEXT_START, View.SCROLL_INDICATOR_TOP,0,0);
                imageView.isOpaque();
                if(i==0) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge_back));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }if(i==1) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge2));
                    imageView.setScaleX((float) 0.5);
                        imageView.setScaleY((float) 1);

                }if(i==2) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge3));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }if(i==3) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge4));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }if(i==4) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.layout.model_group));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);


                }if(i==5) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge6));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }if(i==6) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge7));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);


                }if(i==7) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge3));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);


                }if(i==8) {
                    imageView.setImageBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.badge4));
                    imageView.setScaleX((float) 0.5);
                    imageView.setScaleY((float) 1);

                }if(i==9) {
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
            }}

        toolbar.setTitle(getResources().getString(R.string.menu_perfil));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        NavigationDrawer navigator = new NavigationDrawer();

        navigator.createDrawer(PerfilActivity.this, toolbar, 7);

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
