package studio.brunocasamassa.ajudaaqui.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.ConfiguracoesActivity;
import studio.brunocasamassa.ajudaaqui.ChatActivity;
import studio.brunocasamassa.ajudaaqui.GruposActivity;
import studio.brunocasamassa.ajudaaqui.PedidosActivity;
import studio.brunocasamassa.ajudaaqui.SobreActivity;

/**
 * Created by bruno on 24/04/2017.
 */

public class NavigationDrawer extends AppCompatActivity{



    //NAVIGATION DRAWER
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    Activity setClasse = new Activity();

    public void createDrawer(Activity classe, android.support.v7.widget.Toolbar toolbar){
        setClasse = classe;
        //Itens do Drawer
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_pedidos);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.menu_chats);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.menu_grupos);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.menu_perfil);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.menu_configuracoes);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName(R.string.menu_sobre);

        // Create the Navigation Drawer AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(classe)
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

        //Definition Drawer
        Drawer drawer = new DrawerBuilder()
                .withActivity(classe)
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
                        item5,
                        new DividerDrawerItem(),//Divisor
                        item6
                        //Divisor
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 1){
                            startActivity(new Intent(setClasse.getParent(),PedidosActivity.class));
                        }
                        if (position == 3){
                            startActivity(new Intent(setClasse.getParent(),ChatActivity.class));
                        }
                        if (position == 5){
                            startActivity(new Intent(NavigationDrawer.this,GruposActivity.class));
                        }
                        if (position == 7){
                            startActivity(new Intent(NavigationDrawer.this,ConfiguracoesActivity.class));
                        }
                        if (position == 9){
                            startActivity(new Intent(NavigationDrawer.this,SobreActivity.class));
                        }
                        return false;
                    }
                })
                .withSelectedItemByPosition(0)
                .build();
    }


}
