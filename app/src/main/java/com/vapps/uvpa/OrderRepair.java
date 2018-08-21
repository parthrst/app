package com.vapps.uvpa;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class OrderRepair extends AppCompatActivity {


    private Spinner spinner;
    private Spinner seriesSearch;
    private Spinner typeRepair;

    private Button Login;
    private Button OderRepairButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_repair);


        Login = (Button)findViewById(R.id.login_button);

        OderRepairButton = findViewById(R.id.order_repair_but);

        spinner = findViewById(R.id.spinner_search);

        typeRepair = findViewById(R.id.type_repair);

        seriesSearch = findViewById(R.id.spinner_seriesSearch);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.brand_names,R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        PhoneDataDbHelper phoneDataDbHelper = new PhoneDataDbHelper(this);

        SQLiteDatabase sqLiteDatabase = phoneDataDbHelper.getWritableDatabase();

        InputStream inputStream = getResources().openRawResource(R.raw.phonedat);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String Phones="";

        try {

            Phones = bufferedReader.readLine();

        }

        catch (IOException e)
        {
            e.printStackTrace();
        }


        String SplittedPhones[] = Phones.split("VANIKPHONES");

        String SamsungPhones[] = SplittedPhones[1].split(",");

        Log.i("METAD",SplittedPhones[0]);

        Log.i(" METAD5",SamsungPhones[0]);

        ContentValues contentValues = new ContentValues();

        for (String each : SamsungPhones)
        {
            contentValues.put(PhoneDataContract.PhonedData.COLUNMN_SAMSUNG,each);

            contentValues.put(PhoneDataContract.PhonedData.COLUNMN_MI,0);

            sqLiteDatabase.insert(PhoneDataContract.PhonedData.TABLE_NAME, null ,contentValues);

            contentValues = new ContentValues();
        }


        String XiaomiPhones[] = SplittedPhones[4].split(",");


        Cursor cursor = sqLiteDatabase.query
                (
                        PhoneDataContract.PhonedData.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );


        int count = cursor.getCount();

        Log.i("METAD3", Integer.toString(count));

        List<String> seriesNames = new ArrayList<String>();

        while(cursor.moveToNext())
        {

            String seriesName= cursor.getString(cursor.getColumnIndex(PhoneDataContract.PhonedData.COLUNMN_SAMSUNG));


            seriesNames.add(seriesName);

        }


        Log.i("METAD2",seriesNames.toString());
        cursor.close();

        ArrayAdapter<String> adapter1 =  new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,seriesNames);

        seriesSearch.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter3  = ArrayAdapter.createFromResource(this,R.array.type_repair,R.layout.support_simple_spinner_dropdown_item);

        adapter3.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        typeRepair.setAdapter(adapter3);


    }




    public void Login(View view)
    {

        startActivity(new Intent(OrderRepair.this,LoginActivity.class));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.afterlogin, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.SignOut) {
            FirebaseAuth.getInstance().signOut();
        }
        return true;
    }
}

