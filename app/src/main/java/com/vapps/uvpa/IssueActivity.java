package com.vapps.uvpa;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class IssueActivity extends AppCompatActivity {
   ListView listView;
    String[] type_repair={"Battery Problem",
            "Button Problem",
            "Broken Screen",
            "Charging Problem",
            "Camera Problem",
            "Water Damage",
            "Headphone Jack Issue","Software Issue"};
    String[] est_price={"1500","1000","2500","1500","2000","1750","700","1000"};
    public void nextActivity(View view)
    {
        startActivity(new Intent(IssueActivity.this,MapsActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        listView=findViewById(R.id.list);
        custom adapter=new custom();
        listView.setAdapter(adapter);


    }
    class custom extends BaseAdapter
    {

        @Override
        public int getCount() {
            return  8;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            view=getLayoutInflater().inflate(R.layout.custom_listview,null);
            CheckBox cb=view.findViewById(R.id.issue);
            TextView es=view.findViewById(R.id.estimate);
            cb.setText(type_repair[position]);
            es.setText(est_price[position]);
            return view;
        }
    }
}

