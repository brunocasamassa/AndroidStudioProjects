package opt2flow.com.br.magolandiaapp.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import opt2flow.com.br.magolandiaapp.R;

/**
 * Created by Caio on 28/01/2017.
 */

public class ConfiguracoesTab extends Fragment {

    private ViewPager mViewPager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.configuracoes_tab, container, false);
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setTitle("Logar como Administrador");
        final EditText input = new EditText(rootView.getContext());

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                password = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        */
        final EditText passwordEditText = (EditText) rootView.findViewById(R.id.passwordEditText);

        Button logarButton = (Button) rootView.findViewById(R.id.logarButton);
        logarButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                if(password.equals("opt2flow")){
                    Toast.makeText(rootView.getContext(), "Configurações", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(rootView.getContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(rootView.getContext(), "Senha inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
}