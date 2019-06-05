package com.vapps.uvpa;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class Forgotpassword extends AppCompatActivity {
    String regEmail;
    int resp;

    EditText regmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
         /*AlertDialog.Builder reset= new AlertDialog.Builder(Forgotpassword.this);
        View alertView=getLayoutInflater().inflate(R.layout.activity_forgotpassword,null);*/
        regmail=findViewById(R.id.registerdMial);

        //regEmail=regmail.getText().toString();
//Toast.makeText(this,regmail.getText().toString(),Toast.LENGTH_SHORT).show();


        /*reset.setView(alertView);
        AlertDialog dialog=reset.create();
        dialog.show();*/
    }
    Reset resetMial=new Reset();
    public void reset(View view)
    {

        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("email",regmail.getText().toString());
            Log.i("JSON",jsonObject.toString());

            resetMial.execute("http://www.amxdp.club/api/user/password",jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    class Reset extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        { super.onPreExecute();
            //linearLayout.setVisibility(View.VISIBLE);
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
            { e.printStackTrace();
            }
            catch (IOException e)
            { e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response)
        {
            //linearLayout.setVisibility(View.INVISIBLE);
            if (response != null)
            {
                if(response.equals("401"))
                {
                    Toast.makeText(Forgotpassword.this, "Please enter Registered Email! " , Toast.LENGTH_SHORT).show();
                }
                else {
                    try
                    {
                        JSONObject jsonResponse = new JSONObject(response);
                        String success = jsonResponse.getString("success");
                        if (success.equals("true"))
                        {
                            Toast.makeText(getApplicationContext(),"Link sent successfully!Check your mail",Toast.LENGTH_SHORT);
                            JSONObject jsonUser = jsonResponse.getJSONObject("data");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                Toast.makeText(Forgotpassword.this, "Please Check Your Internet Connection and Try Again", Toast.LENGTH_SHORT).show();
            }
           // Log.i("TEST",response);
        }

    }
    public String getResponse(HttpURLConnection httpURLConnection)
    {
        String result = "";
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
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
        }
    }
}

