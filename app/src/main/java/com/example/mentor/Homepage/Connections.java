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

        if(Account_Details.User_Details.getCurrConnection()){
            binding.btnSwitchAccepted.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.btnSwitchAccepted.setTextColor(this.requireContext().getColor(R.color.white));
        }else {
            binding.btnSwitchRequests.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.btnSwitchRequests.setTextColor(this.requireContext().getColor(R.color.white));
            getRequests();
        }

        binding.btnSwitchAccepted.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrConnection(true);
            binding.btnSwitchAccepted.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.btnSwitchAccepted.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnSwitchRequests.setBackgroundColor(this.requireContext().getColor(R.color.white));
            binding.btnSwitchRequests.setTextColor(this.requireContext().getColor(R.color.blue));
        });
        binding.btnSwitchRequests.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrConnection(true);
            binding.btnSwitchRequests.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.btnSwitchRequests.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnSwitchAccepted.setBackgroundColor(this.requireContext().getColor(R.color.white));
            binding.btnSwitchAccepted.setTextColor(this.requireContext().getColor(R.color.blue));
        });

        return view;
    }

    private void getRequests(){
        fStore.collection("Users").get().addOnCompleteListener(task -> {
            String currentUserId = FirebaseAuth.getInstance().getUid();
            if(task.isSuccessful() && task.getResult() != null){
                List<User> list_users = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    assert currentUserId != null;
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    User user = new User();
                    user.fullName = queryDocumentSnapshot.getString("FullName");
                    user.email = queryDocumentSnapshot.getString("Email");
                    user.pictureStr= queryDocumentSnapshot.getString("Picture");
                    user.authLvl = Objects.requireNonNull(queryDocumentSnapshot.getLong("AuthLevel")).intValue();
                    user.bioEssay = queryDocumentSnapshot.getString("bioEssay");
                    user.fbUsername = queryDocumentSnapshot.getString("FB_Username");
                    user.lInUsername = queryDocumentSnapshot.getString("LinkedIn_Username");
                    user.isAccepting = queryDocumentSnapshot.getBoolean("isAccepting");
                    user.isMentor = queryDocumentSnapshot.getBoolean("isMentor");
                    user.subjectsBinary = Objects.requireNonNull(queryDocumentSnapshot.getLong("subjectsBinary")).intValue();
                    user.uid = queryDocumentSnapshot.getId();
                    list_users.add(user);
                }
                if(list_users.size()>0){
                    UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                    binding.recyclerUsers.setAdapter(usersAdapter);
                    binding.recyclerUsers.setHasFixedSize(true);
                } else{
                    Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Error getting list of users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUserClicked(User user) {

    }
}