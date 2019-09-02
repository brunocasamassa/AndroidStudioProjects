package opt2flow.com.br.magolandiaapp.Model;

import android.content.Context;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Caio on 12/03/2015.
 */
public class Validador {

    public static String traduzirData(String data){
        String ano = data.substring(0,4);
        String mes = data.substring(5,7);
        String dia = data.substring(8);
        return dia + "-" + mes + "-" + ano;
    }

    public static boolean validarNotNull(View pView, String pMessage) {
        if (pView instanceof EditText) {
            EditText edText = (EditText) pView;
            Editable text = edText.getText();
            if (text != null) {
                String strText = text.toString();
                if (!TextUtils.isEmpty(strText)) {
                    return true;
                }
            }
            // em qualquer outra condição é gerado um erro
            edText.setError(pMessage);
            edText.setFocusable(true);
            edText.requestFocus();
            return false;
        }
        return false;
    }

    public static void sendSignal(String msg, Context context){
        String[] rows = msg.split(";");
        BluetoothArduino bluetoothArduino = BluetoothArduino.getInstance("Personagem");
        if(!bluetoothArduino.isBluetoothEnabled()){
            Toast.makeText(context, "Bluetooth não está ativado !!!", Toast.LENGTH_SHORT).show();
        } else {
            if(!bluetoothArduino.Connect()){
                Toast.makeText(context, "Falha na conexão com o Arduino", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < rows.length; i++){
                    String aux = rows[i].trim();
                    if(aux.startsWith("delay")){
                        Log.d("d", "Wait");
                        aux = aux.substring(6, aux.length() - 1);
                        int time = Integer.parseInt(aux) / 10 * 1000;
                        SystemClock.sleep(time);
                    } else {
                        Log.d("d", aux);
                        bluetoothArduino.SendMessage(aux);
                    }
                }
            }
        }
    }
}
