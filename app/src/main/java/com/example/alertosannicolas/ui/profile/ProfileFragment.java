package com.example.alertosannicolas.ui.profile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.alertosannicolas.Authentication;
import com.example.alertosannicolas.UserModel;
import com.example.alertosannicolas.databinding.FragmentProfileBinding;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    Button signOutBtn;
    Authentication auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        signOutBtn = binding.signOutBtn;

        auth = new Authentication((Activity) root.getContext(), registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                        //user is signed in do something

                    }
                }
        ));

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
            }
        });

        setPersonalInformationValues();

        return root;
    }

    private void setPersonalInformationValues() {
        FirebaseUser user = auth.getUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    UserModel userInfo = task.getResult().getValue(UserModel.class);
                    Log.d("firebase", String.valueOf(userInfo));

                    binding.viewTextFullName.setText(userInfo.getFullName());
                    binding.viewTextContactNumber.setText(userInfo.getContactNumber());
                    binding.viewTextEmail.setText(userInfo.getEmail());
                    binding.viewTextAddress.setText(userInfo.getAddress());
                    //set image src to logged in user
                    Glide.with(binding.getRoot())
                            .asBitmap()
                            .load(auth.getUser().getPhotoUrl())
                            .into(binding.viewImageProfile);
                }
            }
        });

        //binding.viewTextFullName.setText(auth);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}