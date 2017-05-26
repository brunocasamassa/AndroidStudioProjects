package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;
import studio.brunocasamassa.ajudaaqui.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;
import studio.brunocasamassa.ajudaaqui.helper.User;

/**
 * Created by bruno on 24/04/2017.
 */

public class PerfilActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private int posicao;
    private CircleImageView profileImg;
    private TextView profileName;
    private RecyclerView badges;
    private TextView pontosConquistados;
    private TextView pedidosFeitos;
    private TextView pedidosAtendidos;
    private MainActivity main;
    private CadastroActivity cdrst;
    private ArrayAdapter<String> adapter;
    private ArrayList<Integer> badgesList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        profileImg = (CircleImageView) findViewById(R.id.profileImg);
        profileName = (TextView) findViewById(R.id.profileName);
        pedidosAtendidos = (TextView) findViewById(R.id.perfilPedidosAtendidos);
        pedidosFeitos = (TextView) findViewById(R.id.perfilPedidosFeitos);
        pontosConquistados = (TextView) findViewById(R.id.perfilPontosConquistados);
        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);


       /* LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        badgesList.add(1,"hue");*/

        final String userKey = Base64Decoder.encoderBase64(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        DatabaseReference databaseUsers = FirebaseConfig.getFireBase().child("usuarios").child(userKey);

        databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User usuario = dataSnapshot.getValue(User.class);
                System.out.println("recebe usuario NAME: " + usuario.getName());
                System.out.println("recebe usuario DATA: " + dataSnapshot.getValue());
                if (usuario.getProfileImageURL() != null) {
                    profileImg.setImageURI(usuario.getProfileImageURL());
                }


                profileName.setText(usuario.getName());
                if (usuario.getPedidosAtendidos() != null) {
                    pedidosAtendidos.setText(usuario.getPedidosAtendidos());
                } else pedidosAtendidos.setText(""+0);
                if (usuario.getPedidosFeitos() != null) {
                    pedidosFeitos.setText(""+usuario.getPedidosFeitos().size());
                } else pedidosFeitos.setText(""+0);
                pontosConquistados.setText(usuario.getPontos());
                if (usuario.getProfileImageURL() != null) {
                    Glide.with(PerfilActivity.this).load(usuario.getProfileImageURL()).into(profileImg);
                }
                usuario.setId(userKey);
                usuario.save();
                return;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* badgesList.add(0,2);    //TODO MEDALHAS

        usuario.setMedalhas(badgesList);

        for (int i = 0; i < 10; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.logo));
            if (!usuario.getMedalhas().contains(i)) {
                imageView.setColorFilter(R.color.cardview_shadow_end_color);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            layout.addView(imageView);
        }
*/


        toolbar.setTitle(getResources().getString(R.string.menu_perfil));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        NavigationDrawer navigator = new NavigationDrawer();

        navigator.createDrawer(PerfilActivity.this, toolbar, 7);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                //logoutUser();
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PerfilActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
