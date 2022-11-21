package com.example.alertosannicolas.ui.emergency;

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

import com.example.alertosannicolas.ReportModel;
import com.example.alertosannicolas.UserModel;
import com.example.alertosannicolas.databinding.FragmentVerifyusersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmergencyFragment extends Fragment {

    private FragmentVerifyusersBinding binding;
    private RecyclerView pendingUsersRecView;
    EmergencyRecViewAdapter verifyuserRecViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmergencyViewModel galleryViewModel =
                new ViewModelProvider(this).get(EmergencyViewModel.class);

        binding = FragmentVerifyusersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pendingUsersRecView = binding.pendingUsersRecView;

        verifyuserRecViewAdapter = new EmergencyRecViewAdapter(this.getContext(), getParentFragmentManager());
        pendingUsersRecView.setAdapter(verifyuserRecViewAdapter);
        pendingUsersRecView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        setPendingUsersListener();

        return root;
    }

    private void setPendingUsersListener() {
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference().child("reports");
        ArrayList<ReportModel> pendingUsers = new ArrayList<>();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pendingUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReportModel user = snapshot.getValue(ReportModel.class);
                    if(user.getStatus().equals("pending") ){
                        pendingUsers.add(0,user);
                    }
                    verifyuserRecViewAdapter.setUsers(pendingUsers);
                }
                verifyuserRecViewAdapter.setUsers(pendingUsers);
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