package com.vapps.uvpa;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  DownloadData extends AppCompatActivity
{

    SharedPreferences sharedPreferences;
    ArrayList<String> phoneURLs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_data);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        newlist();
 }



public void newlist() {

    DownloadTask task = new DownloadTask();
    String result = null;

    try {
        result = task.execute("https://www.repairbuck.com/repairs?auth_token="+sharedPreferences.getString("auth_token", null)).get();
        Log.i("METAres",result);
        String[] splitResult = result.split("<div class=\"need-space\">");
        Log.i("METAsplit",splitResult[1]);

        Pattern p = Pattern.compile("<span class=\"badge badge-success\">(.*?)</span>",Pattern.DOTALL);
        Matcher m = p.matcher(splitResult[1]);
        Pattern p1 = Pattern.compile("<span class=\"badge badge-success\">(.*?)</span>",Pattern.DOTALL);
        Matcher m1 = p1.matcher(splitResult[1]);


        Log.i("META1",m.toString());
        while (m.find())
        {
            phoneURLs.add(m.group(1));
            Log.i("META2",m.group(1));
        }

    } catch (InterruptedException | ExecutionException e1) {
        e1.printStackTrace();
    }
    Log.i("META",phoneURLs.toString());
    phoneURLs.clear();
}


    class DownloadTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection)url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();
                }

                return result;

            }
            catch (Exception e)
            {

                e.printStackTrace();

            }
            return null;
        }
    }}



