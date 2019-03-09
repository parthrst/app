package com.vapps.uvpa;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;




public class MainActivity extends AppCompatActivity
{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT > 21) {
            Window window = getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBlack));
        }

        new Handler().postDelayed
                (new Runnable() {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent(MainActivity.this, RepairOrder1.class);
                        startActivity(intent);
   
                        finish();
                    }
                }, 1000);


    }

}
