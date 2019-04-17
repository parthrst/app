package com.vapps.uvpa;


import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class RepairOrder1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {



    private Button Login;
    private Button OderRepairButton;
    private ImageView imageViewDp;
    private TextView textViewEmail;
    private TextView textViewUsername;
    private ImageDownloader imageDownloader;
    private TextView uname;
    private IntentIntegrator qrScan;

   // RecyclerView recyclerView;
   // RecyclerView.LayoutManager layoutManager;


    List<String> seriesNames = new ArrayList<String>();

    private  FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();
    private  DatabaseReference mDatabase = firebaseDatabase.getReference();


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
      //  spinner = findViewById(R.id.spinner_search);
        fetchData(parent.getSelectedItem().toString(),position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {



    }

    public void OrderRepair(View view) {
    }


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>
    {


        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imageViewDp.setImageBitmap(bitmap);

        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repair_order1);
               Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    //    FirebaseApp.initializeApp(RepairOrder1.this);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        List<Brands> brandsNamesList = new ArrayList<>();

        Resources res = getResources();

        String array[] = res.getStringArray(R.array.brand_names);


    for(String brandName: array)
    {

        brandsNamesList.add(new Brands(brandName));

    }

       // List.size());


       BrandViewAdapter brandViewAdapter = new BrandViewAdapter(this,brandsNamesList);


View headerView = navigationView.getHeaderView(0);

textViewEmail = headerView.findViewById(R.id.textViewEmail);
imageViewDp = headerView.findViewById(R.id.imageView);
textViewUsername = headerView.findViewById(R.id.textViewUsername);

  if (user!=null)
{
    textViewEmail.setText(user.getEmail());
    textViewUsername.setText(user.getDisplayName());

    uname.setText("Hi! "+user.getDisplayName());


    imageDownloader = new ImageDownloader();


            if(user.getPhotoUrl().toString() != null)
            {
                imageDownloader.execute(user.getPhotoUrl().toString());

            }

}



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.brand_names,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

      //  spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter3  = ArrayAdapter.createFromResource(this,R.array.type_repair,R.layout.support_simple_spinner_dropdown_item);

        adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //typeRepair.setAdapter(adapter3);
        qrScan = new IntentIntegrator(this);

    }


      @Override
    public void onBackPressed()
      {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        {
            SignOut();
            return true;

        }
         return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_qr)
        {

            qrScan.initiateScan();

   }
        else if (id == R.id.nav_location)
        {
            startActivity(new Intent(RepairOrder1.this, MapsActivity.class));
        }
        else if (id == R.id.nav_payment)
        {

            startActivity(new Intent(RepairOrder1.this, Payment_details.class));

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
              startActivity(new Intent(RepairOrder1.this,Demo.class));

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
        FirebaseAuth.getInstance().signOut();
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

            {
                super.onActivityResult(requestCode, resultCode, data);

            }
    }



    public void fetchData(String company,int pos)
    {
           mDatabase.child("MODEL/"+pos+"/"+company+"/").addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
                {
                       String series = postSnapshot.getValue(String.class);
                          seriesNames.add(series);

               }

           }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

           ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, seriesNames);
           //seriesSearch.setAdapter(adapter);

        }

    }

        
        
        
        
        