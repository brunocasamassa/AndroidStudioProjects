package studio.brunocasamassa.superchat.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import studio.brunocasamassa.superchat.R;

import static android.R.layout.activity_list_item;
import static android.R.layout.simple_list_item_2;

/**
 * A simple {@link Fragment} subclass.
 */


public class ContatosFragment extends Fragment {

    private ListView listview_nomes;
    private ArrayList<String> arraylist_nomes;
    private ArrayAdapter<String> adapter_nomes = null;
    private DatabaseReference firebaseDatabase;


    public ContatosFragment() {
        // Required empty public constructor
    }


    public String insertContact(String nomeContato) {
        arraylist_nomes = new ArrayList<String>();
        arraylist_nomes.add(nomeContato);
        adapter_nomes = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                arraylist_nomes);
        return nomeContato;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (adapter_nomes == null) {
            insertContact("teste");
        }

        listview_nomes = (ListView) getActivity().findViewById(R.id.ListContatos);
        System.out.println(adapter_nomes);
        listview_nomes.setAdapter(adapter_nomes);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contatos, container, false);

    }


}
