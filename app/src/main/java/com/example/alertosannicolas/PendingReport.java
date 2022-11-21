package com.example.alertosannicolas;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingReport extends AppCompatActivity {
    Authentication auth;
    FlexboxLayout pendingStatus;
    FlexboxLayout otwStatus;
    Button helpRecieved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_report);
        //remove top nav title
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }
        pendingStatus = findViewById(R.id.pendingStatus);
        otwStatus = findViewById(R.id.otwStatus);
        helpRecieved = findViewById(R.id.helpRecieved);
        helpRecieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHelpRecieved();
            }
        });
        auth = new Authentication(this, null);
        listenToReportChange();
    }

    private void setHelpRecieved() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("reports").child(auth.getUser().getUid());
        mPostReference.removeValue();
    }

    private void listenToReportChange() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("reports").child(auth.getUser().getUid());
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               if(!dataSnapshot.exists()){
                   Intent intent = new Intent(PendingReport.this, UsersActivity.class);
                   finish();
                   startActivity(intent);
               }else{
                   ReportModel report = dataSnapshot.getValue(ReportModel.class);
                   if(report.getStatus().equals("pending")){
                       pendingStatus.setVisibility(View.VISIBLE);
                       otwStatus.setVisibility(View.GONE);
                   }else{
                       pendingStatus.setVisibility(View.GONE);
                       otwStatus.setVisibility(View.VISIBLE);
                   }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);
    }
}