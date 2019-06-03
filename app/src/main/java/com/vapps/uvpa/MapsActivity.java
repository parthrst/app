package com.vapps.uvpa;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;


    private static final int REQUEST_CODE = 1000;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Geocoder geocoder;
    List<Address> addresses;
    TextView textView;
    LatLng latLng;
    LinearLayout linearLayout;
    SharedPreferences sharedPreferences;
    EditText hno;
    EditText landmark;
    String city;
    SharedPreferences sharedPreference;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {

            case REQUEST_CODE:
                {

                if (grantResults.length > 0)
                {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        buildLocationRequest();
                        buildLocationCallback();

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                    {
                        Toast.makeText(getApplicationContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MapsActivity.this,RepairOrder1.class));
                    }
          }
            }
      }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        linearLayout=findViewById(R.id.progressbar_layout);
        sharedPreference = getSharedPreferences("user_details",MODE_PRIVATE);

       // Log.i("VANIK",sharedPreference.getString("id",null));

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        } else
            {

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            buildLocationRequest();
            buildLocationCallback();


        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        sharedPreferences = getSharedPreferences("user_details",MODE_PRIVATE);
        hno = findViewById(R.id.hno);
        landmark = findViewById(R.id.lndmrk);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
      //  Location location = LocationServices.getFusedLocationProviderClient(fusedLocationProviderClient).getLastLocation();
        // Add a marker in Sydney and move the camera
        //latLng = new LatLng(location.getLatitude(), location.getLongitude());
        buildLocationRequest();
        buildLocationCallback();
       // mMap.addMarker(new MarkerOptions().position(latLng).title(completeAddress));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
      //  mMap.setMinZoomPreference(5);


    }


    public void buildLocationRequest()
    {
        linearLayout.setVisibility(View.VISIBLE);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);

    }


    public void buildLocationCallback()
    {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {


                    geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    textView = findViewById(R.id.location);
                    if (addresses == null || addresses.size()  == 0) {
                        Toast.makeText(MapsActivity.this,"Error in fetching loacation!",Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.INVISIBLE);
                        textView.setText("Location can't be fetched");
                        }

                       else
                           {
                        String address = addresses.get(0).getAddressLine(0);
                        city = addresses.get(0).getLocality();// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        //  addresses.get(0).ge
                        textView.setText(address);
                        linearLayout.setVisibility(View.INVISIBLE);
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.setMinZoomPreference(5);
                    }
     }
         }
        };
  }
    Order order = new Order();

    public void nextActivity(View view)
    {
        JSONObject orderDetails = new JSONObject();
        JSONObject orderHolder = new JSONObject();

        try {

           // orderDetails.put("id","13");
            
            orderDetails.put("repair_id",sharedPreferences.getString("id",null));
            orderDetails.put("room",hno.getText().toString());
            orderDetails.put("street",landmark.getText().toString());
            orderDetails.put("area","Urrapakkam");
            orderDetails.put("city",city);
            orderHolder.put("order",orderDetails);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        order.execute("http://www.repairbuck.com/orders.json?auth_token="+sharedPreferences.getString("auth_token",null),orderHolder.toString());
        startActivity(new Intent(MapsActivity.this, ConfirnmationActivity.class));
    }



    class Order extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // loadingMsg.setText("Loading");
            //linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String result;
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                //connection.addRequestProperty("Accept","application/json");
                connection.addRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");
                //  connection.setDoOutput(true);
                connection.connect();
                DataOutputStream outputStream=new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(params[1]);
                Log.i("VANIK",params[1]);
                result = getResponse(connection);
                Log.i("VANIK",result);
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

            linearLayout.setVisibility(View.INVISIBLE);
            super.onPostExecute(response);
            try {
                JSONObject jsonResponse = new JSONObject(response);
             Log.i("VANIK",jsonResponse.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("RESPONSE",response);
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
/*




 */
