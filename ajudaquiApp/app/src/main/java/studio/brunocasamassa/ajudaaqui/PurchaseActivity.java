package studio.brunocasamassa.ajudaaqui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import studio.brunocasamassa.ajudaaqui.payment.IabHelper;
import studio.brunocasamassa.ajudaaqui.payment.IabResult;
import studio.brunocasamassa.ajudaaqui.payment.Purchase;

public class PurchaseActivity extends AppCompatActivity {
    private IabHelper mHelper;
    private ImageButton buy_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);


        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwxlZs67chlV5V6kF+8aNjmqcyE3IQjsoinnd7mh5BI6rSxxZSE09sqkL5HMjZtXpUn/4nLMfDNjb1BgtkMnc9wkFNt8Mp3azIjufbVpWPBxCj3J879u3z37USf0qBgfF39ksxOht+ReIMLN8aVvwqLOyzuz74xHvem8j9PvRTDrkZfulWXctPMVRqyvhtvNy/kSZ7fRAQEWdiufayZMuZ/mT345gDBvQQctMZI3i+fWnMCz2kLmRjjI4HPaxWB2c+uOPHd7zugbCXdMk5h1vlSPGPDUq4JnkYZjbEsniqeNYZ8Wqxr7qYAf+5qI6eZt5vOIz1NLkgKueLV/PIcV/kQIDAQAB";


        // compute your public key and store it in base64EncodedPublicKey

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        
        buy_button = (ImageButton) findViewById(R.id.purchase_button);


        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                    public void onIabSetupFinished(IabResult result) {
                        if (!result.isSuccess()) {
                            // Oh no, there was a problem.
                            Log.d("STATUS_BILLING", "Problem setting up In-app Billing: " + result);
                        }
                        Log.d("STATUS_BILLING", "FOI" + result);

                        IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                                = new IabHelper.OnIabPurchaseFinishedListener() {
                            @Override
                            public void onIabPurchaseFinished(IabResult result, Purchase info) {

                            }
                        };


                        try {
                            mHelper.launchPurchaseFlow(PurchaseActivity.this, "premium_user1.", 0, mPurchaseFinishedListener, "");
                        } catch (IabHelper.IabAsyncInProgressException e) {
                            e.printStackTrace();
                        }
                        // Hooray, IAB is fully set up!
                    }
                });

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
    }


}
