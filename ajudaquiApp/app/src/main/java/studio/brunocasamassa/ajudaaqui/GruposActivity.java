package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;

import com.facebook.login.LoginManager;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.GruposTabAdapter;
import studio.brunocasamassa.ajudaaqui.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaaqui.helper.Preferences;
import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;

/**
 * Created by bruno on 24/04/2017.
 */

public class GruposActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private int posicao;
    private com.github.clans.fab.FloatingActionButton fab;
    private Button donation;
    private FloatingActionMenu fabMenu;
    private com.github.clans.fab.FloatingActionButton fab2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_grupos);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(getResources().getString(R.string.menu_grupos));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        fabMenu = (FloatingActionMenu) findViewById(R.id.fab_open_menu);
        fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab2);

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GruposActivity.this, CabineFarturaActivity.class);
                startActivity(intent);
            }
        });

        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GruposActivity.this, CriaGrupoActivity.class);
                startActivity(intent);
            }
        });

        listview_nomes = (ListView) findViewById(R.id.ListContatos);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        GruposTabAdapter gruposTabAdapter = new GruposTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(gruposTabAdapter);

        slidingTabLayout.setViewPager(viewPager);

        NavigationDrawer navigator = new NavigationDrawer();
        navigator.createDrawer(GruposActivity.this, toolbar, 5);

        //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

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

                Preferences preferences = new Preferences(GruposActivity.this);
                preferences.clearSession();
                DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios").child(Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                dbUser.child("notificationToken").removeValue();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(GruposActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(GruposActivity.this, ConfiguracoesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
