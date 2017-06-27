package studio.brunocasamassa.ajudaaqui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.adapters.RankingAdapter;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.User;


/**
 * A simple {@link Fragment} subclass.
 */


public class GrupoAbertoRankingFragment extends Fragment {


    private ListView listview_nomes;
    private ArrayList<User> arraylist_nomes = new ArrayList<>();
    private ArrayAdapter<User> adapter_nomes;
    private DatabaseReference firebaseDatabase;
    private String contactName;
    private String idGroup;
    private String nomeGroup;
    private int qtdMembros;
    private ValueEventListener valueEventListenerRankingGroup;
    private Query query;

    @Override
    public void onStart() {
        super.onStart();
        query.addListenerForSingleValueEvent(valueEventListenerRankingGroup);
    }


    public GrupoAbertoRankingFragment() {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        Bundle extra = getActivity().getIntent().getExtras();

        idGroup = Base64Decoder.encoderBase64(extra.getString("nome"));

        nomeGroup = extra.getString("nome");

        qtdMembros = extra.getInt("qtdmembros");

        View v = inflater.inflate(R.layout.fragment_grupos_ranking, container, false);

        listview_nomes = (ListView) v.findViewById(R.id.ranking_list);


        adapter_nomes = new RankingAdapter(getContext(), arraylist_nomes);

        listview_nomes.setDivider(null);

        listview_nomes.setAdapter(adapter_nomes);


        firebaseDatabase = FirebaseConfig.getFireBase().child("usuarios");

        query = firebaseDatabase.orderByChild("pontos");

        valueEventListenerRankingGroup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arraylist_nomes.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User rankedUser = postSnapshot.getValue(User.class);
                    System.out.println("idGROUP "+idGroup);
                    if (rankedUser.getGrupos().contains(idGroup)) {
                        System.out.println("user no ranking "+rankedUser.getName());
                        arraylist_nomes.add(rankedUser);
                    }

                }

                Collections.reverse(arraylist_nomes);
                adapter_nomes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        // Inflate the layout for this fragment
        return v;

    }


}

