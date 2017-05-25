package studio.brunocasamassa.ajudaaqui.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import studio.brunocasamassa.ajudaaqui.fragments.GruposDoacoesFragment;
import studio.brunocasamassa.ajudaaqui.fragments.GruposEmprestimosFragment;
import studio.brunocasamassa.ajudaaqui.fragments.GruposMeusgruposFragment;
import studio.brunocasamassa.ajudaaqui.fragments.GruposServicosFragment;
import studio.brunocasamassa.ajudaaqui.fragments.GruposTodosgruposFragment;
import studio.brunocasamassa.ajudaaqui.fragments.GruposTrocasFragment;


public class GrupoAbertoTabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"TROCA", "EMPRESTIMOS","SERVIÇOS","DOAÇÕES"};

    public GrupoAbertoTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new GruposTrocasFragment();
                break;
            case 1:
                fragment = new GruposEmprestimosFragment();
               //notifyDataSetChanged();
                break;
            case 2:
                fragment = new GruposServicosFragment();
                //notifyDataSetChanged();
                break;
            case 3:
                fragment = new GruposDoacoesFragment();
                //notifyDataSetChanged();
                break;



        }

        return fragment;

    }


    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }






}