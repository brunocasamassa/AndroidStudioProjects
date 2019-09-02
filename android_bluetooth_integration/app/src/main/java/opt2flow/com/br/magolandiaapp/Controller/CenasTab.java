package opt2flow.com.br.magolandiaapp.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import opt2flow.com.br.magolandiaapp.R;

/**
 * Created by Caio on 28/01/2017.
 */

public class CenasTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cenas_tab, container, false);
        return rootView;
    }
}