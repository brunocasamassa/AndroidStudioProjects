package studio.brunocasamassa.ajudaquiapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import studio.brunocasamassa.ajudaquiapp.R;
import studio.brunocasamassa.ajudaquiapp.adapters.PedidosSelecionadoAdapter;
import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Pedido;
import studio.brunocasamassa.ajudaquiapp.helper.PedidoCriadoActivity;
import studio.brunocasamassa.ajudaquiapp.helper.User;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosMeusPedidosFragment extends Fragment {
    private int premium;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private ArrayList<Pedido> pedidos;
    private ArrayAdapter pedidoArrayAdapter;
    private ListView meusPedidos;
    private DatabaseReference databasePedidos;
    private FloatingActionButton fab;
    private ValueEventListener valueEventListenerPedidos;
    private User usuario = new User();
    private DatabaseReference dbUser;


    public PedidosMeusPedidosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        databasePedidos.addListenerForSingleValueEvent(valueEventListenerPedidos);
        dbUser = FirebaseConfig.getFireBase().child("usuarios");

        dbUser.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.getPedidosNotificationCount() != 0) {
                    //Toast.makeText(getApplicationContext(),"Parabens, voce possui um pedido atendido", Toast.LENGTH_LONG).show();
                    user.setPedidosNotificationCount(/*user.getChatNotificationCount() - mensagens.size()*/ 0);
                    user.setId(userKey);
                    user.save();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        View view = inflater.inflate(R.layout.fragment_pedidos_meuspedidos, container, false);
        pedidos = new ArrayList<>();
        meusPedidos = (ListView) view.findViewById(R.id.meusPedidos_list);
        System.out.println("GRUPO NA POSICAO " + pedidos.isEmpty());

        pedidoArrayAdapter = new PedidosSelecionadoAdapter(getActivity(), pedidos);

        meusPedidos.setDivider(null);
        meusPedidos.setAdapter(pedidoArrayAdapter);

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
                usuario.setPedidosFeitos(user.getPedidosFeitos());
                premium = user.getPremiumUser();
                System.out.println("PMPF:idPedidos " + usuario.getPedidosFeitos());

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
                    System.out.println("pedido " + pedido.getTitulo());
                    if (usuario.getPedidosFeitos() != null) {
                        if (!pedidos.contains(pedido.getIdPedido()) && usuario.getPedidosFeitos().contains(pedido.getIdPedido())) {
                            pedidos.add(pedido);
                        }
                    }
                    System.out.println("PMPF: pilha pedidos na view " + pedidos);

                }


                pedidoArrayAdapter.notifyDataSetChanged();
                System.out.println("PMPF: pilha pedidos na view2 " + pedidos);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pedidos.clear();

            }
        };


        meusPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Pedido pedidoPressed = pedidos.get(position);

                if (pedidoPressed.getStatus() == 2) {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                    alertDialog.setTitle("Apagar pedido");
                    alertDialog.setMessage("desejaxcluir e4ste pedido? ");

                    alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            apagaPedido(pedidoPressed.getIdPedido());

                        }
                    });

                    alertDialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
                }
                return false;
            }
        });
        meusPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), PedidoCriadoActivity.class);

                // recupera dados a serem passados
                Pedido selectedPedido = pedidos.get(position);

                if (selectedPedido.getStatus() == 2) {//finalizado
                    Toast.makeText(getApplicationContext(), "Pedido finalizado", Toast.LENGTH_SHORT).show();
                } else {

                    // enviando dados para grupo activity
                    // enviando dados para pedido activity
                    intent.putExtra("status", selectedPedido.getStatus());
                    intent.putExtra("titulo", selectedPedido.getTitulo());
                    intent.putExtra("tagsCategoria", selectedPedido.getTagsCategoria());
                    intent.putExtra("idPedido", selectedPedido.getIdPedido());
                    intent.putExtra("criadorId", selectedPedido.getCriadorId());
                    intent.putExtra("tipo", selectedPedido.getTipo());
                    intent.putExtra("atendenteId", selectedPedido.getAtendenteId());
                    if (selectedPedido.getGrupo() != null) {
                        intent.putExtra("tagsGrupo", selectedPedido.getGrupo());
                    }
                    intent.putExtra("descricao", selectedPedido.getDescricao());
                    startActivity(intent);
                }

            }
        });

        return view;

    }

    private void apagaPedido(String idPedido) {
        DatabaseReference dbPedidos = FirebaseConfig.getFireBase().child("Pedidos");
        dbPedidos.child(idPedido).removeValue();
    }

}
