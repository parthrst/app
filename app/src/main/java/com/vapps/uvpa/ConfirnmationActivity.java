package com.vapps.uvpa;

import android.content.Intent;
import android.app.Activity;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ConfirnmationActivity extends AppCompatActivity
{
   SharedPreferences sharedPreferences;
    String email;
    String phone;
    String amount;
    String purpose;
    String buyername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirnmation);
        // Call the function callInstamojo to start payment here
        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);
        email = sharedPreferences.getString("email",null);
        phone = sharedPreferences.getString("phone",null);
        buyername = sharedPreferences.getString("username",null);
        amount = "10";
        purpose = "phone repair";

    }

    public void nextActivity(View view)
    {
        callInstamojoPay(email,phone,amount,purpose,buyername);
    }


    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response)
            {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }


}
