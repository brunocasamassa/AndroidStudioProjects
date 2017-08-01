package studio.brunocasamassa.ajudaquiapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import me.gujun.android.taggroup.TagGroup;
import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Mail;
import studio.brunocasamassa.ajudaquiapp.helper.Pedido;
import studio.brunocasamassa.ajudaquiapp.helper.Preferences;
import studio.brunocasamassa.ajudaquiapp.helper.User;

/**
 * Created by bruno on 04/06/2017.
 */

public class DoacaoCriadaActivity extends AppCompatActivity {

    private int statusInt;
    private Toolbar toolbar;
    private ConstraintLayout my_layout;
    private TextView nomePedido;
    private TextView descricao;
    private ImageView pedidoChat;
    private TagGroup tagsCategoria;
    private TagGroup tagsGrupo;
    private ImageView statusImage;
    private Button donationButton;
    private Pedido pedido;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private User user = new User();
    private String keyAtendente;
    private boolean trigger = false;
    private TextView endereco;
    private TextView donationContact;
    private int cameFrom; //if 1 == MeusPedidosList if 2== CabineDaFartura if 3 == EscolhidosList
    private String ajudaquimail = Base64Decoder.encoderBase64("ajudaquisuporte@gmail.com");
    private String ajudaquipass = Base64Decoder.encoderBase64("ajudaqui931931931");
    private String userName;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doacao);

        my_layout = (ConstraintLayout) findViewById(R.id.layout_my_pedido);
        pedidoChat = (ImageView) findViewById(R.id.chat_pedido);
        statusImage = (ImageView) findViewById(R.id.status_image_criado);
        toolbar = (Toolbar) findViewById(R.id.toolbar_donation_activity);
        nomePedido = (TextView) findViewById(R.id.nome_doacao);
        descricao = (TextView) findViewById(R.id.descricao_doacao);
        tagsCategoria = (TagGroup) findViewById(R.id.tags_pedido_categoria_criado);
        tagsGrupo = (TagGroup) findViewById(R.id.tags_pedido_grupo_criado);
        donationButton = (Button) findViewById(R.id.donation_button);
        donationContact = (TextView) findViewById(R.id.donationContact_activitydoacao);
        endereco = (TextView) findViewById(R.id.endereco_activitydoacao);

        Preferences preferencias = new Preferences(DoacaoCriadaActivity.this);
        userName = preferencias.getNome();


        final Bundle extra = getIntent().getExtras();
        if (extra != null) {

            pedido = new Pedido();
            pedido.setIdPedido(extra.getString("idPedido"));
            pedido.setTagsCategoria(extra.getStringArrayList("tagsCategoria"));
            pedido.setDescricao(extra.getString("descricao"));
            pedido.setTitulo(extra.getString("titulo"));
            pedido.setGrupo(extra.getString("tagsGrupo"));
            pedido.setLongitude(extra.getDouble("longitude"));
            pedido.setLatitude(extra.getDouble("latitude"));
            pedido.setStatus(extra.getInt("status"));
            pedido.setTipo(extra.getString("tipo"));
            pedido.setQtdAtual(extra.getInt("qtdAtual"));
            pedido.setQtdDoado(extra.getInt("qtdDoado"));
            pedido.setCriadorId(extra.getString("criadorId"));
            pedido.setAtendenteId(extra.getString("atendenteId"));
            pedido.setEndereco(extra.getString("endereco"));
            pedido.setDonationContact(extra.getString("donationContact"));

            // --> came from which activity
            cameFrom = extra.getInt("cameFrom");

        }

        System.out.println("donation " + pedido.getTitulo());
        toolbar.setTitle(pedido.getTitulo().toUpperCase());

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nomePedido.setText(pedido.getTitulo());

        endereco.setText(pedido.getEndereco());
        donationContact.setText(pedido.getDonationContact());
        descricao.setText(pedido.getDescricao());

        if (cameFrom == 1) {
            donationButton.setText("CANCELAR DOAÇÃO");
            donationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelarDoacao();
                }
            });
        }
        if (cameFrom == 2) {
            donationButton.setText("RECEBER DOAÇÃO");
            donationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receberDoacao();
                }
            });
        }
        if (cameFrom == 3) {
            donationButton.setText("AVALIAR DOADOR");
            donationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    avaliarDoador();
                }
            });

        }

    }

    private void avaliarDoador() {

    }

    private void cancelarDoacao() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DoacaoCriadaActivity.this);

        alertDialog.setTitle("Cancelar doação?");
        alertDialog.setMessage("Se voce cancelar, os itens serão zerados, todas as doações já feitas ainda deverão ser entregues");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startStars(true);
            }

        });

        alertDialog.setNegativeButton("Voltar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }

        }).create().show();
    }

    private void startStars(boolean trigger) {

        if (trigger) {

            final DatabaseReference dbConversa = FirebaseConfig.getFireBase().child("conversas");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DoacaoCriadaActivity.this);
            alertDialog.setTitle("     Avalie sua Experiência");
            alertDialog.setMessage("Como se saiu a pessoa que te ajudou?");
            alertDialog.setCancelable(false);
            final RatingBar ratingBar = new RatingBar(getApplicationContext());
            ratingBar.setNumStars(1);
            ratingBar.setMax(5);
            ratingBar.setDrawingCacheBackgroundColor(Color.BLUE);
            Drawable progress = ratingBar.getProgressDrawable();

            alertDialog.setView(ratingBar);
            alertDialog.setPositiveButton("ENVIAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    final int rating = (int) (ratingBar.getRating() * 10);
                    DatabaseReference atendenteUser = FirebaseConfig.getFireBase().child("usuarios");

                    atendenteUser.child(pedido.getCriadorId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            user.setPontos(user.getPontos() + rating);
                            user.setId(pedido.getAtendenteId());
                            user.save();

                            finish();
                            Toast.makeText(getApplicationContext(), "Obrigado por avaliar o doador", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    finish();

                }
            }).create().show();


        }
    }

    private void receberDoacao() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DoacaoCriadaActivity.this);

        alertDialog.setTitle("Receber Doação");
        alertDialog.setMessage("Digite o CPF do receptor da doação:");
        alertDialog.setCancelable(false);

        final EditText edit = new EditText(DoacaoCriadaActivity.this);

        alertDialog.setView(edit);
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setPositiveButton("Receber", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final DatabaseReference firebase = FirebaseConfig.getFireBase().child("Pedidos");
                firebase.child(pedido.getIdPedido());
                System.out.println("pedido id " + pedido.getIdPedido());
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Pedido pedidoDoado = dataSnapshot.getValue(Pedido.class);
                        System.out.println("dbPedido " + pedidoDoado.getTitulo());
                        pedidoDoado.setCriadorId(pedido.getCriadorId());
                        pedidoDoado.setDescricao(pedido.getDescricao());
                        pedidoDoado.setIdPedido(pedido.getIdPedido());
                        pedidoDoado.setStatus(1);
                        pedidoDoado.setNaCabine(1);
                        pedidoDoado.setEndereco(pedido.getEndereco());
                        pedidoDoado.setDonationContact(pedido.getDonationContact());
                        pedidoDoado.setQtdAtual(pedido.getQtdAtual() - 1);
                        pedidoDoado.setQtdDoado(pedido.getQtdDoado());
                        pedidoDoado.setLatitude(pedido.getLatitude());
                        pedidoDoado.setLongitude(pedido.getLongitude());
                        pedidoDoado.setTagsCategoria(pedido.getTagsCategoria());
                        pedidoDoado.setTipo(pedido.getTipo());
                        pedidoDoado.setTitulo(pedido.getTitulo());
                        pedidoDoado.setAtendenteId(userKey);

                        sendEmail(Base64Decoder.decoderBase64(pedido.getCriadorId()), userName, pedido.getTitulo(), edit.getText().toString());

                        pedidoDoado.save();

                        Toast.makeText(DoacaoCriadaActivity.this, "Parabéns, voce já pode retirar o pedido no local indicado", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(DoacaoCriadaActivity.this, PedidosActivity.class));

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
                        ArrayList<String> pedidosAtendidosUser = new ArrayList<String>();

                        //tratando lista de pedidos atendidos
                        if (usuario.getPedidosAtendidos() != null && !usuario.getPedidosAtendidos().contains(pedido.getIdPedido())) {
                            pedidosAtendidosUser.addAll(usuario.getPedidosAtendidos());
                            pedidosAtendidosUser.add(pedidosAtendidosUser.size(), pedido.getIdPedido());
                            usuario.setPedidosAtendidos(pedidosAtendidosUser);
                        } else if (usuario.getPedidosAtendidos().contains(pedido.getIdPedido())) {
                            Toast.makeText(getApplicationContext(), "Você já requisitou esta doação, apenas uma doação por pessoa", Toast.LENGTH_SHORT).show();
                        } else {
                            pedidosAtendidosUser.add(0, pedido.getIdPedido());

                            usuario.setPedidosAtendidos(pedidosAtendidosUser);
                        }

                        usuario.setCreditos(usuario.getCreditos() - 1);

                        usuario.save();

                        //THIS WAS THE CODE FOR CHAT DONATION --> 'MAYBE LATER'
                   /*   // salvando Conversa para o remetente
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

                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }).create().show();


    }

    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m;
        LoginActivity activity;

        public SendEmailAsyncTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (m.send()) {
                    // activity.displayMessage("Email sent.");
                } else {
                    // activity.displayMessage("Email failed to send.");
                }

                return true;
            } catch (AuthenticationFailedException e) {
                System.out.println(SendEmailAsyncTask.class.getName() + "Bad account details");
                e.printStackTrace();
                // activity.displayMessage("Authentication failed.");
                return false;
            } catch (MessagingException e) {
                System.out.println(SendEmailAsyncTask.class.getName() + "Email failed");
                e.printStackTrace();
                //  activity.displayMessage("Email failed to send.");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                // activity.displayMessage("Unexpected error occured.");
                return false;
            }
        }
    }


    private void sendEmail(String donaterMail, String donationRecepterUser, String nomeDoacao, String docValidator) {
        String[] recipients = {donaterMail};
        SendEmailAsyncTask email = new SendEmailAsyncTask();
        email.m = new Mail(Base64Decoder.decoderBase64(ajudaquimail), Base64Decoder.decoderBase64(ajudaquipass));
        email.m.set_from("ajudaquisuporte@gmail.com");
        email.m.setBody("Voce recebeu este email pois o usuario " + donationRecepterUser + " solicitou sua doacao de " + nomeDoacao + ".\n" + "Documento do usuario para validação: " + docValidator);
        email.m.set_to(recipients);
        email.m.set_subject("AJUDAQUI - ALGUEM ACEITOU SUA DOAÇÃO");
        email.execute();

    }

}