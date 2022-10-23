package com.example.alertosannicolas.ui.verifyusers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alertosannicolas.UserModel;
import com.example.alertosannicolas.VerifyuserRecViewAdapter;
import com.example.alertosannicolas.databinding.FragmentVerifyusersBinding;

import java.util.ArrayList;

public class VerifyusersFragment extends Fragment {

    private FragmentVerifyusersBinding binding;
    private RecyclerView pendingUsersRecView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VerifyusersViewModel galleryViewModel =
                new ViewModelProvider(this).get(VerifyusersViewModel.class);

        binding = FragmentVerifyusersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pendingUsersRecView = binding.pendingUsersRecView;

        //Todo: convert this pendingUsers to data from firebase
        ArrayList<UserModel> pendingUsers = new ArrayList<>();

        pendingUsers.add(new UserModel("123","test","test","test","test","test","https://www.pngfind.com/pngs/m/617-6175085_sample-photo-id-card-by-castlemount-ltd-id.png",false ));
        pendingUsers.add(new UserModel("123","test","test","test","test","test","https://www.pngfind.com/pngs/m/617-6175085_sample-photo-id-card-by-castlemount-ltd-id.png",false ));


        VerifyuserRecViewAdapter adapter = new VerifyuserRecViewAdapter(this.getContext());

        adapter.setUsers(pendingUsers);

        pendingUsersRecView.setAdapter(adapter);
        pendingUsersRecView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}