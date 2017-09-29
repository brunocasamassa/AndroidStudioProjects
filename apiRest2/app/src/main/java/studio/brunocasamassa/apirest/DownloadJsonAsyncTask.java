package studio.brunocasamassa.apirest;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruno on 22/03/2017.
 */

public class DownloadJsonAsyncTask extends AsyncTask {

    ProgressDialog dialog;

    @Override
    protected Object doInBackground(Object[] params) {

        String urlString = (String) params[0];

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(urlString);

        try {
            HttpResponse response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                String json = toString(instream);


                instream.close();

                List pessoas = getPessoas(json);

                return pessoas;
            }
        } catch (Exception e) {
            Log.e("DEVMEDIA", "Falha ao acessar Web service", e);
        }
        return null;
    }

    private List getPessoas(String jsonString) {

        List pessoas = new ArrayList();

        try {
            JSONArray trendLists = new JSONArray(jsonString);
            JSONObject trendList = trendLists.getJSONObject(0);
            JSONArray trendsArray = trendList.getJSONArray("trends");

            JSONObject pessoa;

            for (int i = 0; i < trendsArray.length(); i++) {
                pessoa = new JSONObject(trendsArray.getString(i));

                Log.i("DEVMEDIA", "nome=" + pessoa.getString("name"));

                Pessoa objetoPessoa = new Pessoa();
                objetoPessoa.nome = pessoa.getString("name");
                objetoPessoa.url = pessoa.getString("url");

                pessoas.add(objetoPessoa);
            }
        } catch (JSONException e) {
            Log.e("DEVMEDIA", "Erro no parsing do JSON", e);
        }

        return pessoas;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        dialog = ProgressDialog.show(null, "Aguarde",
                "Baixando JSON, Por Favor Aguarde...");
    }



    @Override
    protected void onPostExecute(List result) {
        super.onPostExecute(result);
        dialog.dismiss();
        if (result.size() > 0) {
            ArrayAdapter adapter = new ArrayAdapter(ConsumirJson.class,android.R.layout.simple_list_item_1, result);
            setAdapter(adapter);

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    ConsumirJson.this).setTitle("Atenção")
                    .setMessage("Não foi possivel acessar essas informções...")
                    .setPositiveButton("OK", null);
            builder.create().show();
        }
    }

    private String toString(InputStream is) throws IOException {

        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }
}










