package com.vapps.uvpa;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class BackupPhoneSelection extends AppCompatActivity
{
    RadioGroup radioGroup;
    RadioButton radioButton;
    Intent  i;
    String ph="";
    JSONObject jsonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_phone_selection);
        radioGroup=findViewById(R.id.rg);
      //  ph="";

    }
    public void check(View view){
        int rb=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(rb);
        if(radioButton.getTag().toString().equals("yes"))
            ph="1";
        else if(radioButton.getTag().toString().equals("no"))
            ph="0";
    }
    public void nextActivity(View view)
    {
        i=getIntent();
        Intent j=new Intent(BackupPhoneSelection.this,MapsActivity.class);
        JSONObject repairHolder = new JSONObject();
        try
        { String str=i.getStringExtra("param");
            jsonObj=new JSONObject(str);

            if(ph.equals(""))
            {
                Toast.makeText(this,"Please Select one of the options",Toast.LENGTH_SHORT).show();
            }
            else
                {
                     jsonObj.put("phone", ph);
                     repairHolder.put("repair",jsonObj);
                     Log.i("Final", repairHolder.toString());
                     j.putExtra("order", repairHolder.toString());
                     j.putExtra("gadget",i.getStringExtra("gadget"));
                    Log.i("gadget",i.getStringExtra("gadget"));
                     startActivity(j);


                }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

   }


}
