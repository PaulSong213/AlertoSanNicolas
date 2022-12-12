package com.example.alertosannicolas.ui.addadmin;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alertosannicolas.Authentication;
import com.example.alertosannicolas.databinding.FragmentAddadminBinding;
import com.example.alertosannicolas.databinding.FragmentStatisticBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AddadminFragment extends Fragment {

    private FragmentAddadminBinding binding;
    ListView l;
    List<String> adminsEmails = new ArrayList<String>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddAdminViewModel galleryViewModel =
                new ViewModelProvider(this).get(AddAdminViewModel.class);

        binding = FragmentAddadminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textAdmin;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        l = binding.list;

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo : show add admin form
                AddAdminFormFragment.newInstance(30).show(getParentFragmentManager(), "dialog");
            }
        });
        setAdminListListener();
        return root;
    }

    private void setAdminListListener() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("whitelistedAdmins")
                ;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminsEmails.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String adminEmail = snapshot.getKey();
                    adminsEmails.add(Authentication.toNormalEmailFromFirebase(adminEmail));
                    ArrayAdapter<String> arr;
                    arr = new ArrayAdapter<String>(
                            binding.getRoot().getContext(),
                            android.R.layout.simple_list_item_1,
                            adminsEmails);
                    l.setAdapter(arr);
                    l.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String adminEmail = adapterView.getItemAtPosition(i).toString();
                            DeleteAdminDialogFragment.newInstance(adminEmail).show(getParentFragmentManager(), "dialog");
                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}