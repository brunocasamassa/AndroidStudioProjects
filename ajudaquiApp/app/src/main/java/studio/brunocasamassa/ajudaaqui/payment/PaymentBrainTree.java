/*
package studio.brunocasamassa.ajudaaqui.payment;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.ConfigurationListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.Configuration;

import studio.brunocasamassa.ajudaaqui.R;

*/
/**
 * Created by bruno on 04/07/2017.
 *//*


public class PaymentBrainTree extends BaseActivity implements ConfigurationListener,
        PaymentMethodNonceCreatedListener, BraintreeErrorListener, View.OnClickListener{
    private TextView editTextAmount;
    private ImageButton buttonPay;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);
        buttonPay = (ImageButton) findViewById(R.id.buttonPay);
        editTextAmount = (TextView) findViewById(R.id.editTextAmount);

        buttonPay.setOnClickListener(this);


    }

    @Override
    protected void reset() {

    }

    @Override
    protected void onAuthorizationFetched() {

    }

    public void launchFuturePayment(View v) {
        setProgressBarIndeterminateVisibility(true);

        PayPalOverrides.setFuturePaymentsOverride(true);
        PayPal.authorizeAccount(mBraintreeFragment);

    }

    @Override
    public void onClick(View v) {
     launchFuturePayment(v);
    }


    @Override
    public void onConfigurationFetched(Configuration configuration) {

    }
}




*/
