package com.vapps.uvpa;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
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
     ArrayList<String> orderidList;
     ArrayList<String> addressList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket);
        listView = findViewById(R.id.exp);

        idList=new ArrayList<>();
        orderidList=new ArrayList<>();
        addressList=new ArrayList<>();
        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);
        String auth=sharedPreferences.getString("auth_token",null);
        Model modelLoader = new Model();
        modelLoader.execute("http://www.repairbuck.com/mobbuckets.json?auth_token="+auth);
        loader loader=new loader();
        loader.execute("http://www.repairbuck.com/orders.json?auth_token="+auth);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Header header = details.get(groupPosition);
                body info = header.getList().get(childPosition);

                Toast.makeText(getBaseContext(), "Clicked on" + header.getId() + "/" + info.getStatus(), Toast.LENGTH_LONG).show();
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
        });
    }

    private void expandAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            listView.expandGroup(i);
        }
    }

    private void collapseAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            listView.collapseGroup(i);
        }
    }

  public int loadata(String id,String adress) {
      int groupPosition = 0;
      ArrayList<Header> arrayList;
      Header info = headerList.get(id);
          info.setId(id);
          // info.setBrand(brand);
          //info.setModel(model);
          headerList.put(id, info);
          details.add(info);

      ArrayList<body> prod = info.getList();
      body detail = new body();
      detail.setAddress(adress);
      prod.add(detail);
      info.setList(prod);
      groupPosition = details.indexOf(info);
      return groupPosition;


    }

    public int add(String id,String status ) {
        int groupPosition = 0;
        ArrayList<Header> arrayList;
        Header info = headerList.get(id);
        if (info == null) {
            info = new Header();
            info.setId(id);
           // info.setBrand(brand);
            //info.setModel(model);
            headerList.put(id, info);
            details.add(info);

        }
        ArrayList<body> prod = info.getList();
        int lsize = prod.size();
        lsize++;
        body detail = new body();
        //detail.setAddress(address);
        detail.setStatus(status);
        //detail.setTotal(price);
        prod.add(detail);
        info.setList(prod);
        groupPosition = details.indexOf(info);
        return groupPosition;
    }


    class Model extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

              try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String status=jsonObject.getString("order_id");
                    idList.add(id);
                    orderidList.add(status);
                   // Log.i("test",id);
                    //Log.i("test2",status);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i=0;i<idList.size();i++)
            {
               // Log.i("List elemnt",idList.get(i));
                add(idList.get(i),orderidList.get(i));
            }

           adapter = new Dataadapter(Bucket.this, details);
           listView.setAdapter(adapter);



        }
    }
    class loader extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int j=0;j<idList.size();j++) {
                    for (int i = 0; i < jsonArray.length(); ++i) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        if(idList.get(j)==id) {
                            String repairId= jsonObject.getString("repair_id");
                            String room = jsonObject.getString("room");
                            String street = jsonObject.getString("street");
                            String area = jsonObject.getString("area");
                            String city = jsonObject.getString("city");
                            String address = room + "," + street + "," + area + "," + city;
                           // Log.i("Id",id);
                            Log.i("repairId",repairId);
                            Log.i("Address",address);
                            addressList.add(address);
                        }
                       // Log.i("AdressList",addressList.toString());
                   }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
          loadata(idList.get(1),addressList.get(0));
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