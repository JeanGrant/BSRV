package com.example.mentor.CreateAccount;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccSubjectsBinding;
import com.example.mentor.misc.Account_Details;

public class CreateAcc_Subjects extends Fragment implements View.OnClickListener{

    View view;
    Integer subjects = 0;
    FragmentCreateAccSubjectsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        subjects = Account_Details.User_Details.getSubjects();
        Log.i("Subject Binary", subjects.toString());

        // Inflate the layout for this fragment
        binding = FragmentCreateAccSubjectsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        for (int i = 0; i<11; i++){
            if((subjects & (1 << i)) > 0) {
                switch (i) {
                    case (0):
                        uiSetUnset(binding.chkAdobePs, true);break;
                    case (1):
                        uiSetUnset(binding.chkAnimation, true);break;
                    case (2):
                        uiSetUnset(binding.chkArts, true);break;
                    case (3):
                        uiSetUnset(binding.chkAutoCAD, true);break;
                    case (4):
                        uiSetUnset(binding.chkProgramming, true);break;
                    case (5):
                        uiSetUnset(binding.chkMSOffice, true);break;
                    case (6):
                        uiSetUnset(binding.chkMathematics, true);break;
                    case (7):
                        uiSetUnset(binding.chkSciences, true);break;
                    case (8):
                        uiSetUnset(binding.chkLanguages, true);break;
                    case (9):
                        uiSetUnset(binding.chkLaw, true);break;
                    case (10):
                        uiSetUnset(binding.chkEngineering, true);break;
                    default: break; } } }

        binding.imgBTNBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Interview()).commit());

        //region setOnClickListener
        binding.layoutAdobePs.setOnClickListener(this);
        binding.layoutAnimation.setOnClickListener(this);
        binding.layoutArts.setOnClickListener(this);
        binding.layoutAutoCAD.setOnClickListener(this);
        binding.layoutProgramming.setOnClickListener(this);
        binding.layoutMSOffice.setOnClickListener(this);
        binding.layoutMathematics.setOnClickListener(this);
        binding.layoutSciences.setOnClickListener(this);
        binding.layoutLanguages.setOnClickListener(this);
        binding.layoutLaw.setOnClickListener(this);
        binding.layoutEngineering.setOnClickListener(this);
        binding.btnProceed.setOnClickListener(this);
        //endregion

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_AdobePs:
                isKthSet(0, binding.chkAdobePs);
                break;
            case R.id.layout_Animation:
                isKthSet(1, binding.chkAnimation);
                break;
            case R.id.layout_Arts:
                isKthSet(2, binding.chkArts);
                break;
            case R.id.layout_AutoCAD:
                isKthSet(3, binding.chkAutoCAD);
                break;
            case R.id.layout_Programming:
                isKthSet(4, binding.chkProgramming);
                break;
            case R.id.layout_MSOffice:
                isKthSet(5, binding.chkMSOffice);
                break;
            case R.id.layout_Mathematics:
                isKthSet(6, binding.chkMathematics);
                break;
            case R.id.layout_Sciences:
                isKthSet(7, binding.chkSciences);
                break;
            case R.id.layout_Languages:
                isKthSet(8, binding.chkLanguages);
                break;
            case R.id.layout_Law:
                isKthSet(9, binding.chkLaw);
                break;
            case R.id.layout_Engineering:
                isKthSet(10, binding.chkEngineering);
                break;
            case R.id.btnProceed:
                if (subjects > 0){
                    Account_Details.User_Details.setSubjects(subjects);
                    if(Account_Details.User_Details.getIsMentor()) {
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Rates()).commit();
                    }else{
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Finalize()).commit();
                    }
                } else {
                    Toast.makeText(getContext(), "Kindly specify a subject.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void isKthSet(Integer k, CheckBox setCHK){
        uiSetUnset(setCHK, (subjects & (1 << k)) == 0);
        subjects = subjects ^ (1 << k);
        Log.i("Subject Binary", subjects.toString());
    }

    public void uiSetUnset(CheckBox setCHK, Boolean shouldSet){
        setCHK.setChecked(shouldSet);
    }

}