package alcoolgasolina.studio.brunocasamassa.frasesdodia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Button botao;
    private TextView fraseSelecionada;

    private String[] frases = {
            "Ter um dia bom ou um dia mau vai depender da sua vontade de transformar os problemas em oportunidades. Tenha um ótimo dia!",
            "Saber encontrar a alegria na alegria dos outros é o segredo da felicidade. Tenha um bom dia.",
            "Quero que esse dia seja para você muito mais que felicidade, que ele tenha as cores do sucesso e faça da sua vida uma vitória constante. Bom dia!",
            "Bom dia! Hoje é o dia da conquista, da superação, de dar a volta por cima. Tenha fé!",
            "Hoje seu dia será especial. Muito melhor que ontem e um aprendizado para amanhã. Hoje você tem a oportunidade de fazer as coisas diferentes. Tenha um ótimo dia!"};


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fraseSelecionada = (TextView) findViewById(R.id.textView);
        botao = (Button) findViewById(R.id.button);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Random randomico = new Random();

                int numeroAleatorio = randomico.nextInt(frases.length);

                fraseSelecionada.setText(frases[numeroAleatorio]);

            }


        });
    }}
