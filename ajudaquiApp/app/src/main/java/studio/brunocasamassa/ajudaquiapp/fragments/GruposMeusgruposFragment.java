package studio.brunocasamassa.ajudaquiapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaquiapp.GrupoAbertoActivity;
import studio.brunocasamassa.ajudaquiapp.R;
import studio.brunocasamassa.ajudaquiapp.adapters.MyGroupsAdapter;
import studio.brunocasamassa.ajudaquiapp.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquiapp.helper.Grupo;
import studio.brunocasamassa.ajudaquiapp.helper.User;

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
    private GridView listView;
    private ValueEventListener valueEventListenerAllGroups;

    public static User usuario = new User();
    private int premium;

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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_mygroups, container, false);
        grupos = new ArrayList<>();


        listView = (GridView) view.findViewById(R.id.mygroups_list);

        adapter = new MyGroupsAdapter(getActivity(), grupos);
        listView.setAdapter(adapter);

       /* fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CriaGrupoActivity.class));
            }
        });
*/

        final String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail()).toString();

        dbGroups = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
        dbGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usuario.setGrupos(user.getGrupos());
                usuario.setName(user.getName());
                    premium = user.getPremiumUser();
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
                intent.putExtra("premium", premium);
                intent.putExtra("uri", grupo.getGrupoImg());
                intent.putExtra("nome", grupo.getNome());
                intent.putExtra("qtdmembros", String.valueOf(grupo.getQtdMembros()));
                intent.putExtra("descricao", grupo.getDescricao());
                startActivity(intent);

            }
        });


        return view;


    }
}
