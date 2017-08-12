package studio.brunocasamassa.ajudaquiapp.adapters;

/**
 * Created by bruno on 09/05/2017.
 */

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaquiapp.R;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Grupo;


public class MyGroupsAdapter extends ArrayAdapter<Grupo> {

    private ArrayList<Grupo> grupos;
    private Context context;
    private StorageReference storage;

    public MyGroupsAdapter(Context c, ArrayList<Grupo> objects) {
        super(c, 0, objects);
        this.grupos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        storage = FirebaseConfig.getFirebaseStorage().child("groupImages");
        View view = null;

        // Verifica se a lista está vazia
        if (grupos != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.model_group, parent, false);

            // recupera elemento para exibição
            ImageView lock = (ImageView) view.findViewById(R.id.lock_icon);
            TextView nomeGrupo = (TextView) view.findViewById(R.id.nomeGrupo);
            TextView qtdMmebros = (TextView) view.findViewById(R.id.qtd_membros);
            final CircleImageView imgGrupo = (CircleImageView) view.findViewById(R.id.groupImg);

            final Grupo grupo = grupos.get(position);
            nomeGrupo.setText(grupo.getNome());
            qtdMmebros.setText("Membros: " + String.valueOf(grupo.getQtdMembros()));
            if (!grupo.isOpened()) {
                lock.setBackgroundResource(R.drawable.ic_lock);
            }
            // DOWNLOAD GROUP IMG FROM STORAGE
            storage.child(grupo.getId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        Glide.with(getContext()).load(uri).override(68, 68).into(imgGrupo);
                        grupo.setGrupoImg(uri.toString());
                    } catch (Exception e) {
                        imgGrupo.setImageURI(uri);
                        System.out.println("EXCEPTION PedidosAdapter " + e);
                    }

                    System.out.println("my groups lets seee2" + uri);

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