
package caraoucoroa.studio.brunocasamassa.signos;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.Toast;


public class SignosActivity extends AppCompatActivity {

    private String[] signos = {"Aries","Touro","Virgem","Capricornio","Sagitario","Peixes","Libra","Aquario","Leão","Escorpião","Cancer","Gêmeos"};

    private ListView lista;

    private String[] perfis = {"O ariano é uma pessoa cheia de energia e entusiasmo. Pioneiro e aventureiro, lhe encantam as metas, a liberdade e as idéias novas.",
            "Um Touro costuma ser prático, decidido e ter uma grande força de vontade. Os touro são pessoas estáveis e conservadores, e seguirão de forma leal um líder no que têm confiança. Encanta-lhes a paz e tranqüilidade e são muito respeitosos com as leis e as regras. Respeitam os valores materiais e evitam as dívidas.",
            "Virgem, o único signo representado por uma mulher, é um signo caracterizado por sua precisão, sua convencionalidad, sua atitude reservado e seu afã, as vezes até obsessão, com a limpeza. Os virgens costumam ser observadores, e pacientes. Podem parecer as vezes frios, e de fato lhes custa fazer grandes amigos.",
            "Capricórnio é um dos signos do zodíaco mais estáveis, seguros e calmos. São trabalhadores, responsáveis, práticos e dispostos a persistir o quanto for necessário para conquistar seu objetivo. São fiáveis e muitas vezes têm o papel de terminar um projeto iniciado por outro signo mais pioneiro. Adoram a música.",
            "Sagitário é um dos signos mais positivos do zodíaco. São versáteis e lhes encanta a aventura e o desconhecido. Têm a mente aberta para novas idéias e experiências e mantêm um atitude otimista inclusive quando as coisas parecem difíceis. São fiáveis, honestos, bons, sinceros e dispostos a lutar pelas boas causas custe o que custar.",
            "Sagitário é um dos signos mais positivos do zodíaco. São versáteis e lhes encanta a aventura e o desconhecido. Têm a mente aberta para novas idéias e experiências e mantêm um atitude otimista inclusive quando as coisas parecem difíceis. São fiáveis, honestos, bons, sinceros e dispostos a lutar pelas boas causas custe o que custar.",
            "Os piscianos muitas vezes se retiram para um mundo de sonhos em que suas capacidades podem contribuir-lhes benefícios. Têm uma grande capacidade criativa artística.",
            "Libra está entre os signos mais civilizados do zodíaco. Têm encanto, elegância, bom gosto, são amáveis e pacíficos. Gostam da beleza e da harmonia, e são capazes de serem imparciais ante os conflitos. Não obstante, uma vez que chegam a uma opinião sobre algo, não gostam de serem contrariados.",
            "Os aquarianos têm uma personalidade forte e atraente. Há dois tipos de aquarianos: um é tímido, sensível, e paciente",
            "Leão é o signo mais dominador do zodíaco. Também é criativo e extrovertido. São os reis entre os humanos, assim como os leões são os reis no reino animal. Têm ambição, força, valentia, independência e total segurança em suas capacidades. Não costumam ter dúvidas sobre o que fazer.",
            "Escorpião é um signo intenso, com uma energia emocional única em todo o zodíaco. Ainda que possam parecer calmos, os escorpianos têm uma agressividade e magnetismo escondidos internamente.",
            "O caráter de um canceriano é o menos claro de todos os signos do zodíaco. Um canceriano pode ser de tímido e aborrecido a brilhante e famoso. Os cancerianos são conservadores e adoram a segurança e o calor do lar. De fato, para os homens de Câncer o lar é como um ninho, um refúgio para onde ir quando o estresse do trabalho é demasiado. A casa de um canceriano tende a ser seu refúgio pessoal mais do que uma vitrine para deslumbrar aos demais.",
            "Gêmeos é o signo dos irmãos idênticos e, como tal, seu caráter é duplo, bastante complexo e contraditório. Por um lado é versátil, mas pelo outro pode não ser sincero. Costumam ter elegância e cometer os erros dos jovens"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lista = (ListView) findViewById(R.id.listaid);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                signos
        );

        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener(){;
                                         @Override
                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                             int posicao = position;
                                             Toast.makeText(getApplicationContext(),perfis[posicao],Toast.LENGTH_SHORT).show();
                                         }
                                     }

        );
    }
}
