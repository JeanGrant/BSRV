package com.example.mentor;

import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mentor.adapters.SubjectRatesAdapter;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.databinding.FragmentSearchUsersBinding;
import com.example.mentor.databinding.FragmentUserPreviewBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.SubjectRates;
import com.example.mentor.misc.User;

import java.util.ArrayList;
import java.util.List;

public class user_preview extends Fragment {

    FragmentUserPreviewBinding binding;
    View view;
    private GridLayoutManager mGridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = FragmentUserPreviewBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        Boolean isAccepting = Account_Details.User_Clicked.getIsAccepting();
        if(isAccepting != null){
            if(isAccepting){
                binding.txtStatus.setText(R.string.CurrentlyAccepting);
                binding.imgBTNRequest.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_person_add_24));
            } else{
                binding.txtStatus.setText(R.string.NoLongerAccepting);
                binding.imgBTNRequest.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_person_add_24_disabled));
            } }

        switch (Account_Details.User_Clicked.getAuthLevel()){
            case(0):
                binding.txtAuthLVL.setText(R.string.AuthLVL_0);
                break;
            case(1):
                binding.txtAuthLVL.setText(R.string.AuthLVL_1);
                break;
            case(2):
                binding.txtAuthLVL.setText(R.string.AuthLVL_2);
        }

        if(Account_Details.User_Clicked.requests.contains(Account_Details.User_Details.getUID())){
            binding.imgBTNRequest.setVisibility(View.GONE);
        } else {
            binding.imgBTNRequest.setVisibility(View.VISIBLE);
        }
        binding.txtFullName.setText(Account_Details.User_Clicked.getFullName());
        binding.txtEmail.setText(Account_Details.User_Clicked.getEmail());
        binding.txtBio.setText(Account_Details.User_Clicked.getBioEssay());
        binding.txtFBUser.setText(Account_Details.User_Clicked.getFbUser());
        binding.txtLInUser.setText(Account_Details.User_Clicked.getlInUser());
        binding.cardUserPreview.setVisibility(View.VISIBLE);
        Log.i("Search_Users before initLstSubj", Account_Details.User_Clicked.rates.toString());
        initLstSubj();

        return view;
    }

    public void initLstSubj() {
        Boolean isMentor = Account_Details.User_Clicked.getIsMentor();
        ArrayList<Long> rates = Account_Details.User_Clicked.rates;
        Integer subjects = Account_Details.User_Clicked.getSubjects();
        List<SubjectRates> list_subjrate = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            SubjectRates subjRates = new SubjectRates();
            if ((subjects & (1 << i)) > 0) {
                subjRates.rate = "₱"+rates.get(i)+"/hr";
                switch (i) {
                    case 0:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_adobe_ps, null);
                        subjRates.name = getResources().getString(R.string.AdobePs);
                        subjRates.red = 37;
                        subjRates.green = 93;
                        subjRates.blue = 170;
                        Log.i("subjRates wawa", i+subjRates.name);
                        break;
                    case 1:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_animation, null);
                        subjRates.name = getResources().getString(R.string.Animation);
                        subjRates.red = 52;
                        subjRates.green = 34;
                        subjRates.blue = 76;
                        Log.i("subjRates wawa", i+subjRates.name);
                        break;
                    case 2:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_arts, null);
                        subjRates.name = getResources().getString(R.string.Arts);
                        subjRates.red = 127;
                        subjRates.green = 38;
                        subjRates.blue = 175;
                        break;
                    case 3:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_autocad, null);
                        subjRates.name = getResources().getString(R.string.AutoCAD);
                        subjRates.red = 127;
                        subjRates.green = 18;
                        subjRates.blue = 11;
                        break;
                    case 4:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_programming, null);
                        subjRates.name = getResources().getString(R.string.Programming);
                        subjRates.red = 43;
                        subjRates.green = 160;
                        subjRates.blue = 189;
                        break;
                    case 5:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_ms_office, null);
                        subjRates.name = getResources().getString(R.string.Microsoft);
                        subjRates.red = 184;
                        subjRates.green = 41;
                        subjRates.blue = 22;
                        break;
                    case 6:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_mathematics, null);
                        subjRates.name = getResources().getString(R.string.Mathematics);
                        subjRates.red = 189;
                        subjRates.green = 143;
                        subjRates.blue = 6;
                        break;
                    case 7:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_science, null);
                        subjRates.name = getResources().getString(R.string.Sciences);
                        subjRates.red = 24;
                        subjRates.green = 134;
                        subjRates.blue = 55;
                        break;
                    case 8:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_languages, null);
                        subjRates.name = getResources().getString(R.string.Languages);
                        subjRates.red = 107;
                        subjRates.green = 134;
                        subjRates.blue = 34;
                        break;
                    case 9:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_law, null);
                        subjRates.name = getResources().getString(R.string.Law);
                        subjRates.red = 3;
                        subjRates.green = 62;
                        subjRates.blue = 88;
                        break;
                    case 10:
                        subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_engineering, null);
                        subjRates.name = getResources().getString(R.string.Engineering);
                        subjRates.red = 173;
                        subjRates.green = 79;
                        subjRates.blue = 50;
                        break;
                    default:
                        break;
                }
                list_subjrate.add(subjRates);
                Log.i("subjRates wawa", list_subjrate.toString());
            }
        }
        if(list_subjrate.size()>0){
//            binding.progressBar.setVisibility(View.GONE);
            mGridLayoutManager = new GridLayoutManager(getContext(), 2);
            binding.recylcerSubjects.setLayoutManager(mGridLayoutManager);
            binding.recylcerSubjects.setVisibility(View.VISIBLE);
            SubjectRatesAdapter subjectRatesAdapter = new SubjectRatesAdapter(list_subjrate);
            binding.recylcerSubjects.setAdapter(subjectRatesAdapter);
            binding.recylcerSubjects.setHasFixedSize(true);
        } else{
            Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
        }
    }
}