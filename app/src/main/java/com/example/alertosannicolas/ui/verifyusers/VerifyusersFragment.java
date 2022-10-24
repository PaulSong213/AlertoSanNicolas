package com.example.alertosannicolas.ui.verifyusers;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertosannicolas.UserModel;
import com.example.alertosannicolas.databinding.FragmentVerifyusersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifyusersFragment extends Fragment {

    private FragmentVerifyusersBinding binding;
    private RecyclerView pendingUsersRecView;
    VerifyuserRecViewAdapter verifyuserRecViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VerifyusersViewModel galleryViewModel =
                new ViewModelProvider(this).get(VerifyusersViewModel.class);

        binding = FragmentVerifyusersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pendingUsersRecView = binding.pendingUsersRecView;

        verifyuserRecViewAdapter = new VerifyuserRecViewAdapter(this.getContext(), getParentFragmentManager());
        pendingUsersRecView.setAdapter(verifyuserRecViewAdapter);
        pendingUsersRecView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        setPendingUsersListener();

        return root;
    }

    private void setPendingUsersListener() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("users");
        ArrayList<UserModel> pendingUsers = new ArrayList<>();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pendingUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel user = snapshot.getValue(UserModel.class);
                    if(!user.getIsVerified()){
                        pendingUsers.add(user);
                        verifyuserRecViewAdapter.setUsers(pendingUsers);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}