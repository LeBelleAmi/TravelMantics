package com.lebelleami.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdminActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private static final int PICTURE_RESULT = 42;

    TextInputEditText txtDestination, txtDescription, txtAmount;

    AppCompatSpinner spinnerCurrency;

    MaterialButton saveVacationDeal, addImageButton;

    ImageView dealImage;

    TravelDeal travelDeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        mFirebaseDatabase = mFirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("traveldeals");



        //setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        txtDestination = findViewById(R.id.destination);
        txtDescription = findViewById(R.id.description);
        txtAmount = findViewById(R.id.amount_entry);
        spinnerCurrency = findViewById(R.id.currency_spinner);
        saveVacationDeal = findViewById(R.id.save_vacation);
        addImageButton = findViewById(R.id.add_image);
        dealImage = findViewById(R.id.vac_image);


        Intent intent = getIntent();
        travelDeal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(travelDeal == null){
            travelDeal = new TravelDeal();
        }
        this.travelDeal = travelDeal;
        txtDestination.setText(travelDeal.getDestination());
        txtDescription.setText(travelDeal.getDescription());
        txtAmount.setText(travelDeal.getAmount());
        spinnerCurrency.getSelectedItem().toString();
        showImage(travelDeal.getImageUrl());




        saveVacationDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDeal();
                clean();
                backToDashboard();
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/jpeg");
                intent1.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent1.createChooser(intent1, "Insert Picture"), PICTURE_RESULT);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_LONG).show();
                backToDashboard();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICTURE_RESULT && resultCode == RESULT_OK){
            Uri imageFile = data.getData();
            StorageReference reference = FirebaseUtil.mStorageRef.child(imageFile.getLastPathSegment());
            reference.putFile(imageFile).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            travelDeal.setImageUrl(url);
                        }
                    });
                    String url = taskSnapshot.getStorage().getDownloadUrl().toString();
                    travelDeal.setImageUrl(url);
                    showImage(url);
                    String pictureName = taskSnapshot.getStorage().getPath();
                    travelDeal.setImageName(pictureName);

                }
            });

        }
    }


    private void saveDeal(){
        travelDeal.setDestination(txtDestination.getText().toString());
        travelDeal.setDescription(txtDescription.getText().toString());
        travelDeal.setAmount(txtAmount.getText().toString());
        travelDeal.setCurrency(spinnerCurrency.getSelectedItem().toString());
        if(travelDeal.getId() == null){
            mDatabaseReference.push().setValue(travelDeal);
        }
        else {
            mDatabaseReference.child(travelDeal.getId()).setValue(travelDeal);
        }
       }


    private void clean() {
        txtDestination.setText("");
        txtAmount.setText("");
        txtDescription.setText("");
        txtDestination.requestFocus();
    }

    private void deleteDeal() {
        if (travelDeal == null){
            Toast.makeText(this, "Travel Deal not saved, please save before deleting", Toast.LENGTH_LONG).show();
            return;
        }
        mDatabaseReference.child(travelDeal.getId()).removeValue();

        if (travelDeal.getImageName() != null && travelDeal.getImageName().isEmpty() == false){
            StorageReference pictureRef = FirebaseUtil.mStorage.getReference().child(travelDeal.getImageName());
            pictureRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Delete Image", "Image deleted successfully");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Delete Image", e.getMessage());
                }
            });
        }
    }

    private void backToDashboard(){
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);

    }

    private void showImage(String url){
        if(url != null && url.isEmpty() == false){
            Glide.with(this).load(url).into(dealImage);
        }
    }


}
