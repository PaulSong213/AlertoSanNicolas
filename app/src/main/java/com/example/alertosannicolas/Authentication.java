package com.example.alertosannicolas;


import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

//Source: https://github.com/firebase/snippets-android/blob/bc74fe8e59253db2b712eb0d1e362e990b7d69fe/auth/app/src/main/java/com/google/firebase/quickstart/auth/GoogleSignInActivity.java#L67-L68

public class Authentication {
    Activity activity;


    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher;
    // [END auth_fui_create_launcher]

    public Authentication(Activity activity, ActivityResultLauncher<Intent> signInLauncher) {
        this.activity = activity;

        this.signInLauncher = signInLauncher;
    }

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        navigateToSignInActivity();
                    }
                });
        // [END auth_fui_signout]
    }

    public void navigateToSignInActivity(){
        activity.finish();
        Intent accountSetupActivity = new Intent(activity.getApplicationContext(), MainActivity.class);
        activity.startActivity(accountSetupActivity);
    }

    public void navigateToActivity(Class navigateClass){
        activity.finish();
        Intent accountSetupActivity = new Intent(activity.getApplicationContext(), navigateClass);
        activity.startActivity(accountSetupActivity);
    }

    public void allowAnonymousOnly(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            navigateToActivity(AccountSetupActivity.class);
        }
    }

    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_create_intent]
    }

    public void googleBtnUi() {

        SignInButton googleButton = (SignInButton) activity.findViewById(R.id.google_button);
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignInIntent();
            }
        });
        googleButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        for (int i = 0; i < googleButton.getChildCount(); i++) {
            View v = googleButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Continue using Google");
                tv.setAllCaps(true);
                return;
            }
        }
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }


    FirebaseUser getUser() {
       return FirebaseAuth.getInstance().getCurrentUser();
    }



}
