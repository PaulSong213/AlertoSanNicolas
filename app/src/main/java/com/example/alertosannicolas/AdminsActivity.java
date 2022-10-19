package com.example.alertosannicolas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.alertosannicolas.databinding.ActivityAdminsBinding;
import com.example.alertosannicolas.databinding.FragmentProfileBinding;

public class AdminsActivity extends AppCompatActivity {

    ActivityAdminsBinding binding;
    Authentication auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = new Authentication(this, null);

        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

    }
}