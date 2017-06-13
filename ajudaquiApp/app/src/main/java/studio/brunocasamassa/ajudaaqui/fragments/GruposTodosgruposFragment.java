package studio.brunocasamassa.ajudaaqui.fragments;


import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaaqui.CriaGrupoActivity;
import studio.brunocasamassa.ajudaaqui.GrupoFechadoActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.adapters.MyGroupsAdapter;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.User;


/**
 * A simple {@link Fragment} subclass.
 */


public class GruposTodosgruposFragment extends Fragment {
    private GridView listView;
    private ArrayAdapter adapter;
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());
    private ArrayList<Grupo> grupos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerAllGroups;
    private FloatingActionButton fab;
    private User usuario = new User();
    private String userName = new String();

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

        usuario.setGrupos(GruposMeusgruposFragment.usuario.getGrupos());
        usuario.setName(GruposMeusgruposFragment.usuario.getName());
        System.out.println("grupos do usuario: "+ usuario.getGrupos());
        System.out.println("Nome do usuario2: "+ usuario.getName());

        listView = (GridView) view.findViewById(R.id.allgroups_list);
        adapter = new MyGroupsAdapter(getContext(), grupos);
        //adapter = new AllGroupsAdapter(getActivity(), grupos );
        listView.setAdapter( adapter);

        /*fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (getActivity(), CriaGrupoActivity.class));
            }
        });*/

        firebase = FirebaseConfig.getFireBase()
                .child("grupos");

        //Listener para recuperar contatos
        valueEventListenerAllGroups = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                grupos.clear();

                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    System.out.println("get children allgroups " + dados);

                    Grupo grupo = dados.getValue( Grupo.class );
                    System.out.println("grupo "+ grupo.getNome());
                    //remove user groups
                    if(usuario.getGrupos() == null || !usuario.getGrupos().contains(grupo.getId())) {
                        grupos.add( grupo );

                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.getName();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try{
                Intent intent = new Intent(getActivity(), GrupoFechadoActivity.class);

                // recupera dados a serem passados
                Grupo grupo = grupos.get(position);

                // enviando dados para grupo activity
                if(grupo.getIdAdms() != null) {
                    intent.putExtra("idAdmins", grupo.getIdAdms());
                }
                intent.putExtra("userName", userName);
                intent.putExtra("nome", grupo.getNome() );
                intent.putExtra("qtdmembros", String.valueOf(grupo.getQtdMembros()) );
                intent.putExtra("descricao", grupo.getDescricao() );

                startActivity(intent);
            }
                catch (Exception e){
                    System.out.println("Exception grupos "+ e);
                }

            }
        });

        return view;



    }



}
