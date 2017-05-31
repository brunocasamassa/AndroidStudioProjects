package studio.brunocasamassa.ajudaaqui.adapters;

/**
 * Created by bruno on 09/05/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import me.gujun.android.taggroup.TagGroup;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.Pedido;

public class PedidosAdapter extends ArrayAdapter<Pedido> {

    private ArrayList<Pedido> pedidos;
    private Context context;

    public PedidosAdapter(Context c, ArrayList<Pedido> objects) {
        super(c, 0, objects);
        this.pedidos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if( pedidos != null ){

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.model_pedido, parent, false);

            // recupera elemento para exibição
            TextView nomePedido = (TextView) view.findViewById(R.id.nomePedido);
            TextView descricao = (TextView) view.findViewById(R.id.descricao_pedido);
            TagGroup tags = (TagGroup) view.findViewById(R.id.tagPedidos);

            Pedido pedido = pedidos.get( position );
            nomePedido.setText( pedido.getTitulo());
            System.out.println("DADOS PEDIDO NO ADAPTER: "+pedido.getTitulo());
            descricao.setText( String.valueOf(pedido.getDescricao()));
            tags.setTags(pedido.getTags());

        }

        return view;

    }
}