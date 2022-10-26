package com.example.alertosannicolas;

import static android.content.ContentValues.TAG;
import static com.example.alertosannicolas.AccountSetupActivity.isEditTextRequiredValidated;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    Authentication auth;
    TextInputEditText email;
    TextInputEditText password;
    TextInputEditText confirmPassword;
    Button signin;
    Button signup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //change notification bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.homeNotificationBarColor));
        }

        setContentView(R.layout.activity_sign_up);
        auth = new Authentication(this, registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        onSignInResult(result);
                    }
                }
        ));
        auth.allowAnonymousOnly();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        confirmPassword = findViewById(R.id.confirmPassword);

        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpEmailPass();
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(newActivity);
            }
        });

    }

    private void signUpEmailPass() {
        String emailVal = email.getText().toString();
        String passwordVal = password.getText().toString();
        Boolean isAllValidated = true;

        //input validation
        isAllValidated = isEditTextRequiredValidated(email);
        isAllValidated = isEditTextRequiredValidated(password);
        isAllValidated = isConfirmPassword();

        if(!isAllValidated)return;

        mAuth.createUserWithEmailAndPassword(emailVal, passwordVal)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            auth.dynamicUserNavigate(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Snackbar errorEmailPass = Snackbar.make(findViewById(R.id.parent),
                                            task.getException().getMessage(),
                                            10000)
                                    .setBackgroundTint(getResources().getColor(R.color.md_red_900))
                                    ;
                            password.setText("");
                            confirmPassword.setText("");
                            View view = errorEmailPass.getView();
                            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
                            params.gravity = Gravity.TOP;
                            params.topMargin = 50;
                            view.setLayoutParams(params);
                            errorEmailPass.show();
                        }
                    }
                });


    }

    private Boolean isConfirmPassword() {
        String confirmVal = confirmPassword.getText().toString();
        String passwordVal = password.getText().toString();
        if(passwordVal.isEmpty())return false;
        if(confirmVal.equals(passwordVal)){
            return true;
        }
        confirmPassword.setError("Passwords not match");
        return false;
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