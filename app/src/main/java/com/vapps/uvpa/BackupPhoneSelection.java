package com.vapps.uvpa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

public class BackupPhoneSelection extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
        Intent  i;
        String ph;
    JSONObject jsonObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_phone_selection);
        radioGroup=findViewById(R.id.rg);


    }

    public void nextActivity(View view)  {
        i=getIntent();
        Intent j=new Intent(BackupPhoneSelection.this,MapsActivity.class);

        String str=i.getStringExtra("param");

        try{
            jsonObj=new JSONObject(str);
            int rb=radioGroup.getCheckedRadioButtonId();
            radioButton=findViewById(rb);
            if(rb==2131361839)
                ph="1";
            else if(rb==2131361838)
                ph="0";
            jsonObj.put("phone",ph);
            Log.i("Final",jsonObj.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(j);
    }


}
