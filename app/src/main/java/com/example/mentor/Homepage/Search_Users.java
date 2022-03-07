package com.example.mentor.Homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.databinding.FragmentSearchUsersBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Search_Users extends Fragment {

    View view;
    FragmentSearchUsersBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchUsersBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        getUsers();

        if(Account_Details.User_Details.getCurrSearch()){
            binding.switchMentor.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.switchMentor.setTextColor(this.requireContext().getColor(R.color.white));
        }else {
            binding.switchStudent.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.switchStudent.setTextColor(this.requireContext().getColor(R.color.white));
        }

        binding.switchStudent.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(false);
            getUsers();
            binding.switchStudent.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.switchStudent.setTextColor(this.requireContext().getColor(R.color.white));
            binding.switchMentor.setBackgroundColor(this.requireContext().getColor(R.color.white));
            binding.switchMentor.setTextColor(this.requireContext().getColor(R.color.blue));
        });
        binding.switchMentor.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(true);
            getUsers();
            binding.switchMentor.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.switchMentor.setTextColor(this.requireContext().getColor(R.color.white));
            binding.switchStudent.setBackgroundColor(this.requireContext().getColor(R.color.white));
            binding.switchStudent.setTextColor(this.requireContext().getColor(R.color.blue));
        });
        return view;
    }

    private void getUsers(){
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fStore.collection("Users").get().addOnCompleteListener(task -> {
            String currentUserId = FirebaseAuth.getInstance().getUid();
            if(task.isSuccessful() && task.getResult() != null){
                List<User> list_users = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    if (!queryDocumentSnapshot.getBoolean("isMentor") == Account_Details.User_Details.getCurrSearch()){
                        continue;
                    }
                    User user = new User();
                    user.fullName = queryDocumentSnapshot.getString("FullName");
                    user.email = queryDocumentSnapshot.getString("Email");
                    user.pictureStr= queryDocumentSnapshot.getString("Picture");
                    list_users.add(user);
                }
                if(list_users.size()>0){
                    UsersAdapter usersAdapter = new UsersAdapter(list_users);
                    binding.recyclerUsers.setHasFixedSize(true);
                    binding.recyclerUsers.setAdapter(usersAdapter);
                } else{
                    Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Error getting list of users", Toast.LENGTH_SHORT).show();
            }
        });

    }
}