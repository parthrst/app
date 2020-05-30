package com.vapps.uvpa;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.parse.ParseAnalytics;


public class MainActivity extends AppCompatActivity
{

SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        if (Build.VERSION.SDK_INT > 21) {
            Window window = getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorBlack));
        }
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                           startActivity(intent);
      //startActivity(new Intent(MainActivity.this, RepairOrder1.class));

//        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);


//        new Handler().postDelayed
//                (new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                        String token = sharedPreferences.getString("auth_token",null);
//                        if (token != null)
//                        {
//                            startActivity(new Intent(MainActivity.this, RepairOrder1.class));
//
//                        }
//                        else {
//                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }
//                        finish();
//                    }
//                }, 700);


    }

}
