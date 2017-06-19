package studio.brunocasamassa.ajudaaqui.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.view.BezelImageView;

import studio.brunocasamassa.ajudaaqui.ConversasActivity;
import studio.brunocasamassa.ajudaaqui.CriaPedidoActivity;
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
    private int premium;
    public String nomeUser;
    private static String idUser;
    private StorageReference storage;
    private Uri userUri;


    public void createDrawer(final Activity classe, final Toolbar toolbar, final int posicao) {

        storage = FirebaseConfig.getFirebaseStorage().child("userImages");


        storage.child(idUser + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                userUri = uri;
                System.out.println("URI USER no storage " + userUri);

                //Glide.with(classe).load(uri).override(68, 68).into(image);
                System.out.println("my groups lets seee2 " + uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {


            }
        });

        FirebaseUser authentication = FirebaseConfig.getFirebaseAuthentication().getCurrentUser();
        System.out.println("usuario no drawer: " + authentication);

        final String emailUser = authentication.getEmail();
        System.out.println("email user " + emailUser);
        idUser = Base64Decoder.encoderBase64(emailUser);
        System.out.println("URI USER " + userUri);

        try {
            firebaseData = FirebaseConfig.getFireBase().child("usuarios").child(idUser);
            firebaseData.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    System.out.println("DATASNAPSHOT " + dataSnapshot);
                    User user = dataSnapshot.getValue(User.class);
                    System.out.println("MAIL " + user.getEmail());
                    System.out.println("NAME " + user.getName());
                    nomeUser = user.getName().toString();
                    premium = user.getPremiumUser();
                    System.out.println("Premium user drawer in response"+ premium);
                    usuario = user;

                    PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.menu_pedidos).withIcon(R.drawable.pedidos_icon);
                    PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.menu_chats).withIcon(R.drawable.chat_icon);
                    PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName(R.string.menu_grupos).withIcon(R.drawable.groups_icon);
                    PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName(R.string.menu_perfil).withIcon(R.drawable.profile_icon);
                    PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.menu_sobre).withIcon(R.drawable.sobre_icon);

                    // Create the Navigation Drawer AccountHeader

                    AccountHeader headerResult = new AccountHeaderBuilder()
                            .withActivity(classe)
                            .withHeaderBackground(R.drawable.background_navigation_drawer)
                            .addProfiles(
                                    new ProfileDrawerItem().withName(nomeUser).withEmail(emailUser).withIcon(android.R.color.transparent)
                            )

                            .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                                @Override
                                public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                                    return false;
                                }
                            })
                            .withSelectionListEnabled(false)
                            .build();

                    //Definition Drawer
                    Drawer drawer = new DrawerBuilder()
                            .withSliderBackgroundColor(Color.WHITE)
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
                            }).withSelectedItemByPosition(posicao)
                            .build();


                }



                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });
        } catch (Exception e) {

            System.out.println("exception " + e);
            FirebaseAuth.getInstance().signOut();


        }



    }

    //nao me julgue
    private void verifyActivity(Activity classe, int position) {
        if (position == 1) {
            Intent intent = new Intent(classe, PedidosActivity.class);
            classe.startActivity(intent);
        }
        if (position == 3) {
            //Toast.makeText(classe, "Em Breve!", Toast.LENGTH_SHORT).show();
            classe.startActivity(new Intent(classe, ConversasActivity.class));
        }
        if (position == 5) {
            classe.startActivity(new Intent(classe, GruposActivity.class));
        }
        if (position == 7) {
            classe.startActivity(new Intent(classe, PerfilActivity.class));
        }
        if (position == 9) {
            Intent sobreIntent = new Intent(classe, SobreActivity.class);
            sobreIntent.putExtra("NOME",nomeUser) ;
            sobreIntent.putExtra("EMAIL", FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail()) ;
            classe.startActivity(sobreIntent);
        }
    }


}
