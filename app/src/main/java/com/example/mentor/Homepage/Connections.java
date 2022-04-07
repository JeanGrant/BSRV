package com.example.mentor.Homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.databinding.FragmentConnectionsBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Connections extends Fragment implements UserListener {

    View view;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FragmentConnectionsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConnectionsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

//        if(Account_Details.User_Details.getCurrConnection()){
//            binding.btnSwitchAccepted.setBackgroundColor(this.requireContext().getColor(R.color.blue));
//            binding.btnSwitchAccepted.setTextColor(this.requireContext().getColor(R.color.white));
//        }else {
//            binding.btnSwitchRequests.setBackgroundColor(this.requireContext().getColor(R.color.blue));
//            binding.btnSwitchRequests.setTextColor(this.requireContext().getColor(R.color.white));
//            getRequests();
//        }
//
//        binding.btnSwitchAccepted.setOnClickListener(view -> {
//            Account_Details.User_Details.setCurrConnection(true);
//            binding.btnSwitchAccepted.setBackgroundColor(this.requireContext().getColor(R.color.blue));
//            binding.btnSwitchAccepted.setTextColor(this.requireContext().getColor(R.color.white));
//            binding.btnSwitchRequests.setBackgroundColor(this.requireContext().getColor(R.color.white));
//            binding.btnSwitchRequests.setTextColor(this.requireContext().getColor(R.color.blue));
//        });
//        binding.btnSwitchRequests.setOnClickListener(view -> {
//            Account_Details.User_Details.setCurrConnection(true);
//            binding.btnSwitchRequests.setBackgroundColor(this.requireContext().getColor(R.color.blue));
//            binding.btnSwitchRequests.setTextColor(this.requireContext().getColor(R.color.white));
//            binding.btnSwitchAccepted.setBackgroundColor(this.requireContext().getColor(R.color.white));
//            binding.btnSwitchAccepted.setTextColor(this.requireContext().getColor(R.color.blue));
//        });

        return view;
    }

    @Override
    public void onUserClicked(User user) {

    }
}