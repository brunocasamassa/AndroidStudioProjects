package opt2flow.com.br.magolandiaapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import opt2flow.com.br.magolandiaapp.Controller.NovaExpressaoActivity;
import opt2flow.com.br.magolandiaapp.Model.Expressao;
import opt2flow.com.br.magolandiaapp.R;

import static opt2flow.com.br.magolandiaapp.R.drawable.button_add_round;

/**
 * Created by Caio on 14/04/2017.
 */

public class ExpressaoAdapter extends BaseAdapter {

    private Context mContext;
    private List<Expressao> expressoes;

    public ExpressaoAdapter(Context mContext, List<Expressao> expressoes) {
        this.mContext = mContext;
        this.expressoes = expressoes;
    }

    @Override
    public int getCount() {
        return this.expressoes.size();
    }

    @Override
    public Object getItem(int i) {
        return this.expressoes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.expressoes.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Button expressao;
        if (view == null) {
            // if it's not recycled, initialize some attributes
            expressao = new Button(mContext);
            expressao.setPadding(8, 8, 8, 8);
            expressao.setText(expressoes.get(i).getNome());
            expressao.setBackgroundResource(R.drawable.button_bg_round);
            expressao.setTextSize(16);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            expressao.setLayoutParams(params);
            expressao.setGravity(Gravity.CENTER);
            expressao.setTextColor(Color.WHITE);
            final Expressao aux = this.expressoes.get(i);
            expressao.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, aux.getNome(), Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();
                    b.putInt("id", aux.getId());
                    b.putString("nome", aux.getNome());
                    b.putString("codigo", aux.getCodigo());
                    b.putBoolean("edit", true);
                    Intent i = new Intent(mContext, NovaExpressaoActivity.class);
                    i.putExtras(b);
                    mContext.startActivity(i);
                }
            });
        } else {
            expressao = (Button) view;
        }
        return expressao;
    }

}
