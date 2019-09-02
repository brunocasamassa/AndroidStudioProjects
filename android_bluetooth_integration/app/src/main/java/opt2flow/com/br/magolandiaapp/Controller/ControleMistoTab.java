package opt2flow.com.br.magolandiaapp.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import opt2flow.com.br.magolandiaapp.R;

/**
 * Created by Caio on 28/01/2017.
 */

public class ControleMistoTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.controle_misto_tab, container, false);

        final TextView sobrancelhaPosteriorEsquerdaTextView = (TextView) rootView.findViewById(R.id.sobrancelhaPosteriorEsquerdaTextView);
        SeekBar sobrancelhaPosteriorEsquerdaSeekBar = (SeekBar) rootView.findViewById(R.id.sobrancelhaPosteriorEsquerdaSeekBar);
        sobrancelhaPosteriorEsquerdaSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sobrancelhaPosteriorEsquerdaTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView sobrancelhaPosteriorDireitaTextView = (TextView) rootView.findViewById(R.id.sobrancelhaPosteriorDireitaTextView);
        SeekBar sobrancelhaPosteriorDireitaSeekBar = (SeekBar) rootView.findViewById(R.id.sobrancelhaPosteriorDireitaSeekBar);
        sobrancelhaPosteriorDireitaSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sobrancelhaPosteriorDireitaTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView sobrancelhaInteriorEsquerdaTextView = (TextView) rootView.findViewById(R.id.sobrancelhaInteriorEsquerdaTextView);
        SeekBar sobrancelhaInteriorEsquerdaSeekBar = (SeekBar) rootView.findViewById(R.id.sobrancelhaInteriorEsquerdaSeekBar);
        sobrancelhaInteriorEsquerdaSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sobrancelhaInteriorEsquerdaTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView sobrancelhaInteriorDireitaTextView = (TextView) rootView.findViewById(R.id.sobrancelhaInteriorDireitaTextView);
        SeekBar sobrancelhaInteriorDireitaSeekBar = (SeekBar) rootView.findViewById(R.id.sobrancelhaInteriorDireitaSeekBar);
        sobrancelhaInteriorDireitaSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sobrancelhaInteriorDireitaTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView olhoEsquerdoVerticalTextView = (TextView) rootView.findViewById(R.id.olhoEsquerdoVerticalTextView);
        SeekBar olhoEsquerdoVerticalSeekBar = (SeekBar) rootView.findViewById(R.id.olhoEsquerdoVerticalSeekBar);
        olhoEsquerdoVerticalSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                olhoEsquerdoVerticalTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView olhoDireitoVerticalTextView = (TextView) rootView.findViewById(R.id.olhoDireitoVerticalTextView);
        SeekBar olhoDireitoVerticalSeekBar = (SeekBar) rootView.findViewById(R.id.olhoDireitoVerticalSeekBar);
        olhoDireitoVerticalSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                olhoDireitoVerticalTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView olhoEsquerdoHorizontalTextView = (TextView) rootView.findViewById(R.id.olhoEsquerdoHorizontalTextView);
        SeekBar olhoEsquerdoHorizontalSeekBar = (SeekBar) rootView.findViewById(R.id.olhoEsquerdoHorizontalSeekBar);
        olhoEsquerdoHorizontalSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                olhoEsquerdoHorizontalTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView olhoDireitoHorizontalTextView = (TextView) rootView.findViewById(R.id.olhoDireitoHorizontalTextView);
        SeekBar olhoDireitoHorizontalSeekBar = (SeekBar) rootView.findViewById(R.id.olhoDireitoHorizontalSeekBar);
        olhoDireitoHorizontalSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                olhoDireitoHorizontalTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView bocaSeekTextView = (TextView) rootView.findViewById(R.id.bocaSeekTextView);
        SeekBar bocaSeekBar = (SeekBar) rootView.findViewById(R.id.bocaSeekBar);
        bocaSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bocaSeekTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView cantoDaBocaTextView = (TextView) rootView.findViewById(R.id.cantoDaBocaTextView);
        SeekBar cantoDaBocaSeekBar = (SeekBar) rootView.findViewById(R.id.cantoDaBocaSeekBar);
        cantoDaBocaSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cantoDaBocaTextView.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final TextView labioSuperiorTextView = (TextView) rootView.findViewById(R.id.labioSuperiorTextView);
        SeekBar labioSuperiorSeekBar = (SeekBar) rootView.findViewById(R.id.labioSuperiorSeekBar);
        labioSuperiorSeekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                labioSuperiorTextView.setText(String.valueOf(progress));
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
}