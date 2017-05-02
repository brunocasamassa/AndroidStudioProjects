package studio.brunocasamassa.ajudaaqui.helper;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//static for no instantiation

public class FirebaseConfig {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autenticacao;

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


}
