package studio.brunocasamassa.ajudaaqui;

import android.app.Activity;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import me.gujun.android.taggroup.TagGroup;
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
    private DatabaseReference databaseGroups;
    private DatabaseReference databaseCategories;
    private DatabaseReference databaseUsers;
    private DatabaseReference databasePedidos;
    private Grupo grupo;
    private String groupId;
    private Button createButton;
    private static User usuario = new User();
    private String tagCaptured;
    private ArrayList<String> tagsCaptured = new ArrayList<String>();
    private boolean validatedName = true;
    private TagGroup categorias;
    private TagGroup grupos;
    private ChildEventListener childEventListener;
    private Pedido pedido;
    private ImageButton addTagButton;

    @Override
    protected void onStart() {

        super.onStart();
        System.out.println("vamos ver " + tagCaptured);
        categorias.setTags(tagsCaptured);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_pedido);

        toolbar = (Toolbar) findViewById(R.id.toolbar_create_group);
        toolbar.setTitle("Criar Pedido");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //TODO FIX BACK BUG
                startActivity(new Intent(CriaPedidoActivity.this, GruposActivity.class));
                Toast.makeText(getApplicationContext(), "voltei", Toast.LENGTH_LONG).show();
            }
        });
        setSupportActionBar(toolbar);

        categorias = (TagGroup) findViewById(R.id.tagGroupCategoria);
        grupos = (TagGroup) findViewById(R.id.tagGroupGrupos);


        pedidoName = (EditText) findViewById(R.id.create_pedido_name);
        descricao = (EditText) findViewById(R.id.create_pedido_description);
        createButton = (Button) findViewById(R.id.create_pedido_button);
        addTagButton = (ImageButton) findViewById((R.id.add_tag_button));

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
                    pedido = new Pedido();
                    pedido.setTags(tagsCaptured);
                    pedido.setDescricao(descricao.getText().toString());
                    pedido.setTitulo(pedidoName.getText().toString());
                    pedido.setIdPedido(Base64Decoder.encoderBase64(pedido.getTitulo()));
                    FirebaseAuth autenticacao = FirebaseConfig.getFirebaseAuthentication();
                    String userKey = Base64Decoder.encoderBase64(autenticacao.getCurrentUser().getEmail());

            /*databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
            System.out.println("AUTH caraio" + userKey);*/

                    pedido.save();
                    Toast.makeText(getApplicationContext(), "Pedido criado com sucesso", Toast.LENGTH_LONG).show();
                    finish();

                }

            }
        });

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
    }
}





