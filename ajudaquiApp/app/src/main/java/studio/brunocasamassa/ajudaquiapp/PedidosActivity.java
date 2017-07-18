package studio.brunocasamassa.ajudaquiapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;

import im.delight.android.location.SimpleLocation;
import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaquiapp.helper.PedidosTabAdapter;
import studio.brunocasamassa.ajudaquiapp.helper.Permissao;
import studio.brunocasamassa.ajudaquiapp.helper.Preferences;
import studio.brunocasamassa.ajudaquiapp.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaquiapp.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class PedidosActivity extends AppCompatActivity {
    private Toolbar toolbar , searchToolbar;
    private MenuItem action_search;
    private Menu search_menu;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private int premium;
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());
    private SimpleLocation localizacao;
    private User usuario;
    private String[] permissoesNecessarias = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION,
    };

    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private static NavigationDrawer navigator = new NavigationDrawer();
    private FloatingActionButton fab;
    private DatabaseReference dbUser;
    private DatabaseReference getLocalization;
    private ValueEventListener localizationListener;
    private String keyWord = null;
    private boolean trigger = true;


    @Override
    protected void onStart() {
        super.onStart();
        //getLocalization.addValueEventListener(localizationListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //localizacao.beginUpdates();
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

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(getResources().getString(R.string.menu_pedidos));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        setSearchtoolbar();

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
                DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios").child(Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                dbUser.child("notificationToken").removeValue();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(PedidosActivity.this, MainActivity.class));
                return true;

            case R.id.action_settings:
                startActivity(new Intent(PedidosActivity.this, ConfiguracoesActivity.class));
                return true;

            case R.id.item_search:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchable ,1,true,true);
                else
                    searchToolbar.setVisibility(View.VISIBLE);

                action_search.expandActionView();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void setSearchtoolbar() {

        searchToolbar = (Toolbar) findViewById(R.id.searchable);
        if (searchToolbar!= null) {
            searchToolbar.inflateMenu(R.menu.menu_search);
            search_menu=searchToolbar.getMenu();

            searchToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchable,1,true,false);
                    else
                        searchToolbar.setVisibility(View.GONE);
                }
            });

            action_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(action_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchable,1,true,false);
                    }
                    else
                        searchToolbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });

            initSearchView();


        } else
            Log.d("toolbar", "setSearchtollbar: NULL");
    }

    private void initSearchView() {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        // Enable/Disable Submit button in the keyboard

        System.out.println("searchview 0"+ searchView);
        searchView.setSubmitButtonEnabled(false);

        // Change search close button image

        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_cross);


        // set hint and the text colors

        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));


        // set the cursor

        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, Color.RED); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
                Log.i("query", "" + query);

            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow)
    {
        final View myView = findViewById(viewID);

        System.out.println("minha view "+viewID + myView);

        int width=myView.getWidth();

        if(posFromRight>0)
            width-=(posFromRight*getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))-(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)/ 2);
        if(containsOverflow)
            width-=getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx=width;
        int cy=myView.getHeight()/2;

        Animator anim;
        if(isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0,(float)width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float)width, 0);

        anim.setDuration((long)220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(!isShow)
                {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if(isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();


    }

}