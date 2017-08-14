
package studio.brunocasamassa.ajudaquioficial.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaquioficial.R;
import studio.brunocasamassa.ajudaquioficial.helper.Preferences;
import studio.brunocasamassa.ajudaquioficial.helper.User;


/**
 * Created by bruno on 26/06/2017.
 */


public class RankingAdapter extends ArrayAdapter<User> {
    private ArrayList<User> usersGroup;
    private Context context;
    private StorageReference storage;
    private Preferences preferences;


    public RankingAdapter(Context c, ArrayList<User> objects) {
        super(c, 0, objects);
        this.usersGroup = objects;
        this.context = c;
    }


    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View view = null;

        // Verifica se a lista está preenchida
        if (usersGroup != null) {

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.model_ranking, parent, false);

            // recupera elemento para exibição
            TextView ranked_place = (TextView) view.findViewById(R.id.ranked_place);
            TextView nome = (TextView) view.findViewById(R.id.ranking_name);
            TextView pontos = (TextView) view.findViewById(R.id.ranking_points);

            User usuario = usersGroup.get(position);
            System.out.println("user no adapter " + usuario.getName());
            nome.setText(usuario.getName());
            pontos.setText(String.valueOf(usuario.getPontos()));
            ranked_place.setText(String.valueOf(position+1));
        }
        ;



        return view;
    }

}


