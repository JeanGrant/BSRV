package com.example.mentor.CreateAccount;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
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
                        uiSetUnset(binding.btnAdobePs, true);break;
                    case (1):
                        uiSetUnset(binding.btnAnimation, true);break;
                    case (2):
                        uiSetUnset(binding.btnArts, true);break;
                    case (3):
                        uiSetUnset(binding.btnAutoCAD, true);break;
                    case (4):
                        uiSetUnset(binding.btnProgramming, true);break;
                    case (5):
                        uiSetUnset(binding.btnMicrosoft, true);break;
                    case (6):
                        uiSetUnset(binding.btnMathematics, true);break;
                    case (7):
                        uiSetUnset(binding.btnSciences, true);break;
                    case (8):
                        uiSetUnset(binding.btnLanguages, true);break;
                    case (9):
                        uiSetUnset(binding.btnLaw, true);break;
                    case (10):
                        uiSetUnset(binding.btnEngineering, true);break;
                    default: break; } } }

        binding.imgBTNBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Interview()).commit());

        //region setOnClickListener
        binding.btnAdobePs.setOnClickListener(this);
        binding.btnAnimation.setOnClickListener(this);
        binding.btnArts.setOnClickListener(this);
        binding.btnAutoCAD.setOnClickListener(this);
        binding.btnProgramming.setOnClickListener(this);
        binding.btnMicrosoft.setOnClickListener(this);
        binding.btnMathematics.setOnClickListener(this);
        binding.btnSciences.setOnClickListener(this);
        binding.btnLanguages.setOnClickListener(this);
        binding.btnLaw.setOnClickListener(this);
        binding.btnEngineering.setOnClickListener(this);
        binding.btnProceed.setOnClickListener(this);
        //endregion

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_AdobePs:
                isKthSet(0, binding.btnAdobePs);
                break;
            case R.id.btn_Animation:
                isKthSet(1, binding.btnAnimation);
                break;
            case R.id.btn_Arts:
                isKthSet(2, binding.btnArts);
                break;
            case R.id.btn_AutoCAD:
                isKthSet(3, binding.btnAutoCAD);
                break;
            case R.id.btn_Programming:
                isKthSet(4, binding.btnProgramming);
                break;
            case R.id.btn_Microsoft:
                isKthSet(5, binding.btnMicrosoft);
                break;
            case R.id.btn_Mathematics:
                isKthSet(6, binding.btnMathematics);
                break;
            case R.id.btn_Sciences:
                isKthSet(7, binding.btnSciences);
                break;
            case R.id.btn_Languages:
                isKthSet(8, binding.btnLanguages);
                break;
            case R.id.btn_Law:
                isKthSet(9, binding.btnLaw);
                break;
            case R.id.btn_Engineering:
                isKthSet(10, binding.btnEngineering);
                break;
            case R.id.btnProceed:
                if (subjects > 0){
                    Account_Details.User_Details.setSubjects(subjects);
                    requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Finalize()).commit();
                } else {
                    Toast.makeText(getContext(), "Kindly specify a subject.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void isKthSet(Integer k, Button clickedBTN){
        uiSetUnset(clickedBTN, (subjects & (1 << k)) == 0);
        subjects = subjects ^ (1 << k);
        Log.i("Subject Binary", subjects.toString());
    }

    public void uiSetUnset(Button targetBTN, Boolean shouldSet){
        if (shouldSet){
            targetBTN.setBackground(AppCompatResources.getDrawable(this.requireContext(), R.drawable.roundedbutton_blue));
            targetBTN.setTextColor(this.requireContext().getColor(R.color.white));
            Log.i(targetBTN.getText().toString(), "Enabled");
        }else{
            targetBTN.setBackground(AppCompatResources.getDrawable(this.requireContext(), R.drawable.roundedbutton_blue_outline));
            targetBTN.setTextColor(this.requireContext().getColor(R.color.blue));
            Log.i(targetBTN.getText().toString(), "Unabled");
        }
    }

}