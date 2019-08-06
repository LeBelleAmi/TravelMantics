package com.lebelleami.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TravelDealAdapter extends RecyclerView.Adapter<TravelDealAdapter.TravelDealViewHolder> {

    ArrayList<TravelDeal> dealsArraylist;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    public TravelDealAdapter() {
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        dealsArraylist = FirebaseUtil.mDeals;

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal: ", td.getDestination());
                td.setId(dataSnapshot.getKey());
                dealsArraylist.add(td);
                notifyItemInserted(dealsArraylist.size() - 1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addChildEventListener(mChildListener);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TravelDealAdapter.TravelDealViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_content, viewGroup, false);
        TravelDealViewHolder travelDealViewHolder = new TravelDealViewHolder(view);
        return travelDealViewHolder;
    }

    @Override
    public void onBindViewHolder(TravelDealViewHolder travelDealViewHolder, int i) {
        TravelDeal travelDeal = dealsArraylist.get(i);
        travelDealViewHolder.bind(travelDeal);
        /*travelDealViewHolder.txtLocation.setText(travelDeal.getDestination());
        travelDealViewHolder.txtDescription.setText(travelDeal.getDescription());
        travelDealViewHolder.txtCurrency.setText(travelDeal.getCurrency());
        travelDealViewHolder.txtAmount.setText(travelDeal.getAmount());*/

    }


    @Override
    public int getItemCount() {
        return dealsArraylist.size();
    }


    public class TravelDealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView imgVacation;
        TextView txtLocation;
        TextView txtDescription;
        TextView txtCurrency;
        TextView txtAmount;


        public TravelDealViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.vacationCard);
            imgVacation = itemView.findViewById(R.id.vacation_image);
            txtLocation = itemView.findViewById(R.id.vacation_location);
            txtDescription = itemView.findViewById(R.id.vacation_description);
            txtCurrency = itemView.findViewById(R.id.destination_currency);
            txtAmount = itemView.findViewById(R.id.destination_amount);
            itemView.setOnClickListener(this);

        }


        public void bind(TravelDeal travelDeal){
            showImage(travelDeal.getImageUrl());
            txtLocation.setText(travelDeal.getDestination());
            txtDescription.setText(travelDeal.getDescription());
            txtCurrency.setText(travelDeal.getCurrency());
            txtAmount.setText(travelDeal.getAmount());
        }

        @Override
        public void onClick(View view) {
            if(FirebaseUtil.isAdmin == true){
                int pos = getAdapterPosition();
                TravelDeal selectedDeal = dealsArraylist.get(pos);
                Intent intent = new Intent(view.getContext(), AdminActivity.class);
                intent.putExtra("Deal", selectedDeal);
                view.getContext().startActivity(intent);
            }else {
                int pos = getAdapterPosition();
                TravelDeal selectedDeal = dealsArraylist.get(pos);
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                intent.putExtra("Deal", selectedDeal);
                view.getContext().startActivity(intent);
            }

        }

        private void showImage(String url){
            if(url != null && url.isEmpty() == false){
                Glide.with(imgVacation.getContext()).load(url).into(imgVacation);
            }
        }

    }

}
