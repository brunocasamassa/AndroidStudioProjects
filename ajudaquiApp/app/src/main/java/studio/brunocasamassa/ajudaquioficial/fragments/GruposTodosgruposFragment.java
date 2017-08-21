package studio.brunocasamassa.ajudaquioficial.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import studio.brunocasamassa.ajudaquioficial.CabineFarturaActivity;
import studio.brunocasamassa.ajudaquioficial.GrupoFechadoActivity;
import studio.brunocasamassa.ajudaquioficial.GruposActivity;
import studio.brunocasamassa.ajudaquioficial.R;
import studio.brunocasamassa.ajudaquioficial.adapters.AllGroupsAdapter;
import studio.brunocasamassa.ajudaquioficial.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquioficial.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquioficial.helper.Grupo;
import studio.brunocasamassa.ajudaquioficial.helper.User;


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
    private ArrayList<String> gruposSolicitadosUser = new ArrayList<>();
    private SwipeRefreshLayout refresh;
    private AllGroupsAdapter groupsAdapter;
    private GruposActivity ga;


    public GruposTodosgruposFragment() {

        // Required empty public constructor
    }

    @Override
    public void onStart() {
        if (ga.getArrayAdapterAllGroups() != null) {
            adapter = ga.getArrayAdapterAllGroups();
        }
        super.onStart();
        firebase.addValueEventListener(valueEventListenerAllGroups);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerAllGroups);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allgroups, container, false);
        ga = (GruposActivity) getActivity();

        grupos = new ArrayList<>();

        refresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        usuario.setGrupos(GruposMeusgruposFragment.usuario.getGrupos());
        usuario.setName(GruposMeusgruposFragment.usuario.getName());
        System.out.println("grupos do usuario: " + usuario.getGrupos());
        System.out.println("Nome do usuario 2: " + usuario.getName());
        userName = usuario.getName();

        listView = (GridView) view.findViewById(R.id.allgroups_list);

        groupsAdapter= new AllGroupsAdapter(getContext(), grupos);

        if (ga.getArrayAdapterAllGroups() != null) {
            adapter = ga.getArrayAdapterAllGroups();
        } else adapter = groupsAdapter;

        ga.setArrayAdapterAllGroups(adapter);

        listView.setAdapter(adapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();

            }
        });

        firebase = FirebaseConfig.getFireBase()
                .child("grupos");

        //Listener para recuperar contatos
        valueEventListenerAllGroups = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //instantiate 'cabine da fartura' position
                Grupo cabine = new Grupo();
                //Limpar lista
                grupos.clear();
                grupos.add(0,cabine);

                //Listar contatos
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    System.out.println("get children allgroups " + dados);

                    Grupo grupo = dados.getValue(Grupo.class);
                    System.out.println("grupo " + grupo.getNome());
                    //remove user groups
                    if (usuario.getGrupos() == null || !usuario.getGrupos().contains(grupo.getId())) {

                        try {
                            if (grupo.getId() != null) {
                                grupos.add(grupo);
                            }
                        } catch (Exception e) {
                            System.out.println("exception " + e);
                        }
                       adapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // recupera dados a serem passados

                if (position == 0) {
                    startActivity(new Intent(getActivity(), CabineFarturaActivity.class));
                }
                // enviando dados para grupo activity
                else {
                    Grupo grupo = groupsAdapter.getGruposFiltrado().get(position);
                    try {
                        Intent intent = new Intent(getActivity(), GrupoFechadoActivity.class);
                        if (grupo.getIdAdms() != null) {
                            intent.putExtra("idAdmins", grupo.getIdAdms());
                        }

                        intent.putExtra("isOpened", grupo.isOpened());
                        intent.putExtra("userName", usuario.getName());
                        intent.putExtra("nome", grupo.getNome());
                        intent.putExtra("groupId", grupo.getId());
                        intent.putExtra("qtdmembros", String.valueOf(grupo.getQtdMembros()));
                        intent.putExtra("descricao", grupo.getDescricao());


                        startActivity(intent);

                    } catch (Exception e) {
                        System.out.println("Exception grupos " + e);
                    }

                }
            }
        });

        return view;


    }



    private void refresh() {
        Intent intent = new Intent(getActivity(), GruposActivity.class);
        getActivity().finish();
        startActivity(intent);
    }


}
