package studio.brunocasamassa.superchat.helper;

/**
 * Created by bruno on 01/03/2017.
 */

import com.google.firebase.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//static for no instantiation

public class FirebaseConfig {

    private static DatabaseReference referenciaFirebase;

    public static DatabaseReference getFireBase() {

        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }


}
