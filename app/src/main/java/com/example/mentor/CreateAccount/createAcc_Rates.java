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

public class createAcc_Rates extends Fragment {

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
        binding.imgBTNBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new createAcc_Subjects()).commit());
        binding.btnProceed.setOnClickListener(view -> {

            ArrayList<String> subjects = Account_Details.User_Details.getSubjects();
            for (int i = 0; i < subjects.size(); i++) {
                if(isConstRates){if(String.valueOf(binding.inpTXTConstRates.getText()).isEmpty()){isRateEmpty=true;break;}else{isRateEmpty=false;}}

                switch (subjects.get(i)) {
                    case "Adobe Ps":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTAdobePs.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTAdobePs.getText())));}}
                        break;
                    case "Animation":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTAnimation.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTAnimation.getText())));}}
                        break;
                    case "Arts":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTArts.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTArts.getText())));}}
                        break;
                    case "AutoCAD":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTAutoCAD.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTAutoCAD.getText())));}}
                        break;
                    case "Engineering":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTEngineering.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTEngineering.getText())));}}
                        break;
                    case "Languages":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTLanguages.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTLanguages.getText())));}}
                        break;
                    case "Law":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTLaw.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTLaw.getText())));}}
                        break;
                    case "MS Office":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTMSOffice.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTMSOffice.getText())));}}
                        break;
                    case "Mathematics":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTMathematics.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTMathematics.getText())));}}
                        break;
                    case "Programming":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTProgramming.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTProgramming.getText())));}}
                        break;
                    case "Sciences":
                        if(isConstRates){rates.add(Long.parseLong(String.valueOf(binding.inpTXTConstRates.getText())));}
                        else{if(String.valueOf(binding.inpTXTSciences.getText()).isEmpty()){isRateEmpty=true;}
                        else{isRateEmpty=false;rates.add(Long.parseLong(String.valueOf(binding.inpTXTSciences.getText())));}}
                        break;
                    default:
                        break;
                }
            }

            if(!isRateEmpty){
                Log.i("CreateAcc_Rates:", rates.toString());
                Account_Details.User_Details.setRates(rates);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new createAcc_Finalize()).commit();
            }else{
                Toast.makeText(getContext(), "Kindly specify your hourly rate", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void initLstSubj() {
        ArrayList<String> subjects = Account_Details.User_Details.subjects;
        for (int i = 0; i < subjects.size(); i++) {
            switch (subjects.get(i)) {
                case "Adobe Ps":
                    binding.layoutAdobePs.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTAdobePs.setVisibility(View.GONE);}else{binding.inpTXTAdobePs.setVisibility(View.VISIBLE);}
                    break;
                case "Animation":
                    binding.layoutAnimation.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTAnimation.setVisibility(View.GONE);}else{binding.inpTXTAnimation.setVisibility(View.VISIBLE);}
                    break;
                case "Arts":
                    binding.layoutArts.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTArts.setVisibility(View.GONE);}else{binding.inpTXTArts.setVisibility(View.VISIBLE);}
                    break;
                case "AutoCAD":
                    binding.layoutAutoCAD.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTAutoCAD.setVisibility(View.GONE);}else{binding.inpTXTAutoCAD.setVisibility(View.VISIBLE);}
                    break;
                case "Engineering":
                    binding.layoutEngineering.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTEngineering.setVisibility(View.GONE);}else{binding.inpTXTEngineering.setVisibility(View.VISIBLE);}
                    break;
                case "Languages":
                    binding.layoutLanguages.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTLanguages.setVisibility(View.GONE);}else{binding.inpTXTLanguages.setVisibility(View.VISIBLE);}
                    break;
                case "Law":
                    binding.layoutLaw.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTLaw.setVisibility(View.GONE);}else{binding.inpTXTLaw.setVisibility(View.VISIBLE);}
                    break;
                case "MS Office":
                    binding.layoutMSOffice.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTMSOffice.setVisibility(View.GONE);}else{binding.inpTXTMSOffice.setVisibility(View.VISIBLE);}
                    break;
                case "Mathematics":
                    binding.layoutMathematics.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTMathematics.setVisibility(View.GONE);}else{binding.inpTXTMathematics.setVisibility(View.VISIBLE);}
                    break;
                case "Programming":
                    binding.layoutProgramming.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTProgramming.setVisibility(View.GONE);}else{binding.inpTXTProgramming.setVisibility(View.VISIBLE);}
                    break;
                case "Sciences":
                    binding.layoutSciences.setVisibility(View.VISIBLE);
                    if(isConstRates){binding.inpTXTSciences.setVisibility(View.GONE);}else{binding.inpTXTSciences.setVisibility(View.VISIBLE);}
                    break;
                default:
                    break;
            }
        }
    }
}