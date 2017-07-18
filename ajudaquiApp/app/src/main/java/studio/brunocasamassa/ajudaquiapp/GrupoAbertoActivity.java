package studio.brunocasamassa.ajudaquiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Grupo;
import studio.brunocasamassa.ajudaquiapp.helper.GrupoAbertoTabAdapter;
import studio.brunocasamassa.ajudaquiapp.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaquiapp.helper.User;

/**
 *
 * Created by bruno on 24/04/2017.
 *
 */

public class GrupoAbertoActivity extends AppCompatActivity {
    private DatabaseReference firebase;
    private DatabaseReference dbUser =FirebaseConfig.getFireBase().child("usuarios");
    private Toolbar toolbar;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private CircleImageView groupImage;
    private int posicao;
    private static Grupo grupo = new Grupo();
    private StorageReference storage;
    private FloatingActionMenu menuFAB;
    private com.github.clans.fab.FloatingActionButton donationFAB;
    private com.github.clans.fab.FloatingActionButton pedidoFAB;
    private int premium;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_aberto);


        menuFAB = (FloatingActionMenu) findViewById(R.id.fab_menu);

        donationFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.doacao_fab);
        pedidoFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.pedido_fab);

        Bundle extra = getIntent().getExtras();

        final String groupKey = Base64Decoder.encoderBase64(extra.getString("nome").toString());
        System.out.println("group Name bundleded " + extra.getString("nome").toString());

        premium = extra.getInt("premium");
        String uri = extra.getString("uri");
        String titulo = extra.getString("nome").toString();
        firebase = FirebaseConfig.getFireBase().child("grupos").child(groupKey);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Grupo aux = dataSnapshot.getValue(Grupo.class);
                grupo.setNome(aux.getNome());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupImage = (CircleImageView) findViewById(R.id.circleImageView);

        storage = FirebaseConfig.getFirebaseStorage().child("groupImages");

        storage.child(grupo.getNome() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(GrupoAbertoActivity.this).load(uri).override(68, 68).into(groupImage);


                System.out.println("group image chat " + uri);
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(titulo);

        Glide.with(GrupoAbertoActivity.this).load(uri).override(68, 68).into(groupImage);
        System.out.println("nome grupo " + grupo.getNome());
        System.out.println("uri grupo " + uri);
        dbUser.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                        final User user = dataSnapshot.getValue(User.class);

                donationFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GrupoAbertoActivity.this, CriaDoacaoActivity.class);
                        intent.putExtra("groupKey", groupKey);
                        intent.putExtra("latitude", user.getLatitude());
                        intent.putExtra("longitude", user.getLongitude());
                        startActivity(intent);
                    }
                });


                pedidoFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GrupoAbertoActivity.this, CriaPedidoActivity.class);
                        intent.putExtra("premium", premium);
                        intent.putExtra("latitude", user.getLatitude());
                        intent.putExtra("longitude", user.getLongitude());
                        startActivity(intent);
                    }
                });

                toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                /*startActivity(new Intent(GrupoAbertoActivity.this, GruposActivity.class));*/
                        finish();
                    }
                });

                //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

                listview_nomes = (ListView) findViewById(R.id.ListContatos);
                viewPager = (ViewPager) findViewById(R.id.vp_pagina);

                slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
                slidingTabLayout.setDistributeEvenly(true);
                slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(GrupoAbertoActivity.this, R.color.colorAccent));

                GrupoAbertoTabAdapter grupoAbertoTabAdapter = new GrupoAbertoTabAdapter(getSupportFragmentManager());
                viewPager.setAdapter(grupoAbertoTabAdapter);

                slidingTabLayout.setViewPager(viewPager);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(GrupoAbertoActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



}
