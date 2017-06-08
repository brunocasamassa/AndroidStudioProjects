package studio.brunocasamassa.ajudaaqui.adapters;

/**
 * Created by bruno on 09/05/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.FacebookGraphResponseException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareOpenGraphObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;
import studio.brunocasamassa.ajudaaqui.LoginActivity;
import studio.brunocasamassa.ajudaaqui.MainActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Pedido;
import studio.brunocasamassa.ajudaaqui.helper.Preferences;

public class PedidosAdapter extends ArrayAdapter<Pedido> {

    private ArrayList<Pedido> pedidos;
    private Context context;
    private StorageReference storage;
    private Preferences preferences;
    private String facebookPhoto;


    private MainActivity mainActivity = new MainActivity();

    public PedidosAdapter(Context c, ArrayList<Pedido> objects) {
        super(c, 0, objects);
        this.pedidos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        View view = null;

        // Verifica se a lista está vazia
        if (pedidos != null) {


            if(mainActivity.facebookImg != null) {
                facebookPhoto = mainActivity.facebookImg;
            }
            System.out.println("facebook img pedido: "+ facebookPhoto);
            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.model_pedido, parent, false);

            // recupera elemento para exibição
            TextView nomePedido = (TextView) view.findViewById(R.id.nomePedido);
            TextView descricao = (TextView) view.findViewById(R.id.descricao_pedido);
            TagGroup tagsCategoria = (TagGroup) view.findViewById(R.id.tagPedidos);
            final CircleImageView pedidoImg = (CircleImageView) view.findViewById(R.id.imagePedido);

            final Pedido pedido = pedidos.get(position);/*
            if (pedido.getGrupo() != null) {
                storage = FirebaseConfig.getFirebaseStorage().child("groupImages");
            } else if (pedido.getGrupo() == null) {
                storage = FirebaseConfig.getFirebaseStorage().child("userImages");
            }*/
            storage = FirebaseConfig.getFirebaseStorage().child("groupImages");
            nomePedido.setText(pedido.getTitulo());
            System.out.println("DADOS PEDIDO NO ADAPTER: " + pedido.getTitulo());
            descricao.setText(String.valueOf(pedido.getDescricao()));
            tagsCategoria.setTags(pedido.getTagsCategoria());
            // DOWNLOAD GROUP IMG FROM STORAGE
            if (pedido.getGrupo() != null) {
                storage.child(pedido.getGrupo() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("grupo "+ pedido.getGrupo());
                        Glide.with(getContext()).load(uri).override(68, 68).into(pedidoImg);
                        System.out.println("my pedidos lets seee2" + uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            }
            else Glide.with(getContext()).load(R.drawable.logo).override(68,68).into(pedidoImg);

            /*else if (facebookPhoto != null){
                Glide.with(getContext()).load(facebookPhoto).override(68,68).into(pedidoImg);
            } else {
                storage.child(pedido.getCriadorId() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Glide.with(getContext()).load(uri).override(68, 68).into(pedidoImg);
                        System.out.println("my pedidos lets seee2" + uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

            }*/
        }

        return view;

    }
}