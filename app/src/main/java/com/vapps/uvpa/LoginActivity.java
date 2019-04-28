package com.vapps.uvpa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.LogInCallback;

import com.parse.ParseException;

import com.parse.ParseUser;




public class LoginActivity extends AppCompatActivity
{

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        getSupportActionBar().hide();




        username = findViewById(R.id.username);
        password = findViewById(R.id.password);



    }

    public void SignUp(View view)
    {
        startActivity(new Intent(LoginActivity.this,SignUp.class));

    }


    public void login(View view)
    {

        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback()
        {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (user != null)
                {
                    Toast.makeText(LoginActivity.this,"Login succesful!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,RepairOrder1.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }


        });


    }
}
