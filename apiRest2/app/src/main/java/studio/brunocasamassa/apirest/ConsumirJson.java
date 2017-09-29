package studio.brunocasamassa.apirest;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by bruno on 22/03/2017.
 */

public class ConsumirJson extends ListActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DownloadJsonAsyncTask().execute("https://api.twitter.com/1.1/account/settings.json");
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Pessoa pessoa = (Pessoa) l.getAdapter().getItem(position);

        Intent intent = new Intent(this, InfoActivity.class);
        //Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(pessoa.url));
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);

    }




}

