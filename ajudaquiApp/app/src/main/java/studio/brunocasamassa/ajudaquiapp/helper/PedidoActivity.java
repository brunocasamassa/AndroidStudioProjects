package studio.brunocasamassa.ajudaquiapp.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import me.gujun.android.taggroup.TagGroup;
import studio.brunocasamassa.ajudaquiapp.R;

/**
 * Created by bruno on 04/06/2017.
 */

public class PedidoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView nomePedido;
    private TextView descricao;
    private TagGroup tagsCategoria;
    private TagGroup tagsGrupo;
    private Button atenderPedido;
    private Pedido pedido;
    private String criadorId;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private User user = new User();
    private DatabaseReference firebase;
    private DatabaseReference dbUserDestinatario;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

       final Bundle extra = getIntent().getExtras();
        toolbar = (Toolbar) findViewById(R.id.toolbar_create_pedido);
        nomePedido = (TextView) findViewById(R.id.nome_pedido_feito);
        descricao = (TextView) findViewById(R.id.descricao_pedido_feito);
        tagsCategoria = (TagGroup) findViewById(R.id.tags_pedido_categoria);
        tagsGrupo = (TagGroup) findViewById(R.id.tags_pedido_grupo);
        atenderPedido = (Button) findViewById(R.id.atender_pedido_button);

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
            criadorId = pedido.getCriadorId();

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

        tagsCategoria.setTags(pedido.getTagsCategoria());

        if (pedido.getGrupo() != null) {
            tagsGrupo.setTags(pedido.getGrupo());
        }

        if (pedido.getTipo().equals("Doacoes")) {
            atenderPedido.setText("Receber Doacao");
            atenderPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receberDoacao();
                }
            });
        } else {
            atenderPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidoActivity.this);

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
                            System.out.println("pedido id " + pedido.getIdPedido());
                            firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Pedido dbPedido = dataSnapshot.getValue(Pedido.class);
                                    System.out.println("dbPedido " + dbPedido.getTitulo());
                                    dbPedido.setCriadorId(pedido.getCriadorId());
                                    dbPedido.setDescricao(pedido.getDescricao());
                                    dbPedido.setIdPedido(pedido.getIdPedido());
                                    dbPedido.setStatus(1);
                                    dbPedido.setTagsCategoria(pedido.getTagsCategoria());
                                    dbPedido.setTipo(pedido.getTipo());
                                    dbPedido.setTitulo(pedido.getTitulo());
                                    dbPedido.setAtendenteId(userKey);

                                    firebase.child(pedido.getIdPedido()).setValue(dbPedido);

                             /*       String msgId = "teste";
                                    FirebaseMessaging fm = FirebaseMessaging.getInstance();

                                    fm.send(new RemoteMessage.Builder(SENDER_ID + "")
                                            .setMessageId(Integer.toString(msgId.incrementAndGet()))
                                            .addData("my_message", "Hello World")
                                            .addData("my_action","SAY_HELLO")
                                            .build());
*/
                                    Toast.makeText(PedidoActivity.this, "Parabéns, voce já pode conversar com o criador do pedido", Toast.LENGTH_LONG).show();
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.out.println("error create atendimento " + databaseError);
                                }
                            });


                            final DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios");

                            dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final User usuario = dataSnapshot.child(userKey).getValue(User.class);
                                    user.setId(userKey);
                                    if (usuario.getMedalhas() != null) {
                                        user.setMedalhas(usuario.getMedalhas());
                                    }
                                    if (usuario.getMsgSolicitacoes() != null) {
                                        user.setMsgSolicitacoes(usuario.getMsgSolicitacoes());
                                    }
                                    if (usuario.getGrupos() != null) {
                                        user.setGrupos(usuario.getGrupos());
                                    }
                                    user.setCreditos(usuario.getCreditos());
                                    if (usuario.getEmail() != null) {
                                        user.setEmail(usuario.getEmail());
                                    }
                                    if (usuario.getName() != null) {
                                        user.setName(usuario.getName());
                                    }
                                    user.setPremiumUser(usuario.getPremiumUser());
                                    if (usuario.getProfileImageURL() != null) {
                                        user.setProfileImageURL(usuario.getProfileImageURL());
                                    }
                                    if (usuario.getProfileImg() != null) {
                                        user.setProfileImg(usuario.getProfileImg());
                                    }
                                    if (usuario.getPedidosFeitos() != null) {
                                        user.setPedidosFeitos(usuario.getPedidosFeitos());
                                    }
                                    user.setPontos(usuario.getPontos());


                                    ArrayList<String> pedidosAtendidos = new ArrayList<String>();
                                    if (usuario.getPedidosAtendidos() != null) {
                                        pedidosAtendidos = usuario.getPedidosAtendidos();
                                        pedidosAtendidos.add(pedidosAtendidos.size(), extra.getString("idPedido"));
                                    } else pedidosAtendidos.add(0, extra.getString("idPedido"));

                                    user.setPedidosAtendidos(pedidosAtendidos);
                                    user.save();

                                    DateFormat formatter = new SimpleDateFormat("HH:mm");
                                    formatter.setTimeZone(TimeZone.getTimeZone("GMT-3:00"));
                                    String currentTime = formatter.format(new Date());
                                    System.out.println("formatter: "+currentTime);

                                    // salvando Conversa para o remetente
                                    Conversa conversa = new Conversa();
                                    conversa.setIdUsuario(criadorId);
                                    conversa.setNome(pedido.getTitulo());
                                    conversa.setMensagem("bem vindo");
                                    conversa.setTime(currentTime);
                                    Boolean retornoConversaRemetente = salvarConversa(userKey, criadorId, conversa);
                                    System.out.println("SALVANDO CONVERSA PARA O REMETENTE(atendente pedido): " + userKey);
                                    if (!retornoConversaRemetente) {
                                        System.out.println("PROBLEMA AO CRIAR CAMPO DE CONVERSA PARA O ATENDENTE");
                                    } else {

                                        // salvando Conversa para o Destinatario
                                        System.out.println("SALVANDO CONVERSA PARA O DESTINATARIO(criador pedido): " + criadorId);
                                        conversa = new Conversa();
                                        conversa.setIdUsuario(userKey);
                                        conversa.setNome(pedido.getTitulo());
                                        conversa.setMensagem("bem vindo");
                                        conversa.setTime(currentTime);

                                        dbUserDestinatario = FirebaseConfig.getFireBase().child("usuarios");

                                        dbUserDestinatario.child(criadorId).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User user = dataSnapshot.getValue(User.class);
                                                user.setPedidosNotificationCount(user.getPedidosNotificationCount() + 1);
                                                user.setId(criadorId);


                                                user.save();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        Boolean retornoConversaDestinatario = salvarConversa(criadorId, userKey, conversa);
                                        if (!retornoConversaDestinatario) {
                                            System.out.println("PROBLEMA AO CRIAR CAMPO DE CONVERSA PARA O CRIADOR DO PEDIDO");
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }).create().show();


                }
            });
        }
    }

    private void receberDoacao() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidoActivity.this);

        alertDialog.setTitle("Receber Doacao");
        alertDialog.setMessage("Deseja Receber esta doacao?");
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
                System.out.println("pedido id " + pedido.getIdPedido());
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pedido dbPedido = dataSnapshot.getValue(Pedido.class);
                        System.out.println("dbPedido " + dbPedido.getTitulo());
                        dbPedido.setCriadorId(pedido.getCriadorId());
                        dbPedido.setDescricao(pedido.getDescricao());
                        dbPedido.setIdPedido(pedido.getIdPedido());
                        dbPedido.setStatus(1);
                        dbPedido.setTagsCategoria(pedido.getTagsCategoria());
                        dbPedido.setTipo(pedido.getTipo());
                        dbPedido.setTitulo(pedido.getTitulo());
                        dbPedido.setAtendenteId(userKey);

                        firebase.child(pedido.getIdPedido()).setValue(dbPedido);

                        Toast.makeText(PedidoActivity.this, "Parabéns, voce já pode conversar com o doador do produto", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("error create atendimento " + databaseError);
                    }
                });


                final DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios");

                dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User usuario = dataSnapshot.child(userKey).getValue(User.class);
                        user.setId(userKey);
                        if (usuario.getMedalhas() != null) {
                            user.setMedalhas(usuario.getMedalhas());
                        }
                        if (usuario.getMsgSolicitacoes() != null) {
                            user.setMsgSolicitacoes(usuario.getMsgSolicitacoes());
                        }
                        if (usuario.getGrupos() != null) {
                            user.setGrupos(usuario.getGrupos());
                        }
                        user.setCreditos(usuario.getCreditos() - 1);

                        if (usuario.getEmail() != null) {
                            user.setEmail(usuario.getEmail());
                        }
                        if (usuario.getName() != null) {
                            user.setName(usuario.getName());
                        }
                        user.setPremiumUser(usuario.getPremiumUser());
                        if (usuario.getProfileImageURL() != null) {
                            user.setProfileImageURL(usuario.getProfileImageURL());
                        }
                        if (usuario.getProfileImg() != null) {
                            user.setProfileImg(usuario.getProfileImg());
                        }
                        if (usuario.getPedidosFeitos() != null) {
                            user.setPedidosFeitos(usuario.getPedidosFeitos());
                        }
                        user.setPontos(usuario.getPontos());

                        if (usuario.getPedidosAtendidos() != null) {

                            user.setPedidosAtendidos(usuario.getPedidosAtendidos());
                        }
                        user.save();

                        // salvando Conversa para o remetente
                        Conversa conversa = new Conversa();
                        conversa.setIdUsuario(criadorId);
                        conversa.setNome(pedido.getTitulo());
                        conversa.setMensagem("bem vindo");
                        Boolean retornoConversaRemetente = salvarConversa(userKey, criadorId, conversa);
                        System.out.println("SALVANDO CONVERSA PARA O REMETENTE(atendente pedido): " + userKey);
                        if (!retornoConversaRemetente) {
                            System.out.println("PROBLEMA AO CRIAR CAMPO DE CONVERSA PARA O ATENDENTE");
                        } else {

                            // salvando Conversa para o Destinatario
                            System.out.println("SALVANDO CONVERSA PARA O DESTINATARIO(criador pedido): " + criadorId);
                            conversa = new Conversa();
                            conversa.setIdUsuario(userKey);
                            conversa.setNome(pedido.getTitulo());
                            conversa.setMensagem("bem vindo");

                            dbUserDestinatario = FirebaseConfig.getFireBase().child("usuarios");

                            dbUserDestinatario.child(criadorId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    user.setPedidosNotificationCount(user.getPedidosNotificationCount() + 1);
                                    user.setId(criadorId);
                                    user.save();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Boolean retornoConversaDestinatario = salvarConversa(criadorId, userKey, conversa);
                            if (!retornoConversaDestinatario) {
                                System.out.println("PROBLEMA AO CRIAR CAMPO DE CONVERSA PARA O CRIADOR DO PEDIDO");
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }).create().show();


    }


    private boolean salvarConversa(String idRemetente, String idDestinatario, Conversa conversa) {
        try {
            firebase = FirebaseConfig.getFireBase().child("conversas");
            firebase.child(idRemetente)
                    .child(pedido.getIdPedido())
                    .setValue(conversa);


            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
