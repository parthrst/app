package com.vapps.uvpa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BackupPhoneSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_phone_selection);


    }

    public void nextActivity(View view)
    {
        startActivity(new Intent(BackupPhoneSelection.this,Payment_details.class));
    }


}
