package com.example.mentor.CreateAccount;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccSubjectsBinding;
import com.example.mentor.misc.Account_Details;
import com.google.android.material.card.MaterialCardView;

public class createAcc_Subjects extends Fragment implements View.OnClickListener{

    private Integer subjects = 0;
    private FragmentCreateAccSubjectsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        subjects = Account_Details.User_Details.getSubjects();
        Log.i("Subject Binary", subjects.toString());

        // Inflate the layout for this fragment
        binding = FragmentCreateAccSubjectsBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        for (int i = 0; i<11; i++){
            if((subjects & (1 << i)) > 0) {
                switch (i) {
                    case (0):
                        uiSetUnset(binding.layoutAdobePs, binding.imgAdobePs, binding.txtAdobePs);break;
                    case (1):
                        uiSetUnset(binding.layoutAnimation, binding.imgAnimation, binding.txtAnimation);break;
                    case (2):
                        uiSetUnset(binding.layoutArts, binding.imgArts, binding.txtArts);break;
                    case (3):
                        uiSetUnset(binding.layoutAutoCAD, binding.imgAutoCAD, binding.txtAutoCAD);break;
                    case (4):
                        uiSetUnset(binding.layoutProgramming, binding.imgProgramming, binding.txtProgramming);break;
                    case (5):
                        uiSetUnset(binding.layoutMSOffice, binding.imgMSOffice, binding.txtMSOffice);break;
                    case (6):
                        uiSetUnset(binding.layoutMathematics, binding.imgMathematics, binding.txtMathematics);break;
                    case (7):
                        uiSetUnset(binding.layoutSciences, binding.imgSciences, binding.txtSciences);break;
                    case (8):
                        uiSetUnset(binding.layoutLanguages, binding.imgLanguages, binding.txtLanguages);break;
                    case (9):
                        uiSetUnset(binding.layoutLaw, binding.imgLaw, binding.txtLaw);break;
                    case (10):
                        uiSetUnset(binding.layoutEngineering, binding.imgEngineering, binding.txtEngineering);break;
                    default: break; } } }

        binding.imgBTNBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new createAcc_Type()).commit());

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

        return viewLayout;
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View v){
        switch (v.getId()){
            case R.id.layout_AdobePs:
                isKthSet(0, binding.layoutAdobePs, binding.imgAdobePs, binding.txtAdobePs);break;
            case R.id.layout_Animation:
                isKthSet(1, binding.layoutAnimation, binding.imgAnimation, binding.txtAnimation);break;
            case R.id.layout_Arts:
                isKthSet(2, binding.layoutArts, binding.imgArts, binding.txtArts);break;
            case R.id.layout_AutoCAD:
                isKthSet(3, binding.layoutAutoCAD, binding.imgAutoCAD, binding.txtAutoCAD);break;
            case R.id.layout_Programming:
                isKthSet(4, binding.layoutProgramming, binding.imgProgramming, binding.txtProgramming);break;
            case R.id.layout_MSOffice:
                isKthSet(5, binding.layoutMSOffice, binding.imgMSOffice, binding.txtMSOffice);break;
            case R.id.layout_Mathematics:
                isKthSet(6, binding.layoutMathematics, binding.imgMathematics, binding.txtMathematics);break;
            case R.id.layout_Sciences:
                isKthSet(7, binding.layoutSciences, binding.imgSciences, binding.txtSciences);break;
            case R.id.layout_Languages:
                isKthSet(8, binding.layoutLanguages, binding.imgLanguages, binding.txtLanguages);break;
            case R.id.layout_Law:
                isKthSet(9, binding.layoutLaw, binding.imgLaw, binding.txtLaw);break;
            case R.id.layout_Engineering:
                isKthSet(10, binding.layoutEngineering, binding.imgEngineering, binding.txtEngineering);break;
            case R.id.btnProceed:
                if (subjects > 0){
                    Account_Details.User_Details.setSubjects(subjects);
                    if(Account_Details.User_Details.getIsMentor()) {
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new createAcc_Rates()).commit();
                    }else{
                        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new createAcc_Finalize()).commit();
                    }
                } else {
                    Toast.makeText(getContext(), "Kindly add a subject.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void isKthSet(Integer k, MaterialCardView card, ImageView imgSubj, TextView txtSubj){
        uiSetUnset(card, imgSubj, txtSubj);
        subjects = subjects ^ (1 << k);
        Log.i("Subject Binary", subjects.toString());
    }

    public void uiSetUnset(MaterialCardView card, ImageView imgSubj, TextView txtSubj){
        ColorStateList newBG, newTint;
        newBG = imgSubj.getImageTintList();
        newTint = card.getCardBackgroundColor();
        card.setCardBackgroundColor(newBG);
        imgSubj.setImageTintList(newTint);
        txtSubj.setTextColor(newTint);
    }

}