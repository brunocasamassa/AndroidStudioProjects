package studio.brunocasamassa.ajudaaqui.adapters;

/**
 * Created by bruno on 09/05/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;


public class AllGroupsAdapter extends ArrayAdapter<Grupo> {

    private ArrayList<Grupo> grupos;
    private Context context;

    public AllGroupsAdapter(Context c, ArrayList<Grupo> objects) {
        super(c, 0, objects);
        this.grupos = objects;
        this.context = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está vazia
        if( grupos != null ){

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.model_group, parent, false);

            // recupera elemento para exibição
            TextView nomeGrupo = (TextView) view.findViewById(R.id.nomeGrupo);
            TextView qtdMmebros = (TextView) view.findViewById(R.id.qtd_membros);

            Grupo grupo = grupos.get( position );
            nomeGrupo.setText( grupo.getNome());
            qtdMmebros.setText( String.valueOf(grupo.getQtdMembros()));

        }

        return view;

    }
}