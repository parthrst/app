package com.vapps.uvpa;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class DeviceSelectionActivity extends AppCompatActivity {

    ViewFlipper flipper;
    public void mobile(View view){
  Intent intent=new Intent(DeviceSelectionActivity.this,RepairOrder1.class);
  intent.putExtra("val",0);
        startActivity(intent);



    }
    public void laptop(View view){
        Intent intent=new Intent(DeviceSelectionActivity.this,RepairOrder1.class);
        intent.putExtra("val",1);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_selection);
        flipper=findViewById(R.id.flip);
        int images[]={R.drawable.slide1,R.drawable.slide2,R.drawable.slide4};
        for(int image:images){
            fliping(image);

        }
    }
    public void fliping(int image){
        ImageView img=new ImageView(this);
        img.setBackgroundResource(image);
        flipper.addView(img);
        flipper.setFlipInterval(3000);
        flipper.setAutoStart(true);
        flipper.setInAnimation(this,android.R.anim.slide_in_left);
        flipper.setOutAnimation(this, android.R.anim.slide_out_right);
    }
}
