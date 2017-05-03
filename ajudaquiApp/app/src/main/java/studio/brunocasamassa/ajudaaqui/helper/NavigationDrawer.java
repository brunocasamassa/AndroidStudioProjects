package studio.brunocasamassa.ajudaaqui.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import studio.brunocasamassa.ajudaaqui.MainActivity;
import studio.brunocasamassa.ajudaaqui.PerfilActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.ConfiguracoesActivity;
import studio.brunocasamassa.ajudaaqui.ChatActivity;
import studio.brunocasamassa.ajudaaqui.GruposActivity;
import studio.brunocasamassa.ajudaaqui.PedidosActivity;
import studio.brunocasamassa.ajudaaqui.SobreActivity;

/**
 * Created by bruno on 24/04/2017.
 */

public class NavigationDrawer {


    //NAVIGATION DRAWER
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //private static Activity setClasse = new Activity();
    private static MainActivity main;
    private static User usuario = main.user;
    public int pivotPosition;
    public Activity pivotClass;


    public void createDrawer(final Activity classe, Toolbar toolbar) {
        //setClasse = classe;
        //Itens do Drawer
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_pedidos);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.menu_chats);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.menu_grupos);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.menu_perfil);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.menu_sobre);

        // Create the Navigation Drawer AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(classe)
                .withHeaderBackground(R.color.colorPrimary)
                .addProfiles(
                        new ProfileDrawerItem().withName("User").withEmail("user@example.com").withIcon(usuario.getProfileImageURL())
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
                .withSelectedItemByPosition(pivotPosition)
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
                       pivotClass = classe;
                      pivotPosition = position;
                        verifyActivity(pivotClass, pivotPosition);
                        return false;
                    }
                })
                .build();




    }

            private void verifyActivity(Activity classe, int position) {
                if (position == 1) {
                    // HERE I AM TRYING USING DIFFERENT FORMS TO START THE ACTITIVIES
                    classe.startActivity(new Intent(classe, PedidosActivity.class));
                }
                if (position == 3) {
                    classe.startActivity(new Intent(classe, ChatActivity.class));
                }
                if (position == 5) {
                    classe.startActivity(new Intent(classe, GruposActivity.class));
                }
                if (position == 7) {
                    classe.startActivity(new Intent(classe, PerfilActivity.class));
                }
                if (position == 9) {
                    classe.startActivity(new Intent(classe, SobreActivity.class));
                }
    }


}
