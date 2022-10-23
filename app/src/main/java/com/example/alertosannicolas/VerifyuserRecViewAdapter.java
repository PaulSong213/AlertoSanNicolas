package com.example.alertosannicolas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VerifyuserRecViewAdapter extends RecyclerView.Adapter<VerifyuserRecViewAdapter.ViewHolder> {

    private ArrayList<UserModel> users = new ArrayList<>();
    Context context;

    public VerifyuserRecViewAdapter(Context context) {
        this.context = context;
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
        holder.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupVerify(verifyingUser);
            }
        });
    }

    private void showPopupVerify(UserModel user) {
        //Todo: show a modal to verify the user
        Toast.makeText(context, user.getUid(), Toast.LENGTH_SHORT).show();
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            viewImageId = itemView.findViewById(R.id.viewImageId);
            verifyBtn = itemView.findViewById(R.id.verifyBtn);
        }
    }


}
