package studio.brunocasamassa.ajudaquioficial;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaquioficial.helper.Base64Decoder;
import studio.brunocasamassa.ajudaquioficial.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaquioficial.helper.Grupo;

/**
 * Created by bruno on 24/05/2017.
 */


public class PedidoAddGroupsList extends AppCompatActivity {
    private ListView groupView;
    private String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private ArrayAdapter groupAdapter;
    private ArrayList<String> groups;
    private ArrayList<String> idGroups;
    private DatabaseReference groupsRefs;
    public String selectedGroup;
    private ProgressDialog dialog = null;
    private TextView title;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taglist);

        groups = new ArrayList<>();
        idGroups = new ArrayList<>();
        groupView = (ListView) findViewById(R.id.tagsList);
        title = (TextView) findViewById(R.id.list_title);
        title.setText("  Selecione um Grupo");
final DatabaseReference dbGroup =FirebaseConfig.getFireBase().child("grupos");
        groupsRefs = FirebaseConfig.getFireBase();
        groupsRefs.child("usuarios").child(userKey);
        //dialog.show(TagsList.this, "Por favor aguarde", "Recebendo Tags...", true);
        groupsRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("usuarios").child(userKey).child("grupos").getChildren() == null){
                    Toast.makeText(getApplicationContext(), "Você não possui nenhum Grupo", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    for (final DataSnapshot dados : dataSnapshot.child("usuarios").child(userKey).child("grupos").getChildren()) {
                        System.out.println("group ADAPTER " + groupAdapter);
                        System.out.println("group EXTRAIDA NO grouplist " + dados.getValue());
                        String idGrupo = dados.getValue().toString();

                        dbGroup.child(idGrupo).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Grupo grupo = dataSnapshot.getValue(Grupo.class);
                                try {
                                    String nomeGrupo = grupo.getNome();
                                    groups.add(nomeGrupo);
                                    idGroups.add(grupo.getId());

                                    System.out.println("GRUPO ADICIONADO " + nomeGrupo);
                                    System.out.println("GRUPO ADICIONADO " + groups.size());
                                } catch (Exception e) {
                                    System.out.println("excepetion " + e);
                                }


                                System.out.println("groups size " + groups.size());
                                groupAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2,
                                        android.R.id.text1,
                                        groups);

                                groupView.setAdapter(groupAdapter);

                                System.out.println("GROUP VIEW " + groupView);

                                groupView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        selectedGroup = (String) groupView.getItemAtPosition(position);
                                        String idSelectedGroup = idGroups.get(position);
                                        System.out.println("selected group " + selectedGroup);
                                        Intent intent = new Intent(PedidoAddGroupsList.this, CriaPedidoActivity.class);
                                        intent.putExtra("groupSelected", selectedGroup);
                                        intent.putExtra("idGroupSelected", idSelectedGroup);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();

                                    }

                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
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

