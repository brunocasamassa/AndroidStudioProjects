package studio.brunocasamassa.ajudaquioficial;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import im.delight.android.location.SimpleLocation;
import studio.brunocasamassa.ajudaquioficial.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquioficial.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquioficial.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaquioficial.helper.Pedido;
import studio.brunocasamassa.ajudaquioficial.helper.PedidosTabAdapter;
import studio.brunocasamassa.ajudaquioficial.helper.Permissao;
import studio.brunocasamassa.ajudaquioficial.helper.Preferences;
import studio.brunocasamassa.ajudaquioficial.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaquioficial.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class PedidosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar, searchToolbar;
    private MenuItem action_search;
    private Menu search_menu;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private ArrayAdapter<Pedido> arrayAdapter;
    private ArrayAdapter<Pedido> arrayEscolhidosAdapter;
    private ArrayAdapter<Pedido> arrayMeusPedidosAdapter;
    private SlidingTabLayout slidingTabLayout;
    private int premium;
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());
    private SimpleLocation localizacao;
    private User usuario;
    private String[] permissoesNecessarias = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private String versionFromDb;
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private static NavigationDrawer navigator = new NavigationDrawer();
    private FloatingActionButton fab;
    private DatabaseReference dbUser;
    private DatabaseReference getLocalization;
    private ValueEventListener localizationListener;
    private String keyWord = null;
    private boolean trigger = true;
    private ArrayList<String> filter;
    private String currentTime = null;

    @Override
    protected void onStart() {
        super.onStart();
        //getLocalization.addValueEventListener(localizationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        // ...
        //localizacao.endUpdates();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //getLocalization.removeEventListener(localizationListener);
        //localizacao.endUpdates();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal_configuracoes);
        toolbar.setTitle(getResources().getString(R.string.menu_pedidos));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();

        ;
        getLocalization = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

        getLocalization.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println("ENTREI ");
                boolean permissao = Permissao.validaPermissoes(1, PedidosActivity.this, permissoesNecessarias);

                localizacao = new SimpleLocation(PedidosActivity.this);

                if (permissao) {
                    // if we can't access the location yet

                    if (!localizacao.hasLocationEnabled()) {
                        // ask the user to enable location access
                        SimpleLocation.openSettings(PedidosActivity.this);

                        latitude = localizacao.getLatitude();
                        longitude = localizacao.getLongitude();
                    }

                    latitude = localizacao.getLatitude();
                    longitude = localizacao.getLongitude();
                    System.out.println("LATITUDE> " + localizacao.getLatitude() + " LONGITUDE> " + longitude);

                    //*preferencias.saveMyCoordinates(latitude,longitude);*//*

                    user.setLatitude(localizacao.getLatitude());
                    user.setLongitude(localizacao.getLongitude());
                    user.save();
                    localizacao.endUpdates();

                    /*System.out.println("FIZ UPDATE " + gps.getLatitude());*/

                    if (user.getPedidosNotificationCount() != 0) {

                        Toast.makeText(getApplicationContext(), "Parabens, voce possui um pedido atendido", Toast.LENGTH_LONG).show();

                    }

                    final int premiumUser = user.getPremiumUser();
                    final int creditos = user.getCreditos();
                    final Intent intent = new Intent(PedidosActivity.this, CriaPedidoActivity.class);

                    final Intent intent2 = new Intent(PedidosActivity.this, CriaDoacaoActivity.class);
                    intent2.putExtra("latitude", latitude);
                    intent2.putExtra("longitude", longitude);

                    fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("PREMIUM PASSA" + premiumUser);
                            intent.putExtra("premium", premiumUser);
                            intent.putExtra("creditos",creditos);
                            intent.putExtra("latitude", localizacao.getLatitude());
                            intent.putExtra("longitude", localizacao.getLongitude());
                            startActivity(intent);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        listview_nomes = (ListView) findViewById(R.id.ListContatos);

        viewPager = (ViewPager)

                findViewById(R.id.vp_pagina);

        slidingTabLayout = (SlidingTabLayout)

                findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        PedidosTabAdapter pedidosTabAdapter = new PedidosTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pedidosTabAdapter);
        slidingTabLayout.setViewPager(viewPager);

        navigator.createDrawer(PedidosActivity.this, toolbar, 0);

       // checkVersion();
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

                Preferences preferences = new Preferences(PedidosActivity.this);
                preferences.clearSession();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(PedidosActivity.this, MainActivity.class));

                return true;

