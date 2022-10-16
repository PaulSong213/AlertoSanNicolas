package com.example.alertosannicolas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;

public class IdUploadActivity extends AppCompatActivity implements View.OnClickListener {

    Button continueBtn;
    Button selectImageBtn;
    ImageView viewImageId;
    Authentication auth;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    TextView proofIdErrorMsg;

    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    imageUri = result.getData().getData();
                    viewImageId.setImageURI(imageUri);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_upload);

        //TODO: create a static method for removing nav and changing color
        //remove top nav title
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            System.out.println(e.getMessage());
        }

        //change notification bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.homeNotificationBarColor));
        }

        //initialize authentication
        auth = new Authentication(this, null);

        allowNoProfIdOnly(auth.getUser());

        continueBtn = findViewById(R.id.continueBtn);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        viewImageId = findViewById(R.id.viewImageId);
        proofIdErrorMsg = findViewById(R.id.proofIdErrorMsg);

        selectImageBtn.setOnClickListener(this);
        continueBtn.setOnClickListener(this);


    }

    private void allowNoProfIdOnly(FirebaseUser user) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users")
                .child(user.getUid())
                .child("proofIdLink")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    Object snapshot = task.getResult().getValue();
                    if(snapshot.toString().isEmpty() ){
                        //show the upload image form

                    }else{
                        finish();
                        Intent intentUsersActivity = new Intent(getApplicationContext(), UsersActivity.class);
                        startActivity(intentUsersActivity);
                    }
                }
                else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectImageBtn:
            case R.id.viewImageId:
                openImageSelection();
                break;

            case R.id.continueBtn:
                uploadImageToFirebase();
                break;
            default:

        }
    }

    private void uploadImageToFirebase() {
        if(imageUri == null){
            proofIdErrorMsg.setVisibility(View.VISIBLE);
            return;
        }else{
            proofIdErrorMsg.setVisibility(View.GONE);
        }

        //Code source: https://github.com/firebase/snippets-android/blob/bc74fe8e59253db2b712eb0d1e362e990b7d69fe/storage/app/src/main/java/com/google/firebase/referencecode/storage/StorageActivity.java#L315-L338
        progressDialog = new ProgressDialog(IdUploadActivity.this);
        progressDialog.setTitle("Uploading File...");
        progressDialog.show();

        storageReference = FirebaseStorage.getInstance().getReference("proof_id/"+auth.getUser().getUid());

        UploadTask  uploadTasId = storageReference.putFile(imageUri);

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
                    setUserProofIdLink(downloadUri);
                } else {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    Snackbar.make(findViewById(R.id.parent),"Something went wrong. Please try again.", Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void setUserProofIdLink(Uri downloadUri) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users")
                .child(auth.getUser().getUid())
                .child("proofIdLink")
                .setValue(downloadUri.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        finish();
                        Intent userActivity = new Intent(getApplicationContext(), UsersActivity.class);
                        startActivity(userActivity);
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Snackbar.make(findViewById(R.id.parent), R.string.errorMessage, Snackbar.LENGTH_LONG)
                                .show();
                        System.out.println(e.getMessage());
                    }
                });;
    }

    private void openImageSelection() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        mLauncher.launch(intent);
    }

}