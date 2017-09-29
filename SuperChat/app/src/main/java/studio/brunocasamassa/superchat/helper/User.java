package studio.brunocasamassa.superchat.helper;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

/**
 * Created by bruno on 01/03/2017.
 */

public class User implements DatabaseReference.CompletionListener {

    private String nome;
    private String senha;
    private String email;
    private String id;

    public User(){
    }

    public void save (){

        DatabaseReference referenciaFirebase =  FirebaseConfig.getFireBase();
        referenciaFirebase.child("usuarios").child( getId() ).setValue( this );

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

    }
}
