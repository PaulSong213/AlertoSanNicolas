package com.example.alertosannicolas;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.alertosannicolas.databinding.ActivityAdminsBinding;
import com.example.alertosannicolas.databinding.ActivityPendingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PendingActivity extends AppCompatActivity {

    Authentication auth;
    ActivityPendingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = new Authentication(this, null);
        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

        setLoggedInViewInfo();
        watchVerify();

    }

    private void watchVerify(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(auth.getUser().getUid());

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                System.out.println(dataSnapshot.getValue());
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if(user.getIsVerified()){
                    finish();
                    Intent newActivity = new Intent(getApplicationContext(), UsersActivity.class);
                    startActivity(newActivity);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(userListener);
    }

    private void setLoggedInViewInfo() {
        binding.viewTextEmail.setText(auth.getUser().getEmail());

        if(auth.getUser().getPhotoUrl() != null ){
            Glide.with(this)
                    .asBitmap()
                    .load(auth.getUser().getPhotoUrl())
                    .into(binding.viewImageAcc);
        }

    }
}