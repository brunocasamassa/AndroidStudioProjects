package studio.brunocasamassa.retrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import JSON.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    private TextView nome;
    private TextView sobrenome;
    private TextView email;
    private TextView endereco;
    private TextView cidade;
    private TextView estado;
    private TextView username;
    private TextView senha;
    private TextView nascimento;
    private TextView telefone;
    private ImageView foto;
    private ProgressDialog load;
    public String gender;
    public Result results;
    public static final String BASE_URL = "https://randomuser.me/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nome = (TextView) findViewById(R.id.nome);
        sobrenome = (TextView) findViewById(R.id.sobrenome);
        email = (TextView) findViewById(R.id.email);
        endereco = (TextView) findViewById(R.id.endereco);
        cidade = (TextView) findViewById(R.id.cidade);
        estado = (TextView) findViewById(R.id.estado);
        username = (TextView) findViewById(R.id.username);
        senha = (TextView) findViewById(R.id.senha);
        nascimento = (TextView) findViewById(R.id.nascimento);
        telefone = (TextView) findViewById(R.id.telefone);
        foto = (ImageView) findViewById(R.id.foto);

        Network network = ServiceGenerator.createService(Network.class);

        Call<User> requestUser = network.randomUser();

        requestUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    ProgressDialog progress = new ProgressDialog(MainActivity2.this);
                    progress.setTitle("sending...");
                    progress.show();
                    User r = response.body();

                    results = r.getResults().get(0);
                    System.out.println("NOME: " + results.getName().getFirst() + " " + results.getName().getLast());


                    if (r != null) {


                        // r.setGender(results.getGender());

                        nome.setText(results.getName().getFirst());
                        sobrenome.setText(results.getName().getLast());
                        email.setText(results.getEmail());
                        // endereco.setText(results.getLocation());
                        // cidade.setText(results.getLocation().);
                        // estado.setText();
                        username.setText(results.getLogin().getUsername());
                        senha.setText(results.getLogin().getPassword());
                        nascimento.setText(results.getDob());
                        telefone.setText(results.getCell());
                        System.out.println("URI " + results.getPicture().getLarge());
                        Picasso.with(getBaseContext()).load(results.getPicture().getLarge()).into(foto);
                        gender = results.getGender();
                        verifyGender(gender);


                        //System.out.println("USER 2: " + response.raw() );


                        //r.setGender(r.getGender());
                        //Results r = new Results();


                        progress.dismiss();

                        //nome.setText(results.gender.toString());


                        //System.out.println("random user: CARAIO  " + user.random);
                        //nome.setText((CharSequence) r.gender);


                    } else
                        Toast.makeText(MainActivity2.this, "ERROR IN GET JSON", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("HTTP ERROR:  + " + t.getCause().toString());
            }


        });


    }

    private void verifyGender(String gender) {

        View v = findViewById(R.id.mainLayout);
        System.out.println("GENERO CARAIO: " + gender);

        if (gender.equals("female")) {
            v.setBackgroundColor(ContextCompat.getColor(MainActivity2.this, R.color.colorAccent));
        } else
            v.setBackgroundColor(ContextCompat.getColor(MainActivity2.this, R.color.colorPrimary));
    }
}