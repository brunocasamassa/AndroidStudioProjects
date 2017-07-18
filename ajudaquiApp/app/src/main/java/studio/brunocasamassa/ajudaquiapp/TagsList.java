package studio.brunocasamassa.ajudaquiapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import studio.brunocasamassa.ajudaquiapp.helper.FirebaseConfig;

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
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taglist);

        /*Categoria categorias = new Categoria();
        ArrayList<String> txtTags = new ArrayList<String>();

        Scanner scan = null;    //TODO FILE READER TAGS
        try {
            scan = new Scanner(new FileInputStream("C:\\users\\bruno\\Documentos\\BitBucket\\ajudaaqui\\ajudaquiApp\\app\\tags.txt"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("scanner input "+ e);
        }
        int i = 0;
        while (scan.hasNext()) {
            String s = scan.nextLine();
            txtTags.add(i, s);
            i++;
        }

        categorias.setCategorias(tags);
        categorias.save();
*/
        tags = new ArrayList<>();
        tagView = (ListView) findViewById(R.id.tagsList);

        tagsRefs = FirebaseConfig.getFireBase();
        tagsRefs.child("tags").child("categorias");
        //dialog.show(TagsList.this, "Por favor aguarde", "Recebendo Tags...", true);
        tagsRefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot dados : dataSnapshot.child("tags").getChildren()) {
                    System.out.println("TAG ADAPTER " + tagAdapter);

                    System.out.println("tag EXTRAIDA NO taglist " + dados.getValue());
                    dados.getValue();
                    ArrayList tagss = (ArrayList) dados.getValue();
                    tags.addAll(tagss);
                    tagAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_2,
                            android.R.id.text1,
                            tags);

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

    }

    public String getSelectedTag() {
        return selectedTag;
    }

    public void setSelectedTag(String selectedTag) {
        this.selectedTag = selectedTag;
    }
}

