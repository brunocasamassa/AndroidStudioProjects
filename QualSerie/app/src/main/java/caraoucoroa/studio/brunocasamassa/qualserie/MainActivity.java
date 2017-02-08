package caraoucoroa.studio.brunocasamassa.qualserie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private ImageView imagemExibição;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekBarid);
        imagemExibição = (ImageView) findViewById(R.id.rostoid);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int valorProgresso = progress;

                if(valorProgresso==1){
                    imagemExibição.setImageResource(R.drawable.pouco);
                }
                else if (valorProgresso==2){
                    imagemExibição.setImageResource(R.drawable.medio);
                }
                else if (valorProgresso==3){
                    imagemExibição.setImageResource(R.drawable.medio);
                }
                else if (valorProgresso==4){
                    imagemExibição.setImageResource(R.drawable.susto);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
