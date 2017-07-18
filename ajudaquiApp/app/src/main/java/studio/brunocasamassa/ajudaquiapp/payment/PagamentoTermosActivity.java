package studio.brunocasamassa.ajudaquiapp.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import studio.brunocasamassa.ajudaquiapp.R;

import static studio.brunocasamassa.ajudaquiapp.payment.PaymentActivity3.PAYPAL_REQUEST_CODE;

/**
 * Created by bruno on 12/07/2017.
 */



public class PagamentoTermosActivity extends AppCompatActivity {
    private String paymentAmount = "0.09";

    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos);


        TextView text = (TextView) findViewById(R.id.texto_termos);

        Button aceite = (Button) findViewById(R.id.aceite_termos);


        text.setText("    O AJUDAQUI estabelece nestes Termos De Uso e Política de Privacidade as condições para utilização da plataforma/site/blog e fanpage denominado AJUDAQUI, por meio dos quais o Usuário poderá fazer uso desta ferramenta que disponibiliza atividades que envolvam empréstimos, trocas, doações de bens/itens/objetos/coisas/alimentos e serviços, ou seja, aquela “ajudinha” que pode fazer uma diferença positiva na vida das pessoas que utilizam a rede AJUDAQUI, ou seja, o Usuário. \n" +
                " \n" +
                "O AJUDAQUI promove uma plataforma para os usuários compartilharem serviços em geral, empréstimos, trocas, doações que envolvam bens/itens/objetos/coisas/alimentos e serviços uns com os outros.  O AJUDAQUI apenas facilita a conexão entre usuários e não tem nenhuma responsabilidade com relação aos itens envolvidos na transação entre eles ou qualquer outro acontecimento que decorra de contatos e/ou relações online e/ou off-line, entre usuários que tenham se conectado por meio do aplicativo ou se encontrem pessoalmente.  \n" +
                " \n" +
                "O AJUDAQUI poderá estabelecer Termos de Uso e Políticas de Privacidade específicas e aplicáveis a determinadas plataformas/sites/blogs e fanpages, que complementarão e/ou prevalecerão sobre estes Termos de Uso e Política de Privacidade. \n" +
                " \n" +
                "Qualquer usuário, para que possa utilizar os serviços do AJUDAQUI deverá aceitar integralmente os Termos de Uso e Política de Privacidade. \n" +
                " \n" +
                "1. DEFINIÇÕES:  \n" +
                "Para os fins deste Termos de Uso e Política de Privacidade, consideram-se: \n" +
                "1.1. Internet: o sistema constituído do conjunto de protocolos lógicos, estruturado em escala mundial para uso público e irrestrito, com a finalidade de possibilitar a comunicação de dados entre terminais por meio de diferentes redes; 1.2. Senha: conjunto de caracteres que podem ser constituídos por letras e/ou números, com a finalidade de verificar a identidade do Usuário para acesso a plataforma/site/blog e fanpage; 1.3. Plataforma/Sistema/site/blog e fanpage: sites e aplicativos do AJUDAQUI por meio dos quais o Usuário acessa os serviços e conteúdos disponibilizados pelo AJUDAQUI; 1.4. URL: endereço virtual de um recurso disponível na internet ou intranet com um caminho que indica onde está o que o usuário procura; 1.5. Terminais (ou “Terminal”, quando individualmente considerado): computadores, notebooks, netbooks, smartphones, tablets, palm tops e quaisquer outros dispositivos por meio dos quais se conecta a Internet; \n" +
                "1.6. Usuários (ou “Usuário”, quando individualmente considerado): todas as pessoas físicas que utilizarão a plataforma/site/blog e fanpage, maiores de 18 (dezoito) anos ou emancipadas e totalmente capazes de praticar os atos da vida civil ou os absolutamente ou relativamente incapazes, devidamente representados ou assistidos por seus pais, tutores, curadores, os quais aceitaram os Termos de Uso e Política de Privacidade em nome daqueles, bem como pessoas jurídicas devidamente registradas perante órgãos competentes; 1.7. Servidor de arquivo: é um computador conectado a uma rede que tem o objetivo principal a de proporcionar um local para o armazenamento de arquivos de computadores. \n" +
                " \n" +
                "2. ACEITE DOS TERMOS DE USO E POLÍTICA DE PRIVACIDADE \n" +
                " \n" +
                "2.1. Ao acessar a plataforma/site/blog e fanpage denominado AJUDAQUI, o Usuário concorda integralmente e aceita as disposições destes Termos de Uso e Política de Privacidade. \n" +
                " \n" +
                "3. OBJETO \n" +
                " \n" +
                "3.1. O serviço objeto deste Termo de Uso e Política de Privacidade consiste em oferecer uma plataforma que possibilite e facilite aos usuários se conectarem para interagir e compartilhar bens/itens/objetos/coisas/alimentos e serviços entre si, sem que envolva qualquer tipo de comercialização/transação financeira para isso, com o objetivo de promover a conscientização, humanização e socialização de pessoas e entre pessoas, além de economizar dinheiro e poupar o planeta terra.  \n" +
                " \n" +
                "4. CADASTRO E UTILIZAÇÃO DO AJUDAQUI \n" +
                " \n" +
                "4.1. O cadastro e a utilização do AJUDAQUI são gratuitos para os usuários na versão para uso apenas por geolocalização. Para a utilização da “Versão Premium”, a partir da inserção e criação de grupos, os usuários deverão contribuir anualmente com o valor estipulado no ato da adesão a esse formato e vigente no momento da renovação anual. Isso também dará o direito aos usuários de continuar a utilizar, se assim desejarem, o formato geolocalização, sendo o uso de ambas versões ilimitado. 4.2. Os serviços do AJUDAQUI estão disponíveis para as pessoas físicas maiores de 18 (dezoito) anos ou emancipadas e totalmente capazes de praticar os atos da vida civil ou os absolutamente ou relativamente incapazes, devidamente representados ou assistidos por seus pais, tutores, curadores que aceitarem os Termos de Uso e Política de Privacidade em nome daqueles, bem como pessoas jurídicas devidamente registradas perante órgãos competentes. O uso \n" +
                "da plataforma em outros países além do Brasil poderá estar sujeito a legislação específica quanto à capacidade legal para uso, o qual deverá prevalecer sobre estes Termos de Uso e Política de Privacidade. Não podem utilizá-los, assim, pessoas que não gozem dessa capacidade, ou pessoas que tenham sido inabilitadas do sistema do AJUDAQUI, temporária ou definitivamente. 4.3. No caso que envolva a utilização do aplicativo por menores de dezoito anos, a responsabilidade total por todos os atos e suas consequências on-line e off-line, fica por conta de seus genitores e/ou responsável legal, nos termos do artigo 932 do Código Civil Brasileiro, ou legislação vigente no país que estiver fazendo uso da plataforma, isentando a plataforma de qualquer atribuição ou correlação entre os fatos e consequências. 4.4. Serão admitidas, também na qualidade de usuários, pessoas jurídicas. Tais usuários necessitarão realizar o cadastro de sua empresa e deverão pagar pelas taxas vigentes no momento da inserção à Plataforma, como também na renovação anual. Tal participação seguirá os mesmos critérios das pessoas físicas, salientando que é proibida a comercialização/venda/compra, qualquer tipo de negociação que envolva dinheiro ou qualquer outra negociação monetária. 4.5. Não é permitido que uma mesma pessoa tenha mais de um cadastro. Se o AJUDAQUI detectar, através do sistema de verificação de dados, cadastros duplicados e/ou falsos (fakes), irá inabilitar definitivamente todos os cadastros daqueles usuários. 4.6. As pessoas jurídicas terão a oportunidade de promoverem seus negócios a partir do fornecimento SEM COBRANÇA de seus produtos e serviços a título de doação/troca/empréstimo/ajuda para os demais usuários.  4.7. A plataforma AJUDAQUI não se responsabiliza por produtos/itens/objetos/coisas/alimentos e serviços oferecidos pelas pessoas físicas ou jurídicas, como também data de validade, origem, procedência, condições de uso, e suas consequências para quem os recebeu ou fez uso. Tal responsabilidade é irrestrita das empresas que publicaram ou anunciaram assegurando todo o tramite e consequências do que estão fornecendo em termos de segurança, durabilidade, confiabilidade, prazos do produto ou serviço oferecido e tudo o mais que o cerca. 4.8. A eventual participação a outro título será oportunamente avaliada e regulamentada pelo AJUDAQUI. O preenchimento de todos os campos obrigatórios do cadastro é condição indispensável para poder usufruir dos serviços do AJUDAQUI. 4.9. No ato do cadastro, o futuro usuário deverá completar todos os campos de preenchimento com informações exatas, precisas e verdadeiras, e assume o compromisso de atualizar os dados pessoais ou dados de sua empresa sempre que neles ocorrer alguma alteração.  O AJUDAQUI se reserva ao direito de utilizar todos os meios válidos, lícitos e possíveis para identificar possíveis fraudes e uso de dados falsos manipulados pelos usuários. 4.10. O AJUDAQUI não se responsabiliza pela correção dos dados inseridos pelas pessoas físicas ou jurídicas. Os usuários garantem e respondem, em qualquer caso, pela veracidade, exatidão e \n" +
                "autenticidade dos dados cadastrados. O AJUDAQUI se reserva ao direito de solicitar dados adicionais e documentos que estime serem idôneos a conferir os dados pessoais e de empresas informados, assim como de inabilitar, temporária ou definitivamente, o usuário que apresentar qualquer informação inverídica ou que a plataforma AJUDAQUI não conseguir contatar para a verificação dos dados. 4.11. Ao cancelar o cadastro do usuário, automaticamente serão cancelados os pedidos e buscas por bens/itens/objetos/coisas/alimentos e serviços por ele veiculados ou respostas de empréstimos/serviços/doações/trocas de bens/itens/objetos/coisas/alimentos por ele solicitado a outros usuários, não assistindo ao usuário, por essa razão, qualquer sorte de indenização ou ressarcimento de valor de qualquer natureza. 4.12. O usuário acessará sua conta através de um nome de usuário (login) e senha e compromete-se a não informar a terceiros esses dados, responsabilizando-se integralmente pelo uso que deles seja feito.  O usuário compromete-se a notificar o AJUDAQUI imediatamente, através de meio seguro, a respeito de qualquer uso ou acesso não autorizado de sua conta, por terceiros.  O usuário será o único responsável pelas operações efetuadas em sua conta, uma vez que o acesso a ela só será possível mediante o fornecimento da senha, cuja responsabilidade é exclusiva do usuário. Em nenhuma hipótese será permitida a cessão, venda, aluguel ou outra forma de transferência da conta, incluindo-se qualificações e reputação. 4.13. O nome de usuário (login) utilizado no AJUDAQUI não poderá guardar semelhança com o nome AJUDAQUI. Tampouco poderá ser utilizado qualquer nome que insinue ou sugira que os serviços ou bens/itens/objetos/coisas/alimentos anunciados sejam pertencentes ao AJUDAQUI ou que fazem parte de suas publicações ou promoções. 4.14. Também serão eliminados nomes considerados ofensivos, bem como os que contenham dados pessoais do usuário ou alguma URL ou endereço eletrônico. O AJUDAQUI se reserva ao direito de recusar qualquer solicitação de cadastro e de cancelar um cadastro previamente aceito, a seu exclusivo critério. 4.15. Será possível e permitido, nas condições estipuladas nesse instrumento, o direito de uso dessa ferramenta dentro de instituições de ensino público e privado, assim como, no meio corporativo dentro das empresas/organizações. \n" +
                " \n" +
                "5. PRIVACIDADE DAS INFORMAÇÕES \n" +
                " \n" +
                "5.1. Os dados pessoais prestados pelos usuários do AJUDAQUI, seus registros de conexão e de acesso a aplicações serão armazenados em servidores de alta segurança. 5.2. O AJUDAQUI tomará todas as medidas possíveis para manter a confidencialidade e a segurança descrita nesta clausula, porém não responderá por qualquer prejuízo que possa ser derivado da violação dessas medidas por parte de terceiros que utilizem as redes públicas ou a internet, subvertendo os sistemas de segurança para acessar as informações de usuários. \n" +
                "5.3. Os registros armazenados nos servidores poderão ser solicitados mediante ordem judicial, nos termos da Lei nº 12.965 de 23 de abril de 2014. 5.4. A plataforma AJUDAQUI tão somente permite a interação e conexão entre os usuários e interessados, nada mais do que isso, assim, a plataforma não armazena ou possui acesso, visualização, bem como qualquer outro tipo de interferência no teor da comunicação estabelecida entre os usuários, seja dentro de um “chat”, “grupos” ou outro canal que permita a comunicação entre os usuários, ficando sob inteira responsabilidade destes as consequências cíveis e criminais cujo teor das referidas comunicações possam vir a ocasionar. Com isso, cabe exclusivamente ao usuário manter, ou não, armazenado o conteúdo da comunicação estabelecida na plataforma com outros usuários. \n" +
                " \n" +
                "6. MODIFICAÇÃO DOS TERMOS E CONDIÇÕES GERAIS \n" +
                " \n" +
                "6.1. O AJUDAQUI poderá alterar, a qualquer tempo, estes Termos e Condições Gerais, visando seu aprimoramento e melhoria dos serviços prestados.  Os novos Termos e Condições entrarão em vigor imediatamente depois de publicados no site. 6.2. No prazo de 05 (cinco) dias contados a partir da publicação das modificações, o usuário deverá comunicar-se com o AJUDAQUI pelas vias disponíveis caso não concorde com as alterações.  Neste caso, o vínculo contratual deixará de existir, ficando cancelado o cadastro do usuário.  Não havendo manifestação no prazo estipulado, entenderse-á que o usuário aceitou os novos Termos e Condições Gerais.   \n" +
                " \n" +
                "7. ANÚNCIO E BUSCA \n" +
                " \n" +
                "7.1. O usuário poderá pedir ajuda que envolva serviços, trocas, doações, empréstimos de bens/itens/objetos/coisas/alimentos e ao concordar em ajudar: doar/trocar/emprestar alguns objetos/bens/coisas/itens/alimentos ou mesmo prestar algum serviço, presumir-se-á que o usuário manifesta a intenção e declara possuir o direito de emprestar/trocar/doar tais objetos/coisas/bens/itens/alimentos ou está disponível a prestar tal serviço, ou que está facultado para tal por seu titular.  7.2. O AJUDAQUI poderá remover, a seu exclusivo critério, os anúncios cuja especificação não esteja suficientemente clara, ou que permitam algum tipo de variação, dupla interpretação ou violação. 7.3. O usuário poderá indicar no sistema AJUDAQUI o bens/itens/objetos/coisas/alimentos que lhe interessa para empréstimo/troca/doação ou a necessidade de algum serviço. O sistema apenas facilitará o contato entre os usuários que respondam a tal solicitação. Os usuários poderão entrar em contato direto, por qualquer meio, para efetuar o combinado, não podendo o AJUDAQUI ser responsabilizado neste caso. \n" +
                "7.4. Ao efetuar o empréstimo/troca/doação ou serviço, caso os mesmos se efetivem corretamente, os usuários envolvidos deverão informar tal fato no sistema através de VALIDAÇÃO (confirmação de que ocorreu). As condições do empréstimo/troca/doação ou serviço quanto a forma, duração, local e todas as demais questões envolvidas serão estabelecidas de comum acordo entre os usuários e sob sua exclusiva responsabilidade. 7.5. O AJUDAQUI se isenta de toda e qualquer forma de transação realizada pelos usuários e tudo que a envolve, desde as condições dos objetos, serviços, data de validade dos alimentos, prazos, existência dos objetos, entregas, ocorrências, arrependimentos, etc. São de completa e única responsabilidade dos usuários tudo que envolve tais negociações entre si, ficando claro que a plataforma AJUDAQUI tão somente oferece e permite a interação e conexão entre os usuários e interessados, nada mais do que isso. 7.6. Poderão ser anunciados bens/itens/objetos/coisas/alimentos e serviços cujo empréstimo/troca/doação e atendimento não estejam expressamente proibidos pelos Termos de Uso e Política de Privacidade do AJUDAQUI, ou pela legislação vigente no Brasil, ou no país em que esteja sendo utilizada a plataforma.   7.7. Fica expressamente proibido o empréstimo/troca/doação/serviços de tempo, pessoas ou bens/itens/objetos/coisas/alimentos para qualquer atividade ilícita ou imoral, como também serviços e/ou produtos relacionados a prostituição ou similares, material pornográfico, obsceno ou contrário a moral e os bons costumes, quaisquer produtos cuja a venda é expressamente proibida pelas leis vigentes no Brasil ou no país em que esteja sendo utilizada a plataforma, de atividades ou bens/itens/objetos/coisas/alimentos que promovam a violência e/ou a discriminação baseada em questões de raça, sexo, religião, cor, nacionalidade, orientação sexual ou de qualquer outro tipo de discriminação.  7.8. Também ficam proibidos os serviços/atividades ou bens/itens/coisas/objetos/alimentos que violem a propriedade intelectual, como direitos autorais, marcas, patentes, modelos, desenhos industriais, autoria de softwares, direitos de imagem, voz e quaisquer outros protegidos pelas leis brasileiras ou do país em que esteja sendo utilizada a plataforma.  7.9. É de responsabilidade exclusiva do usuário velar pela legalidade dos seus empréstimos/trocas/doações e serviços. \n" +
                " \n" +
                "8. PRÁTICAS VEDADAS \n" +
                " \n" +
                "8.1. Os usuários não podem: a) Manipular as características dos produtos/bens/itens/objetos/coisas/alimentos e serviços oferecidos; b) Comercializar ou realizar transações financeiras de quaisquer produtos/bens/itens/objetos/coisas/alimentos; c) Interferir nas transações entre outros usuários; d) Prestar informações falsas; \n" +
                "e) Anunciar atividades ou produtos/bens/itens/objetos/coisas/alimentos e serviços proibidos pelas políticas do AJUDAQUI e pelas leis em vigor no país; f) Agredir, caluniar, injuriar ou difamar outros usuários. 8.2. Estes comportamentos poderão ser sancionados com a suspensão ou cancelamento do pedido, ou com a suspensão ou cancelamento do cadastro do usuário do AJUDAQUI, sem prejuízo das ações legais que possam ocorrer pela configuração de infrações penais ou os prejuízos civis que possam causar aos demais e sem prévio aviso por parte da plataforma AJUDAQUI. \n" +
                " \n" +
                "9. SOBRE A “CABINE DA FARTURA” 9.1. O AJUDAQUI disponibiliza em sua plataforma um espaço destinado àqueles usuários que possuem especial preocupação com o desperdício de alimentos, para tanto, há na ferramenta “Cabine da Fartura” um espaço reservado para aquele usuário que quer dar uma “ajudinha” para que este alimento chegue à mesa de quem precisa. 9.2. Podem fazer uso da “Cabine da Fartura” apenas os usuários pessoas físicas e jurídicas que estejam cadastrados na plataforma AJUDAQUI na “Versão Premium”. 9.3. Nesta ferramenta somente será possível a DOAÇÃO de alimentos/itens/objetos/bens/coisas e serviços em quantidade mínima de 10 (dez) itens/unidades/serviços para no mínimo 10 (dez) usuários (considera-se que 1 (um) usuário somente possa adquirir/receber 1 (uma) unidade/1 (um) item/1 (um) serviço do que esta sendo ofertado em cada publicação). 9.4. Ao anunciar a doação, o usuário deverá informar as condições do alimento, tais como: a) Descrição do(s) alimento(s) e seu fabricante; b) Data de validade; c) Se foi devidamente acondicionado em local indicado pelo fabricante. 9.5. O AJUDAQUI apenas permite a conexão entre usuários interessados que querem doar e outros que querem receber os alimentos, não possuindo acesso ou mesmo interferindo, moderando ou validando qualquer comunicação entre eles. Assim, o AJUDAQUI se isenta integralmente de qualquer responsabilidade decorrente da doação de alimentos ofertada pelo usuário pessoa física ou jurídica, como também pela data de validade, origem, procedência, condições de uso e armazenamento anunciadas e praticadas, bem como suas consequências para quem os recebeu ou fez uso. Tal responsabilidade é irrestrita dos usuários que publicaram, anunciaram ou doaram e, também daqueles que produziram o alimento, sendo estes responsáveis por todo trâmite e consequências do que estão fornecendo em termos de segurança, durabilidade, confiabilidade, prazos de validade do alimento oferecido e tudo o mais que o cerca. \n" +
                " \n" +
                "10. OBRIGAÇÕES DOS USUÁRIOS \n" +
                " \n" +
                "10.1. Os usuários interessados em realizar uma transação de empréstimo/troca/doação de bens/itens/coisas/objetos/alimentos e \n" +
                "serviços anunciados por um outro usuário no AJUDAQUI devem fazer contato dentro do sistema AJUDAQUI, estabelecendo as condições da transação de empréstimo/troca/doação ou serviço, destacando que o produto ou serviço em questão não pode ser proibido por lei ou por estes Termos e Condições Gerais.  Ao manifestar o interesse em algum bens/itens/objetos/coisas/alimentos ou serviço, o usuário obriga-se a atender às condições de negociação descritas no anúncio. 10.2. Os usuários comprometem-se a prestar uns aos outros apenas informações verdadeiras, tanto sobre si mesmos quanto sobre o bens/itens/objetos/coisas/alimentos e serviço em questão e as condições da transação. 10.3. Após a realização da transação de troca, empréstimo, doação e serviço, os usuários poderão realizar uma avaliação do outro usuário, que de maneira global afetará o seu perfil, segundo os requisitos estabelecidos na plataforma, tais como confiabilidade, satisfação, pontualidade, etc. A avaliação é opcional, mas condicionará a possibilidade de realização de novas transações/negociações. 10.4. O usuário deverá ter capacidade legal para efetuar a transação de doação/troca/empréstimo ou serviço a que se propôs. No caso de menores de 18 (dezoito) anos, relativamente ou absolutamente incapazes, os pais ou responsáveis legais deverão manter constante acompanhamento e controle além de nortearem as transações que serão realizadas, assumindo total e irrestrita responsabilidade sobre as ações/atitude de seus filhos e posterior consequências isentando totalmente a plataforma AJUDAQUI. 10.5. Em virtude do AJUDAQUI possibilitar o encontro entre usuários, e por não participar das transações que se realizam entre eles, a responsabilidade por todas as obrigações, sejam elas fiscais, jurídicas, trabalhistas, consumeristas, criminais ou de qualquer outra natureza, decorrentes das transações originadas no espaço virtual do aplicativo serão exclusivamente dos usuários. Em caso de interpelação judicial que tenha como Réu o AJUDAQUI, cujos fatos fundem-se em ações do usuário, este será chamado ao processo devendo arcar com todos os ônus que daí decorram, nos termos do artigo 125, II do Código de Processo Civil. 10.6. Em virtude da característica do aplicativo, também não pode obrigar o usuário a honrar sua obrigação ou completar a negociação, fazer devoluções, entre outros. 10.7. O AJUDAQUI não se responsabiliza pelas obrigações de natureza tributária que incidam sobre os negócios realizados entre usuários.  Assim, o usuário que praticar ato que gere hipótese de incidência tributária de qualquer natureza, nos termos da lei em vigor, responsabilizar-se-á pela integralidade das obrigações oriundas de suas atividades, notadamente pelos tributos envolvidos. \n" +
                " \n" +
                "11. AVALIAÇÕES E QUALIFICAÇÕES \n" +
                " \n" +
                "11.1. A plataforma AJUDAQUI não tem condições de realizar a averiguação da identidade dos internautas usuários do aplicativo e por essa razão o sistema de avaliação e qualificação mútua, realizada através dos \n" +
                "comentários após as transações/negociações finalizadas, é de extrema valia.  11.2. Todos os usuários da plataforma devem informar sobre a finalização da negociação seja por troca, doação, empréstimo ou serviço, incluindo comentários de total e irrestrita responsabilidade sobre o que disserem. O AJUDAQUI não tem obrigação e nem responsabilidade de verificar a veracidade das informações lançadas pelo usuário a fim de avaliar e qualificar o outro usuário na plataforma, como também no site, blog, fanpage, e-mail ou em qualquer espaço na internet e fora dela.   11.3. A Plataforma se reserva ao direito de excluir as avaliações/comentários que julgar inadequado, impróprios, impertinentes e ofensivos, ainda, o AJUDAQUI se reserva ao direito de aplicar as sanções necessárias e previstas nesse instrumento aos usuários que sejam reiteradamente mal avaliados pelos demais usuários ou se realizarem atividades vedadas. 11.4. A avaliação e qualificação é uma forma de auxiliar a todos a escolher pessoas confiáveis e bem intencionadas para fazer suas negociações/interações.    \n" +
                " \n" +
                "12. RESPONSABILIDADES \n" +
                " \n" +
                "12.1. Estes Termos e Condições Gerais não geram nenhum contrato de sociedade, de mandato, franquia ou relação de trabalho entre o AJUDAQUI e o usuário.  O usuário manifesta ciência de que o AJUDAQUI não é parte de nenhuma transação, nem possui controle algum sobre a qualidade, segurança ou legalidade dos anúncios/pedidos, sobre a sua veracidade ou exatidão, e sobre a capacidade dos usuários para negociar. 12.2. O AJUDAQUI não interfere de nenhuma forma na negociação ou na realização dos empréstimos/trocas/doações e serviços entre os usuários que se iniciam no aplicativo, somente disponibiliza a plataforma online como meio facilitador, assim sendo, a Plataforma não se responsabiliza pela existência, quantidade, qualidade, estado, integridade, validade ou legitimidade dos produtos/bens/itens/objetos/coisas/alimentos e/ou serviços oferecidos, adquiridos, alienados, emprestados, doados, trocados pelos usuários, assim como pela capacidade para contratar/negociar dos usuários ou pela veracidade das informações por eles prestadas. 12.3. O AJUDAQUI não se responsabiliza pela existência de vícios ocultos ou aparentes do objeto a ser negociações entre os usuários, cabendo a estes, exclusivamente, garantir a boa-fé da negociação. 12.4. Cada usuário conhece e aceita ser o único responsável pelos pedidos que anuncia e atende. O AJUDAQUI não será responsável pelo efetivo cumprimento das obrigações assumidas pelos usuários.  Os usuários reconhecem e aceitam que ao realizar negociações com outros usuários ou terceiros faz por sua conta e risco. 12.5. Em nenhum caso o AJUDAQUI será responsável pelo lucro cessante ou por qualquer outro dano e/ou prejuízo que o usuário possa sofrer \n" +
                "devido às negociações ou transações realizadas ou não realizadas através da plataforma AJUDAQUI. A Plataforma não é intermediária das transações e recomenda que toda transação seja realizada com cautela e bom senso. 12.6. O AJUDAQUI não será responsável pelas transações entre os usuários, mesmo as firmadas com base na confiança depositada no sistema ou nos serviços prestados pelo AJUDAQUI.   12.7. Nos casos em que um ou mais usuários ou algum terceiro inicie qualquer tipo de reclamação ou ação legal contra outro(s) usuário(s) envolvido(s) na(s) reclamação(es) ou ação(es), exime(m) de toda e qualquer responsabilidade o AJUDAQUI e a seus diretores, gerentes, empregados, agentes, representantes, terceirizados e procuradores. 12.8. O AJUDAQUI resguarda-se de toda e qualquer responsabilidade por fatos resultantes da interação entre os usuários, dentro ou fora do mundo virtual, devendo cada usuário zelar e responsabilizar-se por sua segurança e pela das pessoas que com ele interagem e de seus filhos menores de idade que fazem uso da plataforma. 12.9. O AJUDAQUI não pode assegurar o êxito de qualquer transação, tampouco verificar a identidade ou os dados pessoais dos usuários. O AJUDAQUI não garante a veracidade da publicação de terceiros que apareça em sua plataforma e não será responsável pela correspondência ou contratos que o usuário realize com terceiros. \n" +
                " \n" +
                "13. INDENIZAÇÃO \n" +
                " \n" +
                "13.1. O usuário indenizará o AJUDAQUI, suas filiais, empresas controladas ou controladoras, diretores, administradores, colaboradores, representantes e empregados por qualquer demanda promovida por outros usuários ou terceiros decorrentes de suas atividades no aplicativo ou por seu descumprimento dos Termos de Uso e Política de Privacidade da plataforma, ou pela violação de qualquer lei ou direitos de terceiros, incluindo honorários de advogados. \n" +
                " \n" +
                "14. PROPRIEDADE INTELECTUAL E LINKS \n" +
                " \n" +
                "14.1. Os conteúdos das telas relativas aos serviços do AJUDAQUI, assim como os programas, logomarca, bancos de dados, redes, arquivos que permitem que o usuário acesse e use sua conta são de propriedade do AJUDAQUI e estão protegidos pelas leis e tratados internacionais de direito autoral, marcas, patentes, modelos e desenhos industriais. 14.2. O uso indevido e a reprodução total ou parcial dos referidos conteúdos são proibidos, salvo autorização expressa do AJUDAQUI.  14.3. O aplicativo/site/blog e fanpage podem apresentar conexão (links) com outros sites/aplicativos/blogs e fanpages da rede, o que não significa que esses sites/aplicativos/blogs e fanpages sejam de propriedade ou operados pelo AJUDAQUI. Não possuindo controle sobre esses sites/aplicativos/blogs e fanpages, o AJUDAQUI não será \n" +
                "responsável pelos conteúdos, práticas, benefícios e serviços ofertados pelos mesmos. Observe que ao usar serviços de terceiros, os termos e as políticas de privacidade aplicáveis serão os elaborados por estes. 14.4. A presença de links para outros sites/aplicativos/blogs e fanpages não implica relação de sociedade, de supervisão, de cumplicidade ou solidariedade do AJUDAQUI para com esses sites, seus conteúdos, práticas, propostas, benefícios e produtos. \n" +
                " \n" +
                "15. VIOLAÇÃO DO SISTEMA OU BANCO DE DADOS \n" +
                " \n" +
                "15.1. Não é permitida a utilização de nenhum dispositivo, software, ou outro recurso por um terminal utilizado por usuário ou não usuário que venha a interferir nas atividades e operações do AJUDAQUI, bem como nos anúncios, descrições, contas ou seus bancos de dados. Qualquer intromissão, ou tentativa, ou atividade que viole ou contrarie as leis de direito de propriedade intelectual e/ou as proibições estipuladas nestes Termos de Uso e Política de Privacidade, tornarão o responsável passível das ações legais pertinentes, bem como das sanções aqui previstas, sendo ainda responsável pelas indenizações por eventuais danos causados. 15.2. Sem prejuízo de outras medidas, o AJUDAQUI poderá advertir suspender ou cancelar, temporária ou definitivamente, a conta de um usuário a qualquer tempo, e iniciar as ações legais cabíveis se: a) O usuário não cumprir, fraudar ou dolosamente incorrer em qualquer dispositivo destes Termos de Uso e Política de Privacidade do AJUDAQUI; b) Se descumprir com seus deveres de usuário; c) Se não puder ser verificada a identidade do usuário ou qualquer informação fornecida por ele esteja incorreta; d) Caso se verifique duplicidade ou falsidade do cadastro; e) Se o AJUDAQUI entender que os anúncios/pedidos ou qualquer atitude do usuário tenha causado qualquer dano a terceiros ou ao próprio AJUDAQUI ou tenha a potencialidade de assim o fazer.   15.3. Nos casos de inabilitação do cadastro do usuário, todos os produtos/bens/itens/objetos/alimentos e serviços por este solicitado ou oferecido serão automaticamente cancelados. \n" +
                " \n" +
                "16. FALHAS NO SISTEMA \n" +
                " \n" +
                "16.1. O AJUDAQUI não se responsabiliza por qualquer dano, prejuízo ou perda no equipamento do usuário causada por falhas no sistema, no servidor ou na internet.   16.2. O AJUDAQUI também não será responsável por qualquer vírus que possa atacar o equipamento do usuário em decorrência do acesso, utilização ou navegação no aplicativo/site/fanpage ou blog na internet ou como consequência da transferência de dados, arquivos, imagens, textos ou áudio contidos no mesmo. \n" +
                "16.3. Os usuários não poderão atribuir ao AJUDAQUI nenhuma responsabilidade nem exigir o pagamento por lucro cessante em virtude de prejuízos resultante de dificuldades técnicas ou falhas nos sistemas ou na internet.   16.4. O AJUDAQUI não garante o acesso, uso contínuo ou sem interrupções de seu aplicativo. Eventualmente, o sistema poderá não estar disponível por motivos técnicos ou falhas da internet, ou por qualquer outra circunstância alheia a Plataforma. \n" +
                " \n" +
                "17. TARIFAS \n" +
                " \n" +
                "17.1. O cadastro do usuário pessoa física ou jurídica e a utilização do AJUDAQUI são gratuitos na versão apenas por geolocalização. 17.2. No caso de participar/criar qualquer grupo o AJUDAQUI cobrará a taxa em vigor no momento da adesão da “Versão Premium”, como também uma taxa de anuidade, podendo esta ser alterada sem qualquer aviso prévio. \n" +
                " \n" +
                "18. LEGISLAÇÃO APLICÁVEL E FORO DE ELEIÇÃO \n" +
                " \n" +
                "18.1. Todos os itens destes Termos de Uso e Política de Privacidade estão regidos pelas leis vigentes na República Federativa do Brasil.  18.2. Para todos os assuntos referentes à interpretação e ao cumprimento deste Termo de Uso e Política de Privacidade, as partes se submetem ao Foro Central da Comarca de São Paulo – SP. \n" +
                " \n" +
                "19. DISPOSIÇÕES GERAIS 19.1. O presente Termo de Uso e Política de Privacidade foi elaborado em português (BR). As versões traduzidas são fornecidas apenas para sua conveniência. Caso haja conflitos entre a versão traduzida e a versão em português dos nossos Termos, a versão em português prevalecerá. ");


        text.setMovementMethod(new ScrollingMovementMethod());

        aceite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPayment();
            }
        });




    }
    private void getPayment() {
        //Getting the amount from editText

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "BRL", "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);



        //Creating Paypal Payment activity intent
        Intent intent = new Intent(PagamentoTermosActivity.this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PaymentActivity.RESULT_OK);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal

        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {

                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {

                    try {

                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

}
