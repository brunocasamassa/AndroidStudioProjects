package studio.brunocasamassa.ajudaaqui.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import studio.brunocasamassa.ajudaaqui.fragments.PedidosDisponiveisFragment;
import studio.brunocasamassa.ajudaaqui.fragments.PedidosEscolhidosFragment;
import studio.brunocasamassa.ajudaaqui.fragments.PedidosMeusPedidosFragment;


public class PedidosTabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"DISPONIVEIS", "ESCOLHIDOS", "MEUS PEDIDOS"};

    public PedidosTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new PedidosDisponiveisFragment();
                break;
            case 1:
                fragment = new PedidosEscolhidosFragment();
                notifyDataSetChanged();
                break;

            case 2:
                fragment = new PedidosMeusPedidosFragment();
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