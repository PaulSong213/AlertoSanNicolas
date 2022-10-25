package com.example.alertosannicolas;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Authentication auth;
    TextInputEditText email;
    TextInputEditText password;
    Button signin;
    TextView errorEmail;
    TextView errorPassword;
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

        setContentView(R.layout.activity_main);
        auth = new Authentication(this, registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        onSignInResult(result);
                    }
                }
        ));
        auth.googleBtnUi();
        auth.allowAnonymousOnly();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        errorEmail = findViewById(R.id.errorEmail);
        errorPassword = findViewById(R.id.errorPassword);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();

        if(emailVal.isEmpty()){
            errorEmail.setText("Email is Required");
            errorEmail.setVisibility(View.VISIBLE);
        }else{
            errorEmail.setText("");
            errorEmail.setVisibility(View.GONE);
        }

        if(passwordVal.isEmpty()){
            errorPassword.setText("Password is Required");
            errorPassword.setVisibility(View.VISIBLE);
        }else{
            errorPassword.setText("");
            errorPassword.setVisibility(View.GONE);
        }

    }

    public void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            auth.dynamicUserNavigate(user);
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            Snackbar.make(findViewById(R.id.activity_main_layout),"Something went wrong. Please try again.", Snackbar.LENGTH_LONG)
                    .show();
            System.out.println(response.getError().getMessage());
        }
    }




}