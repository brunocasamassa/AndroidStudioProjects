package opt2flow.com.br.magolandiaapp.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import opt2flow.com.br.magolandiaapp.Adapter.BotaoAdapter;
import opt2flow.com.br.magolandiaapp.Model.BluetoothArduino;
import opt2flow.com.br.magolandiaapp.Model.Expressao;
import opt2flow.com.br.magolandiaapp.Model.ManualControl;
import opt2flow.com.br.magolandiaapp.Model.SessionManager;
import opt2flow.com.br.magolandiaapp.Model.Validador;
import opt2flow.com.br.magolandiaapp.R;

/**
 * Created by Caio on 28/01/2017.
 */

public class ControleManualTab extends Fragment {

    private boolean isViewShown = false;
    private View rootView;
    private SessionManager sessionManager;
    private ManualControl manualControl;
    private int sliderMin;
    private int sliderMax;
    private Timer timer;
    private TimerTask timerTask;

    private TextView sobrancelhasTextView;
    private SeekBar sobrancelhasSeekBar;
    private TextView piscaOlhoTextView;
    private SeekBar piscaOlhoSeekBar;
    private TextView bocaTextView;
    private SeekBar bocaSeekBar;
    private TextView viraOlhoTextView;
    private SeekBar viraOlhoSeekBar;
    private TextView sorrisoTextView;
    private SeekBar sorrisoSeekBar;
    private Button botao1Button;
    private Button botao2Button;
    private Button botao3Button;
    private Button botao4Button;
    private Button piscaAutoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.controle_manual_new_tab, container, false);

        sessionManager = new SessionManager(rootView.getContext());

        manualControl = sessionManager.getUserDetails();

        sobrancelhasTextView = (TextView) rootView.findViewById(R.id.sobrancelhasTextView);
        sobrancelhasSeekBar = (SeekBar) rootView.findViewById(R.id.sobrancelhasSeekBar);
        piscaOlhoTextView = (TextView) rootView.findViewById(R.id.piscaOlhoTextView);
        piscaOlhoSeekBar = (SeekBar) rootView.findViewById(R.id.piscaOlhoSeekBar);
        bocaTextView = (TextView) rootView.findViewById(R.id.bocaTextView);
        bocaSeekBar = (SeekBar) rootView.findViewById(R.id.bocaSeekBar);
        viraOlhoTextView = (TextView) rootView.findViewById(R.id.viraOlhoTextView);
        viraOlhoSeekBar = (SeekBar) rootView.findViewById(R.id.viraOlhoSeekBar);
        sorrisoTextView = (TextView) rootView.findViewById(R.id.sorrisoTextView);
        sorrisoSeekBar = (SeekBar) rootView.findViewById(R.id.sorrisoSeekBar);
        botao1Button = (Button) rootView.findViewById(R.id.botao1Button);
        botao1Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validador.sendSignal(manualControl.getButtonOne(), rootView.getContext());
            }
        });
        botao2Button = (Button) rootView.findViewById(R.id.botao2Button);
        botao2Button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Validador.sendSignal(manualControl.getButtonTwo(), rootView.getContext());
            }
        });
        botao3Button = (Button) rootView.findViewById(R.id.botao3Button);
        botao3Button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Validador.sendSignal(manualControl.getButtonThree(), rootView.getContext());
            }
        });
        botao4Button = (Button) rootView.findViewById(R.id.botao4Button);
        botao4Button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Validador.sendSignal(manualControl.getButtonFour(), rootView.getContext());
            }
        });
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                if(sessionManager.getPisca()){
                    Log.d("Rodando", "TRUE");
                    Validador.sendSignal(manualControl.getBlinkButton(), rootView.getContext());
                } else {
                    Log.d("Rodando", "FALSE");
                }
            }
        };
        piscaAutoButton = (Button) rootView.findViewById(R.id.piscaAutoButton);
        piscaAutoButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(sessionManager.getPisca()){
                    sessionManager.setPisca(false);
                    piscaAutoButton.setBackgroundResource(R.drawable.button_bg_round);
                } else {
                    sessionManager.setPisca(true);
                    piscaAutoButton.setBackgroundResource(R.drawable.button_add_round);
                }
            }
        });

        sliderMin = 0;
        if(manualControl.getSliderMin() >= 0){
            sliderMin = manualControl.getSliderMin();
        }
        sliderMax = 180;
        if(manualControl.getSliderMax() >= 0 && manualControl.getSliderMax() > sliderMin){
            sliderMax = manualControl.getSliderMax();
        }

        sobrancelhasTextView.setText(String.valueOf(sliderMin));
        sobrancelhasSeekBar.setMax(sliderMax - sliderMin);
        sobrancelhasSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sobrancelhasTextView.setText(String.valueOf(sliderMin + progress));
                sendSignal(sobrancelhasSeekBar, piscaOlhoSeekBar, bocaSeekBar, viraOlhoSeekBar, sorrisoSeekBar);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        piscaOlhoTextView.setText(String.valueOf(sliderMin));
        piscaOlhoSeekBar.setMax(sliderMax - sliderMin);
        piscaOlhoSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                piscaOlhoTextView.setText(String.valueOf(sliderMin + progress));
                sendSignal(sobrancelhasSeekBar, piscaOlhoSeekBar, bocaSeekBar, viraOlhoSeekBar, sorrisoSeekBar);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bocaTextView.setText(String.valueOf(sliderMin));
        bocaSeekBar.setMax(sliderMax - sliderMin);
        bocaSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bocaTextView.setText(String.valueOf(sliderMin + progress));
                sessionManager.setBoca(progress);
                sendSignal(sobrancelhasSeekBar, piscaOlhoSeekBar, bocaSeekBar, viraOlhoSeekBar, sorrisoSeekBar);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        viraOlhoTextView.setText(String.valueOf(sliderMin));
        viraOlhoSeekBar.setMax(sliderMax - sliderMin);
        viraOlhoSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viraOlhoTextView.setText(String.valueOf(sliderMin + progress));
                sessionManager.setViraOlho(progress);
                sendSignal(sobrancelhasSeekBar, piscaOlhoSeekBar, bocaSeekBar, viraOlhoSeekBar, sorrisoSeekBar);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sorrisoTextView.setText(String.valueOf(sliderMin));
        sorrisoSeekBar.setMax(sliderMax - sliderMin);
        sorrisoSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sorrisoTextView.setText(String.valueOf(sliderMin + progress));
                sendSignal(sobrancelhasSeekBar, piscaOlhoSeekBar, bocaSeekBar, viraOlhoSeekBar, sorrisoSeekBar);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        int boca = sessionManager.getBoca();
        int viraOlho = sessionManager.getViraOlho();
        bocaSeekBar.setProgress(boca);
        bocaTextView.setText(String.valueOf(sliderMin + boca));
        viraOlhoSeekBar.setProgress(viraOlho);
        viraOlhoTextView.setText(String.valueOf(sliderMin + viraOlho));
        if(!sessionManager.getPisca()){
            piscaAutoButton.setBackgroundResource(R.drawable.button_bg_round);
        } else {
            piscaAutoButton.setBackgroundResource(R.drawable.button_add_round);
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            if (isVisibleToUser){
                int boca = sessionManager.getBoca();
                int viraOlho = sessionManager.getViraOlho();
                bocaSeekBar.setProgress(boca);
                bocaTextView.setText(String.valueOf(sliderMin + boca));
                viraOlhoSeekBar.setProgress(viraOlho);
                viraOlhoTextView.setText(String.valueOf(sliderMin + viraOlho));
                if(!sessionManager.getPisca()){
                    piscaAutoButton.setBackgroundResource(R.drawable.button_bg_round);
                } else {
                    piscaAutoButton.setBackgroundResource(R.drawable.button_add_round);
                }
            }
        }
    }

    private void sendSignal(SeekBar sobrancelhasSeekBar, SeekBar piscaOlhoSeekBar, SeekBar bocaSeekBar,
                            SeekBar viraOlhoSeekBar, SeekBar sorrisoSeekBar){
            BluetoothArduino bluetoothArduino = BluetoothArduino.getInstance("Personagem");
            if(!bluetoothArduino.isBluetoothEnabled()){
                Toast.makeText(getContext(), "Bluetooth não está ativado !!!", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<String> rotacoes = new ArrayList<String>();
                rotacoes.add(String.valueOf(sliderMin +sobrancelhasSeekBar.getProgress()));
                rotacoes.add(String.valueOf(sliderMin +piscaOlhoSeekBar.getProgress()));
                rotacoes.add(String.valueOf(sliderMin +bocaSeekBar.getProgress()));
                rotacoes.add(String.valueOf(sliderMin +viraOlhoSeekBar.getProgress()));
                rotacoes.add(String.valueOf(sliderMin +sorrisoSeekBar.getProgress()));
                String msg = "";
                for (int i = 0; i < rotacoes.size(); i++){
                    if(i == (rotacoes.size() - 1)) {
                        msg += i + ":" + rotacoes.get(i);
                    } else {
                        msg += i + ":" + rotacoes.get(i) + "&";
                    }
                }
                Log.d("Full" , msg.trim());
                if(!bluetoothArduino.Connect()){
                    Toast.makeText(getContext(), "Falha na conexão com o Arduino", Toast.LENGTH_SHORT).show();
                } else {
                    bluetoothArduino.SendMessage(msg.trim());
                }
            }
    }
}