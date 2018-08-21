package com.vapps.uvpa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


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
} @Override
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
            if(FirebaseAuth.getInstance().getCurrentUser()==null) {
             startActivity(new Intent(Payment_details.this,OrderRepair.class));
            }
            }

        return true;
    }
}