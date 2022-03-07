package com.example.mentor.Homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentUserProfileBinding;
import com.example.mentor.misc.Account_Details;

import java.util.List;

public class User_Profile extends Fragment {

    View view;
    FragmentUserProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        initLayout();
        return view;
    }

    public Boolean initLayout(){
        Boolean isMentor = Account_Details.User_Details.getIsMentor();
        if(isMentor != null){
            if(isMentor) {
                binding.txtAccType.setText(R.string.Mentor);
            } else{
                binding.txtAccType.setText(R.string.Student);
            } }

        Boolean isAccepting = Account_Details.User_Details.getIsAccepting();
        if(isAccepting != null){
            if(isAccepting){
                binding.txtStatus.setText(R.string.CurrentlyAccepting);
            } else{
                binding.txtStatus.setText(R.string.NoLongerAccepting);
            } }

        switch (Account_Details.User_Details.getAuthLevel()){
            case(0):
                binding.txtAuthLVL.setText(R.string.AuthLVL_0);
                break;
            case(1):
                binding.txtAuthLVL.setText(R.string.AuthLVL_1);
                break;
            case(2):
                binding.txtAuthLVL.setText(R.string.AuthLVL_2);
        }

        List<String> lstSubj = Account_Details.User_Details.initLstSubj();
        String lstSubjString = String.join(" | ", lstSubj);

        binding.txtSubjectsList.setText(lstSubjString);
        binding.txtFullName.setText(Account_Details.User_Details.getFullName());
        binding.txtEmail.setText(Account_Details.User_Details.getEmail());
        binding.txtBioEssay.setText(Account_Details.User_Details.getBioEssay());
        binding.txtFBUsername.setText(Account_Details.User_Details.getFbUser());
        binding.txtLInUsername.setText(Account_Details.User_Details.getlInUser());

        return true;
    }
}