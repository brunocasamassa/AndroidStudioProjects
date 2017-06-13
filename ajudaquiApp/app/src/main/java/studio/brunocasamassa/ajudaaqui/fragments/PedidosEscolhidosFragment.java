package studio.brunocasamassa.ajudaaqui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import studio.brunocasamassa.ajudaaqui.CriaPedidoActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.adapters.PedidosAdapter;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Pedido;
import studio.brunocasamassa.ajudaaqui.helper.PedidoAtendidoActivity;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * A simple {@link Fragment} subclass.
 */

public class PedidosEscolhidosFragment extends Fragment {
    private int premium;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private DatabaseReference databasePedidos;
    private ValueEventListener valueEventListenerPedidos;
    private FloatingActionButton fab;
    private User usuario = new User();
    private ArrayList<Pedido> pedidos;
    private ListView pedidosEscolhidos;
    private ArrayAdapter pedidoArrayAdapter;


    public PedidosEscolhidosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        databasePedidos.addListenerForSingleValueEvent(valueEventListenerPedidos);

//      dbGroups.addListenerForSingleValueEvent(valueEventListenerAllGroups);

    }


    @Override
    public void onStop() {
        super.onStop();
        databasePedidos.removeEventListener(valueEventListenerPedidos);
        //dbGroups.removeEventListener(valueEventListenerAllGroups);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedidos_escolhidos, container, false);
        pedidos = new ArrayList<>();
        pedidosEscolhidos = (ListView) view.findViewById(R.id.pedidos_escolhidos_list);
        System.out.println("GRUPO NA POSICAO "+ pedidos.isEmpty());

        pedidoArrayAdapter = new PedidosAdapter(getActivity(), pedidos);

        pedidosEscolhidos.setAdapter(pedidoArrayAdapter);
        /*fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CriaPedidoActivity.class);
                intent.putExtra("premium", premium);
                startActivity(intent);
            }
        });*/


        DatabaseReference databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usuario.setPedidosAtendidos(user.getPedidosAtendidos());
                premium = user.getPremiumUser();
                System.out.println("PMPF:idPedidos "+usuario.getPedidosFeitos());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ERROR GET PEDIDOS USER STRINGS: "+ databaseError);
            }
        });

        databasePedidos = FirebaseConfig.getFireBase()
                .child("Pedidos");

        valueEventListenerPedidos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pedidos.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    System.out.println("get children pedidos " + dados);

                    Pedido pedido = dados.getValue(Pedido.class);
                    System.out.println("pedido " + pedido.getTitulo());
                    if (usuario.getPedidosAtendidos() != null) {
                        if (!pedidos.contains(pedido.getIdPedido()) && usuario.getPedidosAtendidos().contains(pedido.getIdPedido())) {
                            pedidos.add(pedido);
                        }
                    }
                    System.out.println("PMPF: pilha pedidos na view "+ pedidos);

                }

                pedidoArrayAdapter.notifyDataSetChanged();
                System.out.println("PMPF: pilha pedidos na view2 "+ pedidos);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                pedidos.clear();

            }
        };

        pedidosEscolhidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), PedidoAtendidoActivity.class);

                // recupera dados a serem passados
                Pedido selectedPedido = pedidos.get(position);

                // enviando dados para grupo activity
                // enviando dados para pedido activity
                intent.putExtra("status", selectedPedido.getStatus());
                intent.putExtra("titulo", selectedPedido.getTitulo());
                intent.putExtra("tagsCategoria", selectedPedido.getTagsCategoria());
                intent.putExtra("idPedido", selectedPedido.getIdPedido());
                intent.putExtra("criadorId", selectedPedido.getCriadorId());
                intent.putExtra("tipo", selectedPedido.getTipo());
                if (selectedPedido.getGrupo() != null) {
                    intent.putExtra("tagsGrupo", selectedPedido.getGrupo());
                }
                intent.putExtra("descricao", selectedPedido.getDescricao());

                startActivity(intent);

            }
        });


        return view;

    }

}
