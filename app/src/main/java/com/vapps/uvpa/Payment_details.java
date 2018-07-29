package com.vapps.uvpa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class Payment_details extends AppCompatActivity {

    private CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);


        cardForm = findViewById(R.id.card_form);



cardForm.setPayBtnClickListner(new OnPayBtnClickListner() {
        @Override
        public void onClick(Card card) {
            startActivity(new Intent(Payment_details.this,QR.class));
        }
    });
}}