package com.example.alertosannicolas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class SubmitReport extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    String emergencyType = "others";
    String othersHelp = "";

    CheckBox ambulanceBox;
    CheckBox barangayTanodBox;
    CheckBox policeBox;
    CheckBox fireTruckBox;

    ImageView viewImageCaptured;
    FlexboxLayout helpSelection;
    Button submitReportBtn;
    ProgressDialog dialog;
    Uri uri;
    Authentication auth;
    StorageReference storageReference;
    ActivityResultLauncher<Uri> mGetContent = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    dialog.dismiss();
                    viewImageCaptured.setImageURI(uri);
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_report);
        getSupportActionBar().setTitle("Submit Emergency Report");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        auth = new Authentication(this, null);
        if(b != null){
            emergencyType = b.getString("emergencyType");
        }
        viewImageCaptured = findViewById(R.id.viewImageId);
        submitReportBtn = findViewById(R.id.submitReportBtn);

        if(ContextCompat.checkSelfPermission(SubmitReport.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SubmitReport.this,
                    new String[]{Manifest.permission.CAMERA}, 101 );
        }
        File file = new File(getFilesDir(), "images");
        uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        dialog =  ProgressDialog.show(SubmitReport.this, "Emergency Report",
                "Please wait...", true);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        mGetContent.launch(uri);
                    }
                },
                2000
        );
        submitReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitReport();
            }
        });

        helpSelection = findViewById(R.id.helpSelection);

        ambulanceBox = findViewById(R.id.ambulanceBox);
        barangayTanodBox = findViewById(R.id.barangayTanodBox);
        policeBox = findViewById(R.id.policeBox);
        fireTruckBox = findViewById(R.id.fireTruckBox);

        ambulanceBox.setOnCheckedChangeListener(this);
        barangayTanodBox.setOnCheckedChangeListener(this);
        policeBox.setOnCheckedChangeListener(this);
        fireTruckBox.setOnCheckedChangeListener(this);

        if(emergencyType.equals("others")){
            helpSelection.setVisibility(View.VISIBLE);
        }
    }

    private void submitReport() {
        dialog.show();

        storageReference = FirebaseStorage.getInstance().getReference("reports/"+auth.getUser().getUid());

        UploadTask uploadTasId = storageReference.putFile(uri);

        Task<Uri> urlTask = uploadTasId.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    saveReportToDatabase(downloadUri);
                } else {
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Snackbar.make(findViewById(R.id.parent),"Something went wrong. Please try again.", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void saveReportToDatabase(Uri downloadUri) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        String key = mDatabase.child("posts").push().getKey();
        String userId =  auth.getUser().getUid();
        ReportModel report = new ReportModel(emergencyType,userId, downloadUri.toString(), getNeededHelp(emergencyType), "pending");
        mDatabase.child("reports").child(userId).setValue(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PendingReport.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Snackbar.make(findViewById(R.id.activity_account_setup_layout), R.string.errorMessage, Snackbar.LENGTH_LONG)
                                .show();
                    }
                });;

    }

    private String getNeededHelp(String emergencyType) {
        switch (emergencyType){
            case "fire":
                return "Firetruck and Ambulance";
            case "earthquake":
                return "Ambulance";
            case "typhoon":
                return "Rescue Boat";
            case "robbery":
                return "Barangay Tanod";
            case "injuries":
                return "Ambulance";
            default:
                //others
                return othersHelp;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String toAddHelp = compoundButton.getText() + ", ";
        if(b){
            othersHelp += toAddHelp;
        }else{
            othersHelp = othersHelp.replace(toAddHelp, "");
        }
    }
}