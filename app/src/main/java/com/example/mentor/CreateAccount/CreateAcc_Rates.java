package com.example.mentor.CreateAccount;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccRatesBinding;
import com.example.mentor.misc.Account_Details;

import java.util.ArrayList;

public class CreateAcc_Rates extends Fragment {

    View view;
    FragmentCreateAccRatesBinding binding;
    Boolean isConstRates = true, isRateEmpty = true;
    ArrayList<Long> rates = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccRatesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) binding.btnProceed.getLayoutParams();
        initLstSubj();
        binding.inpTXTLayoutConstRates.setVisibility(View.VISIBLE);
        params.addRule(RelativeLayout.BELOW, R.id.inpTXTLayout_ConstRates);
        binding.btnProceed.setLayoutParams(params);

        binding.rdbConstRates.setOnClickListener(view -> {isConstRates = true; initLstSubj(); binding.inpTXTLayoutConstRates.setVisibility(View.VISIBLE); params.addRule(RelativeLayout.BELOW, R.id.inpTXTLayout_ConstRates); binding.btnProceed.setLayoutParams(params);});
        binding.rdbAltRates.setOnClickListener(view -> {isConstRates = false; initLstSubj(); binding.inpTXTLayoutConstRates.setVisibility(View.GONE); params.addRule(RelativeLayout.BELOW, R.id.layout_SubjRates); binding.btnProceed.setLayoutParams(params);});
        binding.imgBTNBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Subjects()).commit());
        binding.btnProceed.setOnClickListener(view -> {

            Integer subjects = Account_Details.User_Details.getSubjects();
            for (int i = 0; i < 11; i++) {
                if(isConstRates){if(binding.inpTXTConstRates.getText().toString().isEmpty()){isRateEmpty=true;break;}else{isRateEmpty=false;}}

                switch (i) {
                    case 0:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTAdobePsRate.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTAdobePsRate.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 1:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTAnimationRate.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTAnimationRate.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 2:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTArts.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTArts.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 3:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTAutoCAD.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTAutoCAD.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 4:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTProgramming.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTProgramming.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 5:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTMSOffice.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTMSOffice.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 6:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTMathematics.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTMathematics.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 7:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTSciences.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTSciences.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 8:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTLanguages.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTLanguages.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 9:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTLaw.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTLaw.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    case 10:
                        if ((subjects & (1 << i)) > 0) {
                            if(isConstRates){rates.add(Long.parseLong(binding.inpTXTConstRates.getText().toString()));}
                            else{if(binding.inpTXTEngineering.getText().toString().isEmpty()){isRateEmpty=true;}
                            else{isRateEmpty=false;rates.add(Long.parseLong(binding.inpTXTEngineering.getText().toString()));}}}
                        else {rates.add(0L);}
                        break;
                    default:
                        break;
                }
            }

            if(!isRateEmpty){
                Log.i("CreateAcc_Rates:", rates.toString());
                Account_Details.User_Details.setRates(rates);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CreateAcc_Finalize()).commit();
            }else{
                Toast.makeText(getContext(), "Kindly specify your hourly rate", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void initLstSubj() {
        Integer subjects = Account_Details.User_Details.getSubjects();
        for (int i = 0; i < 11; i++) {
            if ((subjects & (1 << i)) > 0) {
                switch (i) {
                    case 0:
                        binding.layoutAdobePs.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTAdobePsRate.setVisibility(View.GONE);}else{binding.inpTXTAdobePsRate.setVisibility(View.VISIBLE);}
                        break;
                    case 1:
                        binding.layoutAnimation.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTAnimationRate.setVisibility(View.GONE);}else{binding.inpTXTAnimationRate.setVisibility(View.VISIBLE);}
                        break;
                    case 2:
                        binding.layoutArts.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTArts.setVisibility(View.GONE);}else{binding.inpTXTArts.setVisibility(View.VISIBLE);}
                        break;
                    case 3:
                        binding.layoutAutoCAD.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTAutoCAD.setVisibility(View.GONE);}else{binding.inpTXTAutoCAD.setVisibility(View.VISIBLE);}
                        break;
                    case 4:
                        binding.layoutProgramming.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTProgramming.setVisibility(View.GONE);}else{binding.inpTXTProgramming.setVisibility(View.VISIBLE);}
                        break;
                    case 5:
                        binding.layoutMSOffice.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTMSOffice.setVisibility(View.GONE);}else{binding.inpTXTMSOffice.setVisibility(View.VISIBLE);}
                        break;
                    case 6:
                        binding.layoutMathematics.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTMathematics.setVisibility(View.GONE);}else{binding.inpTXTMathematics.setVisibility(View.VISIBLE);}
                        break;
                    case 7:
                        binding.layoutSciences.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTSciences.setVisibility(View.GONE);}else{binding.inpTXTSciences.setVisibility(View.VISIBLE);}
                        break;
                    case 8:
                        binding.layoutLanguages.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTLanguages.setVisibility(View.GONE);}else{binding.inpTXTLanguages.setVisibility(View.VISIBLE);}
                        break;
                    case 9:
                        binding.layoutLaw.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTLaw.setVisibility(View.GONE);}else{binding.inpTXTLaw.setVisibility(View.VISIBLE);}
                        break;
                    case 10:
                        binding.layoutEngineering.setVisibility(View.VISIBLE);
                        if(isConstRates){binding.inpTXTEngineering.setVisibility(View.GONE);}else{binding.inpTXTEngineering.setVisibility(View.VISIBLE);}
                        break;
                    default:
                        break;
                }
            }
        }
    }
}