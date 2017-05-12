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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaaqui.CriaGrupoActivity;
import studio.brunocasamassa.ajudaaqui.GrupoActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.adapters.AllGroupsAdapter;
import studio.brunocasamassa.ajudaaqui.adapters.MyGroupsAdapter;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.User;

import static studio.brunocasamassa.ajudaaqui.MainActivity.user;

/**
 * A simple {@link Fragment} subclass.
 */
public class GruposMeusgruposFragment extends Fragment {
    private FloatingActionButton fab;
    private DatabaseReference firebase;
    private DatabaseReference dbGroups;
    private ValueEventListener valueEventListenerAllContacts;
    private ArrayAdapter adapter;
    private ArrayList<Grupo> grupos;
    private ListView listView;
    private ValueEventListener valueEventListenerAllGroups;


    public GruposMeusgruposFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addListenerForSingleValueEvent(valueEventListenerAllContacts);


    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerAllContacts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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


        firebase = FirebaseConfig.getFireBase()
                .child("usuarios");


        //Listener para recuperar grupos do usuario
        valueEventListenerAllContacts = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                //grupos.clear();  maybe not needed

                    System.out.println("MEU GRUPOS USER" + dataSnapshot);

                String userKey = dataSnapshot.child("usuarios").child(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getUid()).getKey();
                System.out.println("get children "+dataSnapshot.child("ZmVmZWZlQGhvdC5jb20="));

                User usuario = new User();

                for( DataSnapshot dados : dataSnapshot.child("ZmVmZWZlQGhvdC5jb20=").getChildren()){
                    System.out.println("user class "+ dados.getValue(User.class));
                    //usuario = ;
                    System.out.println("USUARIO POPULADO "+ usuario.getName());
                }

                for (String grupoId : usuario.getGrupos()) {
                    System.out.println("grupos " + grupoId);

                        dbGroups = FirebaseConfig.getFireBase()
                                .child("grupos").child(grupoId);

                        dbGroups.addValueEventListener(valueEventListenerAllGroups);

                        valueEventListenerAllGroups = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Limpar lista
                                grupos.clear();

                                //Listar contatos
                                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                                    Grupo grupo = dados.getValue(Grupo.class);
                                    System.out.println("grupo " + grupo.getNome());
                                    grupos.add(grupo);


                                }

                                adapter.notifyDataSetChanged();

                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };


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
                intent.putExtra("nome", grupo.getNome());
                intent.putExtra("qtdmembros", String.valueOf(grupo.getQtdMembros()));
                intent.putExtra("descricao", grupo.getDescricao());

                startActivity(intent);

            }
        });


        return view;

    }


}
