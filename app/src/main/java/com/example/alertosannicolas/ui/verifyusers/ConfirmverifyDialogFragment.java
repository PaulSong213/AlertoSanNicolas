package com.example.alertosannicolas.ui.verifyusers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.alertosannicolas.UserModel;
import com.example.alertosannicolas.databinding.FragmentConfirmverifyDialogListDialogBinding;
import com.example.alertosannicolas.databinding.FragmentConfirmverifyDialogListDialogItemBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alertosannicolas.R;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ConfirmverifyDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class ConfirmverifyDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    public  UserModel user;
    private FragmentConfirmverifyDialogListDialogBinding binding;

    public ConfirmverifyDialogFragment(UserModel user) {
        this.user = user;
    }

    // TODO: Customize parameters
    public ConfirmverifyDialogFragment newInstance(UserModel user) {
        final ConfirmverifyDialogFragment fragment = new ConfirmverifyDialogFragment(user);
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentConfirmverifyDialogListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter(getArguments().getInt(ARG_ITEM_COUNT)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView viewTextFullName;

        ViewHolder(FragmentConfirmverifyDialogListDialogItemBinding binding) {
            super(binding.getRoot());
            viewTextFullName = binding.viewTextFullName;
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;

        ItemAdapter(int itemCount) {
            mItemCount = itemCount;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentConfirmverifyDialogListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.viewTextFullName.setText(user.getFullName());
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }
}