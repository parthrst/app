package com.vapps.uvpa;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConfirnmationActivity extends AppCompatActivity
 {
     PostOrder postOrder;
     LinearLayout linearLayout;
     TextView loadingMsg;
     SharedPreferences sharedPreferences;
     Intent intentget;
     JSONObject jsonObj;

     JSONObject orderHolder;
     String repairUrl;
     String orderUrl;
     String location;
     String gadget;
     String baseprice="";
     TextView  address;
     TextView baseamount;
     TextView backup;
     TextView issue;
     TextView brand;
     TextView model;
     TextView total;
     String addressString;
     String modelString;
     String totalString;

     LinearLayout backupselection;



         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_confirnmation);
             intentget = getIntent();
             brand=findViewById(R.id.brand_disp);
             model=findViewById(R.id.model_disp);
             issue=findViewById(R.id.issue_disp);
             baseamount=findViewById(R.id.basic_charge);
             backup=findViewById(R.id.backup_price);
             total=findViewById(R.id.total_disp);
             address=findViewById(R.id.address_disp);
backupselection=findViewById(R.id.backup_layout);
             String str = intentget.getStringExtra("confirm");
              location = intentget.getStringExtra("location");
              gadget = intentget.getStringExtra("gadget");

              if(gadget.equals("Mobile"))
                 baseprice=baseprice+"150";
             else
                 baseprice=baseprice+"250";
             Log.i("base",baseprice);
baseamount.setText(baseprice);
         try {
             JSONObject detials = new JSONObject(str);
             JSONObject det = new JSONObject(location);
             addressString = det.getString("room") + "," + det.getString("street") + "," + det.getString("area") + "," + det.getString("city");
             modelString = detials.getString("repair");
             JSONObject inner = new JSONObject(modelString);
             String model_id = inner.getString("model_id");
             Log.i("model", modelString);
             String brandString = inner.getString("company_id");

             if (gadget.equals("Mobile")) {
                 String brandFinal = "";
                 switch (brandString) {
                     case "51":
                         brandFinal = brandFinal + "SAMSUNG";
                         break;
                     case "52":
                         brandFinal = brandFinal + "XIAOMI";
                         break;
                     case "53":
                         brandFinal = brandFinal + "MOTOROLA";
                         break;
                     case "54":
                         brandFinal = brandFinal + "OPPO";
                         break;
                     case "55":
                         brandFinal = brandFinal + "VIVO";
                         break;
                     case "56":
                         brandFinal = brandFinal + "MICROMAX";
                         break;
                     case "57":
                         brandFinal = brandFinal + "BLACKBERRY";
                         break;
                     case "58":
                         brandFinal = brandFinal + "HTC";
                         break;
                     case "59":
                         brandFinal = brandFinal + "LG";
                         break;
                     case "60":
                         brandFinal = brandFinal + "SONY";
                         break;
                     case "61":
                         brandFinal = brandFinal + "ONEPLUS";
                         break;
                     case "62":
                         brandFinal = brandFinal + "NOKIA";
                         break;
                     case "63":
                         brandFinal = brandFinal + "GIONEE";
                         break;
                 }
                 brand.setText(brandFinal);
             } else {
                 brand.setText(brandString);
             }
             model.setText(model_id);
             String problemString="";
             JSONArray problem =inner.getJSONArray("problem_id");
             for (int j = 0; j < problem.length(); j++) {
                 String code = problem.getString(j);
                 switch (code) {
                     case "0":
                         problemString = problemString + "Battery Problem";
                         break;
                     case "1":
                         problemString = problemString + "Button Problem,";
                         break;
                     case "2":
                         problemString = problemString + "Broken Screen,";
                         break;
                     case "3":
                         problemString = problemString + "Charging Problem,";
                         break;
                     case "4":
                         problemString = problemString + "Camera Problem,";
                         break;
                     case "5":
                         problemString = problemString + "Water Damage,";
                         break;
                     case "6":
                         problemString = problemString + "Headphone Jack Issue,";
                         break;
                     case "7":
                         problemString = problemString + "Software Issue,";
                         break;
                 }
                 issue.setText(problemString);
                 String phone = inner.getString("phone");
                 if (phone.equals("1"))
                     backup.setText("30");
                 else
                     backup.setText("0");
                 if (gadget.equals("Mobile")) {
                     totalString = String.valueOf(Integer.parseInt(backup.getText().toString()) + Integer.parseInt(baseamount.getText().toString()));
                 } else
                     totalString = baseprice;
                 total.setText(totalString);
                 Log.i("Detials", detials.toString());
             }
         }catch (JSONException e) {
             e.printStackTrace();
         }
         address.setText(addressString);
         Log.i("gadget", intentget.getStringExtra("gadget"));
             if (gadget.equals("Mobile")) {
                 repairUrl = "https://www.repairbuck.com/repairs.json?auth_token=";
                 orderUrl = "https://www.repairbuck.com/orders.json?auth_token=";
             } else {
                 repairUrl = "https://www.repairbuck.com/laprepairs.json?auth_token=";
                 orderUrl = "https://www.repairbuck.com/laporders.json?auth_token=";
                 backupselection.setVisibility(View.GONE);
             }

             getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
             sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
             postOrder = new PostOrder();
             linearLayout = findViewById(R.id.progressbar_layout);
             loadingMsg = findViewById(R.id.loading_msg);

             try {
                 jsonObj = new JSONObject(str);
             } catch (JSONException e) {
                 e.printStackTrace();
             }

         }

  Order postLocation = new Order();


 public void nextActivity(View view)
 {
     postOrder.execute(repairUrl + sharedPreferences.getString("auth_token", null), jsonObj.toString());

     Intent intent = new Intent(ConfirnmationActivity.this,Checksum.class);
     intent.putExtra("gadget",gadget);
     startActivity(intent);

 }



     class PostOrder extends AsyncTask<String, Void, String>
     {URL url;
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
           //  loadingMsg.setText("Loading");
             //linearLayout.setVisibility(View.VISIBLE);
         }

         @Override
         protected String doInBackground(String... params)
         {
             try {
                 String result;
                 url = new URL(params[0]);
                 HttpURLConnection connection = (HttpURLConnection)url.openConnection();
               //  connection.addRequestProperty("Accept","application/json");
                 connection.addRequestProperty("Content-Type","application/json");
                 connection.setRequestMethod("POST");
                 //connection.setDoOutput(true);
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

        //     linearLayout.setVisibility(View.INVISIBLE);
             super.onPostExecute(response);
            try {
                JSONObject jsonResponse = new JSONObject(response);

                String id = jsonResponse.getString("id");
                Log.i("VANIK", id);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("id", id);
                editor.apply();


                JSONObject dets = new JSONObject(location);
                Log.i("VANIK",dets.toString());
                JSONObject orderDetails = new JSONObject();
                orderDetails.put("repair_id", sharedPreferences.getString("id", null));
                orderDetails.put("room", dets.getString("room"));
                orderDetails.put("street", dets.getString("street"));
                orderDetails.put("area", dets.getString("area"));
                orderDetails.put("city", dets.getString("city"));
                Log.i("VANIK1", orderDetails.toString());
                orderHolder = new JSONObject();
                orderHolder.put("order", orderDetails);
                Log.i("VANIK1", orderHolder.toString());
                postLocation.execute(orderUrl + sharedPreferences.getString("auth_token", null), orderHolder.toString());


            }

             catch (JSONException e)
             {
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
         {
             return e.getMessage();
         }
     }




     class Order extends AsyncTask<String, Void, String>
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