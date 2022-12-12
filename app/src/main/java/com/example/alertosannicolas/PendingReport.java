package com.example.alertosannicolas;

import static android.content.ContentValues.TAG;

import static com.google.firebase.storage.internal.Util.ISO_8601_FORMAT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

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
        mPostReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    ReportModel report = task.getResult().getValue(ReportModel.class);
                    String key = FirebaseDatabase.getInstance().getReference("reportsHistory").push().getKey();
                    DatabaseReference historyReference = FirebaseDatabase.getInstance().getReference()
                            .child("reportsHistory")
                            .child(key)
                            ;

                    historyReference.setValue(report)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    historyReference.child("date").setValue(System.currentTimeMillis())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Write was successful!
                                                    mPostReference.removeValue();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Write failed
                                                    System.out.println(e.getMessage());
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    System.out.println(e.getMessage());
                                }
                            });

                }
            }
        });

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