package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.Grupo;
import studio.brunocasamassa.ajudaaqui.helper.NavigationDrawer;
import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;

/**
 * Created by bruno on 24/04/2017.
 */

public class GrupoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private TextView qtdMembros;
    private TextView groupName;
    private TextView descricao;
    private ImageView groupImg;
    private Grupo grupo;
    private Bundle itens;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);


        qtdMembros = (TextView) findViewById(R.id.qtdMembros);
        groupName = (TextView) findViewById(R.id.groupName);
        descricao = (TextView) findViewById(R.id.grupoDescricao);
        groupImg = (ImageView) findViewById(R.id.groupImg);


        grupo = new Grupo();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {

            grupo.setNome(extra.getString("nome"));
            grupo.setQtdMembros(Integer.valueOf(extra.getString("qtdmembros")));

        }
        grupo.setId(Base64Decoder.encoderBase64(grupo.getNome()));
        groupName.setText(grupo.getNome());
        qtdMembros.setText(String.valueOf(grupo.getQtdMembros()));
        // groupImg.setImageURI();
        groupImg.setImageBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.logo));
        descricao.setText("Descricao exemplo");
        grupo.save();

        //grupo.setGrupoImg(groupImg);

        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(R.string.menu_grupos);
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);

        NavigationDrawer navigator = new NavigationDrawer();
        navigator.createDrawer(GrupoActivity.this, toolbar);

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
                startActivity(new Intent(GrupoActivity.this, MainActivity.class));
                return true;
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
