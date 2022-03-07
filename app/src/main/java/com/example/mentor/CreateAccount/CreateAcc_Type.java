package com.example.mentor.CreateAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccTypeBinding;
import com.example.mentor.misc.Account_Details;

public class CreateAcc_Type extends Fragment {

    View view;
    FragmentCreateAccTypeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccTypeBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.imgBTNBearMentor.setOnClickListener(view -> {
            Account_Details.User_Details.setIsMentor(true);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Interview()).commit();
        });

        binding.imgBTNBearStudent.setOnClickListener(view -> {
            Account_Details.User_Details.setIsMentor(false);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Interview()).commit();
        });

        return view;
    }
}