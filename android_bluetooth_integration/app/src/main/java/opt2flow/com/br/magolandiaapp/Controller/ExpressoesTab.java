package opt2flow.com.br.magolandiaapp.Controller;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import opt2flow.com.br.magolandiaapp.Adapter.BotaoAdapter;
import opt2flow.com.br.magolandiaapp.Model.BluetoothArduino;
import opt2flow.com.br.magolandiaapp.Model.Expressao;
import opt2flow.com.br.magolandiaapp.Model.ManualControl;
import opt2flow.com.br.magolandiaapp.Model.SessionManager;
import opt2flow.com.br.magolandiaapp.R;

/**
 * Created by Caio on 28/01/2017.
 */

public class ExpressoesTab extends Fragment {

    private View rootView;
    private GridView botoesGridView;
    private List<Expressao> expressoes;
    private SessionManager sessionManager;
    private ManualControl manualControl;
    private int sliderMin;
    private int sliderMax;

    private SeekBar viraOlhoSeekBar;
    private SeekBar bocaSeekBar;
    private TextView viraOlhoTextView;
    private TextView bocaTextView;
    private Button botaoPiscaButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.expressoes_tab, container, false);

        sessionManager = new SessionManager(rootView.getContext());

        manualControl = sessionManager.getUserDetails();

        botoesGridView = (GridView) rootView.findViewById(R.id.botoesGridView);
        viraOlhoSeekBar = (SeekBar) rootView.findViewById(R.id.viraOlhoSeekBar);
        bocaSeekBar = (SeekBar) rootView.findViewById(R.id.bocaSeekBar);
        viraOlhoTextView  = (TextView) rootView.findViewById(R.id.viraOlhoTextView);
        bocaTextView  = (TextView) rootView.findViewById(R.id.bocaTextView);
        botaoPiscaButton = (Button) rootView.findViewById(R.id.botaoPiscaButton);
        botaoPiscaButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(sessionManager.getPisca()){
                    sessionManager.setPisca(false);
                    botaoPiscaButton.setBackgroundResource(R.drawable.button_bg_round);
                } else {
                    sessionManager.setPisca(true);
                    botaoPiscaButton.setBackgroundResource(R.drawable.button_add_round);
                }
            }
        });

        expressoes = Expressao.buscarExpressoes(rootView.getContext());
        botoesGridView.setAdapter(new BotaoAdapter(rootView.getContext(), expressoes));

        sliderMin = 0;
        if(manualControl.getSliderMin() >= 0){
            sliderMin = manualControl.getSliderMin();
        }
        sliderMax = 180;
        if(manualControl.getSliderMax() >= 0 && manualControl.getSliderMax() > sliderMin){
            sliderMax = manualControl.getSliderMax();
        }

        viraOlhoTextView.setText(String.valueOf(sliderMin));
        viraOlhoSeekBar.setMax(sliderMax - sliderMin);
        viraOlhoSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                viraOlhoTextView.setText(String.valueOf(sliderMin + progress));
                sessionManager.setViraOlho(progress);
                sendSignal(viraOlhoSeekBar, bocaSeekBar);
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
                sendSignal(viraOlhoSeekBar, bocaSeekBar);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            if (isVisibleToUser){
                Log.d("Visible" , "Expre");
                expressoes = Expressao.buscarExpressoes(rootView.getContext());
                botoesGridView.setAdapter(new BotaoAdapter(rootView.getContext(), expressoes));
                int boca = sessionManager.getBoca();
                int viraOlho = sessionManager.getViraOlho();
                bocaSeekBar.setProgress(boca);
                bocaTextView.setText(String.valueOf(sliderMin + boca));
                viraOlhoSeekBar.setProgress(viraOlho);
                viraOlhoTextView.setText(String.valueOf(sliderMin + viraOlho));
                if(!sessionManager.getPisca()){
                    botaoPiscaButton.setBackgroundResource(R.drawable.button_bg_round);
                } else {
                    botaoPiscaButton.setBackgroundResource(R.drawable.button_add_round);
                }
            }
        }

    }


    private void sendSignal(SeekBar viraOlhoSeekBar, SeekBar bocaSeekBar){
        BluetoothArduino bluetoothArduino = BluetoothArduino.getInstance("Personagem");
        if(!bluetoothArduino.isBluetoothEnabled()){
            Toast.makeText(getContext(), "Bluetooth não está ativado !!!", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> rotacoes = new ArrayList<String>();
            rotacoes.add(String.valueOf(sliderMin +bocaSeekBar.getProgress()));
            rotacoes.add(String.valueOf(sliderMin +viraOlhoSeekBar.getProgress()));
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