package studio.brunocasamassa.superchat.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.superchat.R;
import studio.brunocasamassa.superchat.adapter.TabAdapter;
import studio.brunocasamassa.superchat.fragments.ContatosFragment;
import studio.brunocasamassa.superchat.helper.Base64Decoder;
import studio.brunocasamassa.superchat.helper.Contato;
import studio.brunocasamassa.superchat.helper.FirebaseConfig;
import studio.brunocasamassa.superchat.helper.Preferences;
import studio.brunocasamassa.superchat.helper.SlidingTabLayout;

/**
 * Created by bruno on 08/03/2017.
 */

public class HelloActivity extends AppCompatActivity {


    private ListView listview_nomes  ;
    private ArrayAdapter<String> adapter_nomes;
    private ArrayList<String> arraylist_nomes = new ArrayList<>();
    private Button logout;
    private FirebaseAuth autenticator;
    private Toolbar toolbar;
    private DatabaseReference firebaseDatabase;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private static String idContact;
    private String contactName = "noise";
    private Base64Decoder decoder;


    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        autenticator = FirebaseConfig.getFirebaseAuthentication();

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(getResources().getString(R.string.app_title));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);


        listview_nomes = (ListView) findViewById(R.id.ListContatos);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);
        System.out.println("DISPLAY CARAIO: "+listview_nomes);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccent));

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

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
                logoutUser();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.item_add:
                addUser();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addUser() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HelloActivity.this);

        alertDialog.setTitle("Novo Contato");
        alertDialog.setMessage("E-mail do usuario: ");
        alertDialog.setCancelable(false);


        final EditText editText = new EditText(HelloActivity.this);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final String emailContact = editText.getText().toString();

                //Validate e-mail contact
                if (emailContact.isEmpty()) {
                    Toast.makeText(HelloActivity.this, "Preencha o e-mail ", Toast.LENGTH_LONG).show();
                } else {
                    idContact = decoder.encoderBase64(emailContact);

                    firebaseDatabase = FirebaseConfig.getFireBase();

                    firebaseDatabase.child("usuarios").child(idContact);

                    firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            System.out.println("dataSnapshot: " + dataSnapshot);

                            if (dataSnapshot.child("usuarios").child(idContact).exists()) {

                                Toast.makeText(HelloActivity.this, "user existente", Toast.LENGTH_LONG).show();
                                //Recuperar dados do contato
                                Iterable<DataSnapshot> d = dataSnapshot.child("usuarios").child(idContact).getChildren();
                                System.out.println(d);

                                //Recuperar identificador
                                Preferences preferences = new Preferences(HelloActivity.this);
                                String idUser = preferences.getIdentifier();
                                System.out.println("idUser: " + autenticator.toString() + " idContact: " + idContact);

                                firebaseDatabase = FirebaseConfig.getFireBase().child("contatos").child(idUser).child(idContact);

                                Contato contato = new Contato();
                                contato.setidUser(idContact);
                                contato.setEmail(emailContact);
                                contato.setNome(contactName);




                                firebaseDatabase.setValue(contato);

                                arraylist_nomes.add(emailContact);

                                adapter_nomes = new ArrayAdapter<String>(
                                        getApplication().getBaseContext(),
                                        android.R.layout.simple_list_item_2,
                                        android.R.id.text2,
                                        arraylist_nomes);



                                System.out.println("Adapter nomes CARAIO 2 :  " + adapter_nomes);
                                System.out.println("listview CARAIO 2:  " + listview_nomes);

                                ContatosFragment contact = new ContatosFragment();
                                contact.insertContact(adapter_nomes);
                                System.out.println("contatinho CARAIO: "+ contact);

                            } else {

                                Toast.makeText(HelloActivity.this, "user NAAAAAO existente", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }

        });

        alertDialog.setNegativeButton("Cancelar:", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    private void logoutUser() {
        autenticator.signOut();
        Intent intent = new Intent(HelloActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public static String getName() {
        if (idContact == null) {
            idContact = "semID";
        }

        return idContact;

    }


    public String insertContact(String nomeContato) {


        contactName = nomeContato;

        //arraylist_nomes.add(contactName);

        System.out.println("nomes_array: " + arraylist_nomes);


        return contactName;
    }
}