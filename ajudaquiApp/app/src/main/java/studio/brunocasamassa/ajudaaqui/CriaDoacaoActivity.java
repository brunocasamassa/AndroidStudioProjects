package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import me.gujun.android.taggroup.TagGroup;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.Pedido;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 22/05/2017.
 */

public class CriaDoacaoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText pedidoName;
    private EditText descricao;
    private Button createButton;
    private String tagCaptured;
    ArrayList<String> tagsCaptured = new ArrayList<String>();
    private TagContainerLayout categorias;
    private TagGroup grupos;
    private TextView add_grupos;
    private TextView add_tags;
    private Pedido pedido;
    private int premium;
    private ImageButton addTagButton;
    private ImageButton addGroupButton;
    private String tipoPedido;   //doacao, servico, troca, emprestimo
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private String groupKey;
    private EditText doacaoQtd;
    private int qtd;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_doacao);
        final Bundle extras = getIntent().getExtras();


        groupKey = extras.getString("groupKey");

        System.out.println("group key " + groupKey);

        toolbar = (Toolbar) findViewById(R.id.toolbar_create_group);
        toolbar.setTitle("Criar Pedido");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        grupos = (TagGroup) findViewById(R.id.tagGroupGruposDoacao);

        doacaoQtd = (EditText) findViewById(R.id.qtdProdutos);
        add_grupos = (TextView) findViewById(R.id.word_add_groups);
        add_tags = (TextView) findViewById(R.id.word_add_tags);
        pedidoName = (EditText) findViewById(R.id.create_pedido_name);
        descricao = (EditText) findViewById(R.id.create_pedido_description);
        createButton = (Button) findViewById(R.id.create_pedido_button);
        addTagButton = (ImageButton) findViewById((R.id.add_tag_button));
        addGroupButton = (ImageButton) findViewById((R.id.addGroup_tag_button));

        doacaoQtd.setText("3");

        qtd = Integer.valueOf(doacaoQtd.getText().toString());

        System.out.println("taggroup " + grupos.getTags());

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (pedidoName.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Insira um Titulo para o pedido", Toast.LENGTH_LONG).show();
                    return;
                } else if (descricao.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Insira uma descricao para o pedido", Toast.LENGTH_LONG).show();
                    return;
                } else createPedido();
            }

        });

    }

    private void createPedido() {

        for (int i = 0; i < qtd; i++) {

            pedido = new Pedido();
            System.out.println("tipo de pedido: " + tipoPedido);
            pedido.setTagsCategoria(tagsCaptured);
            pedido.setTipo("Doacoes");

            pedido.setGrupo(groupKey);

            pedido.setDescricao(descricao.getText().toString());
            pedido.setTitulo(pedidoName.getText().toString());
            pedido.setIdPedido(Base64Decoder.encoderBase64(pedido.getTitulo() + i));
            pedido.setCriadorId(userKey);
            pedido.setGrupo(Base64Decoder.decoderBase64(groupKey));

            if (pedido.getGrupo() != null) {
                savePedidoIntoGroup(pedido);
            }

            System.out.println("user id key " + userKey);

            pedido.save();
            pedidoSaveIntoUser(true);
            Toast.makeText(getApplicationContext(), "Pedido criado com sucesso", Toast.LENGTH_LONG).show();
            refresh();

        }

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

    private void refresh() {
        Intent intent = new Intent(CriaDoacaoActivity.this, PedidosActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        grupos.setTag(Base64Decoder.decoderBase64(groupKey));

    }

    ;
}





