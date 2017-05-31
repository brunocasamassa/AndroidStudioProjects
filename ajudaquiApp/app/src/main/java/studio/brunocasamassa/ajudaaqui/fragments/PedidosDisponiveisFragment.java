package studio.brunocasamassa.ajudaaqui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaaqui.CriaPedidoActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.adapters.PedidosAdapter;
import studio.brunocasamassa.ajudaaqui.adapters.RecyclerAdapterPedidos;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Pedido;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosDisponiveisFragment extends Fragment {

    private FloatingActionButton fab;
    private int premium;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private ArrayList<Pedido> pedidos;
    private ArrayAdapter pedidoArrayAdapter;
    private RecyclerView.LayoutManager pedidosLayout;
    private RecyclerView.Adapter pedidoRecyclerAdapter;
    private RecyclerView todosPedidos;
    private DatabaseReference databasePedidos;
    private ValueEventListener valueEventListenerPedidos;
    private User usuario = new User();

    public PedidosDisponiveisFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pedidos_disponiveis, container, false);
        pedidos = new ArrayList<>();
        todosPedidos = (RecyclerView) view.findViewById(R.id.allpedidos_list);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CriaPedidoActivity.class);
                intent.putExtra("premium",premium);
                System.out.println("PREMIUM FRAGMENT "+premium);
                startActivity(intent);

            }
        });

        pedidoRecyclerAdapter = new RecyclerAdapterPedidos(getContext(), pedidos);

        todosPedidos.setAdapter(pedidoRecyclerAdapter);

        RecyclerView.LayoutManager layout = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        todosPedidos.setLayoutManager(layout);

        DatabaseReference databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usuario.setPedidosFeitos(user.getPedidosFeitos());
                premium = user.getPremiumUser();
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

                    if(usuario.getPedidosFeitos()==null || !usuario.getPedidosFeitos().contains(pedido.getIdPedido())) {
                        System.out.println("pedido " + pedido);
                        pedidos.add(pedido);
                    }
                    //remover pedidos do usuario na lista de pedidos geral
                    System.out.println("PMPF: pilha pedidos na view "+ pedidos);

                }

                pedidoRecyclerAdapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                pedidos.clear();

            }
        };





        return view;
    }


}
