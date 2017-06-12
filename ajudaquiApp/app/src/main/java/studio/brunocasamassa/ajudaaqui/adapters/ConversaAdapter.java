package studio.brunocasamassa.ajudaaqui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.helper.Conversa;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.User;


public class ConversaAdapter extends ArrayAdapter<Conversa> {

    private DatabaseReference firebase;
    private ArrayList<Conversa> conversas;
    private Context context;
    private StorageReference storage;

    public ConversaAdapter(Context c, ArrayList<Conversa> objects) {
        super(c, 0, objects);
        this.context = c;
        this.conversas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        // Verifica se a lista está preenchida
        if( conversas != null ){

            // inicializar objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Monta view a partir do xml
            view = inflater.inflate(R.layout.model_chat, parent, false);

            // recupera elemento para exibição
            final CircleImageView img = (CircleImageView) view.findViewById(R.id.img_chat_contact);
            TextView nome = (TextView) view.findViewById(R.id.name_chat_contact);
            TextView ultimaMensagem = (TextView) view.findViewById(R.id.last_message);

            Conversa conversa = conversas.get(position);
            nome.setText( conversa.getNome() );
            ultimaMensagem.setText( conversa.getMensagem() );
            String idUser = conversa.getIdUsuario();

            firebase = FirebaseConfig.getFireBase().child("usuarios").child(idUser);

            storage = FirebaseConfig.getFirebaseStorage().child("userImages");

            storage.child(idUser+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getContext()).load(uri).override(68,68).into(img);
                    System.out.println("user image chat "+  uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);

                            if (user.getProfileImg() !=  null || user.getProfileImageURL() != null){
                                Glide.with(getContext()).load(user.getProfileImg()).into(img);
                                System.out.println("user image chat "+ user.getProfileImg());

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });


        }

        return view;
    }
}
