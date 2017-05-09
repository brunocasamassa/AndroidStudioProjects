package studio.brunocasamassa.ajudaaqui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaaqui.CriaGrupoActivity;
import studio.brunocasamassa.ajudaaqui.GrupoActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.adapters.AllGroupsAdapter;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;

/**
 * A simple {@link Fragment} subclass.
 */


public class GruposTodosgruposFragment extends Fragment {
    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Grupo> grupos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerAllGroups;
    private FloatingActionButton fab;

    public GruposTodosgruposFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener( valueEventListenerAllGroups );
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener( valueEventListenerAllGroups );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allgroups, container, false);
        grupos = new ArrayList<>();

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listView = (ListView) view.findViewById(R.id.allgroups_list);

        adapter = new AllGroupsAdapter(getActivity(), grupos );
        listView.setAdapter( adapter );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (getActivity(),CriaGrupoActivity.class));
            }
        });

        firebase = FirebaseConfig.getFireBase()
                .child("grupos");

        //Listener para recuperar contatos
        valueEventListenerAllGroups = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                grupos.clear();

                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Grupo grupo = dados.getValue( Grupo.class );
                    System.out.println("grupo "+ grupo.getNome());
                    grupos.add( grupo );


                }

                adapter.notifyDataSetChanged();

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), GrupoActivity.class);

                // recupera dados a serem passados
                Grupo grupo = grupos.get(position);

                // enviando dados para grupo activity
                intent.putExtra("nome", grupo.getNome() );
                intent.putExtra("qtdmembros", grupo.getQtdMembros() );

                startActivity(intent);

            }
        });

        return view;



    }



}
