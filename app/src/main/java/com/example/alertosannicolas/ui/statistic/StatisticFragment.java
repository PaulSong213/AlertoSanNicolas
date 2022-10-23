package com.example.alertosannicolas.ui.statistic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alertosannicolas.databinding.FragmentEmegencyBinding;
import com.example.alertosannicolas.databinding.FragmentStatisticBinding;


public class StatisticFragment extends Fragment {

    private FragmentStatisticBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StatisticViewModel galleryViewModel =
                new ViewModelProvider(this).get(StatisticViewModel.class);

        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textStatistic;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}