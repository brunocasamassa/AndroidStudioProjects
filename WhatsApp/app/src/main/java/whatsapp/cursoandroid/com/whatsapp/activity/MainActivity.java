package whatsapp.cursoandroid.com.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import whatsapp.cursoandroid.com.whatsapp.R;
import whatsapp.cursoandroid.com.whatsapp.config.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
