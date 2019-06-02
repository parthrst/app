package com.vapps.uvpa;


import org.json.JSONObject;
import org.json.JSONException;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

 public class ConfirnmationActivity extends AppCompatActivity
 {

         @Override
         protected void onCreate(Bundle savedInstanceState)
         {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_confirnmation);

             getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

         if (ContextCompat.checkSelfPermission(ConfirnmationActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
         {
             ActivityCompat.requestPermissions(ConfirnmationActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);

         }
     }




 public void nextActivity(View view)
 {
     startActivity(new Intent(ConfirnmationActivity.this,Checksum.class));
 }

 }