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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;
import studio.brunocasamassa.ajudaquiapp.MainActivity;
import studio.brunocasamassa.ajudaquiapp.R;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Pedido;
import studio.brunocasamassa.ajudaquiapp.helper.Preferences;

public class PedidosAdapter extends ArrayAdapter<Pedido> implements Filterable {

    private ArrayList<Pedido> pedidos;

    private ArrayList<Pedido> pedidosFiltrado;
    private Context context;
    private PedidosFiltro filtrador;
    private StorageReference storage;
    private StorageReference storageDonation;
    private Preferences preferences;
    private String facebookPhoto;

    private MainActivity mainActivity = new MainActivity();

    public PedidosAdapter(Context c, ArrayList<Pedido> objects) {
        super(c, 0, objects);
        this.pedidos = objects;
        this.pedidosFiltrado = objects;
        this.context = c;

        getFilter();
    }

    public void setPedidosFiltrado(ArrayList<Pedido> pedidosFiltrado) {
        this.pedidosFiltrado = pedidosFiltrado;
    }

    public ArrayList<Pedido> getPedidosFiltrado() {
        return pedidosFiltrado;
    }

    @Override
    public int getCount() {
        return pedidosFiltrado.size();
    }

    @Override
    public Pedido getItem(int position) {
        return pedidosFiltrado.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if (pedidos != null) {

            //IF The user image is from facebook
            if (mainActivity.facebookImg != null) {

                facebookPhoto = mainActivity.facebookImg;

            }

            System.out.println("facebook img pedido: " + facebookPhoto);
            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.model_pedido, parent, false);

            // recupera elemento para exibição
            TextView distancia = (TextView) view.findViewById(R.id.distance);
            TextView nomePedido = (TextView) view.findViewById(R.id.nomePedido);
            TextView descricao = (TextView) view.findViewById(R.id.descricao_pedido);
            TagGroup tagsCategoria = (TagGroup) view.findViewById(R.id.tagPedidos);
            final CircleImageView pedidoImg = (CircleImageView) view.findViewById(R.id.imagePedido);

            final Pedido pedido = pedidosFiltrado.get(position);

            /*
            if (pedido.getGrupo() != null) {
                storage = FirebaseConfig.getFirebaseStorage().child("groupImages");
            } else if (pedido.getGrupo() == null) {
                storage = FirebaseConfig.getFirebaseStorage().child("userImages");
            }
            */
            storage = FirebaseConfig.getFirebaseStorage().child("groupImages");
            storageDonation = FirebaseConfig.getFirebaseStorage().child("donationImages");

            if (pedido.getDistanceInMeters() != null) {
                distancia.setText(String.valueOf(pedido.getDistanceInMeters().intValue() / 1000000) + "km");
            }

            nomePedido.setText(pedido.getTitulo());
            System.out.println("DADOS PEDIDO NO ADAPTER: " + pedido.getTitulo());
            descricao.setText(String.valueOf(pedido.getDescricao()));
            tagsCategoria.setTags(pedido.getTagsCategoria());
            // DOWNLOAD GROUP IMG FROM STORAGE
            if(pedido.getTipo().equals("Doacoes")){
                storageDonation.child(pedido.getIdPedido()+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("grupo " + pedido.getGrupo());
                        try {
                            Glide.with(getContext()).load(uri).override(68, 68).into(pedidoImg);
                        } catch (Exception e) {
                            pedidoImg.setImageURI(uri);
                            System.out.println("EXCEPTION PedidosAdapter " + e);
                        }
                        System.out.println("my pedidos lets seee2" + uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            }

            else if (pedido.getGrupo() != null) {

                storage.child(pedido.getGroupId()+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println("grupo " + pedido.getGrupo());
                        try {
                            Glide.with(getContext()).load(uri).override(68, 68).into(pedidoImg);
                        } catch (Exception e) {
                            pedidoImg.setImageURI(uri);
                            System.out.println("EXCEPTION PedidosAdapter " + e);
                        }
                        System.out.println("my pedidos lets seee2" + uri);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

            } else Glide.with(getContext()).load(R.drawable.logo).override(68, 68).into(pedidoImg);

        }

        return view;

    }

    @Override
    public Filter getFilter() {
        if (filtrador == null) {
            filtrador = new PedidosFiltro();
        }

        return filtrador;
    }

    private class PedidosFiltro extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Pedido> tempList = new ArrayList<>();

                for (Pedido pedido : pedidos) {
                    if (pedido.getTitulo().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(pedido);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = pedidos.size();
                filterResults.values = pedidos;
            }


            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pedidosFiltrado = (ArrayList<Pedido>) results.values;
            notifyDataSetChanged();
        }
    }

}

