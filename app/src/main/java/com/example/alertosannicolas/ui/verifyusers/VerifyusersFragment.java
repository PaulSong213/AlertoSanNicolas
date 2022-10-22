package com.example.alertosannicolas.ui.verifyusers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.alertosannicolas.databinding.FragmentVerifyusersBinding;

public class VerifyusersFragment extends Fragment {

    private FragmentVerifyusersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VerifyusersViewModel galleryViewModel =
                new ViewModelProvider(this).get(VerifyusersViewModel.class);

        binding = FragmentVerifyusersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textVerify;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}