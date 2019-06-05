package com.vapps.uvpa;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Bucket extends AppCompatActivity {

    private LinkedHashMap<String, Header> headerList = new LinkedHashMap<String, Header>();
    private ArrayList<Header> details = new ArrayList<>();
    private Dataadapter adapter;
    private ExpandableListView listView;
    SharedPreferences sharedPreferences;
    ArrayList<String> idList;
    ArrayList<String> problemidList;
    ArrayList<String> backupList;
    ArrayList<String> modelList;
    ArrayList<String> brandList;
    LinearLayout linearLayout;
    TextView loadingMsg;
    DisplayMetrics metrics;
 int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);
        listView = findViewById(R.id.exp);
        idList=new ArrayList<>();
        problemidList=new ArrayList<>();
        backupList=new ArrayList<>();
        modelList=new ArrayList<>();
        brandList=new ArrayList<>();
        loadingMsg = findViewById(R.id.loading);
        linearLayout=findViewById(R.id.progressbar);
        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);
        String auth=sharedPreferences.getString("auth_token",null);
        Model modelLoader = new Model();
        modelLoader.execute("http://www.repairbuck.com/repairs.json?auth_token="+auth);

       /* listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Header header = details.get(groupPosition);
                body info = header.getList().get(childPosition);

                Toast.makeText(getBaseContext(), "Clicked on" + header.getId() + "/" + info.getBackupPhone(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Header header = details.get(groupPosition);
                Toast.makeText(getBaseContext(), "HEader is:" + header.getId(), Toast.LENGTH_LONG).show();
                return false;
            }
        });*/
    }
    public int add(String id,String brand,String model,String problem,String backup ) {
        int groupPosition = 0;
        ArrayList<Header> arrayList;
        Header info = headerList.get(id);
        if (info == null) {
            info = new Header();
            info.setId(id);
           info.setBrand(brand);
           info.setModel(model);
            headerList.put(id, info);
            details.add(info);

        }
        ArrayList<body> prod = info.getList();
        int lsize = prod.size();
        lsize++;
        body detail = new body();

        detail.setProblemids(problem);
        if(backup.equals("true")) {
            detail.setBackupPhone("Yes");
        }
        else if(backup.equals("false"))
        {
            detail.setBackupPhone("No");
        }
        prod.add(detail);
        info.setList(prod);
        groupPosition = details.indexOf(info);
        return groupPosition;
    }
    class Model extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingMsg.setText("Fetching Orders");
            linearLayout.setVisibility(View.VISIBLE);
                    }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String result;
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                result = getResponse(connection);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            linearLayout.setVisibility(View.GONE);
              try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String company_id=jsonObject.getString("company_id");
                    String model_id=jsonObject.getString("model_id");
                    JSONArray problem =jsonObject.getJSONArray("problem_ids");
                    String backup_phone=jsonObject.getString("phone");
                    String str="";
                    for(int j=0;j<problem.length();j++) {
                      String code=problem.getString(j);
                      Log.i("problem",code);
                      switch(code){
                          case "0": str=str+"Battery Problem";
                                       break;
                          case "1":  str=str+"Button Problem,";
                              break;
                          case "2":  str=str+"Broken Screen,";
                              break;
                          case "3":  str=str+"Charging Problem,";
                              break;
                          case "4":  str=str+"Camera Problem,";
                              break;
                          case "5":  str=str+"Water Damage,";
                              break;
                          case "6":  str=str+"Headphone Jack Issue,";
                              break;
                          case "7":  str=str+"Software Issue,";
                              break;
                      }
                    }
                    String company="";
                    switch (company_id){
                        case"51":company=company+"SAMSUNG";
                                 break;
                        case "52":company=company+"XIAOMI";
                        break;
                        case "53":company=company+"MOTOROLA";
                            break;
                        case "54":company=company+"OPPO";
                            break;
                        case "55":company=company+"VIVO";
                            break;
                        case "56":company=company+"MICROMAX";
                            break;
                        case "57":company=company+"BLACKBERRY";
                            break;
                        case "58":company=company+"HTC";
                            break;
                        case "59":company=company+"LG";
                            break;
                        case "60":company=company+"SONY";
                            break;
                        case "61":company=company+"ONEPLUS";
                            break;
                        case "62":company=company+"NOKIA";
                            break;
                        case "63":company=company+"GIONEE";
                            break;

                    }
                    idList.add(id);
                    modelList.add(model_id);
                    brandList.add(company);
                    problemidList.add(str);
                    backupList.add(backup_phone);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i=0;i<idList.size();i++)
            {
                add(idList.get(i),brandList.get(i),modelList.get(i),problemidList.get(i),backupList.get(i));
            }

           adapter = new Dataadapter(Bucket.this, details);
           listView.setAdapter(adapter);
           listView.setVisibility(View.VISIBLE);
            metrics = new DisplayMetrics();
            width = metrics.widthPixels;
           listView.setIndicatorBounds(width-GetDipsFromPixel(3), width-GetDipsFromPixel(5));
       }
    }
    public int GetDipsFromPixel(float pixels)
    {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
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
        { return e.getMessage();
        }
    }
}