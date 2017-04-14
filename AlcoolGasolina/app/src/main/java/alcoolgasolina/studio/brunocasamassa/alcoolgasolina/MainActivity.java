package alcoolgasolina.studio.brunocasamassa.alcoolgasolina;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    private EditText gasosa;
    private EditText cana;
    private Button ok;
    private TextView resposta;
    private ImageView background;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        background = (ImageView) findViewById(R.id.imageView);
        resposta = (TextView) findViewById(R.id.resposta);
        ok = (Button) findViewById(R.id.ok);
        gasosa = (EditText) findViewById(R.id.gasosa_id);
        cana = (EditText) findViewById(R.id.cana_id);

        background.setImageAlpha(50);//darking background

        ok.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {

                                      Toast.makeText(getApplicationContext(), "tks to use", Toast.LENGTH_SHORT).show();

                                      final Double vgas = Double.valueOf(gasosa.getText().toString());
                                      final Double vcana = Double.valueOf(cana.getText().toString());
                                      if (vcana >= vgas - (vgas * 0.3)) {

                                          resposta.setText("BETTER BUY GAS");


                                      } else resposta.setText("BETTER BUY ETHANOL");
                                      ;

                                  }
                              }

        );


    }
}