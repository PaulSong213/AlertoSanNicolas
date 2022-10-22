package com.example.alertosannicolas.ui.addadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alertosannicolas.databinding.FragmentAddadminBinding;

public class AddadminFragment extends Fragment {

    private FragmentAddadminBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddadminViewModel galleryViewModel =
                new ViewModelProvider(this).get(AddadminViewModel.class);

        binding = FragmentAddadminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAddadmin;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}