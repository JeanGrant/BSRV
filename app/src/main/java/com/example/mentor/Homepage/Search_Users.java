package com.example.mentor.Homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        if(Account_Details.User_Details.getIsMentor()){
            Account_Details.User_Details.setCurrSearch(false);
            binding.btnSendProposal.setVisibility(View.GONE);
            getProposals();
        }else {
            if (Account_Details.User_Details.getCurrSearch()) {getUsers();}
            else {getProposals();}
            initSwitches();
        }

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
                    Long authLvl = queryDocumentSnapshot.getLong("authLevel");
                    assert authLvl != null;
                    if(authLvl.intValue()!=2) {
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
        List<String> list_uid = new ArrayList<>();
        //List unique UIDs in proposals
        Log.d("recyclerProposals", "list unique UIDs");
        fStore.collection("Users").document(fUser).collection("proposals").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    if (fUser.equals(queryDocumentSnapshot.getString("requestorUID"))) {
                        if (list_uid.size() > 0) {
                            if (list_uid.contains(queryDocumentSnapshot.getString("requesteeUID"))) {
                                continue;
                            }
                        }
                        list_uid.add(queryDocumentSnapshot.getString("requesteeUID"));
                    } else if (fUser.equals(queryDocumentSnapshot.getString("requesteeUID"))) {
                        if (list_uid.size() > 0) {
                            if (list_uid.contains(queryDocumentSnapshot.getString("requestorUID"))) {
                                continue;
                            }
                        }
                        list_uid.add(queryDocumentSnapshot.getString("requestorUID"));
                    }
                }
                Log.d("recyclerProposals", "list_uid size" + list_uid.size());
                fStore.collection("Users").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful() && task1.getResult() != null) {
                        List<User> list_users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()) {
                            if(list_uid.contains(queryDocumentSnapshot.getId())) {
                                User user = new User();
                                user.uid = queryDocumentSnapshot.getId();
                                user.isMentor = queryDocumentSnapshot.getBoolean("isMentor");
                                user.authLvl = Objects.requireNonNull(queryDocumentSnapshot.getLong("authLevel")).intValue();
                                user.fullName = queryDocumentSnapshot.getString("fullName");
                                user.pictureStr = queryDocumentSnapshot.getString("picture");
                                user.email = queryDocumentSnapshot.getString("email");
                                user.isAccepting = queryDocumentSnapshot.getBoolean("isAccepting");
                                list_users.add(user);
                            }
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
                if (list_uid.isEmpty()) {Toast.makeText(requireContext(), "No proposals found", Toast.LENGTH_SHORT).show();}
            }else{Toast.makeText(getContext(), "Error getting list of proposals", Toast.LENGTH_SHORT).show();}
        });
        //Recycler view initialization for obtained unique UIDs
//        Log.d("recyclerProposals", "initialize recyler");
//        if(list_uid.size()>0){
//            Log.d("recyclerProposals", "list_uid size" + list_uid.size());
//            fStore.collection("Users").get().addOnCompleteListener(task -> {
//                if (task.isSuccessful() && task.getResult() != null) {
//                    List<User> list_users = new ArrayList<>();
//                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                        User user = new User();
//                        user.uid = queryDocumentSnapshot.getId();
//                        user.isMentor = queryDocumentSnapshot.getBoolean("isMentor");
//                        user.authLvl = Objects.requireNonNull(queryDocumentSnapshot.getLong("authLevel")).intValue();
//                        user.fullName = queryDocumentSnapshot.getString("fullName");
//                        user.pictureStr= queryDocumentSnapshot.getString("picture");
//                        user.email = queryDocumentSnapshot.getString("email");
//                        user.isAccepting = queryDocumentSnapshot.getBoolean("isAccepting");
//                        list_users.add(user);
//                    }
//                    if(list_users.size()>0){
//                        binding.progressBar.setVisibility(View.GONE);
//                        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
//                        binding.recyclerUsers.setLayoutManager(mLinearLayoutManager);
//                        binding.recyclerUsers.setVisibility(View.VISIBLE);
//                        UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
//                        binding.recyclerUsers.setAdapter(usersAdapter);
//                        binding.recyclerUsers.setHasFixedSize(true);
//                    }else{Toast.makeText(getContext(), "No proposals found", Toast.LENGTH_SHORT).show();}}
//                else{Toast.makeText(getContext(), "Error getting list of proposals", Toast.LENGTH_SHORT).show();}
//            });
//        }
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
            binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        }else {
            binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue);
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnSendProposal.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.blue));
        }
    }
}