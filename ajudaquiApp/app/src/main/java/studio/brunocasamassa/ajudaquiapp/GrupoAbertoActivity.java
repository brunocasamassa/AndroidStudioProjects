package studio.brunocasamassa.ajudaquiapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Grupo;
import studio.brunocasamassa.ajudaquiapp.helper.GrupoAbertoTabAdapter;
import studio.brunocasamassa.ajudaquiapp.helper.Pedido;
import studio.brunocasamassa.ajudaquiapp.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaquiapp.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class GrupoAbertoActivity extends AppCompatActivity {

    private DatabaseReference firebase;
    private int PICK_IMAGE_REQUEST = 1;
    private boolean photoWasChanged = false;
    private DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios");
    private Toolbar toolbar;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private CircleImageView groupImage;
    private int posicao;
    private static Grupo grupo = new Grupo();
    private StorageReference storage;
    private ImageButton editName;
    private FloatingActionMenu menuFAB;
    private com.github.clans.fab.FloatingActionButton donationFAB;
    private com.github.clans.fab.FloatingActionButton pedidoFAB;
    private int premium;
    private DatabaseReference dbPedido = FirebaseConfig.getFireBase().child("Pedidos");
    private DatabaseReference dbGroup = FirebaseConfig.getFireBase().child("grupos");
    private ArrayList<String> listaAdmins;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_aberto);


        menuFAB = (FloatingActionMenu) findViewById(R.id.fab_menu);
        editName = (ImageButton) findViewById(R.id.edit_groupName_button);
        //donationFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.doacao_fab);
        pedidoFAB = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.pedido_fab);

        Bundle extra = getIntent().getExtras();

        final String groupKey = extra.getString("groupId").toString();
        System.out.println("group Name bundleded " + extra.getString("nome").toString());

        listaAdmins = new ArrayList<>();
        listaAdmins.addAll(extra.getStringArrayList("adminsList"));
        premium = extra.getInt("premium");
        String uri = extra.getString("uri");
        final String titulo = extra.getString("nome").toString();
        firebase = FirebaseConfig.getFireBase().child("grupos").child(groupKey);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal_grupoaberto);
        toolbar.setTitle(titulo);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*startActivity(new Intent(GrupoAbertoActivity.this, GruposActivity.class));*/
                finish();
            }
        });


        if (listaAdmins.contains(userKey)) {
            editName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder edit = new AlertDialog.Builder(GrupoAbertoActivity.this);
                    edit.setTitle("Alterar nome do Grupo");
                    final EditText editText = new EditText(GrupoAbertoActivity.this);
                    edit.setView(editText);
                    edit.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    edit.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbGroup.child(grupo.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Grupo group = dataSnapshot.getValue(Grupo.class);
                                    group.setNome(editText.getText().toString());
                                    group.save();
                                    Toasty.custom(getApplicationContext(), "Nome Alterado com sucesso", getDrawable(R.drawable.logo),
                                            Color.argb(255, 27, 77, 183), Toast.LENGTH_SHORT, true, true).show();
                                    changeNameReferences(editText.getText().toString());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }).create().show();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Apenas Administradores podem alterar o nome do grupo", Toast.LENGTH_SHORT).show();
        }

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Grupo aux = dataSnapshot.getValue(Grupo.class);
                grupo.setNome(aux.getNome());
                grupo.setId(aux.getId());
                grupo.setIdMembros(aux.getIdMembros());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        groupImage = (CircleImageView) findViewById(R.id.circleImageView);
        groupImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listaAdmins.contains(userKey)) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(GrupoAbertoActivity.this);
                    alertDialog.setTitle("Alterar Imagem do grupo");

                    alertDialog.setMessage("Deseja alterar imagem do grupo");

                    alertDialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });

                    alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            changeGroupImage();

                        }
                    }).create().show();


                } else Toast.makeText(getApplicationContext(),"Apenas administradores podem alterar a imagem do grupo", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        storage = FirebaseConfig.getFirebaseStorage().child("groupImages");

        storage.child(grupo.getId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(GrupoAbertoActivity.this).load(uri).override(68, 68).into(groupImage);


                System.out.println("group image chat " + uri);
            }
        });


        System.out.println("titulo " + titulo);

        Glide.with(GrupoAbertoActivity.this).load(uri).override(68, 68).into(groupImage);
        System.out.println("nome grupo " + grupo.getNome());
        System.out.println("uri grupo " + uri);
        dbUser.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
               /* donationFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GrupoAbertoActivity.this, CriaDoacaoActivity.class);
                        intent.putExtra("groupKey", groupKey);
                        intent.putExtra("latitude", user.getLatitude());
                        intent.putExtra("longitude", user.getLongitude());
                        startActivity(intent);
                    }
                });
*/
                pedidoFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GrupoAbertoActivity.this, CriaPedidoActivity.class);
                        intent.putExtra("premium", premium);
                        intent.putExtra("groupName", titulo);
                        intent.putExtra("groupId", groupKey);
                        intent.putExtra("latitude", user.getLatitude());
                        intent.putExtra("longitude", user.getLongitude());
                        startActivity(intent);
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

    private void changeGroupImage() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        photoWasChanged = true;
        uploadImages();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d("image", String.valueOf(bitmap));

                groupImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("error in get image ", e.toString());
            }
        }
    }

    private void uploadImages() {
        StorageReference imgRef = storage.child(grupo.getId() + ".jpg");
        System.out.println("lei lei " + userKey);
        //download img source
        groupImage.setDrawingCacheEnabled(true);
        groupImage.buildDrawingCache();
        Bitmap bitmap = groupImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                System.out.println("huehuebrjava " + downloadUrl);
            }
        });

    }

    private void changeNameReferences(final String newName) {
        final ArrayList<String> groupMembers = new ArrayList<>();
        groupMembers.addAll(grupo.getIdMembros());
        groupMembers.addAll(grupo.getIdAdms());
        for (int i = 0; i < groupMembers.size(); i++) {
            dbUser.child(groupMembers.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    ArrayList<String> userGroups = new ArrayList<String>();
                    userGroups.addAll(user.getGrupos());
                    if (userGroups.contains(grupo.getId())) {
                        userGroups.remove(grupo.getId());
                        userGroups.add(Base64Decoder.encoderBase64(newName));
                    }
                    user.setGrupos(userGroups);
                    user.save();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        dbPedido.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Pedido pedido = post.getValue(Pedido.class);
                    if (pedido.getGrupo().equals(grupo.getNome())) {
                        pedido.setGrupo(newName);
                    }
                    pedido.save();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
