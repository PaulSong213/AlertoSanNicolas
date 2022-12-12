package com.example.alertosannicolas.ui.addadmin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.alertosannicolas.Authentication;
import com.example.alertosannicolas.databinding.FragmentDeleteAdminDialogListDialogBinding;
import com.example.alertosannicolas.databinding.FragmentDeleteAdminDialogListDialogItemBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     DeleteAdminDialogFragment.newInstance(email).show(getSupportFragmentManager(), "dialog");
 * </pre>
 */
public class DeleteAdminDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_SELECTED_EMAIL = "selectedEmail";
    private FragmentDeleteAdminDialogListDialogBinding binding;
    String selectedAdminEmail;

    DeleteAdminDialogFragment(String selectedAdminEmail){
        this.selectedAdminEmail = selectedAdminEmail;
    }

    // TODO: Customize parameters
    public static DeleteAdminDialogFragment newInstance(String selectedAdminEmail) {

        final DeleteAdminDialogFragment fragment = new DeleteAdminDialogFragment(selectedAdminEmail);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentDeleteAdminDialogListDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemAdapter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        final Button deleteAdminButton;

        ViewHolder(FragmentDeleteAdminDialogListDialogItemBinding binding) {
            super(binding.getRoot());
            text = binding.text;
            text.setText("Are you sure you want to delete " + selectedAdminEmail + " as Admin?" );
            deleteAdminButton = binding.deleteAdminButton;
            deleteAdminButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String selectedValidFirebaseEmail = Authentication.toValidFirebaseEmail(selectedAdminEmail);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                            .child("whitelistedAdmins")
                            .child(selectedValidFirebaseEmail);

                    mDatabase.setValue(null)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Write was successful!
                                    Toast
                                            .makeText(getActivity(), selectedAdminEmail + " is removed as Admin", Toast.LENGTH_LONG)
                                            .show();
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

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return new ViewHolder(FragmentDeleteAdminDialogListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 1;
        }

    }
}