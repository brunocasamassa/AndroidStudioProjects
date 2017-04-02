package studio.brunocasamassa.apirest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GetJson download = new GetJson();


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

        //Chama Async Task
        download.execute();

    }

    private class GetJson extends AsyncTask<Void, Void, PessoaObj> {


        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(MainActivity.this, "Por favor Aguarde ...", "Recuperando Informações do Servidor...");


        }

        @Override
        protected PessoaObj doInBackground(Void... params) {
            Utils util = new Utils();
            String url = "https://randomuser.me/api/";
            return util.getInformacao(url);
        }

        @Override
        protected void onPostExecute(PessoaObj pessoa) {
            username.setText(pessoa.getUsername());
            nome.setText(pessoa.getNome().substring(0, 1).toUpperCase() + pessoa.getNome().substring(1));
            sobrenome.setText(pessoa.getSobrenome().substring(0, 1).toUpperCase() + pessoa.getSobrenome().substring(1));
            email.setText(pessoa.getEmail());
            endereco.setText(pessoa.getEndereco());
            cidade.setText(pessoa.getCidade().substring(0, 1).toUpperCase() + pessoa.getCidade().substring(1));
            estado.setText(pessoa.getEstado());
            senha.setText(pessoa.getSenha());
            nascimento.setText(pessoa.getNascimento());
            telefone.setText(pessoa.getTelefone());
            foto.setImageBitmap(pessoa.getFoto());
            load.dismiss();
            gender = pessoa.getGenero();
            verifyGender();

        }

        private void verifyGender() {
            View v = findViewById(R.id.mainLayout);
            System.out.println("GENERO CARAIO: " + gender);

            if (gender.equals("female")) {
                v.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
            } else
                v.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        }

    }
}