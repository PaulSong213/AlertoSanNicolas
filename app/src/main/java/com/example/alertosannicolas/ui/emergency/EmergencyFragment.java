package com.example.alertosannicolas.ui.emergency;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alertosannicolas.databinding.FragmentEmegencyBinding;


public class EmergencyFragment extends Fragment {

    private FragmentEmegencyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EmergencyViewModel galleryViewModel =
                new ViewModelProvider(this).get(EmergencyViewModel.class);

        binding = FragmentEmegencyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textEmergency;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}