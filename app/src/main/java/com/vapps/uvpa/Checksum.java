package com.vapps.uvpa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback
{
    String custid="", orderId="", mid="";
    SharedPreferences sharedPreferences;
    JSONObject orderDetail;
    JSONObject orderDetails;
    String userid;
    Intent intent;
    String amount;
    String gadget;
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
        gadget = intent.getStringExtra("gadget");
       // Log.i("gadget", intentget.getStringExtra("gadget"));
        if (gadget.equals("Mobile"))
        {
            amount="150";
        } else {
             amount="250";
        }
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        userid=sharedPreferences.getString("user_id",null);
          orderDetail = new JSONObject();
        JSONObject orderHolder = new JSONObject();


        try {
            // orderDetails.put("user_id","13");
            orderDetail.put("user_id",userid);
            orderDetail.put("order_id",sharedPreferences.getString("order_id",null));

            // orderDetails.put("street",landmark.getText().toString());
            // orderDetails.put("area","Urrapakkam");
            //orderDetails.put("city",city);
            orderHolder.put("mobbucket",orderDetail);
            Log.i("VANIK",orderHolder.toString());
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        payment.execute("https://www.repairbuck.com/mobbuckets.json?auth_token="+sharedPreferences.getString("auth_token",null),orderHolder.toString());
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
                            "&CHANNEL_ID=WAP&TXN_AMOUNT="+amount+"&WEBSITE=WEBSTAGING"+
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
            paramMap.put("TXN_AMOUNT", amount);
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,varifyurl);
           // paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            //paramMap.put( "MOBILE_NO" , "9410419310");  // no need
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
    Payment payment = new Payment();
    @Override
    public void onTransactionResponse(Bundle bundle)
    {
        Log.e("checksum", " respon true " + bundle.toString());

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
        try {MobPayment mobPayment = new MobPayment();
            JSONObject orderHold = new JSONObject();
            orderDetails.put("amount",bundle.getString("TXN_AMOUNT"));
            orderDetails.put("status",bundle.getString("STATUS"));
            orderHold.put("mobpayment", orderDetails);
            Log.i("VANIK1", orderHold.toString());
            mobPayment.execute("https://www.repairbuck.com/mobpayments.json?auth_token=" + sharedPreferences.getString("auth_token", null), orderHold.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //payment.execute("https://www.repairbuck.com/mobbuckets.json?auth_token="+sharedPreferences.getString("auth_token",null),orderHolder.toString());
    }

    @Override
    public void networkNotAvailable()
    {      startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getApplicationContext(), "Check your Internet Connection and Try Again!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void clientAuthenticationFailed(String s)
    {
        startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getApplicationContext(), "Check your Internet Connection and Try Again!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void someUIErrorOccurred(String s)
    {
        startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getApplicationContext(), "Check your Internet Connection and Try Again!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1)
    {
        //Log.e("checksum ", " "+ s + "  s1 " + s1);
        startActivity(new Intent(Checksum.this,ConfirnmationActivity.class));
        Toast.makeText(getApplicationContext(), "Error loading pagerespon true", Toast.LENGTH_LONG).show();
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
        Toast.makeText(getApplicationContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
        finish();
    }

    class Payment extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // loadingMsg.setText("Loading");
            //linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String result;
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                //connection.addRequestProperty("Accept","application/json");
                connection.addRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");
               // connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("VANIK",params[1]);
                result = getResponse(connection);
                Log.i("VANIK",result);
                return result;

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response)
        {

           // linearLayout.setVisibility(View.INVISIBLE);
            super.onPostExecute(response);
            try {
                JSONObject jsonResponse = new JSONObject(response);
                Log.i("VANIK",jsonResponse.toString());
               // JSONObject dets = new JSONObject(location);
             //   Log.i("VANIK",dets.toString());
                orderDetails = new JSONObject();
                orderDetails.put("user_id",sharedPreferences.getString("user_id", null));
                orderDetails.put("mobbucket_id", jsonResponse.getString("id"));
                orderDetails.put("name",sharedPreferences.getString("username", null));
                orderDetails.put("email",sharedPreferences.getString("email", null));
                orderDetails.put("mobile", sharedPreferences.getString("phone", null));
              //  orderDetails.put("city", dets.getString("city"));
                Log.i("VANIK1", orderDetails.toString());


            }

            catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("RESPONSE",response);
        }
    }

    public String getResponse(HttpURLConnection httpURLConnection)
    {
        String result = "";
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read();
            while(data!= -1 )
            { result += (char)data;
                data = inputStreamReader.read();
            }
            return result;
        }
        catch(Exception e)
        { return e.getMessage();
        }
    }



    class MobPayment extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // loadingMsg.setText("Loading");
            //linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String result;
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                //connection.addRequestProperty("Accept","application/json");
                connection.addRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");
                //  connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("VANIK",params[1]);
                result = getResponse(connection);
                Log.i("VANIK",result);
                return result;

            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response)
        {

            //linearLayout.setVisibility(View.INVISIBLE);
            super.onPostExecute(response);
            try {
                JSONObject jsonResponse = new JSONObject(response);
                Log.i("VANIK",jsonResponse.toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                JSONObject jsonObject = new JSONObject(response);
                String orderid = "";
                orderid = jsonObject.getString("id");
                editor.putString("order_id",orderid);
                editor.apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("RESPONSE",response);
        }
    }

}


