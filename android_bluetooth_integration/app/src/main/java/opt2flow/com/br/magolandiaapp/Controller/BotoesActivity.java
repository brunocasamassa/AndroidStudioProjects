package opt2flow.com.br.magolandiaapp.Controller;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.List;

import opt2flow.com.br.magolandiaapp.Adapter.ExpressaoAdapter;
import opt2flow.com.br.magolandiaapp.DAO.ExpressaoDAO;
import opt2flow.com.br.magolandiaapp.Model.Expressao;
import opt2flow.com.br.magolandiaapp.R;

public class BotoesActivity extends AppCompatActivity {

    private GridView expressionGridView;
    private Button addExpressionButton;
    private List<Expressao> expressoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botoes);

        expressoes = Expressao.buscarExpressoes(getApplicationContext());
        expressionGridView = (GridView) findViewById(R.id.expressionGridView);
        expressionGridView.setAdapter(new ExpressaoAdapter(getApplicationContext(), expressoes));


        addExpressionButton = (Button) findViewById(R.id.addExpressionButton);
        addExpressionButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putBoolean("edit", false);
                Intent i = new Intent(getApplicationContext(), NovaExpressaoActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
        Log.d("Teste", "Teste");
    }

    @Override
    protected void onResume() {
        super.onResume();
        expressoes = Expressao.buscarExpressoes(getApplicationContext());
        expressionGridView.setAdapter(new ExpressaoAdapter(getApplicationContext(), expressoes));
    }
}
