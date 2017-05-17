package studio.brunocasamassa.ajudaaqui.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import studio.brunocasamassa.ajudaaqui.ChatActivity;
import studio.brunocasamassa.ajudaaqui.GruposActivity;
import studio.brunocasamassa.ajudaaqui.PedidosActivity;
import studio.brunocasamassa.ajudaaqui.PerfilActivity;
import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.SobreActivity;

/**
 * Created by bruno on 24/04/2017.
 */

public class NavigationDrawer {


    //NAVIGATION DRAWER
    private DatabaseReference firebaseData;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    //private static Activity setClasse = new Activity();
    //private static MainActivity main;
    private static User usuario = new User();
    public int pivotPosition;
    public Activity pivotClass;
    public String nomeUser;
    private static String idUser;


    public void createDrawer(final Activity classe, final Toolbar toolbar) {
        //setClasse = classe;
        //Itens do Drawer

        FirebaseUser authentication = FirebaseConfig.getFirebaseAuthentication().getCurrentUser();

        final String emailUser = authentication.getEmail();
        System.out.println("email user " + emailUser);
        idUser = Base64Decoder.encoderBase64(emailUser);

        firebaseData = FirebaseConfig.getFireBase().child("usuarios").child(idUser);
        firebaseData.addValueEventListener(new ValueEventListener() {

                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                   System.out.println("DATASNAPSHOT " + dataSnapshot);
                                                   User user = dataSnapshot.getValue(User.class);
                                                   System.out.println("MAIL " + user.getEmail());
                                                   System.out.println("NAME " + user.getName());
                                                   nomeUser = user.getName().toString();
                                                   usuario = user;

                                                   PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_pedidos).withIcon(R.mipmap.pedidos_icon);
                                                   PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.menu_chats).withIcon(R.mipmap.chat_icon);
                                                   PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.menu_grupos).withIcon(R.mipmap.groups_icon);
                                                   PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.menu_perfil).withIcon(R.mipmap.profile_icon);
                                                   PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.menu_sobre).withIcon(R.mipmap.sobre_icon);

                                                   // Create the Navigation Drawer AccountHeader
                                                   AccountHeader headerResult = new AccountHeaderBuilder()
                                                           .withActivity(classe)
                                                           .withHeaderBackground(R.color.colorPrimary)
                                                           .addProfiles(
                                                                   new ProfileDrawerItem().withName(nomeUser).withEmail(emailUser).withIcon(usuario.getProfileImageURL())
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
                                                           }).withSelectedItemByPosition(0)
                                                           .build();


                                               }

                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {

                                               }


                                           }


        );


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
