package studio.brunocasamassa.ajudaaqui.adapters;

/**
 * Created by bruno on 09/05/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;

import studio.brunocasamassa.ajudaaqui.helper.Grupo;

public class AllGroupsAdapter extends ArrayAdapter<Grupo> {

    private ArrayList<Grupo> grupos;
    private Context context;
    private static StorageReference storage;

    public AllGroupsAdapter(Context c, ArrayList<Grupo> objects) {
        super(c, 0, objects);
        this.grupos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //FirebaseOptions opts = FirebaseApp.getInstance().getOptions();
        //Log.i("tag", "Bucket = " + opts.getStorageBucket());
        storage = FirebaseConfig.getFirebaseStorage().child("groupImages");
        View view = null;

        // Verifica se a lista está vazia
        if( grupos != null ){

                // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.model_group, parent, false);

            // recupera elemento para exibição
            TextView nomeGrupo = (TextView) view.findViewById(R.id.nomeGrupo);
            TextView qtdMmebros = (TextView) view.findViewById(R.id.qtd_membros);
            final CircleImageView imgGrupo = (CircleImageView) view.findViewById(R.id.groupImg);

            Grupo grupo = grupos.get( position );
            nomeGrupo.setText( grupo.getNome());
            qtdMmebros.setText( "Membros: "+String.valueOf(grupo.getQtdMembros()));
            // DOWNLOAD GROUP IMG FROM STORAGE


            storage.child(grupo.getNome()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(getContext()).load(uri).override(68,68).into(imgGrupo);
                    System.out.println("my groups lets seee2 "+ uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });



        }

        return view;

    }


}