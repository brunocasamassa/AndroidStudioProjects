package studio.brunocasamassa.ajudaaqui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.brunocasamassa.ajudaaqui.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosDisponiveisFragment extends Fragment {


    public PedidosDisponiveisFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedidos_disponiveis, container, false);


    }



}