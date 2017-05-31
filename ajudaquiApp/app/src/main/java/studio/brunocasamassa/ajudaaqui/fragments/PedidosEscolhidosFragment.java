package studio.brunocasamassa.ajudaaqui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import studio.brunocasamassa.ajudaaqui.CriaPedidoActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosEscolhidosFragment extends Fragment {
    private int premium;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());


    private FloatingActionButton fab;


    public PedidosEscolhidosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pedidos_escolhidos, container, false);

        DatabaseReference databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                premium = user.getPremiumUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab = (FloatingActionButton) view.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CriaPedidoActivity.class);
                intent.putExtra("premium",premium);
                startActivity(intent);
            }
        });
        return view;


    }



}