/*            case R.id.action_settings:
                startActivity(new Intent(PedidosActivity.this, ConfiguracoesActivity.class));
                Toast.makeText(getApplicationContext(), "Filtering", Toast.LENGTH_LONG).show();
                return true;*/

            case R.id.item_search:

                SearchManager searchManager = (SearchManager)
                        getSystemService(Context.SEARCH_SERVICE);

                SearchView searchView = (SearchView) item.getActionView();

                searchView.setSearchableInfo(searchManager.
                        getSearchableInfo(getComponentName()));
                searchView.setSubmitButtonEnabled(true);
                searchView.setOnQueryTextListener(this);

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        refresh();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        arrayAdapter = getArrayAdapter();
        arrayAdapter.getFilter().filter(newText);
        arrayAdapter.notifyDataSetChanged();

        arrayEscolhidosAdapter = getArrayEscolhidosAdapter();
        arrayEscolhidosAdapter.getFilter().filter(newText);
        arrayEscolhidosAdapter.notifyDataSetChanged();

        arrayMeusPedidosAdapter = getArrayMeusPedidosAdapter();
        if (arrayMeusPedidosAdapter != null) { //fica null quando inicia a activity
            arrayMeusPedidosAdapter.getFilter().filter(newText);
            arrayMeusPedidosAdapter.notifyDataSetChanged();
        }
        return true;

    }

    public ArrayAdapter<Pedido> getArrayAdapter() {
        return arrayAdapter;
    }

    public void setArrayAdapter(ArrayAdapter<Pedido> arrayAdapter) {
        this.arrayAdapter = arrayAdapter;
    }

    ;

    private void refresh() {
        Intent intent = new Intent(PedidosActivity.this, PedidosActivity.class);
        finish();
        startActivity(intent);
    }

    public ArrayAdapter<Pedido> getArrayEscolhidosAdapter() {
        return arrayEscolhidosAdapter;
    }

    public void setArrayEscolhidosAdapter(ArrayAdapter<Pedido> arrayEscolhidosAdapter) {
        this.arrayEscolhidosAdapter = arrayEscolhidosAdapter;
    }

    public ArrayAdapter<Pedido> getArrayMeusPedidosAdapter() {
        return arrayMeusPedidosAdapter;
    }

    public void setArrayMeusPedidosAdapter(ArrayAdapter<Pedido> arrayMeusPedidosAdapter) {
        this.arrayMeusPedidosAdapter = arrayMeusPedidosAdapter;
    }

    private boolean checkVersion() {
        //=----------------------------

        /*DatabaseReference version = FirebaseConfig.getFireBase().child("version");
        version.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double versionDouble = dataSnapshot.getValue(Double.class);
                String versionFromDb = versionDouble.toString();*/
        VersionChecker versionChecker = new VersionChecker();


        try {
            versionFromDb = versionChecker.execute().get().toString();
            System.out.println("version from web: "+versionFromDb);
        } catch (InterruptedException e) {
            System.out.println("version from web: ERROR "+e);
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("version from web: ERROR "+e);
            e.printStackTrace();
        }

        PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                String version = String.valueOf(pInfo.versionCode);
                System.out.println("version db " + versionFromDb + "  version app " + version);

                if (!version.equals(versionFromDb)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidosActivity.this);

                    alertDialog.setTitle("Atualização Disponivel");
                    alertDialog.setMessage("Esta versao esta desatualizada, deseja ir para a pagina de atualização?");
                    alertDialog.setCancelable(false);
                    alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    });

                    alertDialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Preferences preferences = new Preferences(PedidosActivity.this);
                            preferences.clearSession();
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            startActivity(new Intent(PedidosActivity.this, MainActivity.class));
                        }
                    }).create().show();
                }

/*            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
//------------
        return true;
    }
}