package com.vapps.uvpa;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class SignUp extends AppCompatActivity {

    EditText email;
    EditText password;
    EditText cpassword;
    EditText name;
    EditText mobileNo;
    int resp;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        cpassword = findViewById(R.id.cpassword);
        mobileNo = findViewById(R.id.mobile);
        linearLayout=findViewById(R.id.progressbar_layout);

       // getSupportActionBar().hide();

    }

    public void Login(View view)

    {
        startActivity(new Intent(SignUp.this,LoginActivity.class));
    }

    public void SignUpViaParse(View view)
    {
        JSONObject regDetails = new JSONObject();
        JSONObject holder = new JSONObject();

        try {

            if (!(email.getText().toString().equals("") || password.getText().toString().equals("")))

            {   regDetails.put("email", email.getText().toString());
                regDetails.put("name",name.getText().toString());
                regDetails.put("mob", mobileNo.getText().toString());
                regDetails.put("password", password.getText().toString());
                regDetails.put("password_confirmation", password.getText().toString());
                holder.put("user",regDetails);
                CredentialsReg task = new CredentialsReg();
                task.execute("http://www.amxdp.fun/api/registrations", holder.toString());

            }
            else
            {
                Toast.makeText(SignUp.this, "Login or Password can't be blank!", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    class CredentialsReg extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            linearLayout.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params)
        {

            try {
                URL url = new URL(params[0]);

                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.addRequestProperty("Accept","application/json");
                connection.addRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("LOGIN JSON DATA",params[1]);
                resp=connection.getResponseCode();
                String response=getResponse(connection);
                Log.i("RESPONSE CODE",String.valueOf(resp));
                Log.i("RESPONSE",response);
                if(resp!=200)
                {
                    Log.i("LOGIN RESPONSE CODE",String.valueOf(resp));
                    return String.valueOf(resp);
                }

                return response;
            }
            catch (ProtocolException e)
            {
                e.printStackTrace();
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String response)
        {
            linearLayout.setVisibility(View.INVISIBLE);

            if (response != null)
            {
                if(response == "401")
                {
                    Toast.makeText(SignUp.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String success = jsonResponse.getString("success");
                        if (success.equals("true"))
                        {
                            startActivity(new Intent(SignUp.this,LoginActivity.class));
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                Toast.makeText(SignUp.this, "Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String getResponse(HttpURLConnection httpURLConnection)
    {
        String result = "";
        try { InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int data = inputStreamReader.read();
            while(data!= -1 )
            { result += (char)data;
                data = inputStreamReader.read();
            }
            return result;
        }
        catch(Exception e)
        {
            return e.getMessage();
        } }

}
