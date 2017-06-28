package studio.brunocasamassa.ajudaaqui.helper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import me.gujun.android.taggroup.TagGroup;
import studio.brunocasamassa.ajudaaqui.ConversasActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.StarsBar;

/**
 * Created by bruno on 04/06/2017.
 */

public class PedidoCriadoActivity extends AppCompatActivity {

    private int statusInt;
    private Toolbar toolbar;
    private TextView nomePedido;
    private TextView descricao;
    private ImageView pedidoChat;
    private TagGroup tagsCategoria;
    private TagGroup tagsGrupo;
    private ImageView statusImage;
    private Button finalizarPedido;
    private Pedido pedido;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private User user = new User();
    private String keyAtendente;
    private boolean trigger = false;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_criado);

        Toast.makeText(getApplicationContext(), "Segure um item para alterá-lo", Toast.LENGTH_SHORT).show();

        pedidoChat = (ImageView) findViewById(R.id.chat_pedido);
        statusImage = (ImageView) findViewById(R.id.status_image_criado);
        toolbar = (Toolbar) findViewById(R.id.toolbar_pedido_criado);
        nomePedido = (TextView) findViewById(R.id.nome_pedido_criado);
        descricao = (TextView) findViewById(R.id.descricao_pedido_criado);
        tagsCategoria = (TagGroup) findViewById(R.id.tags_pedido_categoria_criado);
        tagsGrupo = (TagGroup) findViewById(R.id.tags_pedido_grupo_criado);
        finalizarPedido = (Button) findViewById(R.id.finalizar_pedido_criado);

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
            pedido.setAtendenteId(extra.getString("atendenteId"));

        }

        pedidoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PedidoCriadoActivity.this, ConversasActivity.class);
                startActivity(intent);
                finish();

            }
        });


        descricao.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidoCriadoActivity.this);

                alertDialog.setTitle("Deseja alterar o status deste pedido?");
                alertDialog.setMessage("Escreva abaixo a nova descrição");
                alertDialog.setCancelable(false);

                final EditText editText = new EditText(PedidoCriadoActivity.this);
                alertDialog.setView(editText);

                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pedido.setDescricao(editText.getText().toString());
                        pedido.save();
                        Toast.makeText(getApplicationContext(), "Descrição alterada com sucesso", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
                return false;
            }
        });

        statusImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidoCriadoActivity.this);

                alertDialog.setTitle("Editar Pedido");
                alertDialog.setMessage("Deseja alterar o status deste pedido?");
                alertDialog.setCancelable(false);

                alertDialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(pedido.getStatus() == 0 ){
                            Toast.makeText(getApplication(), "Pedidos abertos nao podem ser alterados", Toast.LENGTH_SHORT).show();
                        }else{
                        AlertDialog.Builder selectStatus = new AlertDialog.Builder(PedidoCriadoActivity.this);
                        selectStatus.setTitle("Selecione o novo status do pedido");
                        selectStatus.setNegativeButton("Cancelar:", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        selectStatus.setItems(new CharSequence[]
                                        {"Aberto", "Finalizado"},
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // The 'which' argument contains the index position
                                        // of the selected item
                                        switch (which) {
                                            case 0:
                                                pedido.setStatus(0);
                                                Glide.with(PedidoCriadoActivity.this).load(R.drawable.tag_aberto).into(statusImage);
                                                pedido.save();
                                                break;
                                            case 1:
                                                pedido.setStatus(2);
                                                Glide.with(PedidoCriadoActivity.this).load(R.drawable.tag_finalizado).into(statusImage);
                                                pedido.save();
                                                break;
                                        }
                                    }
                                }).create().show();

                        selectStatus.setCancelable(false);
                    }}

                }).create().show();

                return false;
            }
        });
        if (pedido.getStatus() != 0) {
            int status = pedido.getStatus();
            System.out.println("status pedido " + pedido.getTitulo() + ": " + status);
            if (status == 0) {
                Glide.with(PedidoCriadoActivity.this).load(R.drawable.tag_aberto).override(274, 274).into(statusImage);
            } else if (status == 1) {
                Glide.with(PedidoCriadoActivity.this).load(R.drawable.tag_emandamento).override(274, 274).into(statusImage);
            } else if (status == 2) {
                Glide.with(PedidoCriadoActivity.this).load(R.drawable.tag_finalizado).override(274, 274).into(statusImage);
            } else if (status == 3) {
                Glide.with(PedidoCriadoActivity.this).load(R.drawable.tag_cancelado).override(274, 274).into(statusImage);
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

                if(pedido.getStatus() == 0){
                    Toast.makeText(getApplicationContext(), "Voce nao pode finalizar um pedido aberto", Toast.LENGTH_SHORT).show();

                }
                else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidoCriadoActivity.this);

                alertDialog.setTitle("Finalizar Pedido");
                alertDialog.setMessage("Deseja finalizar este pedido?");
                alertDialog.setCancelable(false);

                alertDialog.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        trigger = true;
                        startStars(trigger);

                    }
                }).create().show();


/*
                Toast.makeText(PedidoCriadoActivity.this, "Em processo", Toast.LENGTH_LONG).show();
*/
            }
        }});



    }

    private void startStars(boolean trigger) {

        if (trigger){
            System.out.println("trigger entrado");
            keyAtendente = pedido.getAtendenteId();
            Intent intent = new Intent(PedidoCriadoActivity.this, StarsBar.class);
            intent.putExtra("keyAtendente", keyAtendente);
            System.out.println("key nos pedidos "+ keyAtendente);
            startActivity(intent);

            pedido.setStatus(2);
            pedido.save();
            finish();
        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }
        }
    }*/
}