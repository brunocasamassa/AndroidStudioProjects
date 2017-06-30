package studio.brunocasamassa.ajudaaqui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.Pedido;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 22/05/2017.
 */

public class CriaPedidoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText pedidoName;
    private EditText descricao;
    private Button createButton;
    private String tagCaptured;
    ArrayList<String> tagsCaptured = new ArrayList<String>();
    private TagContainerLayout categorias;
    private TagContainerLayout grupos;
    private TextView add_grupos;
    private TextView add_tags;
    private Pedido pedido;
    private int premium;
    private ImageButton addTagButton;
    private ImageButton addGroupButton;
    private String tipoPedido;   //doacao, servico, troca, emprestimo
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private String groupCaptured;

    @Override
    protected void onStart() {

        super.onStart();
        System.out.println("vamos ver " + tagCaptured);
        categorias.setTags(tagsCaptured);
        if (groupCaptured != null) {
            grupos.setTags(groupCaptured);
        }

        System.out.println("vamos ver " + groupCaptured);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_pedido);
        final Bundle extras = getIntent().getExtras();
        premium = (extras.getInt("premium")); //TODO =0 EVEN FIREBASE =1
        System.out.println("PREMIUM RECEBE" + premium);

        toolbar = (Toolbar) findViewById(R.id.toolbar_create_group);
        toolbar.setTitle("Criar Pedido");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        categorias = (TagContainerLayout) findViewById(R.id.tagGroupCategoria);
        grupos = (TagContainerLayout) findViewById(R.id.tagGroupGrupos);

        add_grupos = (TextView) findViewById(R.id.word_add_groups);
        add_tags = (TextView) findViewById(R.id.word_add_tags);
        pedidoName = (EditText) findViewById(R.id.create_pedido_name);
        descricao = (EditText) findViewById(R.id.create_pedido_description);
        createButton = (Button) findViewById(R.id.create_pedido_button);
        addTagButton = (ImageButton) findViewById((R.id.add_tag_button));
        addGroupButton = (ImageButton) findViewById((R.id.addGroup_tag_button));

        grupos.setBackgroundColor(Color.TRANSPARENT);
        grupos.setBorderColor(Color.TRANSPARENT);
        grupos.setTagBackgroundColor(R.color.colorPrimary);
        grupos.setTagTextColor(R.color.md_white_1000);
        grupos.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                grupos.removeTag(position);

            }
        });

        categorias.setBackgroundColor(Color.TRANSPARENT);
        categorias.setBorderColor(Color.TRANSPARENT);
        categorias.setTagBackgroundColor(R.color.colorPrimary);
        categorias.setTagTextColor(R.color.md_white_1000);
        categorias.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                categorias.removeTag(position);
                tagsCaptured.remove(position);

            }
        });

        add_grupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (premium == 0) {
                    Toast.makeText(getApplicationContext(), "Conteudo exclusivo para usuarios Premium, adquira para utilizar o recurso de grupos", Toast.LENGTH_LONG).show();
                } else
                    startActivityForResult(new Intent(CriaPedidoActivity.this, PedidoAddGroupsList.class), 2);

            }
        });

        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (premium == 0) {
                    Toast.makeText(getApplicationContext(), "Conteudo exclusivo para usuarios Premium, adquira para utilizar o recurso de grupos", Toast.LENGTH_LONG).show();
                } else
                    startActivityForResult(new Intent(CriaPedidoActivity.this, PedidoAddGroupsList.class), 2);
            }
        });

        add_tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CriaPedidoActivity.this, TagsList.class), 1);

            }
        });
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(CriaPedidoActivity.this, TagsList.class), 1);

            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (pedidoName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Insira um Titulo para o pedido", Toast.LENGTH_LONG).show();
                    return;
                } else if (descricao.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Insira uma descricao para o pedido", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CriaPedidoActivity.this);

                    alertDialog.setTitle("Selecione o tipo de Pedido que deseja efetuar");
                    alertDialog.setCancelable(false);

                    alertDialog.setNegativeButton("Cancelar:", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setItems(new CharSequence[]
                                    {"Servicos", "Emprestimos", "Trocas"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    switch (which) {
                                        case 0:
                                            Toast.makeText(getApplicationContext(), "Servicos", Toast.LENGTH_SHORT).show();
                                            tipoPedido = "Servicos";
                                            createPedido();
                                            break;
                                        case 1:
                                            Toast.makeText(getApplicationContext(), "Emprestimos", Toast.LENGTH_SHORT).show();
                                            tipoPedido = "Emprestimos";
                                            createPedido();
                                            break;
                                        case 2:
                                            Toast.makeText(getApplicationContext(), "Troca", Toast.LENGTH_SHORT).show();
                                            tipoPedido = "Troca";
                                            createPedido();
                                            break;
                                    }
                                }
                            });


                    alertDialog.create();
                    alertDialog.show();


                }
            }

        });

    }

    private void createPedido() {

        pedido = new Pedido();
        System.out.println("tipo de pedido: " + tipoPedido);
        pedido.setTagsCategoria(tagsCaptured);
        pedido.setTipo(tipoPedido);

        if (groupCaptured != null) {
            pedido.setGrupo(groupCaptured);
        }

        pedido.setDescricao(descricao.getText().toString());
        pedido.setTitulo(pedidoName.getText().toString());
        pedido.setIdPedido(Base64Decoder.encoderBase64(pedido.getTitulo()));
        pedido.setCriadorId(userKey);
        pedido.setGrupo(groupCaptured);

        if (pedido.getGrupo() != null) {
            savePedidoIntoGroup(pedido);
        }

        System.out.println("user id key " + userKey);

        pedido.save();
        pedidoSaveIntoUser(true);
        Toast.makeText(getApplicationContext(), "Pedido criado com sucesso", Toast.LENGTH_LONG).show();
        refresh();

    }

    private void savePedidoIntoGroup(final Pedido pedido) {
        String groupKey = Base64Decoder.encoderBase64(pedido.getGrupo());
        final String tipoPedido = pedido.getTipo();
        final ArrayList<String> pedidosList = new ArrayList<>();
        DatabaseReference dbGroup = FirebaseConfig.getFireBase().child("grupos").child(groupKey);
        dbGroup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Grupo grupo = dataSnapshot.getValue(Grupo.class);
                if (tipoPedido.equals("Emprestimos")) {
                    if (grupo.getEmprestimos() != null) {
                        pedidosList.addAll(grupo.getEmprestimos());
                        pedidosList.add(pedidosList.size(), pedido.getIdPedido());
                        grupo.setEmprestimos(pedidosList);
                    } else {
                        pedidosList.add(0, pedido.getIdPedido());

                    }

                }
                if (tipoPedido.equals("Troca")) {
                    if (grupo.getTrocas() != null) {
                        pedidosList.addAll(grupo.getTrocas());
                        pedidosList.add(pedidosList.size(), pedido.getIdPedido());
                        grupo.setTrocas(pedidosList);
                    } else {
                        pedidosList.add(0, pedido.getIdPedido());

                    }

                }

                if (tipoPedido.equals("Servicos")) {
                    if (grupo.getServicos() != null) {
                        pedidosList.addAll(grupo.getServicos());
                        pedidosList.add(pedidosList.size(), pedido.getIdPedido());
                        grupo.setServicos(pedidosList);
                    } else {
                        pedidosList.add(0, pedido.getIdPedido());

                    }

                }
                if (tipoPedido.equals("Doacoes")) {
                    if (grupo.getDoacoes() != null) {
                        pedidosList.addAll(grupo.getDoacoes());
                        pedidosList.add(pedidosList.size(), pedido.getIdPedido());
                        grupo.setDoacoes(pedidosList);
                    } else {
                        pedidosList.add(0, pedido.getIdPedido());

                    }

                }

                grupo.save();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("erro database " + databaseError);

            }
        });

    }

    ;

    private void pedidoSaveIntoUser(boolean b) {
        if (b) {
            DatabaseReference databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
            databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setId(Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    user.setCreditos(user.getCreditos() - 1);
                    ArrayList<String> totalPedidos = new ArrayList<String>();
                    if (user.getPedidosFeitos() != null) {
                        totalPedidos.addAll(user.getPedidosFeitos());
                        totalPedidos.add(totalPedidos.size(), pedido.getIdPedido());
                        user.setPedidosFeitos(totalPedidos);
                    } else {
                        totalPedidos.add(0, pedido.getIdPedido());
                        user.setPedidosFeitos(totalPedidos);
                    }

                    user.save();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                tagCaptured = data.getStringExtra("result");
                System.out.println("CRIA PEDIDO tag capturada " + tagCaptured);

                if (!tagsCaptured.contains(tagCaptured)) {
                    tagsCaptured.add(tagCaptured);
                } else
                    Toast.makeText(getBaseContext(), "Tag já selecionada", Toast.LENGTH_LONG).show();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                groupCaptured = data.getStringExtra("groupSelected");
                System.out.println("CRIA PEDIDO grupo capturada " + groupCaptured);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void refresh() {
        Intent intent = new Intent(CriaPedidoActivity.this, PedidosActivity.class);
        finish();
        startActivity(intent);
    }

    ;
}





