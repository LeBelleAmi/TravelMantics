package com.lebelleami.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DashboardActivity extends AppCompatActivity {

    /*recycler view*/
    RecyclerView recyclerView;

    BottomAppBar bottomAppBar;

    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        bottomAppBar = findViewById(R.id.bottom_App_bar);
        setSupportActionBar(bottomAppBar);

        floatingActionButton = findViewById(R.id.fab_bottom_appbar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddButton();
            }
        });


    }



    public void initViews() {
        recyclerView = findViewById(R.id.recycler_view_dashboard);
        /*recycler view adapter*/
        final  TravelDealAdapter travelDealAdapter = new TravelDealAdapter();
        recyclerView.setAdapter(travelDealAdapter);
        /*recycler view layout manager*/
        LinearLayoutManager dealLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(dealLayoutManager);

    }

    @Override
    protected void onPause(){
        super.onPause();
        FirebaseUtil.detachListener();
    }

    @Override
    protected void onResume(){
        super.onResume();
        FirebaseUtil.openFbReference("traveldeals", this);
        initViews();
        FirebaseUtil.attachListener();
    }

    public void showAddButton(){
        if(FirebaseUtil.isAdmin == true){
            floatingActionButton.setImageResource(R.drawable.ic_add);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            floatingActionButton.setImageResource(R.drawable.ic_close);
            Toast.makeText(getApplicationContext(), "Please scroll through the list of available deals", Toast.LENGTH_LONG).show();
            floatingActionButton.setEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                //Toast.makeText(this, "Log out", Toast.LENGTH_LONG).show();
               AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // ...
                                Log.d("log out", "User is logged out");
                                FirebaseUtil.attachListener();
                            }
                        });
                FirebaseUtil.detachListener();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
