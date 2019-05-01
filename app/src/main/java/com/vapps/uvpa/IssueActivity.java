package com.vapps.uvpa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IssueActivity extends AppCompatActivity {
    public void nextActivity(View view)
    {
        startActivity(new Intent(IssueActivity.this,MapsActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
    }
}
