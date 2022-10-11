package com.example.alertosannicolas;

import androidx.activity.result.ActivityResultCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

public class IdUploadActivity extends AppCompatActivity implements View.OnClickListener {

    Button signOutBtn;
    Authentication auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_upload);

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

        signOutBtn = findViewById(R.id.signOutBtn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.signOutBtn):
                auth.signOut();
                break;

            default:

        }
    }
}