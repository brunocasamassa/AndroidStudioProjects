package studio.brunocasamassa.ajudaquiapp.adapters;

/**
 * Created by bruno on 09/05/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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


public class AllGroupsAdapter extends ArrayAdapter<Grupo> {

    private ArrayList<Grupo> grupos;
    private Context context;
    private StorageReference storage;

    public AllGroupsAdapter(Context c, ArrayList<Grupo> objects) {
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
            CardView cardView = (CardView) view.findViewById(R.id.cardview_group);
            if (position == 0) {

                cardView.setCardBackgroundColor(Color.parseColor("#1bb1b7"));
                    cardView.setCardElevation(100);
                nomeGrupo.setText("CABINE DA FARTURA");
                nomeGrupo.setTextColor(Color.WHITE);
                //imgGrupo.setBackgroundResource(R.drawable.chest_icon_web);
                qtdMmebros.setTextSize(16);
                qtdMmebros.setGravity(5);
                qtdMmebros.setTextColor(Color.RED);
                qtdMmebros.setText("Confira!");

                Bitmap bm = BitmapFactory.decodeResource(view.getResources(),
                        R.drawable.chest_icon_web);
                Bitmap resized = Bitmap.createScaledBitmap(bm, 200, 200, true);
                imgGrupo.setImageBitmap(resized);

            } else {
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
                        grupo.setGrupoImg(uri.toString());
                        Glide.with(getContext()).load(uri).override(68, 68).into(imgGrupo);
                        System.out.println("my groups lets seee2" + uri);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

            }
        }

        return view;

    }
}