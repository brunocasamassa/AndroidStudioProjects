package studio.brunocasamassa.ajudaaqui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class ConfiguracoesActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    private CircleImageView circleImageView;
    private ImageView cam_config;
    private EditText name;
    private Button salvar;
    private StorageReference storage = FirebaseConfig.getFirebaseStorage();
    private DatabaseReference dbUser = FirebaseConfig.getFireBase();
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());
    private boolean photoWasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        circleImageView = (CircleImageView) findViewById(R.id.photo_config);
        cam_config = (ImageView) findViewById(R.id.cam_config);
        name = (EditText) findViewById(R.id.name_config);
        salvar = (Button) findViewById(R.id.save_config);

        dbUser.child("usuarios").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                if (dataSnapshot.child("profileImg").exists()) { //todo bug manual register or facebook register
                    Glide.with(ConfiguracoesActivity.this).load(user.getProfileImg()).into(circleImageView);
                } else {
                    storage.child(userKey + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(ConfiguracoesActivity.this).load(uri).override(68, 68).into(circleImageView);
                            System.out.println("my groups lets seee2 " + uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        cam_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                photoWasChanged = true;
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConfiguracoesActivity.this);

                alertDialog.setTitle("Alterar Dados Usuario");
                alertDialog.setMessage("Deseja realmente alterar seus dados? A alteração é irreversivel");
                alertDialog.setCancelable(false);


                alertDialog.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });
                alertDialog.setPositiveButton("Prosseguir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbUser.child("usuarios").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if (user.getProfileImg() != null) {
                                    user.setProfileImg(null);
                                }
                                user.setId(userKey);
                                if (!name.getText().equals(user.getName())) {
                                    user.setName(name.getText().toString());
                                    user.save();
                                }else System.out.println("username nao mudad0");

                                if (photoWasChanged) {
                                    uploadImages();
                                    System.out.println("photo mudada");
                                } else System.out.println("photo nao mudada");
                                Toast.makeText(getApplicationContext(), "Alteraçoes Salvas", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                }).create().show();

            }


        });
    }

    ;


    private void uploadImages() {
        StorageReference imgRef = storage.child("userImages").child(userKey + ".jpg");
        System.out.println("lei lei " + userKey);
        //download img source
        circleImageView.setDrawingCacheEnabled(true);
        circleImageView.buildDrawingCache();
        Bitmap bitmap = circleImageView.getDrawingCache();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d("image", String.valueOf(bitmap));

                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("error in get image ", e.toString());
            }
        }
    }
}


