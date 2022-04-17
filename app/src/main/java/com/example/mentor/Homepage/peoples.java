package com.example.mentor.Homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.databinding.FragmentPeoplesBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;
import com.example.mentor.schedule_meeting;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class peoples extends Fragment implements UserListener {

    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FragmentPeoplesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPeoplesBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        binding.btnProposals.setOnClickListener(view -> getSchedMeets());
        binding.btnHistory.setOnClickListener(view -> getHistory());
        binding.btnFollowing.setOnClickListener(view -> getFollowing());

        getSchedMeets();

        return viewLayout;
    }

    private void getSchedMeets() {
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue);
        binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.white));
        binding.btnHistory.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
        binding.btnHistory.setTextColor(this.requireContext().getColor(R.color.blue));
        binding.btnFollowing.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
        binding.btnFollowing.setTextColor(this.requireContext().getColor(R.color.blue));
        List<String> uniqueID = new ArrayList<>();
        fStore.collection("Users").document(Account_Details.User_Details.getUID()).collection("proposals").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if(task.getResult().size() > 0) {
                    for (QueryDocumentSnapshot qDocSnap : task.getResult()) {
                        if(qDocSnap.get("hideUsers") != null) {
                            ArrayList<String> hideUsersUID = (ArrayList<String>) qDocSnap.get("hideUsers");
                            assert hideUsersUID != null;
                            if (hideUsersUID.contains(qDocSnap.getId())) {
                                continue;
                            }
                        }
                        Long status = qDocSnap.getLong("status");
                        assert status != null;
                        if (status.intValue() == 4) {
                            if (Account_Details.User_Details.getUID().equals(qDocSnap.getString("requesteeUID"))) {
                                uniqueID.add(qDocSnap.getString("requestorUID"));
                            } else if (Account_Details.User_Details.getUID().equals(qDocSnap.getString("requestorUID"))) {
                                uniqueID.add(qDocSnap.getString("requesteeUID"));
                            }
                        }
                    }
                    fStore.collection("Users").get().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()) {
                            if(task1.getResult().size() > 0) {
                                List<User> list_users = new ArrayList<>();
                                for (QueryDocumentSnapshot qDocSnap : task1.getResult()) {
                                    if (uniqueID.contains(qDocSnap.getId())) {
                                        User user = new User();
                                        user.uid = qDocSnap.getId();
                                        user.isMentor = qDocSnap.getBoolean("isMentor");
                                        user.authLvl = Objects.requireNonNull(qDocSnap.getLong("authLevel")).intValue();
                                        user.fullName = qDocSnap.getString("fullName");
                                        user.pictureStr = qDocSnap.getString("picture");
                                        user.email = qDocSnap.getString("email");
                                        user.isAccepting = qDocSnap.getBoolean("isAccepting");
                                        if(user.isMentor) {
                                            Long ratingAve = qDocSnap.getLong("ratingAve");
                                            if(ratingAve != null) {
                                                user.rating = ratingAve.intValue();
                                            }
                                        }
                                        if(qDocSnap.getString("picString") != null) {
                                            String picString = qDocSnap.getString("picString");
                                            if (picString != null && !picString.isEmpty() && !picString.equals("null")) {
                                                user.pictureStr = qDocSnap.getString("picString");
                                            }
                                        }

                                        list_users.add(user);
                                    }
                                }
                                if(list_users.size()>0){
                                    binding.progressBar.setVisibility(View.GONE);
                                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                                    binding.recyclerUsers.setLayoutManager(mLinearLayoutManager);
                                    UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                                    binding.recyclerUsers.setAdapter(usersAdapter);
                                    binding.recyclerUsers.setHasFixedSize(true);

                                    binding.recyclerUsers.setVisibility(View.VISIBLE);
                                } else{
                                    Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                                }
                            } else{
                                Toast.makeText(getContext(), "Error getting list of users", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {Log.e("getSchedMeets uniqueID task", String.valueOf(task.getResult().size()));}
            } else {Log.e("getSchedMeets uniqueID task", "task is null");}
        });
    }

    private void getHistory() {
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.btnHistory.setBackgroundResource(R.drawable.roundedbutton_blue);
        binding.btnHistory.setTextColor(this.requireContext().getColor(R.color.white));
        binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
        binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        binding.btnFollowing.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
        binding.btnFollowing.setTextColor(this.requireContext().getColor(R.color.blue));
        fStore.collection("Users").document(Account_Details.User_Details.getUID()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.get("transactionHistory") != null) {
                ArrayList<String> uniqueID = (ArrayList<String>) documentSnapshot.get("transactionHistory");
                assert uniqueID != null;
                if(documentSnapshot.get("hideUsers") != null) {
                    ArrayList<String> hideUsersUID = (ArrayList<String>) documentSnapshot.get("hideUsers");
                    assert hideUsersUID != null;
                    for (int i=0; i<hideUsersUID.size(); i++) {
                        if (uniqueID.contains(hideUsersUID.get(i))) {
                            uniqueID.remove(hideUsersUID.get(i));
                        }
                    }
                }
                fStore.collection("Users").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        if (task1.getResult().size() > 0) {
                            List<User> list_users = new ArrayList<>();
                            for (QueryDocumentSnapshot qDocSnap : task1.getResult()) {
                                if (uniqueID.contains(qDocSnap.getId())) {
                                    User user = new User();
                                    user.uid = qDocSnap.getId();
                                    user.isMentor = qDocSnap.getBoolean("isMentor");
                                    user.authLvl = Objects.requireNonNull(qDocSnap.getLong("authLevel")).intValue();
                                    user.fullName = qDocSnap.getString("fullName");
                                    user.pictureStr = qDocSnap.getString("picture");
                                    user.email = qDocSnap.getString("email");
                                    user.isAccepting = qDocSnap.getBoolean("isAccepting");
                                    if(qDocSnap.getString("picString") != null) {
                                        String picString = qDocSnap.getString("picString");
                                        if (picString != null && !picString.isEmpty() && !picString.equals("null")) {
                                            user.pictureStr = qDocSnap.getString("picString");
                                        }
                                    }
                                    if(user.isMentor) {
                                        Long ratingAve = qDocSnap.getLong("ratingAve");
                                        if(ratingAve != null) {
                                            user.rating = ratingAve.intValue();
                                        }
                                    }
                                    list_users.add(user);
                                }
                            }
                            if (list_users.size() > 0) {
                                binding.progressBar.setVisibility(View.GONE);
                                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                                binding.recyclerUsers.setLayoutManager(mLinearLayoutManager);
                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                                UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                                binding.recyclerUsers.setAdapter(usersAdapter);
                                binding.recyclerUsers.setHasFixedSize(true);

                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error getting list of users", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void getFollowing() {
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.btnFollowing.setBackgroundResource(R.drawable.roundedbutton_blue);
        binding.btnFollowing.setTextColor(this.requireContext().getColor(R.color.white));
        binding.btnHistory.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
        binding.btnHistory.setTextColor(this.requireContext().getColor(R.color.blue));
        binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
        binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        fStore.collection("Users").document(Account_Details.User_Details.getUID()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.get("followings") != null) {
                ArrayList<String> uniqueID = (ArrayList<String>) documentSnapshot.get("followings");
                assert uniqueID != null;
                if(documentSnapshot.get("hideUsers") != null) {
                    ArrayList<String> hideUsersUID = (ArrayList<String>) documentSnapshot.get("hideUsers");
                    assert hideUsersUID != null;
                    for (int i=0; i<hideUsersUID.size(); i++) {
                        if (uniqueID.contains(hideUsersUID.get(i))) {
                            uniqueID.remove(hideUsersUID.get(i));
                        }
                    }
                }
                fStore.collection("Users").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        if (task1.getResult().size() > 0) {
                            List<User> list_users = new ArrayList<>();
                            for (QueryDocumentSnapshot qDocSnap : task1.getResult()) {
                                if (uniqueID.contains(qDocSnap.getId())) {
                                    User user = new User();
                                    user.uid = qDocSnap.getId();
                                    user.isMentor = qDocSnap.getBoolean("isMentor");
                                    user.authLvl = Objects.requireNonNull(qDocSnap.getLong("authLevel")).intValue();
                                    user.fullName = qDocSnap.getString("fullName");
                                    user.pictureStr = qDocSnap.getString("picture");
                                    user.email = qDocSnap.getString("email");
                                    user.isAccepting = qDocSnap.getBoolean("isAccepting");
                                    if(user.isMentor) {
                                        Long ratingAve = qDocSnap.getLong("ratingAve");
                                        if(ratingAve != null) {
                                            user.rating = ratingAve.intValue();
                                        }
                                    }
                                    if(qDocSnap.getString("picString") != null) {
                                        String picString = qDocSnap.getString("picString");
                                        if (picString != null && !picString.isEmpty() && !picString.equals("null")) {
                                            user.pictureStr = qDocSnap.getString("picString");
                                        }
                                    }

                                    list_users.add(user);
                                }
                            }
                            if (list_users.size() > 0) {
                                binding.progressBar.setVisibility(View.GONE);
                                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                                binding.recyclerUsers.setLayoutManager(mLinearLayoutManager);
                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                                UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                                binding.recyclerUsers.setAdapter(usersAdapter);
                                binding.recyclerUsers.setHasFixedSize(true);

                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error getting list of users", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
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
        if(binding.btnProposals.getCurrentTextColor() == getResources().getColor(R.color.white)) {
            SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new schedule_meeting(), "schedule_meeting");
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isHome",false);

            Fragment fragment2 = new user_Preview();
            fragment2.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment2, "user_Preview")
                    .commit();
        }
    }
}