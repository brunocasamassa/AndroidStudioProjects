package studio.brunocasamassa.ajudaaqui.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.gujun.android.taggroup.TagGroup;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.SobreActivity;

/**
 * Created by bruno on 04/06/2017.
 */

public class PedidoAtendidoActivity extends AppCompatActivity {

    private int statusInt;
    private Toolbar toolbar;
    private TextView nomePedido;
    private TextView descricao;
    private TagGroup tagsCategoria;
    private TagGroup tagsGrupo;
    private ImageView statusImage;
    private Button finalizarPedido;
    private Pedido pedido;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private User user = new User();

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_atendido);

        statusImage = (ImageView) findViewById(R.id.status_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar_pedido_atendido);
        nomePedido = (TextView) findViewById(R.id.nome_pedido_atendido);
        descricao = (TextView) findViewById(R.id.descricao_pedido_atendido);
        tagsCategoria = (TagGroup) findViewById(R.id.tags_pedido_categoria_atendido);
        tagsGrupo = (TagGroup) findViewById(R.id.tags_pedido_grupo_atendido);
        finalizarPedido = (Button) findViewById(R.id.finalizar_pedido_button);

        Preferences preferences = new Preferences(PedidoAtendidoActivity.this);

        final String username = preferences.getNome();

        final Bundle extra = getIntent().getExtras();
        if (extra != null) {

            pedido = new Pedido();

            pedido.setIdPedido(extra.getString("idPedido"));
            pedido.setTagsCategoria(extra.getStringArrayList("tagsCategoria"));
            pedido.setDescricao(extra.getString("descricao"));
            pedido.setTitulo(extra.getString("titulo"));
            pedido.setGrupo(extra.getString("tagsGrupo"));
            pedido.setStatus(extra.getInt("status"));
            pedido.setTipo(extra.getString("tipo"));
            pedido.setCriadorId(extra.getString("criadorId"));


        }

        if (pedido.getStatus() != 0) {
            int status = pedido.getStatus();
            System.out.println("status pedido " + pedido.getTitulo() + ": " + status);
            if (status == 0) {
                Glide.with(PedidoAtendidoActivity.this).load(R.drawable.tag_aberto).override(274, 274).into(statusImage);
            } else if (status == 1) {
                Glide.with(PedidoAtendidoActivity.this).load(R.drawable.tag_emandamento).override(274, 274).into(statusImage);
            } else if (status == 2) {
                Glide.with(PedidoAtendidoActivity.this).load(R.drawable.tag_finalizado).override(274, 274).into(statusImage);
            } else if (status == 3) {
                Glide.with(PedidoAtendidoActivity.this).load(R.drawable.tag_cancelado).override(274, 274).into(statusImage);
            }
        }


        toolbar.setTitle(pedido.getTitulo().toUpperCase());

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nomePedido.setText(pedido.getTitulo());

        descricao.setText(pedido.getDescricao());
        if (pedido.getTagsCategoria() != null) {
            tagsCategoria.setTags(pedido.getTagsCategoria());
        }
        if (pedido.getGrupo() != null) {
            tagsGrupo.setTags(pedido.getGrupo());
        }
        finalizarPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidoAtendidoActivity.this);

                alertDialog.setTitle("Desistir do Pedido");
                alertDialog.setMessage("Deseja desistir deste pedido de ajuda? voce nao receberá creditos ou pontos por ele");
                alertDialog.setCancelable(false);



                alertDialog.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                });

                alertDialog.setPositiveButton("Desistir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        pedido.setStatus(0);
                        pedido.setAtendenteId("");
                        pedido.save();

                        DatabaseReference dbUser = FirebaseConfig.getFireBase();
                        dbUser.child("usuarios").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                ArrayList<String> pedidosAtendidos = new ArrayList<>();

                                pedidosAtendidos.addAll(user.getPedidosAtendidos());

                                int targetPedidoToRemove = pedidosAtendidos.indexOf(pedido.getIdPedido());

                                pedidosAtendidos.remove(targetPedidoToRemove);

                                user.setPedidosAtendidos(pedidosAtendidos);
                                user.setId(Base64Decoder.encoderBase64(user.getEmail()));
                                user.save();

                                DatabaseReference dbUser = FirebaseConfig.getFireBase();
                                dbUser.child("usuarios").child(pedido.getCriadorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        user.setCreditos(user.getCreditos()+1);
                                        user.setId(Base64Decoder.encoderBase64(user.getEmail()));
                                        user.setMessageNotification("O usuario "+ username+ " cancelou o pedido de ajuda de seu pedido '"+ pedido.getTitulo() + "' voce pode alterar o status para aberto e procurar um novo ajudante");
                                        user.save();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Toast.makeText(PedidoAtendidoActivity.this, "Voce Desistiu do pedido", Toast.LENGTH_LONG).show();
                        finish();

                    }
                }).create().show();


            }

        });

    }
}
