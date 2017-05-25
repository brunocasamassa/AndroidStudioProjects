package studio.brunocasamassa.ajudaaqui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;

/**
 * Created by bruno on 24/05/2017.
 */


public class TagsList extends AppCompatActivity {
    private ListView tagView;
    private ArrayAdapter tagAdapter;
    private ArrayList<String> tags;
    private DatabaseReference tagsRefs;
    public String selectedTag;
    private ProgressDialog dialog = null;

    @Override
    protected void onDestroy() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taglist);

        tags = new ArrayList<>();
        tagView = (ListView) findViewById(R.id.tagsList);

        tagsRefs = FirebaseConfig.getFireBase();
        tagsRefs.child("tags").child("categorias");
        dialog.show(TagsList.this, "Por favor aguarde", "Recebendo Tags...", true);
        tagsRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dados : dataSnapshot.child("tags").getChildren()) {


                    System.out.println("TAG ADAPTER " + tagAdapter);

                    /*new Thread(new Runnable() {
                        @Override
                        public void run()
                        {*/
                            // do the thing that takes a long time
                            System.out.println("tag EXTRAIDA NO taglist " + dados.getValue());
                            dados.getValue();
                            ArrayList tagss = (ArrayList) dados.getValue();
                            tags.addAll(tagss);
                            tagAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2,
                                    android.R.id.text1,
                                    tags);

                          /*  runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }).start();*/

                    tagView.setAdapter(tagAdapter);

                    System.out.println("TAG VIEW " + tagView);

                    tagView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            selectedTag = (String) tagView.getItemAtPosition(position);

                            System.out.println("seleceted tag " + selectedTag);
                            Intent intent = new Intent(TagsList.this, CriaPedidoActivity.class);
                            intent.putExtra("result", selectedTag);
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
        dialog.dismiss();

    }

    public String getSelectedTag() {
        return selectedTag;
    }

    public void setSelectedTag(String selectedTag) {
        this.selectedTag = selectedTag;
    }
}

