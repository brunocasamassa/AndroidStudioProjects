package studio.brunocasamassa.supergascalculator;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText gasosa;
    private EditText cana;
    private Button ok;
    private TextView resposta;
    private ImageView background;

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