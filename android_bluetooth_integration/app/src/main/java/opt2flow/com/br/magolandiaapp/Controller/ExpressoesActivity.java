package opt2flow.com.br.magolandiaapp.Controller;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import opt2flow.com.br.magolandiaapp.DAO.ExpressaoDAO;
import opt2flow.com.br.magolandiaapp.Model.Expressao;
import opt2flow.com.br.magolandiaapp.R;

public class ExpressoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expressoes);
    }
}
