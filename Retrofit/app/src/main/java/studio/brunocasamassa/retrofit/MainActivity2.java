package studio.brunocasamassa.retrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import JSON.Results;
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
    public Results results;
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


                    String name = r.getResults().getClass().getName();
                    System.out.println("NOME CARAIO: "+ nome);

                    if (r != null) {

                        System.out.println();

                       // r.setGender(results.getGender());

                        //nome.setText((CharSequence) results.setName(r.getName()));

                        //System.out.println("USER 2: " + response.raw() );


                        //r.setGender(r.getGender());
                        //Results r = new Results();



                        progress.dismiss();

                      //  nome.setText(results.gender.toString());


                        // System.out.println("random user: CARAIO  " + user.random);
                           //nome.setText((CharSequence) r.gender);


                    } else Toast.makeText(MainActivity2.this,"ERROR IN GET JSON",Toast.LENGTH_LONG).show();
                }

                }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
}