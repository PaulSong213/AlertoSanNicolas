package com.example.alertosannicolas;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.data.model.User;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountSetupActivity extends AppCompatActivity implements  View.OnClickListener{

    Authentication auth;
    Button signOutBtn;
    Button btnCreateAcc;
    EditText editTextFirstName;
    EditText editTextLastName;
    TextView viewTextEmail;
    EditText editTextContactNumber;
    EditText editTextAddress;
    FirebaseUser user;
    FlexboxLayout accountInfoFormLoader;
    FlexboxLayout accountInfoForm;
    ImageView viewImageAcc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setContentView(R.layout.activity_account_setup);

        //initialize authentication
        auth = new Authentication(this, registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        //user is signed in do something
                    }
                }
        ));

        //Initialize elements
        signOutBtn = findViewById(R.id.signOutBtn);
        btnCreateAcc = findViewById(R.id.btnCreateAcc);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        viewTextEmail = findViewById(R.id.viewTextEmail);
        editTextContactNumber = findViewById(R.id.editTextContactNumber);
        editTextAddress = findViewById(R.id.editTextAddress);
        viewImageAcc = findViewById(R.id.viewImageAcc);

        accountInfoFormLoader = findViewById(R.id.accountInfoFormLoader);
        accountInfoForm = findViewById(R.id.accountInfoForm);

        signOutBtn.setOnClickListener(this);
        user = auth.getUser();

        allowUnregisteredOnly(user);

        btnCreateAcc.setOnClickListener(this);

        viewTextEmail.setText(auth.getUser().getEmail());

        //set image src from the current user logged in email
        Glide.with(this)
                .asBitmap()
                .load(auth.getUser().getPhotoUrl())
                .into(viewImageAcc);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signOutBtn:
                auth.signOut();
                break;

            case R.id.btnCreateAcc:
                createAccount();
                break;
            default:

        }
    }

    private void createAccount() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Boolean isAllValidated = true;
        String requiredMessage = "This field is required.";
        String uid = auth.getUser().getUid();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = auth.getUser().getEmail();
        String contactNumber = editTextContactNumber.getText().toString();
        String address = editTextAddress.getText().toString();

        //input validation
        isAllValidated = isEditTextRequiredValidated(editTextFirstName, firstName);
        isAllValidated = isEditTextRequiredValidated(editTextLastName, lastName);
        isAllValidated = isEditTextRequiredValidated(editTextContactNumber, contactNumber);
        isAllValidated = isEditTextRequiredValidated(editTextAddress, address);

        if(!isAllValidated)return;
        UserModel user = new UserModel(uid,email,firstName,lastName,contactNumber,address,"");
        mDatabase.child("users").child(uid).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // Navigate to upload proof ID
                        finish();
                        Intent idUploadActivity = new Intent(getApplicationContext(), IdUploadActivity.class);
                        startActivity(idUploadActivity);
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

    /*
     * Check if the logged in user has a record on database
     * if user has record redirect to users/admin page depending on the usertype
     * else let the user fill up the sign up form
     */
    private void allowUnregisteredOnly(FirebaseUser user){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    Object snapshot = task.getResult().getValue();
                    if(snapshot == null ){
                        //show the account information form and let the user fill up the form
                        accountInfoForm.setVisibility(View.VISIBLE);
                        accountInfoFormLoader.setVisibility(View.GONE);
                    }else{
                        finish();
                        Intent accountSetupActivity = new Intent(getApplicationContext(), IdUploadActivity.class);
                        startActivity(accountSetupActivity);
                    }
                }
                else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
    }

    /*
    Validate an edit text
    Return the boolean if the input is validated
    */
    public static Boolean isEditTextRequiredValidated(EditText input, String value){
        if(value.isEmpty()){
            input.setError("This field is required.");
            return false;
        }
        return true;
    }

}