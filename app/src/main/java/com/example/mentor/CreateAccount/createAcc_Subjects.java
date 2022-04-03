package com.example.mentor.CreateAccount;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccSubjectsBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;
import com.google.android.material.card.MaterialCardView;

public class createAcc_Subjects extends Fragment implements View.OnClickListener{

    private FragmentCreateAccSubjectsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentCreateAccSubjectsBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        for (int i = 0; i< Account_Details.User_Details.subjects.size(); i++){
            switch (Account_Details.User_Details.subjects.get(i)) {
                case ("Adobe Ps"):
                    uiSetUnset(binding.layoutAdobePs, binding.imgAdobePs, binding.txtAdobePs);break;
                case ("Animation"):
                    uiSetUnset(binding.layoutAnimation, binding.imgAnimation, binding.txtAnimation);break;
                case ("Arts"):
                    uiSetUnset(binding.layoutArts, binding.imgArts, binding.txtArts);break;
                case ("AutoCAD"):
                    uiSetUnset(binding.layoutAutoCAD, binding.imgAutoCAD, binding.txtAutoCAD);break;
                case ("Engineering"):
                    uiSetUnset(binding.layoutEngineering, binding.imgEngineering, binding.txtEngineering);break;
                case ("Languages"):
                    uiSetUnset(binding.layoutLanguages, binding.imgLanguages, binding.txtLanguages);break;
                case ("Law"):
                    uiSetUnset(binding.layoutLaw, binding.imgLaw, binding.txtLaw);break;
                case ("MS Office"):
                    uiSetUnset(binding.layoutMSOffice, binding.imgMSOffice, binding.txtMSOffice);break;
                case ("Mathematics"):
                    uiSetUnset(binding.layoutMathematics, binding.imgMathematics, binding.txtMathematics);break;
                case ("Programming"):
                    uiSetUnset(binding.layoutProgramming, binding.imgProgramming, binding.txtProgramming);break;
                case ("Sciences"):
                    uiSetUnset(binding.layoutSciences, binding.imgSciences, binding.txtSciences);break;
                default: break; } }

        binding.imgBTNBack.setOnClickListener(view -> SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new createAcc_Type(), "createAcc_Type"));

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
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.AdobePs));
                uiSetUnset(binding.layoutAdobePs, binding.imgAdobePs, binding.txtAdobePs);break;
            case R.id.layout_Animation:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.Animation));
                uiSetUnset(binding.layoutAnimation, binding.imgAnimation, binding.txtAnimation);break;
            case R.id.layout_Arts:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.Arts));
                uiSetUnset(binding.layoutArts, binding.imgArts, binding.txtArts);break;
            case R.id.layout_AutoCAD:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.AutoCAD));
                uiSetUnset(binding.layoutAutoCAD, binding.imgAutoCAD, binding.txtAutoCAD);break;
            case R.id.layout_Programming:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.Programming));
                uiSetUnset(binding.layoutProgramming, binding.imgProgramming, binding.txtProgramming);break;
            case R.id.layout_MSOffice:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.MSOffice));
                uiSetUnset(binding.layoutMSOffice, binding.imgMSOffice, binding.txtMSOffice);break;
            case R.id.layout_Mathematics:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.Mathematics));
                uiSetUnset(binding.layoutMathematics, binding.imgMathematics, binding.txtMathematics);break;
            case R.id.layout_Sciences:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.Sciences));
                uiSetUnset(binding.layoutSciences, binding.imgSciences, binding.txtSciences);break;
            case R.id.layout_Languages:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.Languages));
                uiSetUnset(binding.layoutLanguages, binding.imgLanguages, binding.txtLanguages);break;
            case R.id.layout_Law:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.Law));
                uiSetUnset(binding.layoutLaw, binding.imgLaw, binding.txtLaw);break;
            case R.id.layout_Engineering:
                Account_Details.User_Details.toggleSubject(getResources().getString(R.string.Engineering));
                uiSetUnset(binding.layoutEngineering, binding.imgEngineering, binding.txtEngineering);break;
            case R.id.btnProceed:
                if (!Account_Details.User_Details.subjects.isEmpty()){
                    if(Account_Details.User_Details.getIsMentor()) {
                        SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new createAcc_Rates(), "createAcc_Rates");
                    }else{
                        SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new createAcc_Finalize(), "createAcc_Finalize");
                    }
                } else {
                    Toast.makeText(getContext(), "Kindly add a subject.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void uiSetUnset(MaterialCardView card, ImageView imgSubj, TextView txtSubj){
        ColorStateList newBG, newTint;
        newBG = imgSubj.getImageTintList();
        newTint = card.getCardBackgroundColor();
        card.setCardBackgroundColor(newBG);
        imgSubj.setImageTintList(newTint);
        txtSubj.setTextColor(newTint);
        switch (txtSubj.getText().toString()){
            case "Adobe Ps":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_subjects_adobe_ps,null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_adobe_ps, null));}
                break;
            case "Animation":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_animation, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_animation, null));}
                break;
            case "Arts":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_arts, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_arts, null));}
                break;
            case "AutoCAD":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_autocad, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_autocad, null));}
                break;
            case "Engineering":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_engineering, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_engineering, null));}
                break;
            case "Languages":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_languages, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_languages, null));}
                break;
            case "Law":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_law, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_law, null));}
                break;
            case "MS Office":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_ms_office, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_ms_office, null));}
                break;
            case "Mathematics":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_mathematics, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_mathematics, null));}
                break;
            case "Programming":
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_programming, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_programming, null));}
                break;
            case ("Sciences"):
                if(Account_Details.User_Details.subjects.contains(txtSubj.getText().toString()))
                {imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_science, null));}
                else{imgSubj.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_filled_science, null));}
                break;
            default: break;
        }
    }

}