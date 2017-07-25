package studio.brunocasamassa.ajudaquiapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import java.util.Collections;
import java.util.Comparator;

import studio.brunocasamassa.ajudaquiapp.adapters.PedidosAdapter;
import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Pedido;
import studio.brunocasamassa.ajudaquiapp.helper.PedidoActivity;
import studio.brunocasamassa.ajudaquiapp.helper.User;

/**
 * Created by bruno on 05/07/2017.
 */

public class CabineFarturaActivity extends AppCompatActivity {
    private ListView doacoes;
    private Toolbar toolbar;
    private ArrayList<Pedido> listaDoacoes;
    private ArrayAdapter doacoesArrayAdapter;
    private FloatingActionButton fab;
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());
    private DatabaseReference dbPedidos = FirebaseConfig.getFireBase().child("Pedidos");
    private DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

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

        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);

                dbPedidos.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Location userLocation = new Location("my location");
                        userLocation.setLatitude(user.getLatitude());
                        userLocation.setLongitude(user.getLongitude());
                        System.out.println("my location Lat> " + user.getLatitude() + "  LOn> " + user.getLongitude());

                        for (DataSnapshot pedidos : dataSnapshot.getChildren()) {
                            Pedido pedido = pedidos.getValue(Pedido.class);
                            System.out.println("PEDIDO " + pedido.getTipo() + "  " + pedido.getNaCabine());
                            if (pedido.getLongitude() != null && pedido.getLatitude() != null) {
                                Location pedidoLocation = new Location(" pedido " + pedido.getTitulo() + " Location: ");
                                pedidoLocation.setLongitude(pedido.getLongitude());
                                pedidoLocation.setLatitude(pedido.getLatitude());

                                double distance = userLocation.distanceTo(pedidoLocation);

                                pedido.setDistanceInMeters(distance);
                                System.out.println("distance in meters " + pedido.getDistanceInMeters());

                            }
                            if (pedido.getTipo().equals("Doacoes") && pedido.getNaCabine() == 1 && !pedido.getCriadorId().equals(userKey)) {
                                if (!listaDoacoes.isEmpty()) {
                                    System.out.println("doacoes adicioandas " + pedido.getTitulo());
                                    System.out.println("doacoes adicioandas " + pedido.getDistanceInMeters());
                                    listaDoacoes.add(listaDoacoes.size(), pedido);

                                } else listaDoacoes.add(0, pedido);
                            }

                        }

                        System.out.println(listaDoacoes.size());
                        Collections.sort(listaDoacoes, new Comparator<Pedido>() {
                            @Override
                            public int compare(Pedido o1, Pedido o2) {
                                if (o1.getDistanceInMeters() == null) {
                                    listaDoacoes.remove(o1);
                                } else if (o2.getDistanceInMeters() == null) {
                                    listaDoacoes.remove(o2);
                                }

                                return o1.getDistanceInMeters().compareTo(o2.getDistanceInMeters());
                            }
                        });

                        doacoesArrayAdapter = new PedidosAdapter(getApplicationContext(), listaDoacoes);

                        if (!listaDoacoes.isEmpty()) {
                            doacoes.setAdapter(doacoesArrayAdapter);

                            doacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        Intent intent = new Intent(CabineFarturaActivity.this, PedidoActivity.class);

                                        //recupera dados a serem passados
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
                            });
                        } else
                            Toast.makeText(getApplication(), " Nao existem doações disponiveis no momento", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                fab = (FloatingActionButton) findViewById(R.id.fab_cabine);

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CabineFarturaActivity.this, CriaDoacaoNaCabineActivity.class);
                        intent.putExtra("latitude", user.getLatitude());
                        intent.putExtra("longitude", user.getLongitude());
                        startActivity(intent);
                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
