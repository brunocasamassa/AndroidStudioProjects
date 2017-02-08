package caraoucoroa.studio.brunocasamassa.sombichos;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
// TODO: 02/02/2017 IDEA: COUNT TIMES EACH SOUND IS CLICKED AND CHANGE TO AN ANGRY RAW  : SHOWING TO USER LIKE: 3/5

    private ImageView gato;
    private ImageView cao;
    private ImageView macaco;
    private ImageView ovelha;
    private ImageView leao;
    private ImageView vaca;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gato = (ImageView) findViewById(R.id.gatoid);
        cao = (ImageView) findViewById(R.id.caoid);
        macaco = (ImageView) findViewById(R.id.macacoid);
        ovelha = (ImageView) findViewById(R.id.ovelhaid);
        leao = (ImageView) findViewById(R.id.leaoid);
        vaca = (ImageView) findViewById(R.id.vacaid);

        gato.setOnClickListener(this);
        cao.setOnClickListener(this);
        macaco.setOnClickListener(this);
        ovelha.setOnClickListener(this);
        leao.setOnClickListener(this);
        vaca.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()   ){
            case R.id.caoid:
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.cao);
                tocarSom();
                break;
            case R.id.gatoid:
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.gato);
                tocarSom();
                break;
            case R.id.macacoid:
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.macaco);
                tocarSom();
                break;
            case R.id.leaoid:
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.leao);
                tocarSom();
                break;
            case R.id.ovelhaid:
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.ovelha);
                tocarSom();
                break;
            case R.id.vacaid:
                mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.vaca);
                tocarSom();
                break;
        }

    }

    private void tocarSom() {
        if(mediaPlayer != null){
            mediaPlayer.start();
        }
    }
}
