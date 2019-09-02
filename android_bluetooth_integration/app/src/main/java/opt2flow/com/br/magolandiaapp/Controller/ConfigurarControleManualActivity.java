package opt2flow.com.br.magolandiaapp.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import opt2flow.com.br.magolandiaapp.Model.ManualControl;
import opt2flow.com.br.magolandiaapp.Model.SessionManager;
import opt2flow.com.br.magolandiaapp.R;

public class ConfigurarControleManualActivity extends AppCompatActivity {

    private EditText minSlinderEditText;
    private EditText maxSliderEditText;
    private EditText botao1EditText;
    private EditText botao2EditText;
    private EditText botao3EditText;
    private EditText botao4EditText;
    private EditText botaoPiscaEditText;
    private EditText delayEditText;
    private Button salvarButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_controle_manual);

        sessionManager = new SessionManager(getApplicationContext());
        ManualControl aux = sessionManager.getUserDetails();

        minSlinderEditText = (EditText) findViewById(R.id.minSlinderEditText);
        minSlinderEditText.setText(String.valueOf(aux.getSliderMin()));

        maxSliderEditText = (EditText) findViewById(R.id.maxSliderEditText);
        maxSliderEditText.setText(String.valueOf(aux.getSliderMax()));

        botao1EditText = (EditText) findViewById(R.id.botao1EditText);
        botao1EditText.setText(aux.getButtonOne());

        botao2EditText = (EditText) findViewById(R.id.botao2EditText);
        botao2EditText.setText(aux.getButtonTwo());

        botao3EditText = (EditText) findViewById(R.id.botao3EditText);
        botao3EditText.setText(aux.getButtonThree());

        botao4EditText = (EditText) findViewById(R.id.botao4EditText);
        botao4EditText.setText(aux.getButtonFour());

        botaoPiscaEditText = (EditText) findViewById(R.id.botaoPiscaEditText);
        botaoPiscaEditText.setText(aux.getBlinkButton());

        delayEditText  = (EditText) findViewById(R.id.delayEditText);
        delayEditText.setText(String.valueOf(aux.getDelayPisca()));

        salvarButton = (Button) findViewById(R.id.salvarButton);
        salvarButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                ManualControl manualControl = new ManualControl();
                manualControl.setSliderMin(Integer.parseInt(minSlinderEditText.getText().toString()));
                manualControl.setSliderMax(Integer.parseInt(maxSliderEditText.getText().toString()));
                manualControl.setButtonOne(botao1EditText.getText().toString());
                manualControl.setButtonTwo(botao2EditText.getText().toString());
                manualControl.setButtonThree(botao3EditText.getText().toString());
                manualControl.setButtonFour(botao4EditText.getText().toString());
                manualControl.setBlinkButton(botaoPiscaEditText.getText().toString());
                manualControl.setDelayPisca(Integer.parseInt(delayEditText.getText().toString()));

                sessionManager.createSession(manualControl);
                Toast.makeText(getApplicationContext(), "Configurações Salvas", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
