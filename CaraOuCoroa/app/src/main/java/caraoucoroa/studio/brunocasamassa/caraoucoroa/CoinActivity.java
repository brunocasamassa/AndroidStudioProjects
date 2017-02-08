package caraoucoroa.studio.brunocasamassa.caraoucoroa;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by bruno on 25/01/2017.
 */

public class CoinActivity extends AppCompatActivity{

    private ImageView coin;
    private ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_coin);


        coin = (ImageView) findViewById(R.id.coin);
        back = (ImageView) findViewById(R.id.back);

        Bundle extra = getIntent().getExtras();
        if(extra !=null) {
            String target = extra.getString("opcao");

            if (target.equals("cara")){
                //cara

                coin.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.moeda_cara));
            }else

                coin.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.moeda_coroa));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoinActivity.this, MainActivity.class));
            }
        });
    }
}
