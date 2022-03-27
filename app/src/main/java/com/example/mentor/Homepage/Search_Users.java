package com.example.mentor.Homepage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mentor.CreateAccount.CreateAcc_Rates;
import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.databinding.FragmentSearchUsersBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;
import com.example.mentor.user_preview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Search_Users extends Fragment implements UserListener, AdapterView.OnItemSelectedListener {

    View view;
    FragmentSearchUsersBinding binding;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DatePickerDialog datePickerDialog;
    private Integer maxTime = 6;
    private GridLayoutManager mGridLayoutManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchUsersBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.cardUserPreview.setVisibility(View.GONE);
        getUsers();
        initSwitches();
        initDatePicker();
        binding.btnDatePicker.setText(getTodayDate());

        //region Spinner Initialization
        // Spinner click listener
        binding.drpSubject.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<>(Account_Details.User_Details.initLstSubj());
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        binding.drpSubject.setAdapter(dataAdapter);
        //endregion

        binding.btnProposals.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(false);
            getUsers();
            initSwitches();
        });
        binding.btnSendProposal.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(true);
            getUsers();
            initSwitches();
        });

        binding.imgBTNBack.setOnClickListener(view -> binding.cardUserPreview.setVisibility(View.GONE));

        binding.imgBTNRequest.setOnClickListener(view -> {
            if(Account_Details.User_Clicked.getIsAccepting()) {
                //region update receiving user list of received requests
                DocumentReference df = fStore.collection("Users").document(Account_Details.User_Clicked.getUID());
                Map<String, Object> userInfo = new HashMap<>();
                ArrayList<String> addRequest = new ArrayList<>(Account_Details.User_Clicked.requests);
                if(addRequest.contains(Account_Details.User_Details.getUID())){
                    Toast.makeText(getContext(), "Request Failed: You have already sent an application", Toast.LENGTH_SHORT).show();
                }else if(!addRequest.contains(Account_Details.User_Details.getUID())){
                    addRequest.add(Account_Details.User_Details.getUID());
                    Toast.makeText(getContext(), "Request Success: Mentorship Application sent", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Request Failed: Unknown Error", Toast.LENGTH_SHORT).show();
                }
                userInfo.put("userRequests", addRequest);
                df.update(userInfo);
                initPreview();
                //endregion
                //region update sending user list of sent requests
                df = fStore.collection("Users").document(Account_Details.User_Details.getUID());
                userInfo = new HashMap<>();
                addRequest = new ArrayList<>(Account_Details.User_Details.requests);
                userInfo.put("userRequests", addRequest);
                df.update(userInfo);
                getUsers();
                //endregion
            } else {
                Toast.makeText(getContext(), "Request Failed: User is no longer accepting mentorship", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDatePicker.setOnClickListener(view -> openDatePicker());

        return view;
    }

    private void getUsers(){
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        fStore.collection("Users").get().addOnCompleteListener(task -> {
            String currentUserId = FirebaseAuth.getInstance().getUid();
            if(task.isSuccessful() && task.getResult() != null){
                List<User> list_users = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    assert currentUserId != null;
                    //skip if current loaded user is the logged in user
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    //skip if current loaded user's account type is the same as logged in user
                    if (queryDocumentSnapshot.getBoolean("isMentor") == Account_Details.User_Details.getIsMentor()){
                        continue;
                    }
                    User user = new User();
                    user.fullName = queryDocumentSnapshot.getString("FullName");
                    user.email = queryDocumentSnapshot.getString("Email");
                    user.pictureStr= queryDocumentSnapshot.getString("Picture");
                    user.authLvl = Objects.requireNonNull(queryDocumentSnapshot.getLong("AuthLevel")).intValue();
                    user.bioEssay = queryDocumentSnapshot.getString("bioEssay");
                    user.fbUsername = queryDocumentSnapshot.getString("FB_Username");
                    user.lInUsername = queryDocumentSnapshot.getString("LinkedIn_Username");
                    user.isAccepting = queryDocumentSnapshot.getBoolean("isAccepting");
                    user.isMentor = queryDocumentSnapshot.getBoolean("isMentor");
                    user.subjectsBinary = Objects.requireNonNull(queryDocumentSnapshot.getLong("subjectsBinary")).intValue();
                    user.uid = queryDocumentSnapshot.getId();
                    if(queryDocumentSnapshot.get("userRequests") == null){
                        user.requests.clear();
                    } else{
                        user.requests = (ArrayList<String>) queryDocumentSnapshot.get("userRequests");
                    }
                    if(queryDocumentSnapshot.get("SubjectRates") == null){
                        user.rates.clear();
                    } else{
                        user.rates = (ArrayList<Long>) queryDocumentSnapshot.get("SubjectRates");
                    }
                    list_users.add(user);
                }
                if(list_users.size()>0){
                    binding.progressBar.setVisibility(View.GONE);
                    binding.recyclerUsers.setVisibility(View.VISIBLE);
                    UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                    binding.recyclerUsers.setAdapter(usersAdapter);
                    binding.recyclerUsers.setHasFixedSize(true);
                } else{
                    Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Error getting list of users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUserClicked(User user) {
        Account_Details.User_Clicked.setUID(user.uid);
        Account_Details.User_Clicked.setPicString(user.pictureStr);
        Account_Details.User_Clicked.setFullName(user.fullName);
        Account_Details.User_Clicked.setEmail(user.email);
        Account_Details.User_Clicked.setIsMentor(user.isMentor);
        Account_Details.User_Clicked.setIsAccepting(user.isAccepting);
        Account_Details.User_Clicked.setBioEssay(user.bioEssay);
        Account_Details.User_Clicked.setAuthLevel(user.authLvl);
        Account_Details.User_Clicked.setFBUser(user.fbUsername);
        Account_Details.User_Clicked.setLInUser(user.lInUsername);
        Account_Details.User_Clicked.setSubjects(user.subjectsBinary);
        Account_Details.User_Clicked.rates = user.rates;
        Account_Details.User_Clicked.requests = user.requests;
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new user_preview()).commit();
        initPreview();
    }

    public void initPreview(){

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
    }
    public void initLstSubj() {
        Boolean isMentor = Account_Details.User_Clicked.getIsMentor();
        ArrayList<Long> rates = Account_Details.User_Clicked.rates;
        Integer subjects = Account_Details.User_Clicked.getSubjects();
        String subjRate;
        for (int i = 0; i < 11; i++) {
            if ((subjects & (1 << i)) > 0) {
                subjRate = "₱"+rates.get(i)+"/hr";
                switch (i) {
                    case 0:
                        binding.layoutAdobePs.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesAdobePs.setText(subjRate);}else{binding.txtRatesAdobePs.setVisibility(View.GONE);}
                        break;
                    case 1:
                        binding.layoutAnimation.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesAnimation.setText(subjRate);}else{binding.txtRatesAnimation.setVisibility(View.GONE);}
                        break;
                    case 2:
                        binding.layoutArts.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesArts.setText(subjRate);}else{binding.txtRatesArts.setVisibility(View.GONE);}
                        break;
                    case 3:
                        binding.layoutAutoCAD.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesAutoCAD.setText(subjRate);}else{binding.txtRatesAutoCAD.setVisibility(View.GONE);}
                        break;
                    case 4:
                        binding.layoutProgramming.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesProgramming.setText(subjRate);}else{binding.txtRatesProgramming.setVisibility(View.GONE);}
                        break;
                    case 5:
                        binding.layoutMSOffice.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesMSOffice.setText(subjRate);}else{binding.txtRatesMSOffice.setVisibility(View.GONE);}
                        break;
                    case 6:
                        binding.layoutMathematics.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesMathematics.setText(subjRate);}else{binding.txtRatesMathematics.setVisibility(View.GONE);}
                        break;
                    case 7:
                        binding.layoutSciences.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesSciences.setText(subjRate);}else{binding.txtRatesSciences.setVisibility(View.GONE);}
                        break;
                    case 8:
                        binding.layoutLanguages.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesLanguages.setText(subjRate);}else{binding.txtRatesLanguages.setVisibility(View.GONE);}
                        break;
                    case 9:
                        binding.layoutLaw.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesLaw.setText(subjRate);}else{binding.txtRatesLaw.setVisibility(View.GONE);}
                        break;
                    case 10:
                        binding.layoutEngineering.setVisibility(View.VISIBLE);
                        if(isMentor){binding.txtRatesEngineering.setText(subjRate);}else{binding.txtRatesEngineering.setVisibility(View.GONE);}
                        break;
                    default:
                        break;
                }
            }
        }
    }
    public void initSwitches() {
        if(Account_Details.User_Details.getCurrSearch()){
            binding.btnSendProposal.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnProposals.setBackgroundColor(this.requireContext().getColor(R.color.white));
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        }else {
            binding.btnProposals.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnSendProposal.setBackgroundColor(this.requireContext().getColor(R.color.white));
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.blue));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            binding.btnDatePicker.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);

        if(dayWeek>1){maxTime += dayWeek;}

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()+(1000*60*60*24*maxTime));
    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month){
        switch(month){
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "ERR";
        }
    }

    public void openDatePicker(){
        datePickerDialog.show();
    }

    private String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }
}