package studio.brunocasamassa.ajudaaqui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;

/**
 * Created by bruno on 24/05/2017.
 */


public class PedidoAddGroupsList extends AppCompatActivity {
    private ListView groupView;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private ArrayAdapter groupAdapter;
    private ArrayList<String> groups;
    private DatabaseReference groupsRefs;
    public String selectedGroup;
    private ProgressDialog dialog = null;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taglist);

        groups = new ArrayList<>();
        groupView = (ListView) findViewById(R.id.tagsList);

        groupsRefs = FirebaseConfig.getFireBase();
        groupsRefs.child("usuarios").child(userKey);
        //dialog.show(TagsList.this, "Por favor aguarde", "Recebendo Tags...", true);
        groupsRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dados : dataSnapshot.child("usuarios").child(userKey).child("grupos").getChildren()) {
                    System.out.println("group ADAPTER " + groupAdapter);

                    System.out.println("group EXTRAIDA NO grouplist " + dados.getValue());
                    dados.getValue();
                    String tagss = Base64Decoder.decoderBase64((String) dados.getValue());
                    groups.add(tagss);
                    groupAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2,
                            android.R.id.text1,
                            groups);


                    groupView.setAdapter(groupAdapter);

                    System.out.println("GROUP VIEW " + groupView);

                    groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectedGroup = (String) groupView.getItemAtPosition(position);

                            System.out.println("seleceted group " + selectedGroup);
                            Intent intent = new Intent(PedidoAddGroupsList.this, CriaPedidoActivity.class);
                            intent.putExtra("groupSelected", selectedGroup);
                            setResult(Activity.RESULT_OK, intent);
                            finish();

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        // dialog.dismiss();

    }

    public String getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }
}

