package studio.brunocasamassa.ajudaaqui.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
                Toast.makeText(PedidoAtendidoActivity.this, "Em processo", Toast.LENGTH_LONG).show();
            }
        });

       /* atenderPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidoAtendidoActivity.this);

                alertDialog.setTitle("Atender Pedido");
                alertDialog.setMessage("Deseja atender este pedido?");
                alertDialog.setCancelable(false);

                alertDialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final DatabaseReference firebase = FirebaseConfig.getFireBase().child("Pedidos");
                        firebase.child(pedido.getIdPedido());
                        System.out.println("pedido id "+pedido.getIdPedido());
                        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Pedido dbPedido = dataSnapshot.getValue(Pedido.class);
                                System.out.println("dbPedido "+ dbPedido.getTitulo());
                                dbPedido.setCriadorId(pedido.getCriadorId());
                                dbPedido.setDescricao(pedido.getDescricao());
                                dbPedido.setIdPedido(pedido.getIdPedido());
                                dbPedido.setStatus(1);
                                dbPedido.setTagsCategoria(pedido.getTagsCategoria());
                                dbPedido.setTipo(pedido.getTipo());
                                dbPedido.setTitulo(pedido.getTitulo());
                                dbPedido.setAtendenteId(userKey);

                                firebase.child(pedido.getIdPedido()).setValue(dbPedido);

                                Toast.makeText(PedidoAtendidoActivity.this, "Parabéns, voce já pode conversar com o criador do pedido", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("error create atendimento "+databaseError);
                            }
                        });



                        final DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios");


                        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User usuario = dataSnapshot.child(userKey).getValue(User.class);
                                user.setId(userKey);
                                if(usuario.getMedalhas() != null){
                                user.setMedalhas(usuario.getMedalhas());}
                                if(usuario.getMsgSolicitacoes() != null){
                                user.setMsgSolicitacoes(usuario.getMsgSolicitacoes());}
                                if(usuario.getGrupos() != null){
                                user.setGrupos(usuario.getGrupos());}
                                user.setCreditos(usuario.getCreditos());
                                if(usuario.getEmail() != null){
                                user.setEmail(usuario.getEmail());}
                                if(usuario.getName() != null){
                                user.setName(usuario.getName());}
                                user.setPremiumUser(usuario.getPremiumUser());
                                if(usuario.getProfileImageURL() != null){
                                user.setProfileImageURL(usuario.getProfileImageURL());}
                                if(usuario.getProfileImg() != null){
                                user.setProfileImg(usuario.getProfileImg());}
                                if(usuario.getPedidosFeitos() != null){
                                user.setPedidosFeitos(usuario.getPedidosFeitos());}
                                if(usuario.getPontos() != null){
                                user.setPontos(usuario.getPontos());}

                                ArrayList<String> pedidosAtendidos = new ArrayList<String>();
                                if(usuario.getPedidosAtendidos()!= null) {
                                    pedidosAtendidos = usuario.getPedidosAtendidos();
                                    pedidosAtendidos.add(pedidosAtendidos.size(), extra.getString("idPedido"));
                                } else pedidosAtendidos.add(0, extra.getString("idPedido"));

                                user.setPedidosAtendidos(pedidosAtendidos);
                                user.save();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }).create().show();


            }
        });
*/
    }
}
