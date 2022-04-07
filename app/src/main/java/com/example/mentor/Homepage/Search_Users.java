package com.example.mentor.Homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.databinding.FragmentSearchUsersBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Search_Users extends Fragment implements UserListener {

    private FragmentSearchUsersBinding binding;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchUsersBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        if(Account_Details.User_Details.getCurrSearch()){getUsers();}
        else{getProposals();}
        initSwitches();

        binding.btnProposals.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(false);
            getProposals();
            initSwitches();
        });
        binding.btnSendProposal.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(true);
            getUsers();
            initSwitches();
        });

        return viewLayout;
    }

    private void getUsers(){
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        fStore.collection("Users").get().addOnCompleteListener(task -> {
            String currentUserId = FirebaseAuth.getInstance().getUid();
            if(task.isSuccessful() && task.getResult() != null){
                List<User> list_users = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    assert currentUserId != null;
                    //skip if current loaded user is the logged in user
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    //skip if current loaded user's account type is the same as logged in user
                    if (queryDocumentSnapshot.getBoolean("isMentor") == Account_Details.User_Details.getIsMentor()){
                        continue;
                    }
                    User user = new User();
                    user.uid = queryDocumentSnapshot.getId();
                    user.isMentor = queryDocumentSnapshot.getBoolean("isMentor");
                    user.authLvl = Objects.requireNonNull(queryDocumentSnapshot.getLong("authLevel")).intValue();
                    user.fullName = queryDocumentSnapshot.getString("fullName");
                    user.pictureStr= queryDocumentSnapshot.getString("picture");
                    user.email = queryDocumentSnapshot.getString("email");
                    user.isAccepting = queryDocumentSnapshot.getBoolean("isAccepting");
                    list_users.add(user);
                }
                if(list_users.size()>0){
                    binding.progressBar.setVisibility(View.GONE);
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                    binding.recyclerUsers.setLayoutManager(mLinearLayoutManager);
                    binding.recyclerUsers.setVisibility(View.VISIBLE);
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

    private void getProposals(){
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String fUser = Account_Details.User_Details.getUID();
        fStore.collection("Users").document(fUser).collection("proposals").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<User> list_users = new ArrayList<>();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    User user = new User();
                    if(Account_Details.User_Details.getUID().equals(queryDocumentSnapshot.getString("requestorUID"))) {
                        user.uid = queryDocumentSnapshot.getString("requesteeUID");
                        user.fullName = queryDocumentSnapshot.getString("requesteeName");
                        user.pictureStr = queryDocumentSnapshot.getString("requesteePic");
                        user.isMentor = queryDocumentSnapshot.getBoolean("requesteeIsMentor");
                        user.email = queryDocumentSnapshot.getString("requesteeEmail");
                    }else if(Account_Details.User_Details.getUID().equals(queryDocumentSnapshot.getString("requesteeUID"))){
                        user.uid = queryDocumentSnapshot.getString("requestorUID");
                        user.fullName = queryDocumentSnapshot.getString("requestorName");
                        user.pictureStr = queryDocumentSnapshot.getString("requestorPic");
                        user.isMentor = queryDocumentSnapshot.getBoolean("requestorIsMentor");
                        user.email = queryDocumentSnapshot.getString("requestorEmail");
                    }
                    list_users.add(user);
                }
                if(list_users.size()>0){
                    binding.progressBar.setVisibility(View.GONE);
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                    binding.recyclerUsers.setLayoutManager(mLinearLayoutManager);
                    binding.recyclerUsers.setVisibility(View.VISIBLE);
                    UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                    binding.recyclerUsers.setAdapter(usersAdapter);
                    binding.recyclerUsers.setHasFixedSize(true);
                }else{Toast.makeText(getContext(), "No proposals found", Toast.LENGTH_SHORT).show();}}
            else{Toast.makeText(getContext(), "Error getting list of proposals", Toast.LENGTH_SHORT).show();}
        });
    }

    @Override
    public void onUserClicked(User user) {
        Account_Details.User_Clicked.setUID(user.uid);
        Account_Details.User_Clicked.setPicString(user.pictureStr);
        Account_Details.User_Clicked.setFullName(user.fullName);
        Account_Details.User_Clicked.setEmail(user.email);
        Account_Details.User_Clicked.setIsMentor(user.isMentor);
        Account_Details.User_Clicked.setIsAccepting(user.isAccepting);
        Account_Details.User_Clicked.setAuthLevel(user.authLvl);
        SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new user_preview(), "user_Preview");
    }

    public void initSwitches() {
        if(Account_Details.User_Details.getCurrSearch()){
            binding.btnSendProposal.setBackgroundResource(R.drawable.roundedbutton_blue);
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue_outline);;
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        }else {
            binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue);;
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnSendProposal.setBackgroundResource(R.drawable.roundedbutton_blue_outline);;
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.blue));
        }
    }
}