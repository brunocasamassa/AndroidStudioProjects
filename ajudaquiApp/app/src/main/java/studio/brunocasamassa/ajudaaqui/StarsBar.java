package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.PedidoCriadoActivity;
import studio.brunocasamassa.ajudaaqui.helper.User;

public class StarsBar extends AppCompatActivity {
    private RatingBar ratingbar1;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_bar);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar1);
        button = (Button) findViewById(R.id.button1);


        Bundle extras = getIntent().getExtras();
        final String atendenteKey = extras.getString("keyAtendente");
        System.out.println("key nas stars " + atendenteKey);

        //Performing action on Button Click
        Drawable progress = ratingbar1.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.BLUE);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                String rating = String.valueOf(ratingbar1.getRating());
                final int ratingPoints = (int) (ratingbar1.getRating() * 2);
                /*Intent intent = new Intent(StarsBar.this, PedidoCriadoActivity.class);
                intent.putExtra("groupSelected", rating);
                setResult(Activity.RESULT_OK, intent);
                */
                DatabaseReference atendenteUser = FirebaseConfig.getFireBase().child("usuarios");

                atendenteUser.child(atendenteKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setPontos(user.getPontos() + ratingPoints);
                        user.setId(atendenteKey);
                        user.save();
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

        });


    }
}