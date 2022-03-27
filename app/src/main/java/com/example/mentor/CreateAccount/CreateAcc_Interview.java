package com.example.mentor.CreateAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccInterviewBinding;
import com.example.mentor.misc.Account_Details;

import java.util.Objects;


public class CreateAcc_Interview extends Fragment {

    View view;
    String fullName, fbUser, linkedInUser, bioEssay;
    FragmentCreateAccInterviewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccInterviewBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        if(!Account_Details.User_Details.getFullName().trim().isEmpty()){
            binding.inpTXTFullname.setText(Account_Details.User_Details.getFullName());
        }
        if(!Account_Details.User_Details.getFbUser().trim().isEmpty()){
            binding.inpTXTFBUser.setText(Account_Details.User_Details.getFbUser());
        }
        if(!Account_Details.User_Details.getlInUser().trim().isEmpty()){
            binding.inpTXTLInUser.setText(Account_Details.User_Details.getlInUser());
        }
        if(!Account_Details.User_Details.getBioEssay().trim().isEmpty()){
            binding.inpTXTBio.setText(Account_Details.User_Details.getBioEssay());
        }

        binding.imgBTNBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Type()).commit());

        binding.btnProceed.setOnClickListener(view -> {
            fullName = Objects.requireNonNull(binding.inpTXTFullname.getText()).toString().trim();
            fbUser = Objects.requireNonNull(binding.inpTXTFBUser.getText()).toString().trim();
            linkedInUser = Objects.requireNonNull(binding.inpTXTLInUser.getText()).toString().trim();
            bioEssay = Objects.requireNonNull(binding.inpTXTBio.getText()).toString().trim();

            if (fullName.isEmpty()) {
                binding.inpTXTFullname.setError("Required Field");
            }
            if (bioEssay.isEmpty()) {
                binding.inpTXTBio.setError("Required Field");
            }else {
                Account_Details.User_Details.setFullName(fullName);
                Account_Details.User_Details.setFBUser(fbUser);
                Account_Details.User_Details.setLInUser(linkedInUser);
                Account_Details.User_Details.setBioEssay(bioEssay);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Subjects()).commit();
            }
        });
        return view;
    }
}