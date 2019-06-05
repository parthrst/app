package com.vapps.uvpa;


import org.json.JSONObject;
import org.json.JSONException;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

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

         @Override
         protected void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             setContentView(R.layout.activity_confirnmation);
             intentget = getIntent();
             String str = intentget.getStringExtra("confirm");
             String location = intentget.getStringExtra("location");
             String gadget = intentget.getStringExtra("gadget");
             Log.i("gadget", intentget.getStringExtra("gadget"));
             if (gadget.equals("Mobile")) {
                 repairUrl = "http://www.repairbuck.com/repairs.json?auth_token=";
                 orderUrl = "http://www.repairbuck.com/orders.json?auth_token=";
             } else {
                 repairUrl = "http://www.repairbuck.com/laprepairs.json?auth_token=";
                 orderUrl = "http://www.repairbuck.com/laporders.json?auth_token=";
             }
             Log.i("orderc", str);
             getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
             sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
             postOrder = new PostOrder();
             linearLayout = findViewById(R.id.progressbar_layout);
             loadingMsg = findViewById(R.id.loading_msg);
             try {
                 jsonObj = new JSONObject(str);
                 orderHolder = new JSONObject(location);
                 Log.i("orderc", jsonObj.toString());
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }

PostOrder postLocation = new PostOrder();


 public void nextActivity(View view)
 {
     postOrder.execute(repairUrl + sharedPreferences.getString("auth_token", null), jsonObj.toString());
     postLocation.execute(orderUrl+sharedPreferences.getString("auth_token",null),orderHolder.toString());
     startActivity(new Intent(ConfirnmationActivity.this,Checksum.class));

 }



     class PostOrder extends AsyncTask<String, Void, String>
     {
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
           //  loadingMsg.setText("Loading");
             //linearLayout.setVisibility(View.VISIBLE);
         }

         @Override
         protected String doInBackground(String... params) {
             try {
                 String result;
                 URL url = new URL(params[0]);
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
                 Log.i("VANIK",id);
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.putString("id",id);
                 editor.apply();

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





 }