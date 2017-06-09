package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 09/05/2017.
 */

public class CriaGrupoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private int PICK_IMAGE_REQUEST = 1;
    private CircleImageView img;
    private EditText groupName;
    private EditText descricao;
    private DatabaseReference databaseGroups;
    private DatabaseReference databaseUsers;
    private Grupo grupo;
    private String groupId;
    private Button createButton;
    private static User usuario = new User();
    private StorageReference storage;
    private boolean validatedName = true;
    private ArrayList<String> adms = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_grupo);

        toolbar = (Toolbar) findViewById(R.id.toolbar_create_group);
        toolbar.setTitle("Criar Grupo");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

        databaseGroups = FirebaseConfig.getFireBase().child("grupos");
        storage = FirebaseConfig.getFirebaseStorage().child("groupImages");

        groupName = (EditText) findViewById(R.id.create_group_name);
        descricao = (EditText) findViewById(R.id.create_group_description);
        createButton = (Button) findViewById(R.id.create_group_button);
        img = (CircleImageView) findViewById(R.id.import_group_img);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
        // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groupName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Insira um nome para o grupo", Toast.LENGTH_LONG).show();
                    return;
                } else if (descricao.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Insira uma descricao para o grupo", Toast.LENGTH_LONG).show();
                    return;
                } else if (img.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Insira uma imagem para o grupo", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    grupo = new Grupo();
                    groupId = Base64Decoder.encoderBase64(groupName.getText().toString());
                    grupo.setNome(groupName.getText().toString());
                    grupo.setDescricao(descricao.getText().toString());
                    grupo.setId(groupId);


                    FirebaseAuth autenticacao = FirebaseConfig.getFirebaseAuthentication();

                    String userKey = Base64Decoder.encoderBase64(autenticacao.getCurrentUser().getEmail());
                    databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
                    System.out.println("AUTH caraio" + userKey);

                    adms.add(0, userKey);
                    grupo.setIdMembros(adms);
                    grupo.setQtdMembros(1);
                    grupo.setIdAdms(adms);

                    //EVENTO DE LEITURA de IMAGEM
                    databaseGroups.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //verifica ID dos grupos e valida ID
                            System.out.println("datasnapshot caraio BRUTO: " + dataSnapshot);
                            System.out.println("ID MEU caraio GRUPO " + grupo.getId());
                            if (dataSnapshot.child(groupId).exists()) {
                                Toast.makeText(getApplicationContext(), "Nome de grupo Já utilizado", Toast.LENGTH_LONG).show();
                                System.out.println("caraio validador> " + validatedName);
                            } else {
                                Toast.makeText(getApplicationContext(), "Grupo Criado com sucesso", Toast.LENGTH_LONG).show();
                                StorageReference imgRef = storage.child(groupName.getText() + ".jpg");
                                //download img source
                                img.setDrawingCacheEnabled(true);
                                img.buildDrawingCache();
                                Bitmap bitmap = img.getDrawingCache();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask = imgRef.putBytes(data);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                                        System.out.println("huehuebrjava "+ downloadUrl);
                                    }
                                });

                                grupo.save();
                                groupSaveIntoUser(true);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });
    }

    private void groupSaveIntoUser(boolean b) {
        if (b) {
            databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    ArrayList<String> gruposUserLogado = new ArrayList<String>();
                    if (user.getGrupos() != null) {  //populate groups
                        gruposUserLogado.addAll(user.getGrupos());
                        System.out.println("grupos user logado qtd " + gruposUserLogado.size());
                        gruposUserLogado.add(gruposUserLogado.size(), grupo.getId());
                        System.out.println("grupos caraio " + gruposUserLogado);
                    } else {
                        gruposUserLogado.add(0, grupo.getId());
                    }
                    System.out.println("caraio Email user " + user.getEmail() + "nOME USER: " + user.getName());
                    user.setGrupos(gruposUserLogado);
                    System.out.println("refs2 usuario " + user.getGrupos());
                    user.setId(Base64Decoder.encoderBase64(user.getEmail()));
                    System.out.println("grupos do usuario que irão ser salvos" + user.getGrupos());
                    user.save();
                    finish();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

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

                img.setImageBitmap(bitmap);
                } catch (IOException e) {
                e.printStackTrace();
                Log.e("error in get image ", e.toString());
            }
        }
    }
}
