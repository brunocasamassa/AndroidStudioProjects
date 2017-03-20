package studio.brunocasamassa.superchat.fragments;


import android.app.Activity;
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
    private ArrayAdapter<String> adapter_nomes;
    private DatabaseReference firebaseDatabase;
    private String contactName = "teste";


    public ContatosFragment() {
        // Required empty public constructor
    }


    public String insertContact(String nomeContato) {


        contactName = nomeContato;

        arraylist_nomes = new ArrayList<String>();

        arraylist_nomes.add(contactName);
        System.out.println("nomes_array: " + arraylist_nomes);
        adapter_nomes = new ArrayAdapter<String>(
                getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                arraylist_nomes);

        listview_nomes.setAdapter(adapter_nomes);

        return contactName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_contatos, container, false);






        listview_nomes = (ListView) v.findViewById(R.id.ListContatos);




        // Inflate the layout for this fragment
        return v;

    }




}
