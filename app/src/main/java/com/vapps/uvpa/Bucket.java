package com.vapps.uvpa;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    ArrayList<String> totalList;
    ArrayList<String> addressList;
    ArrayList<String> mobileList;
    ArrayList<String> txnStatusList;
    LinearLayout linearLayout;
    TextView loadingMsg;
    ///////////////////////NICHE KE 3 VARIABLES PASTE///////////////////////////////////
    LinearLayout text;
    Button repair;
    int flag=0;
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Bucket.this,RepairOrder1.class));
    }
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
        txnStatusList=new ArrayList<>();
        addressList=new ArrayList<>();
        totalList=new ArrayList<>();
        mobileList=new ArrayList<>();
        /////////////TEXT  AND REPAIR PASTE KRNA/////////////////////
        text=findViewById(R.id.orderplace);
        repair=findViewById(R.id.order_repair_but);
        loadingMsg = findViewById(R.id.loading);
        linearLayout=findViewById(R.id.progressbar);
        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);
        String auth=sharedPreferences.getString("auth_token",null);
        Model modelLoader = new Model();
        modelLoader.execute("https://mobile-rb.herokuapp.com/mobpayments.json?auth_token="+auth);
          }
    public int add(String id,String brand,String model,String problem,String backup,String address,String total,String txnid,String txnstatus ) {
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
        detail.setAddress(address);
        detail.setTotal(total);
        detail.setPhoneNo(txnid);
        detail.setTxnStatus(txnstatus);
        prod.add(detail);
        info.setList(prod);
        groupPosition = details.indexOf(info);
        return groupPosition;
    }
    //////////CODE TO BE PASTED////////////////////////////
    public void repairOrder(View view){
        startActivity(new Intent(Bucket.this,RepairOrder1.class));

    }
    ///////////////////////////BS YAHAN TK/////////////////////////////////
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
                 ///////////////////PASTE START/////////
                  if(jsonArray.toString().equals("[]")){

                      text.setVisibility(View.VISIBLE);
                      repair.setVisibility(View.VISIBLE);
                  }
                  else {
                      flag=1;
                      ///////////////////////PASTE END//////////////
                      for (int i = 0; i < jsonArray.length(); ++i) {
                          JSONObject jsonObject = jsonArray.getJSONObject(i);
                          String id = jsonObject.getString("repair_id");
                          String company_id = jsonObject.getString("company_name");
                          String model_id = jsonObject.getString("model_name");
                          JSONArray problem = jsonObject.getJSONArray("problem_id");
                          String backup_phone = jsonObject.getString("phone");
                          String address = jsonObject.getString("room") + "," + jsonObject.getString("street") + "," + jsonObject.getString("area") + "," + jsonObject.getString("city");
                          String txnstatus = jsonObject.getString("status");
                          String phoneNo = jsonObject.getString("mobile");
                          String total = jsonObject.getString("amount");
                          String str = "";
                          Log.i("Repsonse", jsonObject.toString());
                          for (int j = 0; j < problem.length(); j++) {
                              String code = problem.getString(j);
                              Log.i("problem", code);
                              switch (code) {
                                  case "0":
                                      str = str + "Battery Problem";
                                      break;
                                  case "1":
                                      str = str + "Button Problem,";
                                      break;
                                  case "2":
                                      str = str + "Broken Screen,";
                                      break;
                                  case "3":
                                      str = str + "Charging Problem,";
                                      break;
                                  case "4":
                                      str = str + "Camera Problem,";
                                      break;
                                  case "5":
                                      str = str + "Water Damage,";
                                      break;
                                  case "6":
                                      str = str + "Headphone Jack Issue,";
                                      break;
                                  case "7":
                                      str = str + "Software Issue,";
                                      break;
                              }
                          }
                          totalList.add(total);
                          txnStatusList.add(txnstatus);
                          addressList.add(address);
                          mobileList.add(phoneNo);
                          idList.add(id);
                          modelList.add(model_id);
                          brandList.add(company_id);
                          problemidList.add(str);
                          backupList.add(backup_phone);
                      }
                      //////////////////EK BRACKET PASTE///////////////////
                  }

            } catch (JSONException e) {
                e.printStackTrace();
            }
              ////////////////IF CONDITION DAAL DIYO BS JO CODE HAI USPAO////////////////////
              if(flag!=0) {
                  for (int i = 0; i < idList.size(); i++) {
                      add(idList.get(i), brandList.get(i), modelList.get(i), problemidList.get(i), backupList.get(i), addressList.get(i), totalList.get(i), mobileList.get(i), txnStatusList.get(i));
                  }

                  adapter = new Dataadapter(Bucket.this, details);
                  listView.setAdapter(adapter);
                  listView.setVisibility(View.VISIBLE);
              }

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
        { return e.getMessage();
        }
    }
}