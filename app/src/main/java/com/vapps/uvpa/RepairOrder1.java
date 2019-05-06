package com.vapps.uvpa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
import java.util.List;


public class RepairOrder1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Spinner spinner;
    private Spinner seriesSearch;
    private Button OderRepairButton;
    private ImageView imageViewDp;
    private TextView textViewEmail;
    private TextView textViewUsername;
    private TextView uname;
    private IntentIntegrator qrScan;
    // RecyclerView recyclerView;
   // RecyclerView.LayoutManager layoutManager;
     private ProgressBar progressBar;
     private ArrayList<String> list2=new ArrayList<>();
     SharedPreferences sharedPreferences;
     List<String>  seriesNames = new ArrayList<>();
     private  FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
     private  DatabaseReference mDatabase = firebaseDatabase.getReference();
    LinearLayout linearLayout;

    public void OrderRepair(View view)
    {
        startActivity(new Intent(RepairOrder1.this,IssueActivity.class));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_order1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Choose your device");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        OderRepairButton = findViewById(R.id.order_repair_but);
        spinner = findViewById(R.id.spinner_search);
        seriesSearch = findViewById(R.id.spinner_seriesSearch);
        progressBar=findViewById(R.id.progBar);
        linearLayout=findViewById(R.id.progressbar_layout);


        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);

         View headerView = navigationView.getHeaderView(0);

         textViewEmail = headerView.findViewById(R.id.textViewEmail);
         imageViewDp = headerView.findViewById(R.id.imageView);
         textViewUsername = headerView.findViewById(R.id.textViewUsername);

         textViewEmail.setText(sharedPreferences.getString("email",null));
         textViewUsername.setText(sharedPreferences.getString("username",null));
//       uname.setText("Hi! "+user.getDisplayName());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.brand_names,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
       {
           @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
           {
                    ModelLoader modelLoader = new ModelLoader();
                    modelLoader.execute("http://www.amxdp.club/models/cmodel?name="+parent.getSelectedItem().toString());
           }
           @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //Another interface callback
            }
        });
        qrScan = new IntentIntegrator(this);
    }
      @Override
    public void onBackPressed()
      {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
            {
            super.onBackPressed();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.repair_order1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
         //noinspection SimplifiableIfStatement
        if (item.getItemId()==R.id.SignOut)
        {   SignOut();
            return true;
        }
         return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if (id == R.id.nav_qr)
        { qrScan.initiateScan(); }
        else if (id == R.id.nav_location)
        {
            startActivity(new Intent(RepairOrder1.this, MapsActivity.class));
        }
        else if (id == R.id.nav_payment)
        { startActivity(new Intent(RepairOrder1.this, Payment_details.class));
        }
        else if (id == R.id.nav_manage)
        {
            startActivity(new Intent(RepairOrder1.this,QrGen.class));
        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {
            SignOut();
            startActivity(new Intent(RepairOrder1.this,LoginActivity.class));
            finish();

        }
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void Login(View view)
    {
        startActivity(new Intent(RepairOrder1.this,LoginActivity.class));
    }

    public void SignOut()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null)
            {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            }

            else {
                //if qr contains data
               // try {
                    //converting the data to json
                  //  JSONObject obj = new JSONObject(result.getContents());
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
             //   } catch (JSONException e) {
             //       e.printStackTrace();
              //  }
            }
        } else

            { super.onActivityResult(requestCode, resultCode, data);
            }
    }
   /* public void fetchData(String company,int pos)
    {
      //  progressBar.setVisibility(View.VISIBLE);
        //seriesNames = new ArrayList<>();
        mDatabase.child("MODEL/"+pos+"/"+company+"/").addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   list2.addAll(seriesNames);
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                { String  series = postSnapshot.getValue(String.class);
                          seriesNames.add(series);
               }
                seriesNames.removeAll(list2);
           }
           @Override
               public void onCancelled(@NonNull DatabaseError databaseError)
           {
           }
           });
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, seriesNames);
           seriesSearch.setAdapter(adapter);
          // progressBar.setVisibility(View.GONE);// seriesNames.clear();

        }
        //spinner.OnItemSelectedListener()
*/

      class ModelLoader extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                String result = "";
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();
                while(data != -1)
                {
                 result +=(char)data;
                 data = inputStreamReader.read();
                }
         return result;


            }
            catch (MalformedURLException e)
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
            super.onPostExecute(response);
            linearLayout.setVisibility(View.INVISIBLE);
            list2.addAll(seriesNames);
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0; i < jsonArray.length() ; ++i)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String series = jsonObject.getString("name");
                    seriesNames.add(series);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            seriesNames.removeAll(list2);
            list2.clear();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RepairOrder1.this, R.layout.support_simple_spinner_dropdown_item, seriesNames);
            seriesSearch.setAdapter(adapter);

        }
    }



}