package com.example.alertosannicolas.ui.addadmin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.alertosannicolas.Authentication;
import com.example.alertosannicolas.databinding.FragmentAddAdminFormListDialogBinding;
import com.example.alertosannicolas.databinding.FragmentAddAdminFormListDialogItemBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     AddAdminFormFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class AddAdminFormFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentAddAdminFormListDialogBinding binding;

    // TODO: Customize parameters
    public static AddAdminFormFragment newInstance(int itemCount) {
        final AddAdminFormFragment fragment = new AddAdminFormFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentAddAdminFormListDialogBinding.inflate(inflater, container, false);
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

        //        final TextView text;
        final EditText emailText;
        final Button addAdminButton;

        ViewHolder(FragmentAddAdminFormListDialogItemBinding binding) {
            super(binding.getRoot());
//            text = binding.text;
            emailText = binding.emailText;
            addAdminButton = binding.addAdminButton;
            addAdminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String toAddEmail = emailText.getText().toString();
                    String validFirebaseEmail = Authentication.toValidFirebaseEmail(toAddEmail);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                            .child("whitelistedAdmins")
                            .child(validFirebaseEmail);


                    mDatabase.setValue(validFirebaseEmail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    Toast
                                            .makeText(getActivity(), toAddEmail + " is added as Admin", Toast.LENGTH_LONG)
                                            .show();
                                    emailText.setText("");
                                    dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Write failed
                                    System.out.println(e.getMessage());
                                }
                            });

                }
            });
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

            return new ViewHolder(FragmentAddAdminFormListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.text.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return 1;
        }

    }
}