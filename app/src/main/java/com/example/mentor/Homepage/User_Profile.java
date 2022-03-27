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

import java.util.ArrayList;

public class User_Profile extends Fragment {

    View view;
    FragmentUserProfileBinding binding;
    ArrayList<Long> rates;
    Boolean isMentor;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        isMentor = Account_Details.User_Details.getIsMentor();

        initLayout();
        return view;
    }

    public Boolean initLayout(){

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

        binding.txtFullName.setText(Account_Details.User_Details.getFullName());
        binding.txtEmail.setText(Account_Details.User_Details.getEmail());
        binding.txtBio.setText(Account_Details.User_Details.getBioEssay());
        binding.txtFBUser.setText(Account_Details.User_Details.getFbUser());
        binding.txtLInUser.setText(Account_Details.User_Details.getlInUser());
        rates = new ArrayList<>(Account_Details.User_Details.rates);
        initLstSubj();


        return true;
    }
    public void initLstSubj() {
        Integer subjects = Account_Details.User_Details.getSubjects();
        for (int i = 0; i < 11; i++) {
            if ((subjects & (1 << i)) > 0) {
                switch (i) {
                    case 0:
                        binding.layoutAdobePs.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesAdobePs.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesAdobePs.setVisibility(View.GONE);}
                        break;
                    case 1:
                        binding.layoutAnimation.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesAnimation.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesAnimation.setVisibility(View.GONE);}
                        break;
                    case 2:
                        binding.layoutArts.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesArts.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesArts.setVisibility(View.GONE);}
                        break;
                    case 3:
                        binding.layoutAutoCAD.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesAutoCAD.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesAutoCAD.setVisibility(View.GONE);}
                        break;
                    case 4:
                        binding.layoutProgramming.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesProgramming.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesProgramming.setVisibility(View.GONE);}
                        break;
                    case 5:
                        binding.layoutMSOffice.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesMSOffice.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesMSOffice.setVisibility(View.GONE);}
                        break;
                    case 6:
                        binding.layoutMathematics.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesMathematics.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesMathematics.setVisibility(View.GONE);}
                        break;
                    case 7:
                        binding.layoutSciences.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesSciences.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesSciences.setVisibility(View.GONE);}
                        break;
                    case 8:
                        binding.layoutLanguages.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesLanguages.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesLanguages.setVisibility(View.GONE);}
                        break;
                    case 9:
                        binding.layoutLaw.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesLaw.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesLaw.setVisibility(View.GONE);}
                        break;
                    case 10:
                        binding.layoutEngineering.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesEngineering.setText("₱"+rates.get(i)+"/hr");}else{binding.txtRatesEngineering.setVisibility(View.GONE);}
                        break;
                    default:
                        break;
                }
            }
        }
    }
}