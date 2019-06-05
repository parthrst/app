package com.vapps.uvpa;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    String city, area;
    Intent intentget;
    JSONObject jsonObj;
    SharedPreferences sharedPreference;
    Intent j;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {

            case REQUEST_CODE: {

                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        buildLocationRequest();
                        buildLocationCallback();

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getApplicationContext(), "Please allow permission", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MapsActivity.this, BackupPhoneSelection.class));
                    }
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MapsActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        linearLayout = findViewById(R.id.progressbar_layout);
        intentget = getIntent();
        sharedPreference = getSharedPreferences("user_details", MODE_PRIVATE);
        String str = intentget.getStringExtra("order");
        Log.i("order", str);
        try {
            jsonObj = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Log.i("VANIK",sharedPreference.getString("id",null));
        j = new Intent(MapsActivity.this, ConfirnmationActivity.class);
        Log.i("order", jsonObj.toString());
        j.putExtra("confirm", jsonObj.toString());


        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        } else {

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

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);


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


    public void buildLocationRequest() {
        linearLayout.setVisibility(View.VISIBLE);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);

    }


    public void buildLocationCallback() {

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {


                    geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    textView = findViewById(R.id.location);
                    if (addresses == null || addresses.size() == 0) {
                        Toast.makeText(MapsActivity.this, "Error in fetching loacation!", Toast.LENGTH_SHORT).show();
                        linearLayout.setVisibility(View.INVISIBLE);
                        textView.setText("Location can't be fetched");
                    } else {
                        String address = addresses.get(0).getAddressLine(0);
                        city = addresses.get(0).getLocality();
                        area = addresses.get(0).getAdminArea();// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
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



    public void nextActivity(View view) {
        JSONObject orderDetails = new JSONObject();
        JSONObject orderHolder = new JSONObject();

        try {

            // orderDetails.put("id","13");
            hno = findViewById(R.id.hno);
            landmark = findViewById(R.id.lndmrk);
            orderDetails.put("repair_id", "230");
            orderDetails.put("room", hno.getText().toString());
            orderDetails.put("street", landmark.getText().toString());
            orderDetails.put("area", area);
            orderDetails.put("city", city);
            orderHolder.put("order", orderDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // order.execute("http://www.repairbuck.com/orders.json?auth_token="+sharedPreferences.getString("auth_token",null),orderHolder.toString());
        //startActivity(new Intent(MapsActivity.this, ConfirnmationActivity.class));
        j.putExtra("location", orderHolder.toString());
        j.putExtra("gadget",intentget.getStringExtra("gadget"));
       // Log.i("order",  intentget.getStringExtra("gadget").toS);
        Log.i("gadget",intentget.getStringExtra("gadget"));
        startActivity(j);
    }


}
/*




 */
