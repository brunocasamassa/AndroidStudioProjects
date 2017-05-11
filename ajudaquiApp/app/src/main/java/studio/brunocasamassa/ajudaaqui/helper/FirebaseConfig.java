package studio.brunocasamassa.ajudaaqui.helper;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//static for no instantiation

public class FirebaseConfig {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;
    private static StorageReference storage;

    public static DatabaseReference getFireBase() {

        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAuthentication(){
        if(autenticacao == null){
            autenticacao  = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static StorageReference getFirebaseStorage(){

        if(storage == null){
            storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://ajudaqui-d58a0.appspot.com");

        }
        return storage;
    }


}
