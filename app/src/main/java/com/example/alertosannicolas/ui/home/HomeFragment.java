package com.example.alertosannicolas.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.alertosannicolas.Authentication;
import com.example.alertosannicolas.MainActivity;
import com.example.alertosannicolas.R;
import com.example.alertosannicolas.SubmitReport;
import com.example.alertosannicolas.databinding.FragmentHomeBinding;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    Authentication auth;
    View.OnClickListener emergencySubmit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String type = view.getResources().getResourceEntryName(view.getId()); //get the id of the clicked button
            type = type.substring(0, type.length() - 3); //remove the last 3 char since the id always ends in "Btn"
            Intent intent = new Intent(getContext(), SubmitReport.class);
            Bundle b = new Bundle();
            System.out.println(type);
            b.putString("emergencyType", type); //Your id
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RelativeLayout fireBtn = binding.fireBtn;
        final RelativeLayout earthquakeBtn = binding.earthquakeBtn;
        final RelativeLayout typhoonBtn = binding.typhoonBtn;
        final RelativeLayout robberyBtn = binding.robberyBtn;
        final RelativeLayout injuriesBtn = binding.injuriesBtn;
        final RelativeLayout othersBtn = binding.othersBtn;

        fireBtn.setOnClickListener(emergencySubmit);
        earthquakeBtn.setOnClickListener(emergencySubmit);
        typhoonBtn.setOnClickListener(emergencySubmit);
        robberyBtn.setOnClickListener(emergencySubmit);
        injuriesBtn.setOnClickListener(emergencySubmit);
        othersBtn.setOnClickListener(emergencySubmit);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}