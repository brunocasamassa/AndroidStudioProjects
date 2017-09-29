package alcoolgasolina.studio.brunocasamassa.atmempresa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;


public class ClientesActivity extends AppCompatActivity {
    private ImageButton home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        home = (ImageButton) findViewById(R.id.imageButton);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClientesActivity.this, MainActivity.class));
            }
        });


}}
