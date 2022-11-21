package com.example.alertosannicolas.ui.emergency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.alertosannicolas.R;
import com.example.alertosannicolas.ReportModel;
import com.example.alertosannicolas.UserModel;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class EmergencyRecViewAdapter extends RecyclerView.Adapter<EmergencyRecViewAdapter.ViewHolder> {

    private ArrayList<ReportModel> users = new ArrayList<>();

    FragmentManager parentFragmentManager;
    Context context;

    public EmergencyRecViewAdapter(Context context, FragmentManager parentFragmentManager) {
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
        ReportModel verifyingUser = users.get(position);
        String emergencyType = verifyingUser.getEmergencyType().substring(0, 1).toUpperCase() + verifyingUser.getEmergencyType().substring(1);
        holder.txtUsername.setText(emergencyType);
        holder.txtEmail.setText(verifyingUser.getNeededHelp());
        Glide.with(context)
                .asBitmap()
                .load(verifyingUser.getProofUrl())
                .into(holder.viewImageId);
//        toggleEmptyTableText(holder.listUsers, holder.emptyUsers);
        holder.verifyBtn.setText("Evaluate");
        holder.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupVerify(verifyingUser, holder.parent);
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

    private void showPopupVerify(ReportModel user, View v) {
        ConfirmverifyDialogFragment c = new ConfirmverifyDialogFragment(user, v);
        c.newInstance(user, v).show(parentFragmentManager, "dialog");
    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(ArrayList<ReportModel> users) {
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
        private FlexboxLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyUsers = itemView.findViewById(R.id.emptyUsers);
            listUsers = itemView.findViewById(R.id.listUsers);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            viewImageId = itemView.findViewById(R.id.viewImageId);
            verifyBtn = itemView.findViewById(R.id.verifyBtn);
            parent = itemView.findViewById(R.id.parent);
        }
    }


}
