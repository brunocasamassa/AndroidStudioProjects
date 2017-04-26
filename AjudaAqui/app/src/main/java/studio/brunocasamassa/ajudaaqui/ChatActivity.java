package studio.brunocasamassa.ajudaaqui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import studio.brunocasamassa.ajudaaqui.helper.SlidingTabLayout;

/**
 * Created by bruno on 24/04/2017.
 */

public class ChatActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private ListView listview_nomes;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;
    private int posicao;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello); //// TODO: 25/04/2017 criar layout de cada fragment


        toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        toolbar.setTitle(getResources().getString(R.string.menu_chats));
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);


        //TODO navigator.createDrawer(PedidosActivity.this, toolbar);

        //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

        //START NAVIGATION DRAWER -----------------------------------

        //Itens do Drawer
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_pedidos);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.menu_chats);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.menu_grupos);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.menu_perfil);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.menu_sobre);

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(
                        new ProfileDrawerItem().withName("User").withEmail("user@example.com").withIcon(getResources().getDrawable(R.drawable.logo))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //Definição do Drawer
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),//Divisor
                        item2,
                        new DividerDrawerItem(),//Divisor
                        /*DIVISAO COM MENSAGEM new SectionDrawerItem().withName(R.string.section),//Seção*/
                        item3,
                        new DividerDrawerItem(),//Divisor
                        item4,
                        new DividerDrawerItem(),//Divisor
                        item5
                        //Divisor
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        System.out.println("position: "+position +"View: "+view+ "IDRAWER: "+drawerItem);
                        posicao = position;
                        return false;
                    }
                })
                .withSelectedItemByPosition(0)
                .build();
        if (posicao == 1){
            System.out.println("position: "+posicao );
            startActivity(new Intent(ChatActivity.this,PedidosActivity.class));
        }
        if (posicao == 5){
            System.out.println("position: "+posicao );
            startActivity(new Intent(ChatActivity.this,GruposActivity.class));
        }
        if (posicao == 7){
            System.out.println("position: "+posicao );
            startActivity(new Intent(ChatActivity.this,SobreActivity.class));
        }
        if (posicao == 9 ){
            System.out.println("position: "+posicao );
            startActivity(new Intent(ChatActivity.this,SobreActivity.class));
        }
    }
    //END NAVIGATION DRAWER -----------------------------------



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
                return true;
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



}
