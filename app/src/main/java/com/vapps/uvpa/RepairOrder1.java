package com.vapps.uvpa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
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

    String gadget,brand,model;
    private Spinner spinner;
    private Spinner laptopSeriesSearch;
    private Spinner device;
    private Spinner mobileSeriesSearch;
    private Button OderRepairButton;
    private ImageView imageViewDp;
    private TextView textViewEmail;
    private TextView textViewUsername;
    private TextView uname;
    LinearLayout buttons;
    LinearLayout spinners;
    private ProgressBar progressBar;
    private ArrayList<String> list2=new ArrayList<>();
    SharedPreferences sharedPreferences;
    List<String>  seriesNames = new ArrayList<>();
    private  FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
    private  DatabaseReference mDatabase = firebaseDatabase.getReference();

    LinearLayout linearLayout;
    TextView loadingMsg;
    ArrayList<String> repair;
    Button place;
    Button orderView;

    public void OrderRepair(View view) {

        if (gadget.equals("") || brand.equals("") || model.equals("")) {
            Toast.makeText(this, "Please select all the options!", Toast.LENGTH_SHORT).show();
        } else {
            repair = new ArrayList<>();
            JSONObject repairDetails = new JSONObject();
           // JSONObject repairHolder = new JSONObject();

            try {

              //  repairDetails.put("id", "13");
                repairDetails.put("company_id", brand);
                repairDetails.put("model_id", model);

               // repairHolder.put("repairs", repairDetails);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //postOrder.execute("http://www.repairbuck.com/repairs.json?auth_token=" + sharedPreferences.getString("auth_token", null), repairHolder.toString());


            Intent intent = new Intent(RepairOrder1.this, IssueActivity.class);
            intent.putExtra("repair", repairDetails.toString());
            intent.putExtra("gadget",gadget);
            Log.i("gadget",gadget);
            startActivity(intent);



        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_order1);

        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);
        Repair repair=new Repair();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Choose your device");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        buttons=findViewById(R.id.start);
        spinners=findViewById(R.id.next);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        OderRepairButton = findViewById(R.id.order_repair_but);
        spinner = findViewById(R.id.spinner_search);
        laptopSeriesSearch = findViewById(R.id.laptop_spinner_seriesSearch);
        mobileSeriesSearch = findViewById(R.id.mobile_spinner_seriesSearch);
        device=findViewById(R.id.device);
        progressBar =findViewById(R.id.progBar);
        linearLayout=findViewById(R.id.progressbar_layout);
        loadingMsg = findViewById(R.id.loading_msg);
        mobileSeriesSearch.setVisibility(View.GONE);
        place=findViewById(R.id.place);
        orderView=findViewById(R.id.view);


        View headerView = navigationView.getHeaderView(0);

        textViewEmail = headerView.findViewById(R.id.textViewEmail);
        imageViewDp = headerView.findViewById(R.id.imageView);
        textViewUsername = headerView.findViewById(R.id.textViewUsername);

        textViewEmail.setText(sharedPreferences.getString("email",null));
        textViewUsername.setText(sharedPreferences.getString("username",null));
//       uname.setText("Hi! "+user.getDisplayName());
        ArrayAdapter<CharSequence> deviceAdapter = ArrayAdapter.createFromResource(this,R.array.device,R.layout.support_simple_spinner_dropdown_item);
        deviceAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        device.setAdapter(deviceAdapter);
        repair.execute("http://www.repairbuck.com/repairs.json?auth_token=" + sharedPreferences.getString("auth_token", null));



    }
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
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


        if (id == R.id.bucket)
        {
            startActivity(new Intent(RepairOrder1.this,Bucket.class));

        }
        else if (id == R.id.nav_share)
        {

        }
       else if (id == R.id.about)
       {
           startActivity(new Intent(RepairOrder1.this,About.class));

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
    public void placeOrder(View view){
        getSupportActionBar().setTitle("Choose your device");
        order();

    }
    public void viewOrder(View view)
    {
        startActivity( new Intent(RepairOrder1.this,Bucket.class));
    }
    public void order()
    {
        gadget="";model="";brand="";
        buttons.setVisibility(View.GONE);
        spinners.setVisibility(View.VISIBLE);
        OderRepairButton.setVisibility(View.VISIBLE);
        device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    laptopSeriesSearch.setVisibility(View.VISIBLE);
                    mobileSeriesSearch.setVisibility(View.GONE);
                    gadget = parent.getSelectedItem().toString();
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RepairOrder1.this, R.array.lap_brand, R.layout.support_simple_spinner_dropdown_item);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ArrayAdapter<CharSequence> laptopAdapter = ArrayAdapter.createFromResource(RepairOrder1.this, R.array.laptop_names, R.layout.support_simple_spinner_dropdown_item);
                            laptopAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            laptopSeriesSearch.setAdapter(laptopAdapter);
                            brand =Integer.toString(position);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    laptopSeriesSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            model = parent.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (position == 0) {
                    laptopSeriesSearch.setVisibility(View.GONE);
                    mobileSeriesSearch.setVisibility(View.VISIBLE);
                    gadget = parent.getSelectedItem().toString();
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(RepairOrder1.this, R.array.brand_names, R.layout.support_simple_spinner_dropdown_item);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            brand =Integer.toString(position+51);
                            ModelLoader modelLoader = new ModelLoader();
                            modelLoader.execute("https://www.repairbuck.com/models/cmodel?name=" + parent.getSelectedItem().toString());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            //Another interface callback
                        }
                    });
                    mobileSeriesSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            model = parent.getSelectedItem().toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    class ModelLoader extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loadingMsg.setText("Fetching Models");
            linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            try
            {
                String result;
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                result = getResponse(connection);
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
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
            seriesNames.removeAll(list2);
            list2.clear();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(RepairOrder1.this, R.layout.support_simple_spinner_dropdown_item, seriesNames);
            mobileSeriesSearch.setAdapter(adapter);

        }
    }

    class Repair extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            spinners.setVisibility(View.GONE);
            OderRepairButton.setVisibility(View.GONE);
            buttons.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Choose an option");
            loadingMsg.setText("Initializing");
            linearLayout.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... urls) {
            try
            {
                String result;
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                result = getResponse(connection);
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
                if(jsonArray.toString().equals("[]")){
                    getSupportActionBar().setTitle("Choose your device");
                    order();

                }
                else {
                    buttons.setVisibility(View.VISIBLE);
                    OderRepairButton.setVisibility(View.GONE);
                    spinners.setVisibility(View.GONE);

                }


            } catch (JSONException e) {
                e.printStackTrace();
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
        {
            return e.getMessage();
        }
    }
}