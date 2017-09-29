package studio.brunocasamassa.superchat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import studio.brunocasamassa.superchat.fragments.ContatosFragment;
import studio.brunocasamassa.superchat.fragments.ConversaFragment;


public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ConversaFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                notifyDataSetChanged();
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