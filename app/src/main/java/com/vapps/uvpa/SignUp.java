package com.vapps.uvpa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import java.util.List;

public class SignUp extends AppCompatActivity {


    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
    }
public void signUpAct(View view)
{

        EditText emailEditText =  findViewById(R.id.email);
        EditText usernameEditText =  findViewById(R.id.uname);

        EditText passwordEditText = findViewById(R.id.psw);

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("") || emailEditText.getText().toString().matches("")) {

            Toast.makeText(this, "Email/Username/Password  field(s) is/are required.", Toast.LENGTH_SHORT).show();

        } else {


       /*     ParseUser user = new ParseUser();
            user.setEmail(emailEditText.getText().toString());
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());


            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {

                        Log.i("Signup", "Successful");

                    } else {

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });*/

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {

                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUp.this, user.toString(), Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


        }}





}






