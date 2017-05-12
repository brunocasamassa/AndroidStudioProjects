package studio.brunocasamassa.ajudaaqui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaaqui.fragments.GruposTodosgruposFragment;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;

/**
 * Created by bruno on 09/05/2017.
 */

public class CriaGrupoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView img;
    private EditText groupName;
    private EditText descricao;
    private DatabaseReference database;
    private Grupo grupo;
    private String groupId;
    private Button createButton;
    private StorageReference storage;
    private boolean validatedName = true;
    private ArrayList<String> adms = new ArrayList<>();



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_grupo);

        toolbar = (Toolbar) findViewById(R.id.toolbar_create_group);
        toolbar.setTitle("Criar Grupo");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        setSupportActionBar(toolbar);

        database = FirebaseConfig.getFireBase().child("grupos");
        storage = FirebaseConfig.getFirebaseStorage().child("groupImages");

        groupName = (EditText) findViewById(R.id.create_group_name);
        descricao = (EditText) findViewById(R.id.create_group_description);
        img = (CircleImageView) findViewById(R.id.import_group_img);
        createButton = (Button) findViewById(R.id.create_group_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grupo = new Grupo();
                groupId = Base64Decoder.encoderBase64(groupName.getText().toString());
                grupo.setNome(groupName.getText().toString());
                grupo.setDescricao(descricao.getText().toString());
                grupo.setId(groupId);

                FirebaseAuth autenticacao = FirebaseConfig.getFirebaseAuthentication();
                String uId = autenticacao.getCurrentUser().getUid();
                System.out.println("AUTH "+ uId);
                adms.add(0,uId);
                grupo.setQtdMembros(1);
                grupo.setIdAdms(adms);

                //EVENTO DE LEITURA

                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //verifica ID dos grupos e valida ID
                            System.out.println("datasnapshot BRUTO: "+ dataSnapshot);
                            System.out.println("ID MEU GRUPO "+grupo.getId());
                            if (dataSnapshot.child("grupos").child(groupId).exists()) {   //TODO CORRIGIR BUG VALIDAÇÃO
                                Toast.makeText(getApplicationContext(), "Nome de grupo Já utilizado", Toast.LENGTH_LONG).show();
                                System.out.println("validador> "+ validatedName);
                            }

                            else  {
                            Toast.makeText(getApplicationContext(), "Grupo Criado com sucesso", Toast.LENGTH_LONG).show();
                            StorageReference imgRef = storage.child(groupName.getText()+".jpg");
                            img.setDrawingCacheEnabled(true);
                            img.buildDrawingCache();
                            Bitmap bitmap = img.getDrawingCache();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();

                            UploadTask uploadTask = imgRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                }
                            });
                            grupo.save();
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
