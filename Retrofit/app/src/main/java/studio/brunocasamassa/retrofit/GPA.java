package studio.brunocasamassa.retrofit;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by bruno on 12/04/2017.
 */

 class GPA extends Activity {

    public double[] notas = {7.5, 9.0, 6.9, 6.7, 9.0, 9.9, 6.5, 6.5, 7.0, 6.2, 8.0, 7.5, 6.6, 9.1, 6.6, 7.0, 6.4, 6.7, 9.1, 6.0, 7.0, 6.5, 6.0, 9.3, 8.0, 8.0, 9.0, 8.5, 7.0, 6.0, 6.0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int qtdNotas = notas.length;
        System.out.println(qtdNotas);
        double[] notasGPA = new double[qtdNotas];
        double mediaGeralGPA = 0;

        for (int i = 0; i < qtdNotas; i++) {
            notasGPA[i] = ((notas[i] * 10.0) / 20) - 1.0;
            System.out.println("nota GPA materia " + i + " : " + notasGPA[i]);

        }

        for (int j = 0; j < qtdNotas; j++) {
            mediaGeralGPA = mediaGeralGPA + notasGPA[j];

            if (j == qtdNotas-1) {
                System.out.println("Soma notas GPA: " + mediaGeralGPA);
                mediaGeralGPA = mediaGeralGPA / qtdNotas;
                System.out.println("MINHA MEDIA GERAL GPA: " + mediaGeralGPA);
            }
        }

    }

}
