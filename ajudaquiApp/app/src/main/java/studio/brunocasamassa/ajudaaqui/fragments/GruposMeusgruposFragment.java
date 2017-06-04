package studio.brunocasamassa.ajudaaqui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import studio.brunocasamassa.ajudaaqui.GrupoAbertoActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.adapters.MyGroupsAdapter;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * A simple {@link Fragment} subclass.
 */

public class GruposMeusgruposFragment extends Fragment {
    private FloatingActionButton fab;
    private DatabaseReference firebase;
    private DatabaseReference dbGroups;
    private ValueEventListener valueEventListenerAll;
    private ArrayAdapter adapter;
    private ArrayList<Grupo> grupos;
    private ListView listView;
    private ValueEventListener valueEventListenerAllGroups;

    public static User usuario = new User();

    public GruposMeusgruposFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addListenerForSingleValueEvent(valueEventListenerAll);

//       dbGroups.addListenerForSingleValueEvent(valueEventListenerAllGroups);


    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerAll);
        //dbGroups.removeEventListener(valueEventListenerAllGroups);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mygroups, container, false);
        grupos = new ArrayList<>();

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        listView = (ListView) view.findViewById(R.id.mygroups_list);

        adapter = new MyGroupsAdapter(getActivity(), grupos);
        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CriaGrupoActivity.class));
            }
        });


        final String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail()).toString();

        dbGroups = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
        dbGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usuario.setGrupos(user.getGrupos());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebase = FirebaseConfig.getFireBase()
                .child("grupos");

        //Listener para recuperar grupos do usuario
        valueEventListenerAll = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                grupos.clear();

                //Listar contatos
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    System.out.println("get children allgroups " + dados);

                    Grupo grupo = dados.getValue(Grupo.class);
                    System.out.println("grupo " + grupo.getNome());
                    if (usuario.getGrupos() != null ) {
                        if (!grupos.contains(grupo) && usuario.getGrupos().contains(grupo.getId())) {
                            grupos.add(grupo);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                grupos.clear();

            }
        };

        System.out.println("grupos usuario fora caraio " + usuario.getGrupos());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), GrupoAbertoActivity.class);

                // recupera dados a serem passados
                Grupo grupo = grupos.get(position);

                // enviando dados para grupo activity
                intent.putExtra("nome", grupo.getNome());
                intent.putExtra("qtdmembros", String.valueOf(grupo.getQtdMembros()));
                intent.putExtra("descricao", grupo.getDescricao());

                startActivity(intent);

            }
        });


        return view;


    }
}
