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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.adapters.PedidosAdapter;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Pedido;
import studio.brunocasamassa.ajudaaqui.helper.PedidoActivity;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * A simple {@link Fragment} subclass.
 */

public class PedidosDisponiveisFragment extends Fragment {

    private FloatingActionButton fab;
    private int premium;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private ArrayList<Pedido> pedidos;
    private ArrayAdapter pedidosArrayAdapter;
    private ListView pedidosView;
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
        pedidosView = (ListView) view.findViewById(R.id.allpedidos_list);
        /*fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), CriaPedidoActivity.class);
                intent.putExtra("premium", premium);
                System.out.println("PREMIUM FRAGMENT " + premium);
                startActivity(intent);

            }
        });
*/
        pedidosArrayAdapter = new PedidosAdapter(getContext(), pedidos);

        pedidosView.setDivider(null);
        pedidosView.setAdapter(pedidosArrayAdapter);

        final DatabaseReference databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usuario.setPedidosFeitos(user.getPedidosFeitos());
                premium = user.getPremiumUser();
                if (user.getMessageNotification() != null && !user.getMessageNotification().equals("no message")) {
                    //manual toast delay (nao me julgue)
                    for(int i =0;i<2;i++){

                        Toasty.warning(getContext(), user.getMessageNotification(), Toast.LENGTH_LONG, true).show();
                    }

                    user.setMessageNotification("no message");
                    user.setId(userKey);
                    user.save();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("ERROR GET PEDIDOS USER STRINGS: " + databaseError);

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

                    if (pedido.getStatus() == 0) {
                        pedidos.add(pedido);
                        if (usuario.getPedidosFeitos() != null) {
                            if (usuario.getPedidosFeitos().contains(pedido.getIdPedido())) {
                                System.out.println("pedido " + pedido);
                                pedidos.remove(pedido);
                            }
                            if(pedido.getTipo().equals("Doacoes")){
                                pedidos.remove(pedido);
                            }
                        }
                    }
                    //remover pedidos do usuario na lista de pedidos geral
                    System.out.println("PMPF: pilha pedidos na view " + pedidos);

                }


                pedidosArrayAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                pedidos.clear();

            }
        };


        pedidosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent intent = new Intent(getActivity(), PedidoActivity.class);

                    // recupera dados a serem passados
                    Pedido pedido = pedidos.get(position);

                    // enviando dados para pedido activity
                    intent.putExtra("status", pedido.getStatus());
                    intent.putExtra("titulo", pedido.getTitulo());
                    intent.putExtra("tagsCategoria", pedido.getTagsCategoria());
                    intent.putExtra("idPedido", pedido.getIdPedido());
                    intent.putExtra("criadorId", pedido.getCriadorId());
                    intent.putExtra("tipo", pedido.getTipo());
                    if (pedido.getGrupo() != null) {
                        intent.putExtra("tagsGrupo", pedido.getGrupo());
                    }
                    intent.putExtra("descricao", pedido.getDescricao());

                    System.out.println("titulo " + pedido.getTitulo() + "\n" + "grupo " + pedido.getGrupo() + "\n" + "desxcricao " + pedido.getDescricao() + "\n");
                    startActivity(intent);
                } catch (Exception e) {
                    System.out.println("Exception grupos " + e);
                }

            }
        });


        return view;
    }



}

