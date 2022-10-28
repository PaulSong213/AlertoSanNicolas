package com.example.alertosannicolas.ui.verifyusers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.alertosannicolas.R;
import com.example.alertosannicolas.UserModel;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class VerifyuserRecViewAdapter extends RecyclerView.Adapter<VerifyuserRecViewAdapter.ViewHolder> {

    private ArrayList<UserModel> users = new ArrayList<>();

    FragmentManager parentFragmentManager;
    Context context;

    public VerifyuserRecViewAdapter(Context context, FragmentManager parentFragmentManager) {
        this.context = context;
        this.parentFragmentManager = parentFragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verify_users_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel verifyingUser = users.get(position);
        holder.txtUsername.setText(verifyingUser.getFullName());
        holder.txtEmail.setText(verifyingUser.getEmail());
        Glide.with(context)
                .asBitmap()
                .load(verifyingUser.getProofIdLink())
                .into(holder.viewImageId);
//        toggleEmptyTableText(holder.listUsers, holder.emptyUsers);
        holder.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupVerify(verifyingUser);
            }
        });
    }

    private void toggleEmptyTableText(ScrollView listUsers, FlexboxLayout emptyUsers) {
        //Todo: call this method when getItemCount is changed
        System.out.println(getItemCount());
        if(getItemCount() <= 0){
            emptyUsers.setVisibility(View.VISIBLE);
            listUsers.setVisibility(View.GONE);
        }else{
            emptyUsers.setVisibility(View.GONE);
            listUsers.setVisibility(View.VISIBLE);
        }
    }

    private void showPopupVerify(UserModel user) {
        ConfirmverifyDialogFragment c = new ConfirmverifyDialogFragment(user);
        c.newInstance(user).show(parentFragmentManager, "dialog");
    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(ArrayList<UserModel> users) {
        this.users = users;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtUsername;
        private TextView txtEmail;
        private ImageView viewImageId;
        private Button verifyBtn;
        private FlexboxLayout emptyUsers;
        private ScrollView listUsers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyUsers = itemView.findViewById(R.id.emptyUsers);
            listUsers = itemView.findViewById(R.id.listUsers);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            viewImageId = itemView.findViewById(R.id.viewImageId);
            verifyBtn = itemView.findViewById(R.id.verifyBtn);
        }
    }


}
