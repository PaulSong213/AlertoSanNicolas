package com.example.alertosannicolas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.alertosannicolas.databinding.ActivityAdminsBinding;
import com.example.alertosannicolas.databinding.ActivityPendingBinding;

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

    }

    private void setLoggedInViewInfo() {
        binding.viewTextEmail.setText(auth.getUser().getEmail());
        Glide.with(this)
                .asBitmap()
                .load(auth.getUser().getPhotoUrl())
                .into(binding.viewImageAcc);
    }
}