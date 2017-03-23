package studio.brunocasamassa.apirest;

/**
 * Created by bruno on 22/03/2017.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by allanromanato on 11/4/15.
 */
public class Utils {

    public PessoaObj getInformacao(String repoUrl){
        String json;
        PessoaObj retorno;
        System.out.println("Repourl CARAIO: "+ repoUrl);
        json = NetworkUtils.getJSONFromAPI(repoUrl);
        System.out.println("RESULTADO CARAIO: "+ json);
        retorno = parseJson(json);

        return retorno;
    }

    private PessoaObj parseJson(String json){
        try {
            PessoaObj pessoa = new PessoaObj();

            JSONObject jsonObj = new JSONObject(json);
            JSONArray array = jsonObj.getJSONArray("results");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date data;

            JSONObject objArray = array.getJSONObject(0);

            JSONObject obj = objArray;//.getJSONObject("user");

            //Atribui os objetos que estão nas camadas mais altas
            pessoa.setEmail(obj.getString("email"));
            System.out.println("EMAIL CARAIO: "+ pessoa.getEmail());
            pessoa.setUsername(obj.getJSONObject("login").getString("username"));
            System.out.println("USERNAME CARAIO: "+ pessoa.getUsername());
            pessoa.setSenha(obj.getJSONObject("login").getString("password"));
            System.out.println("SENHA CARAIO: "+ pessoa.getSenha());
            pessoa.setTelefone(obj.getString("phone"));
            System.out.println("PHONE CARAIO: "+ pessoa.getTelefone());
            data = new Date(obj.getLong("dob")*1000);
            pessoa.setNascimento(sdf.format(data));

            //Nome da pessoa é um objeto, instancia um novo JSONObject
            JSONObject nome = obj.getJSONObject("name");
            pessoa.setNome(nome.getString("first"));
            pessoa.setSobrenome(nome.getString("last"));

            //Endereco tambem é um Objeto
            JSONObject endereco = obj.getJSONObject("location");
            pessoa.setEndereco(endereco.getString("street"));
            pessoa.setEstado(endereco.getString("state"));
            pessoa.setCidade(endereco.getString("city"));

            //Imagem eh um objeto
            JSONObject foto = obj.getJSONObject("picture");
            pessoa.setFoto(baixarImagem(foto.getString("large")));

            return pessoa;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap baixarImagem(String url) {
        try{
            URL endereco;
            InputStream inputStream;
            Bitmap imagem; endereco = new URL(url);
            inputStream = endereco.openStream();
            imagem = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return imagem;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
