package studio.brunocasamassa.ajudaaqui.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalService;

import studio.brunocasamassa.ajudaaqui.R;
import studio.brunocasamassa.ajudaaqui.helper.Base64Decoder;
import studio.brunocasamassa.ajudaaqui.helper.FirebaseConfig;

/**
 * Created by bruno on 02/07/2017.
 */

public class PaymentActivity extends AppCompatActivity  implements View.OnClickListener{

    private static final int REQUEST_CODE_FUTURE_PAYMENT = 123;

    private static PayPalConfiguration config = new PayPalConfiguration()

            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId(PayPalConfig.PAYPAL_CLIENT_ID)

            // Minimally, you will need to set three merchant information properties.
            // These should be the same values that you provided to PayPal when you registered your app.
            .merchantName("Hipster Store")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    private ImageButton buttonPay;
    private TextView editTextAmount;
    private String userKey = Base64Decoder.encoderBase64(FirebaseConfig.getFirebaseAuthentication().getCurrentUser().getEmail());
    private DatabaseReference dbUser = FirebaseConfig.getFireBase().child("usuarios").child(userKey);
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);


        buttonPay = (ImageButton) findViewById(R.id.buttonPay);
        editTextAmount = (TextView) findViewById(R.id.editTextAmount);

        buttonPay.setOnClickListener(this);

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);


    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PayPalAuthorization auth = data
                    .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
            if (auth != null) {
                String authorization_code = auth.getAuthorizationCode();
                Log.i("FuturePaymentExample", authorization_code);

                sendAuthorizationToServer(auth);


            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("FuturePaymentExample", "The user canceled.");
        } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("FuturePaymentExample",
                    "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization auth) {

        dbUser.child("auth").setValue(auth);

    }

    public void onFuturePaymentPressed(View pressed) {
        Intent intent = new Intent(PaymentActivity.this, PayPalFuturePaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startActivityForResult(intent, REQUEST_CODE_FUTURE_PAYMENT);
    }

    public void onFuturePaymentPurchasePressed(View pressed) {
// Get the Client Metadata ID from the SDK
        String metadataId = PayPalConfiguration.getClientMetadataId(this);

// TODO: Send metadataId and transaction details to your server for processing with
// PayPal...
        dbUser.child("metadata").setValue(metadataId);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        onFuturePaymentPressed(v);
        GetJson download = new GetJson();
        download.execute();
        finish();
        //onFuturePaymentPurchasePressed(v);
    }


    private class GetJson extends AsyncTask<Void, Void, PaymentObj> {


        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(PaymentActivity.this, "Por favor Aguarde ...", "Recuperando Informações do Servidor...");

        }

        @Override
        protected PaymentObj doInBackground(Void... params) {
            Utils util = new Utils();
            String url = "https://api.paypal.com/v1/oauth2/token";
            return util.getInformacao(url);
        }

        @Override
        protected void onPostExecute(PaymentObj paymentObj) {
            super.onPostExecute(paymentObj);
        }
    }

}