package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaaqui.adapters.PedidosAdapter;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Pedido;
import studio.brunocasamassa.ajudaaqui.helper.PedidoActivity;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 05/07/2017.
 */

public class CabineFarturaActivity extends AppCompatActivity {
    private ListView doacoes;
    private User user;
    private Toolbar toolbar;
    private ArrayList<Pedido> listaDoacoes;
    private ArrayAdapter doacoesArrayAdapter;
    private DatabaseReference dbPedidos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabine_fartura);

        toolbar = (Toolbar) findViewById(R.id.toolbar_cabine_fartura);
        toolbar.setTitle("Cabine da Fartura");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listaDoacoes = new ArrayList<>();

        doacoes = (ListView) findViewById(R.id.cabine_fartura_list);

        dbPedidos = FirebaseConfig.getFireBase().child("Pedidos");

        dbPedidos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot pedidos : dataSnapshot.getChildren()) {
                    Pedido pedido = pedidos.getValue(Pedido.class);
                    System.out.println("PEDIDO "+ pedido.getTipo() + "  "+ pedido.getNaCabine() );
                    if (pedido.getTipo().equals("Doacoes") && pedido.getNaCabine() == 0) {
                        if (!listaDoacoes.isEmpty()) {
                            System.out.println("doacoes adicioandas " + pedido.getTitulo());
                            listaDoacoes.add(listaDoacoes.size(), pedido);

                        } else listaDoacoes.add(0, pedido);
                    }
                }

                doacoesArrayAdapter = new PedidosAdapter(getApplicationContext(), listaDoacoes);

                if(!listaDoacoes.isEmpty()){
                doacoes.setAdapter(doacoesArrayAdapter);

                doacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            Intent intent = new Intent(CabineFarturaActivity.this, PedidoActivity.class);

                            // recupera dados a serem passados
                            Pedido pedido = listaDoacoes.get(position);

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

                            System.out.println("titulo enviando " + pedido.getTitulo() + "\n" + "grupo " + pedido.getGrupo() + "\n" + "desxcricao " + pedido.getDescricao() + "\n");
                            startActivity(intent);
                        } catch (Exception e) {
                            System.out.println("Exception grupos " + e);
                        }

                    }
                });}
                else Toast.makeText(getApplication()," Nao existem doações disponiveis no momento", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
