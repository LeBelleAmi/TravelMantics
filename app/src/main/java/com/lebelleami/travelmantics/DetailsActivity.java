package com.lebelleami.travelmantics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailsActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    TravelDeal travelDeal;

    TextView txtDestination, txtDescription, txtPrice;
    ImageView imgDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("traveldeals");

        //setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDet);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        txtDestination = findViewById(R.id.detDestination);
        txtDescription = findViewById(R.id.detDescription);
        txtPrice = findViewById(R.id.detPrice);
        imgDestination = findViewById(R.id.detImage);


        Intent intent = getIntent();
        travelDeal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(travelDeal == null){
            travelDeal = new TravelDeal();
        }
        this.travelDeal = travelDeal;
        txtDestination.setText(travelDeal.getDestination());
        txtDescription.setText(travelDeal.getDescription());
        txtPrice.setText(travelDeal.getCurrency() + travelDeal.getAmount());
        showImage(travelDeal.getImageUrl());
    }

    private void showImage(String url){
        if(url != null && url.isEmpty() == false){
            Glide.with(this).load(url).into(imgDestination);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            //navigateUpFromSameTask(this);
            onBackPressed(); // close this activity and return to preview activity (if there is any)
            //return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
