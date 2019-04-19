package com.vapps.uvpa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText username;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
       // getSupportActionBar().hide();


    }

    public void Login(View view)

    {
        startActivity(new Intent(SignUp.this,LoginActivity.class));
    }

    public void SignUpViaParse(View view)
    {

        ParseUser user = new ParseUser();

        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
        user.setUsername(username.getText().toString());

        user.signUpInBackground(new SignUpCallback()
        {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    Toast.makeText(SignUp.this,"Sign Up succesful!",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
