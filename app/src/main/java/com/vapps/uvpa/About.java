package com.vapps.uvpa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {
    TextView about;
    TextView name1;
    TextView name2;
    TextView name3;TextView name4;
    TextView pos1;TextView pos2;TextView pos3;TextView pos4;
    TextView info1;TextView info2;TextView info3;TextView info4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        about=findViewById(R.id.about);
        name1=findViewById(R.id.name1);
        name2=findViewById(R.id.name2);
        name3=findViewById(R.id.name3);
        name4=findViewById(R.id.name4);
        pos1=findViewById(R.id.position1);
        pos2=findViewById(R.id.position2);
        pos3=findViewById(R.id.position3);
        pos4=findViewById(R.id.position4);
        info1=findViewById(R.id.info1);
        info2=findViewById(R.id.info2);
        info3=findViewById(R.id.info3);
        info4=findViewById(R.id.info4);
       about.setText("RepairBuck is a delivery based service which aims to provide a platform for consumers who have damaged phone.Pick-up, delivery and offer a back-up phone as well.Best local service providers under one roof along with warranty assurance");
        name1.setText(""+"Vanik Jain");
        name2.setText(""+"Parth Rastogi");
       name3.setText("Ashutosh Singh");
        name4.setText("Arpit Tiwari");
        pos1.setText("Android Developer");
       pos2.setText("Android Developer");
       pos3.setText("Full Stack Web Developer");
       pos4.setText("Public Relation OfficerCU");
       info1.setText("Department of Information Technology Engineering, SRM IST, Kattankulathur, Chennai- 603203. \nM: +9186196963046 \nE-mail: pro.nrai.med@gmail.com");
       info2.setText("Department of Information Technology Engineering, SRM IST, Kattankulathur, Chennai- 603203. \nM: +9186196963046 \nE-mail: pro.nrai.med@gmail.com");
       info3.setText("Department of Information Technology Engineering, SRM IST, Kattankulathur, Chennai- 603203. \nM: +9186196963046 \nE-mail: pro.nrai.med@gmail.com");
       info4.setText("Department of Computer Science Engineering,Chandigarh University\nM: +9186196963046\nE-mail: pro.nrai.med@gmail.com");

    }
}
