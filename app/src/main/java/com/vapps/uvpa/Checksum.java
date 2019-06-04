package com.vapps.uvpa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback
{
    String custid="", orderId="", mid="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        orderId=initOrderId();
        custid = "vcrkhvehfrihveriaahaivhih";
        mid = "OiEieg61305100527529";  /// your marchant key
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
// vollye , retrofit, asynch
    }

    private String initOrderId()
    {
        Random r = new Random(System.currentTimeMillis());
        String rorderId = "ORDER" + (1 + r.nextInt(2)) * 10000
                + r.nextInt(10000);

        return rorderId;
    }



    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(Checksum.this);
        //private String orderId , mid, custid, amt;
        String url ="http://techrb.appspot.com";
        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH ="";
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        protected String doInBackground(ArrayList<String>... alldata)
        {
            JSONParser jsonParser = new JSONParser(Checksum.this);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId+
                            "&CUST_ID="+custid+
                            "&CHANNEL_ID=WAP&TXN_AMOUNT=100&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
            // yaha per checksum ke saht order id or status receive hoga..
            Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                Log.e("CheckSum result >>",jsonObject.toString());
                try {
                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Log.e("CheckSum result >>",CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getStagingService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", "100");
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,varifyurl);
            paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            paramMap.put( "MOBILE_NO" , "9410419310");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction( Checksum.this, true, true, Checksum.this);
        }
    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());

        if(bundle.getString("STATUS").equals("TXN_SUCCESS"))
        {
            Toast.makeText(getApplicationContext(), "Payment Succesful", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Checksum.this,RepairOrder1.class));
            finish();
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Payment Failed Try Again! " , Toast.LENGTH_LONG).show();
            startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
            finish();
        }
    }

    @Override
    public void networkNotAvailable()
    {      startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getBaseContext(), "Check your Internet Connection and Try Again!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void clientAuthenticationFailed(String s)
    {
        startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getBaseContext(), "Check your Internet Connection and Try Again!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void someUIErrorOccurred(String s)
    {
        startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getBaseContext(), "Check your Internet Connection and Try Again!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1)
    {
        //Log.e("checksum ", " "+ s + "  s1 " + s1);
        startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getBaseContext(), "Error loading pagerespon true", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onBackPressedCancelTransaction()
    {
        startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getApplicationContext(),"Back pressed. Transaction cancelled",Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle)
    {
        startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
        finish();
    }

}