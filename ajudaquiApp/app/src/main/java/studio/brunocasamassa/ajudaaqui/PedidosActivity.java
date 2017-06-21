package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaaqui.helper.PedidosTabAdapter;
import studio.brunocasamassa.ajudaaqui.helper.Preferences;
import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class PedidosActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private int premium;
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());

    private User usuario;

    private static NavigationDrawer navigator = new NavigationDrawer();
    private FloatingActionButton fab;
    private DatabaseReference dbUser;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(getResources().getString(R.string.menu_pedidos));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        dbUser = FirebaseConfig.getFireBase().child("usuarios");

        dbUser.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getPedidosNotificationCount() != 0){
                    Toast.makeText(getApplicationContext(),"Parabens, voce possui um pedido atendido", Toast.LENGTH_LONG).show();

                }}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*
        loginResult = MainActivity.lr;
        Profile profile = Profile.getCurrentProfile();
        System.out.println("PROFILE: "+ profile);

        String userid = loginResult.getAccessToken().getUserId();
        String name = message(profile);
        user.setName(name);*/

        listview_nomes = (ListView) findViewById(R.id.ListContatos);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        PedidosTabAdapter pedidosTabAdapter = new PedidosTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pedidosTabAdapter);
        slidingTabLayout.setViewPager(viewPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo por put extra premium aqui
                Intent intent = new Intent(PedidosActivity.this, CriaPedidoActivity.class);
                startActivity(intent);
            }
        });

        navigator.createDrawer(PedidosActivity.this, toolbar ,0);

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
                FirebaseConfig.getFirebaseAuthentication().signOut();
                startActivity(new Intent(PedidosActivity.this, MainActivity.class));
                Preferences preferences = new Preferences(PedidosActivity.this);
                preferences.clearSession();
                return true;
            case R.id.action_settings:

                startActivity(new Intent(PedidosActivity.this, ConfiguracoesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}