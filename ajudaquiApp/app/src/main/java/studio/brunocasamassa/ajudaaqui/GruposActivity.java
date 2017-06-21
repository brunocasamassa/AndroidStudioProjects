package studio.brunocasamassa.ajudaaqui;

import  android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import studio.brunocasamassa.ajudaaqui.helper.GruposTabAdapter;
import studio.brunocasamassa.ajudaaqui.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaaqui.helper.Preferences;
import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;

import static studio.brunocasamassa.ajudaaqui.R.id.fab;
import static studio.brunocasamassa.ajudaaqui.R.id.view;

/**
 *
 * Created by bruno on 24/04/2017.
 *
 */

public class GruposActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private int posicao;
    private FloatingActionButton fab;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);


        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(getResources().getString(R.string.menu_grupos));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GruposActivity.this, CriaGrupoActivity.class);
                startActivity(intent);
            }});

        listview_nomes = (ListView) findViewById(R.id.ListContatos);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        GruposTabAdapter gruposTabAdapter = new GruposTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(gruposTabAdapter);

        slidingTabLayout.setViewPager(viewPager);


        NavigationDrawer navigator = new NavigationDrawer();
        navigator.createDrawer(GruposActivity.this, toolbar,5);

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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(GruposActivity.this, MainActivity.class));
                Preferences preferences = new Preferences(GruposActivity.this);
                preferences.clearSession();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(GruposActivity.this, ConfiguracoesActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



}
