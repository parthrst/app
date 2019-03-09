package com.vapps.uvpa;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Demo extends AppCompatActivity {


    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase =firebaseDatabase.getReference();
    List<String> demoData = new ArrayList<String>();
    Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

            fetchData();



    }


    public void fetchData()
    {

        spinner = findViewById(R.id.type_repair);

for(int i = 0 ; i < 1131 ; ++i)
{
    mDatabase.child("MODEL/0/SAMSUNG/" + i).addValueEventListener(new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String series = dataSnapshot.getValue(String.class);

            if (series != null)
                demoData.add(series);

            //  series = dataSnapshot.getValue(String.class);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,demoData);
        spinner.setAdapter(adapter);

    }


}
