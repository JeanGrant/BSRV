package com.example.mentor.CreateAccount;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccTypeBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;

import java.util.Objects;

public class createAcc_Type extends Fragment {

    private FragmentCreateAccTypeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccTypeBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        initINPTxt();

        binding.cardMentor.setOnClickListener(view -> {
            Account_Details.User_Details.setIsMentor(true);
            binding.cardStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white));
            binding.btnStudent.setTextColor(ContextCompat.getColor(requireContext(),R.color.SQBTN_txtcolor));
            binding.cardMentor.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue));
            binding.btnMentor.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        });
        binding.cardStudent.setOnClickListener(view -> {
            Account_Details.User_Details.setIsMentor(false);
            binding.cardMentor.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white));
            binding.btnMentor.setTextColor(ContextCompat.getColor(requireContext(),R.color.SQBTN_txtcolor));
            binding.cardStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue));
            binding.btnStudent.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        });
        binding.btnProceed.setOnClickListener(view -> {
            String fullName = Objects.requireNonNull(binding.inpTXTFullname.getText()).toString().trim();
            String fbUser = Objects.requireNonNull(binding.inpTXTFBUser.getText()).toString().trim();
            String linkedInUser = Objects.requireNonNull(binding.inpTXTLInUser.getText()).toString().trim();
            String bioEssay = Objects.requireNonNull(binding.inpTXTBio.getText()).toString().trim();

            if (fullName.isEmpty()) {
                binding.inpTXTFullname.setError("Required Field");
            }else{
                if (bioEssay.isEmpty()) {
                    binding.inpTXTBio.setError("Required Field");
                }else {
                    Account_Details.User_Details.setFullName(fullName);
                    Account_Details.User_Details.setBioEssay(bioEssay);
                    SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new createAcc_Subjects(), "createAcc_Subjects");
                }}
        });

        return viewLayout;
    }

    private void initINPTxt() {
        if(Account_Details.User_Details.getIsMentor() != null){
            if(Account_Details.User_Details.getIsMentor()){
                binding.cardMentor.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue));
                binding.btnMentor.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            }else{
                binding.cardStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue));
                binding.btnStudent.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            }
        }
        if(!Account_Details.User_Details.getFullName().trim().isEmpty()){
            binding.inpTXTFullname.setText(Account_Details.User_Details.getFullName());
        }
        if(!Account_Details.User_Details.getBioEssay().trim().isEmpty()){
            binding.inpTXTBio.setText(Account_Details.User_Details.getBioEssay());
        }
    }
}