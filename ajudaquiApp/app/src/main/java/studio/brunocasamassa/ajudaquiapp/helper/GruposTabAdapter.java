package studio.brunocasamassa.ajudaquiapp.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import studio.brunocasamassa.ajudaquiapp.fragments.GruposMeusgruposFragment;
import studio.brunocasamassa.ajudaquiapp.fragments.GruposTodosgruposFragment;


public class GruposTabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"TODOS OS GRUPOS", "MEUS GRUPOS"};



    public GruposTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new GruposTodosgruposFragment();
                break;
            case 1:
                fragment = new GruposMeusgruposFragment();
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