package com.example.alertosannicolas.ui.verifyusers;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.alertosannicolas.UserModel;
import com.example.alertosannicolas.databinding.FragmentConfirmverifyDialogListDialogBinding;
import com.example.alertosannicolas.databinding.FragmentConfirmverifyDialogListDialogItemBinding;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alertosannicolas.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public View parent;

    private FragmentConfirmverifyDialogListDialogBinding binding;

    public ConfirmverifyDialogFragment(UserModel user, View v) {
        this.user = user;
        this.parent = v;
    }

    // TODO: Customize parameters
    public ConfirmverifyDialogFragment newInstance(UserModel user,  View v ) {
        final ConfirmverifyDialogFragment fragment = new ConfirmverifyDialogFragment(user,v);
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
        final TextView viewTextEmail;
        final TextView viewTextContactNumber;
        final TextView viewTextAddress;
        final Button verifyBtn;
        final ImageView idImage;

        ViewHolder(FragmentConfirmverifyDialogListDialogItemBinding binding) {
            super(binding.getRoot());
            viewTextFullName = binding.viewTextFullName;
            viewTextEmail = binding.viewTextEmail;
            viewTextContactNumber = binding.viewTextContactNumber;
            viewTextAddress = binding.viewTextAddress;
            verifyBtn = binding.verifyBtn;
            idImage = binding.idImage;
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
            holder.viewTextAddress.setText(user.getAddress());
            holder.viewTextContactNumber.setText(user.getContactNumber());
            holder.viewTextEmail.setText(user.getEmail());
            if(!user.getProofIdLink().isEmpty()){
                Glide.with(binding.getRoot())
                        .asBitmap()
                        .load(user.getProofIdLink())
                        .into(holder.idImage);
            }
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            holder.verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase.child("users").child(user.getUid()).child("isVerified").setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    System.out.println("Verified");
                                    Toast
                                            .makeText(getActivity(), "User verified successfully", Toast.LENGTH_LONG)
                                            .show();
                                    dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println(e.getMessage());
                                    Snackbar errorEmailPass = Snackbar.make(binding.getRoot(),
                                                    e.getMessage(),
                                                    10000)
                                            .setBackgroundTint(getResources().getColor(R.color.md_red_900))
                                            ;
                                }
                            });
                }
            });


        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }
}