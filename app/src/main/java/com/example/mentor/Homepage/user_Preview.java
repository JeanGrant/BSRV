package com.example.mentor.Homepage;

import static androidx.core.content.res.ResourcesCompat.getColor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.R;
import com.example.mentor.adapters.CalendarAdapter;
import com.example.mentor.adapters.ProposalsAdapter;
import com.example.mentor.adapters.ReviewsAdapter;
import com.example.mentor.adapters.SubjectRatesAdapter;
import com.example.mentor.databinding.FragmentUserPreviewBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.Proposal;
import com.example.mentor.misc.ProposalListener;
import com.example.mentor.misc.Reviews;
import com.example.mentor.misc.SubjectRates;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class user_Preview extends Fragment implements CalendarAdapter.OnItemListener, ProposalListener {

    private FragmentUserPreviewBinding binding;
    private LocalDate selectedDate;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private final String fClicked = Account_Details.User_Clicked.getUID();
    private final String fUser = Account_Details.User_Details.getUID();
    private int hour, minute;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserPreviewBinding.inflate(inflater, container, false);
        View layoutView = binding.getRoot();

        initLayout();
        initLstSubj();
        selectedDate = LocalDate.now();
        setMonthView();

        binding.imgBTNBack.setOnClickListener(view -> {
            Bundle bundle = this.getArguments();

            if(bundle != null){
                if(bundle.getBoolean("isHome")) {
                    SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new search_Users(), "search_Users");
                } else {
                    SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new peoples(), "peoples");
                }
            }

        });
        binding.btnMinusMonth.setOnClickListener(view -> previousMonthAction());
        binding.btnPlusMonth.setOnClickListener(view -> nextMonthAction());
        binding.imgBTNProposals.setOnClickListener(view -> getProposalList());
        binding.imgBTNMinimize.setOnClickListener(view -> {
            if(binding.layoutProposal.getVisibility()==View.VISIBLE){
                if(binding.recyclerProposals.getVisibility()==View.VISIBLE){
                    binding.layoutProposal.setVisibility(View.GONE);
                    binding.layoutActionContainer.setVisibility(View.GONE);
                }
                else if (binding.layoutMainBody.getVisibility()==View.VISIBLE){
                    binding.layoutMainBody.setVisibility(View.GONE);
                    binding.layoutActionContainer.setVisibility(View.GONE);
                    binding.imgBTNDelete.setVisibility(View.GONE);
                    binding.imgNull.setVisibility(View.GONE);
                    binding.imgBTNAccept.setVisibility(View.GONE);
                    binding.recyclerProposals.setVisibility(View.VISIBLE);
                }
                else if (binding.layoutCounter.getVisibility()==View.VISIBLE) {
                    binding.layoutCounter.setVisibility(View.GONE);
                    binding.layoutActionContainer.setVisibility(View.GONE);
                    binding.recyclerProposals.setVisibility(View.VISIBLE);
                }
                else if (binding.txtPaymentProcedure.getVisibility()==View.VISIBLE){
                    binding.txtPaymentProcedure.setVisibility(View.GONE);
                    binding.layoutActionContainer.setVisibility(View.VISIBLE);
                    binding.layoutMainBody.setVisibility(View.VISIBLE);
                    binding.imgBTNDelete.setVisibility(View.VISIBLE);
                }
                else{
                    binding.layoutProposal.setVisibility(View.GONE);
                }
            }
            });
        binding.imgBTNAccept.setOnClickListener(view -> acceptProposal());
        binding.imgBTNDelete.setOnClickListener(view -> deleteProposal());
        binding.btnOfferCounter.setOnClickListener(view -> {
            if(binding.btnOfferCounter.getText().equals(getResources().getString(R.string.counterProposal))){
                binding.layoutMainBody.setVisibility(View.GONE);
                binding.imgBTNAccept.setVisibility(View.GONE);
                binding.imgNull.setVisibility(View.GONE);
                binding.imgBTNDelete.setVisibility(View.GONE);
                binding.layoutCounter.setVisibility(View.VISIBLE);
                binding.layoutActionContainer.setVisibility(View.VISIBLE);
                binding.btnOfferCounter.setText(R.string.Proceed);
                binding.btnTimeStartPicker.setText(binding.txtTime.getText().toString().substring(0,5));
                binding.btnTimeEndPicker.setText(binding.txtTime.getText().toString().substring(8,13));
                binding.btnDatePicker.setText(binding.txtDate.getText().toString());
            }else if(binding.btnOfferCounter.getText().equals(getResources().getString(R.string.Proceed))){
                addProposal();
                binding.layoutMainBody.setVisibility(View.VISIBLE);
                binding.imgBTNAccept.setVisibility(View.GONE);
                binding.imgNull.setVisibility(View.GONE);
                binding.imgBTNDelete.setVisibility(View.GONE);
                binding.layoutCounter.setVisibility(View.GONE);
                binding.layoutActionContainer.setVisibility(View.GONE);
                binding.btnOfferCounter.setText(R.string.counterProposal);
                getProposalList();
            }else if(binding.btnOfferCounter.getText().equals(getResources().getString(R.string.paymentProcedure))){
                binding.imgBTNDelete.setVisibility(View.GONE);
                binding.layoutMainBody.setVisibility(View.GONE);
                binding.layoutActionContainer.setVisibility(View.GONE);
                binding.txtPaymentProcedure.setVisibility(View.VISIBLE);
            }

        });
        binding.btnTimeStartPicker.setOnClickListener(view -> popTimePicker(binding.btnTimeStartPicker));
        binding.btnTimeEndPicker.setOnClickListener(view -> popTimePicker(binding.btnTimeEndPicker));
        binding.btnDatePicker.setOnClickListener(view -> popDatePicker(binding.btnDatePicker));
        binding.imgBTNFavorite.setOnClickListener(view -> updateFavorites());
        binding.imgBTNVisibility.setOnClickListener(view -> updateUserVisibility());

        return layoutView;
    }

    private void updateUserVisibility() {
        DocumentReference df = fStore.collection("Users").document(fUser);
        Map<String, Object> hideUsers = new HashMap<>();
        AtomicBoolean isHidden = new AtomicBoolean(false);

        df.get().addOnSuccessListener(documentSnapshot -> {

            if(documentSnapshot.get("hideUsers") != null){
                ArrayList<String> hideUsersUID = (ArrayList<String>) documentSnapshot.get("hideUsers");
                assert hideUsersUID != null;
                if(hideUsersUID.contains(fClicked)){
                    hideUsersUID.remove(fClicked);
                    isHidden.set(false);
                } else {
                    hideUsersUID.add(fClicked);
                    isHidden.set(true);
                }
                hideUsers.put("hideUsers", hideUsersUID);
                df.update(hideUsers);
            }
            else{
                ArrayList<String> hideUsersUID = new ArrayList<>();
                hideUsersUID.add(fClicked);
                isHidden.set(true);
                hideUsers.put("hideUsers", hideUsersUID);
                df.update(hideUsers);
            }

            DocumentReference df1 = fStore.collection("Users").document(fClicked);
            Map<String, Object> hideUsers1 = new HashMap<>();
            df1.get().addOnSuccessListener(documentSnapshot1 -> {
                if(documentSnapshot1.get("hideUsers") != null){
                    ArrayList<String> hideUsersUID = (ArrayList<String>) documentSnapshot.get("hideUsers");
                    assert hideUsersUID != null;
                    if(!hideUsersUID.contains(fClicked)){
                        hideUsersUID.add(fClicked);
                    }
                    hideUsers1.put("hideUsers", hideUsersUID);
                    df1.update(hideUsers1);
                }
                else{
                    ArrayList<String> hideUsersUID = new ArrayList<>();
                    hideUsersUID.add(fClicked);
                    hideUsers1.put("hideUsers", hideUsersUID);
                    df1.update(hideUsers1);
                }

                if(isHidden.get()){
                    binding.imgBTNVisibility.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_visibility_off_24));
                } else {
                    binding.imgBTNVisibility.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_visibility_24));
                }
            });

        });
    }

    private void updateFavorites() {
        DocumentReference df = fStore.collection("Users").document(fUser);
        Map<String, Object> followings = new HashMap<>();
        AtomicBoolean isFollowed = new AtomicBoolean(false);

        df.get().addOnSuccessListener(documentSnapshot -> {

            if(documentSnapshot.get("followings") != null){
                ArrayList<String> followingsUID = (ArrayList<String>) documentSnapshot.get("followings");
                assert followingsUID != null;
                if(followingsUID.contains(fClicked)){
                    followingsUID.remove(fClicked);
                    isFollowed.set(false);
                } else {
                    followingsUID.add(fClicked);
                    isFollowed.set(true);
                }
                followings.put("followings", followingsUID);
                df.update(followings);
            }
            else{
                ArrayList<String> followingsUID = new ArrayList<>();
                followingsUID.add(fClicked);
                isFollowed.set(true);
                followings.put("followings", followingsUID);
                df.update(followings);
            }

            if(isFollowed.get()){
                binding.imgBTNFavorite.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_bookmark_24));
            } else {
                binding.imgBTNFavorite.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_bookmark_border_24));
            }

        });
    }

    private void popDatePicker(Button btnDatePicker){

        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = makeDateString(day, month, year);
            btnDatePicker.setText(date);
        };

        DateTimeFormatter parser = DateTimeFormatter.ofPattern("d MMMM yyyy");
        LocalDate ldate = LocalDate.parse(binding.txtDate.getText().toString().trim(), parser);
        Date date = Date.from(ldate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        int year = Integer.parseInt(String.valueOf(DateFormat.format("yyyy", date)));
        int month = Integer.parseInt(String.valueOf(DateFormat.format("MM", date)));
        month = month - 1;
        int day = Integer.parseInt(String.valueOf(DateFormat.format("dd", date)));
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, onDateSetListener, year, month, day);

        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month){
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Error";

        }
    }

    private void popTimePicker(Button btnTimePicker){

        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, selectedHour, selectedMinute) -> {
            hour = selectedHour;
            minute = selectedMinute;
            btnTimePicker.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void addProposal() {

        DocumentReference df =  fStore.collection("Users").document(fClicked).collection("proposals").document(binding.txtRequestUID.getText().toString());
        df.get().addOnSuccessListener(documentSnapshot -> {

            Map<String,Object> requestInfo;
            requestInfo = new HashMap<>();

            Long status = documentSnapshot.getLong("status");
            assert status != null;
            if(Math.toIntExact(status) == 0){
                requestInfo.put("status", 1);
            }else if(Math.toIntExact(status) == 1){
                requestInfo.put("status", 0);
            }
            requestInfo.put("date", binding.btnDatePicker.getText().toString());
            requestInfo.put("startTime", binding.btnTimeStartPicker.getText().toString());
            requestInfo.put("endTime", binding.btnTimeEndPicker.getText().toString());

            df.update(requestInfo);

            DocumentReference dfUser = fStore.collection("Users").document(fUser).collection("proposals").document(binding.txtRequestUID.getText().toString());
            dfUser.update(requestInfo);

            binding.txtDate.setText(documentSnapshot.getString("date"));
            String time = documentSnapshot.getString("startTime") + " - " + documentSnapshot.getString("endTime");
            binding.txtTime.setText(documentSnapshot.getString(time));

            getProposalList();
        });
    }

    private void deleteProposal() {
        fStore.collection("Users").document(fClicked).collection("proposals")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    if (binding.txtRequestUID.getText().toString().equals(queryDocumentSnapshot.getId())) {
                        DocumentReference document = fStore.collection("Users").document(fClicked).collection("proposals").document(queryDocumentSnapshot.getId());
                        document.delete();
                        document = fStore.collection("Users").document(fUser).collection("proposals").document(queryDocumentSnapshot.getId());
                        document.delete();
                        binding.layoutProposal.setVisibility(View.GONE);
                    }
                }
            }
        });
        fStore.collection("Users").document(fUser).collection("proposals")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    //skip if current loaded user is the logged in user
                    if (binding.txtRequestUID.getText().toString().equals(queryDocumentSnapshot.getId())) {
                        DocumentReference document = fStore.collection("Users").document(fClicked).collection("proposals").document(queryDocumentSnapshot.getId());
                        document.delete();
                        binding.layoutProposal.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void acceptProposal() {
        fStore.collection("Users").document(fClicked).collection("proposals")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    //skip if current loaded user is the logged in user
                    if (binding.txtRequestUID.getText().toString().equals(queryDocumentSnapshot.getId())) {
                        Long reqStatus = queryDocumentSnapshot.getLong("status");
                        assert reqStatus != null;
                        if((reqStatus.intValue()==0 && fUser.equals(queryDocumentSnapshot.getString("requesteeUID"))) || (reqStatus.intValue()==1 && fUser.equals(queryDocumentSnapshot.getString("requestorUID")))){
                            DocumentReference df = fStore.collection("Users").document(fClicked).collection("proposals").document(queryDocumentSnapshot.getId());
                            df.update("status", 2);
                            df = fStore.collection("Users").document(fUser).collection("proposals").document(queryDocumentSnapshot.getId());
                            df.update("status", 2);
                            getProposalList();
                            binding.layoutMainBody.setVisibility(View.GONE);
                            binding.layoutActionContainer.setVisibility(View.GONE);
                            binding.recyclerProposals.setVisibility(View.VISIBLE);
                            binding.imgBTNDelete.setVisibility(View.GONE);
                            binding.imgBTNAccept.setVisibility(View.GONE);
                            binding.imgNull.setVisibility(View.GONE);

                        }
                    }
                }
            }
        });
    }

    private void getProposalList() {
        binding.layoutProposal.setVisibility(View.VISIBLE);
        binding.layoutMainBody.setVisibility(View.GONE);
        binding.layoutActionContainer.setVisibility(View.GONE);

        fStore.collection("Users").document(fClicked).collection("proposals").get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                List<Proposal> list_proposal = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    String requestorUID = queryDocumentSnapshot.getString("requestorUID");
                    String requesteeUID = queryDocumentSnapshot.getString("requesteeUID");
                    assert requestorUID != null;
                    assert requesteeUID != null;
                    if (!((requestorUID.equals(fClicked)&&requesteeUID.equals(fUser))||(requestorUID.equals(fUser)&&requesteeUID.equals(fClicked)))){
                        Log.i("skipProposal", requestorUID + " " + fClicked);
                        continue;
                    }
                    Proposal proposal = new Proposal();
                    Long statusLong = queryDocumentSnapshot.getLong("status");
                    assert statusLong != null;
                    proposal.status = Math.toIntExact(statusLong);
                    proposal.subject = queryDocumentSnapshot.getString("subject");
                    proposal.date = queryDocumentSnapshot.getString("date");
                    proposal.startTime = queryDocumentSnapshot.getString("startTime");
                    proposal.endTime = queryDocumentSnapshot.getString("endTime");
                    proposal.description = queryDocumentSnapshot.getString("description");
                    proposal.requesteeUID = queryDocumentSnapshot.getString("requesteeUID");
                    proposal.requestorUID = queryDocumentSnapshot.getString("requestorUID");
                    proposal.uid = queryDocumentSnapshot.getId();
                    list_proposal.add(proposal);

                }
                if(list_proposal.size()>0){

                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                    binding.recyclerProposals.setLayoutManager(mLinearLayoutManager);
                    ProposalsAdapter proposalsAdapter = new ProposalsAdapter(list_proposal, this);
                    binding.recyclerProposals.setAdapter(proposalsAdapter);
                    binding.recyclerProposals.setHasFixedSize(true);
                    binding.recyclerProposals.setVisibility(View.VISIBLE);
                }else{Toast.makeText(getContext(), "No proposals found", Toast.LENGTH_SHORT).show();}}
            else{
                Toast.makeText(getContext(), "Error getting proposal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMonthView() {
        binding.txtMonthView.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        binding.recyclerCalendar.setLayoutManager(layoutManager);
        binding.recyclerCalendar.setAdapter(calendarAdapter);
        binding.recyclerCalendar.setVisibility(View.VISIBLE);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i=1; i<=42; i++){
            if(i<=dayOfWeek || i>daysInMonth + dayOfWeek){
                daysInMonthArray.add("");
            }else{
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    private void initLayout() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.layoutPreviewHeader.setVisibility(View.INVISIBLE);
        binding.layoutMainContainer.setVisibility(View.INVISIBLE);
        fStore.collection("Users").document(fClicked).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                task.addOnSuccessListener(documentSnapshot -> {
                    Boolean isAccepting = Account_Details.User_Clicked.getIsAccepting();
                    if(isAccepting != null){
                        if(isAccepting){binding.progressBarStatus.setProgressTintList(AppCompatResources.getColorStateList(requireContext(),R.color.green));
                        }else{binding.progressBarStatus.setProgressTintList(AppCompatResources.getColorStateList(requireContext(),R.color.red));}
                    }
                    binding.progressBarStatus.setProgress(100,true);
                    binding.txtFullName.setText(Account_Details.User_Clicked.getFullName());
                    binding.txtEmail.setText(Account_Details.User_Clicked.getEmail());
                    Account_Details.User_Clicked.setBioEssay(documentSnapshot.getString("bioEssay"));
                    binding.txtBio.setText(Account_Details.User_Clicked.getBioEssay());

                    Account_Details.User_Clicked.rates = (Map<String, Long>) documentSnapshot.get("subjectRates");
                    Account_Details.User_Clicked.subjects = (ArrayList<String>) documentSnapshot.get("subjects");

                    if(documentSnapshot.getString("picString") != null) {
                        String picString = documentSnapshot.getString("picString");
                        if (picString != null && !picString.isEmpty() && !picString.equals("null")) {
                            Account_Details.User_Clicked.setPicString(picString);
                            byte[] bytes = Base64.decode(picString, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            binding.imgUserPic.setImageBitmap(bitmap);
                        }
                    }


                    initLstSubj();
                    selectedDate = LocalDate.now();
                    setMonthView();
                    getReviews();

                    fStore.collection("Users").document(fUser).get().addOnSuccessListener(documentSnapshot1 -> {
                        if(documentSnapshot1.get("followings") != null) {
                            ArrayList<String> followingsUID = (ArrayList<String>) documentSnapshot1.get("followings");
                            if(followingsUID != null) {
                                if (followingsUID.contains(fClicked)) {
                                    binding.imgBTNFavorite.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_bookmark_24));
                                } else {
                                    binding.imgBTNFavorite.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_bookmark_border_24));
                                }
                            }
                        } else { binding.imgBTNFavorite.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_bookmark_border_24)); }

                        if(documentSnapshot1.get("hideUsers") != null) {
                            ArrayList<String> hideUsersUID = (ArrayList<String>) documentSnapshot1.get("hideUsers");
                            if (hideUsersUID != null) {
                                if (hideUsersUID.contains(fClicked)) {
                                    binding.imgBTNVisibility.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_visibility_off_24));
                                } else {
                                    binding.imgBTNVisibility.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_visibility_24));
                                }
                            }
                        } else { binding.imgBTNVisibility.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_visibility_24)); }

                        binding.progressBar.setVisibility(View.GONE);
                        binding.layoutMainContainer.setVisibility(View.VISIBLE);
                        binding.layoutPreviewHeader.setVisibility(View.VISIBLE);
                    });
                });
            }
        });
    }

    private void initLstSubj() {
        Boolean isMentor = Account_Details.User_Clicked.getIsMentor();
        Log.i("isMentor", isMentor.toString());
        Map<String,Long> rates = Account_Details.User_Clicked.rates;
        ArrayList<String> subjects = Account_Details.User_Clicked.subjects;
        List<SubjectRates> list_subjrate = new ArrayList<>();
            for (int i = 0; i < subjects.size(); i++) {
                SubjectRates subjRates = new SubjectRates();
                switch (subjects.get(i)) {
                    case "Adobe Ps":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_adobe_ps, null);
                        subjRates.name = getResources().getString(R.string.AdobePs);
                        subjRates.hexColor = getColor(getResources(), R.color.AdobePsblue, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Adobe Ps")+"/hr";}
                        break;
                    case "Animation":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_animation, null);
                        subjRates.name = getResources().getString(R.string.Animation);
                        subjRates.hexColor = getColor(getResources(), R.color.AdobeAeViolet, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Animation")+"/hr";}
                        break;
                    case "Arts":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_arts, null);
                        subjRates.name = getResources().getString(R.string.Arts);
                        subjRates.hexColor = getColor(getResources(), R.color.ArtsPurple, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Arts")+"/hr";}
                        break;
                    case "AutoCAD":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_autocad, null);
                        subjRates.name = getResources().getString(R.string.AutoCAD);
                        subjRates.hexColor = getColor(getResources(), R.color.AutoCADRed, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("AutoCAD")+"/hr";}
                        break;
                    case "Engineering":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_engineering, null);
                        subjRates.name = getResources().getString(R.string.Engineering);
                        subjRates.hexColor = getColor(getResources(), R.color.EngineeringOrange, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Engineering")+"/hr";}
                        break;
                    case "Languages":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_languages, null);
                        subjRates.name = getResources().getString(R.string.Languages);
                        subjRates.hexColor = getColor(getResources(), R.color.LanguageGreen, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Languages")+"/hr";}
                        break;
                    case "Law":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_law, null);
                        subjRates.name = getResources().getString(R.string.Law);
                        subjRates.hexColor = getColor(getResources(), R.color.LawBlue, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Law")+"/hr";}
                        break;
                    case "MS Office":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_ms_office, null);
                        subjRates.name = getResources().getString(R.string.MSOffice);
                        subjRates.hexColor = getColor(getResources(), R.color.MSOfficeOrange, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("MS Office")+"/hr";}
                        break;
                    case "Mathematics":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_mathematics, null);
                        subjRates.name = getResources().getString(R.string.Mathematics);
                        subjRates.hexColor = getColor(getResources(), R.color.MathYellow, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Mathematics")+"/hr";}
                        break;
                    case "Programming":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_programming, null);
                        subjRates.name = getResources().getString(R.string.Programming);
                        subjRates.hexColor = getColor(getResources(), R.color.ProgrammingCyan, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Programming")+"/hr";}
                        break;
                    case "Sciences":
                        subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_science, null);
                        subjRates.name = getResources().getString(R.string.Sciences);
                        subjRates.hexColor = getColor(getResources(), R.color.ScienceGreen, null);
                        if(isMentor){subjRates.rate = "₱"+rates.get("Sciences")+"/hr";}
                        break;
                    default:
                        break;
                }
                list_subjrate.add(subjRates);
            }
        if(list_subjrate.size()>0){
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.recyclerSubjects.setLayoutManager(mLinearLayoutManager);
            binding.recyclerSubjects.setVisibility(View.VISIBLE);
            SubjectRatesAdapter subjectRatesAdapter = new SubjectRatesAdapter(list_subjrate);
            binding.recyclerSubjects.setAdapter(subjectRatesAdapter);
            binding.recyclerSubjects.setHasFixedSize(true);
        }
    }

    private void getReviews(){
        fStore.collection("Users").document(Account_Details.User_Clicked.getUID()).collection("rating").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().size()>0){
                    List<Reviews> list_reviews = new ArrayList<>();
                    Integer ratingSum = 0;
                    for(QueryDocumentSnapshot qDocSnap : task.getResult()){
                        Reviews reviews = new Reviews();
                        reviews.dateTime = qDocSnap.getString("dateTime");
                        reviews.fullName = qDocSnap.getString("rator");
                        reviews.comment = qDocSnap.getString("comment");
                        Long rating = qDocSnap.getLong("rating");
                        assert rating != null;
                        reviews.rating = rating.intValue();
                        list_reviews.add(reviews);
                        ratingSum += reviews.rating;
                    }
                    int ratingAve = ratingSum / list_reviews.size();
                    Log.i("list_reviews", String.valueOf(list_reviews.size()));
                    if(list_reviews.size()>0){
                        String rating = ratingAve+".0";
                        binding.txtRating.setText(rating);
                        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                        binding.recyclerReviews.setLayoutManager(mLinearLayoutManager);
                        binding.recyclerReviews.setVisibility(View.VISIBLE);
                        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(list_reviews);
                        binding.recyclerReviews.setAdapter(reviewsAdapter);
                        binding.recyclerReviews.setHasFixedSize(true);
                    }else {
                        if(Account_Details.User_Clicked.getIsMentor()) {
                            binding.txtRating.setText(R.string.noRatings);
                        }else{
                            binding.layoutRating.setVisibility(View.GONE);
                        }
                        binding.txtReviewsPrompt.setVisibility(View.GONE);
                        binding.recyclerReviews.setVisibility(View.GONE);

                        binding.progressBar.setVisibility(View.GONE);
                        binding.layoutHeader.setVisibility(View.VISIBLE);
                        binding.txtBio.setVisibility(View.VISIBLE);
                        binding.cardSecondary.setVisibility(View.VISIBLE);
                    }
                }  else {
                    if(Account_Details.User_Clicked.getIsMentor()) {
                        binding.txtRating.setText(R.string.noRatings);
                    }else{
                        binding.layoutRating.setVisibility(View.GONE);
                    }
                    binding.txtReviewsPrompt.setVisibility(View.GONE);
                    binding.recyclerReviews.setVisibility(View.GONE);

                    binding.progressBar.setVisibility(View.GONE);
                    binding.layoutHeader.setVisibility(View.VISIBLE);
                    binding.txtBio.setVisibility(View.VISIBLE);
                    binding.cardSecondary.setVisibility(View.VISIBLE);
                }
            }  else { Toast.makeText(requireContext(), "Error retrieving data", Toast.LENGTH_SHORT).show(); }
        });
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals("")){
            DateTimeFormatter MY = DateTimeFormatter.ofPattern("MMMM yyyy");
            String selectedMY = MY.format(selectedDate);
            DateTimeFormatter dMY = DateTimeFormatter.ofPattern("d MMMM yyyy");
            LocalDate selectedDay = LocalDate.parse(dayText + " " + selectedMY, dMY);
            Log.i("selectedDay", selectedDay.toString());
            Account_Details.User_Details.setSetDate(selectedDay);

            Bundle bundleOld = this.getArguments();

            Boolean isHome = true;
            if(bundleOld != null) {
                isHome = bundleOld.getBoolean("isHome");
            }

            Bundle bundle = new Bundle();
            bundle.putBoolean("isHome", isHome);
            bundle.putBoolean("isPreview",true);

            Fragment fragment2 = new dailyTime_preview();
            fragment2.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment2, "dailyTime_preview")
                    .commit();
        }
    }

    private void previousMonthAction(){
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    private void nextMonthAction(){
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onUserClicked(Proposal proposal) {

        String request_status;
        switch (proposal.status){
            case 0:
                request_status = "Waiting for Mentor's Confirmation";
                break;
            case 1:
                request_status = "Waiting for Student's Confirmation";
                break;
            case 2:
                request_status = "Waiting for Payment";
                break;
            case 3:
                request_status = "Kindly re-Log In to Confirm Payment";
                break;
            case 4:
                request_status = "Cleared Proposal";
                break;
            default:
                request_status = "Error";
                break;
        }
        binding.txtReqStatus.setText(request_status);

        binding.txtRequestUID.setText(proposal.uid);
        binding.txtSubject.setText(proposal.subject);
        binding.txtDate.setText(proposal.date);
        String time = proposal.startTime+" - "+proposal.endTime;
        binding.txtTime.setText(time);
        binding.txtDesc.setText(proposal.description);
        binding.recyclerProposals.setVisibility(View.GONE);
        binding.layoutMainBody.setVisibility(View.VISIBLE);
        if(fUser.equals(proposal.requesteeUID)){
            if(proposal.status==0){
                binding.layoutActionContainer.setVisibility(View.VISIBLE);
                binding.btnOfferCounter.setText(R.string.counterProposal);
                binding.imgBTNAccept.setVisibility(View.VISIBLE);
                binding.imgNull.setVisibility(View.VISIBLE);
                binding.imgBTNDelete.setVisibility(View.VISIBLE);
            } else if(proposal.status==1){
                binding.imgBTNDelete.setVisibility(View.VISIBLE);
                binding.layoutActionContainer.setVisibility(View.GONE);
                binding.imgBTNAccept.setVisibility(View.GONE);
                binding.imgNull.setVisibility(View.GONE);
            }
            else {
                binding.layoutActionContainer.setVisibility(View.GONE);
                binding.imgBTNAccept.setVisibility(View.GONE);
                binding.imgNull.setVisibility(View.GONE);
            }
        }else if (fUser.equals(proposal.requestorUID)){
            if(proposal.status==0){
                binding.imgBTNDelete.setVisibility(View.VISIBLE);
                binding.layoutActionContainer.setVisibility(View.GONE);
                binding.imgBTNAccept.setVisibility(View.GONE);
                binding.imgNull.setVisibility(View.GONE);
            }
            else if(proposal.status==1){
                binding.layoutActionContainer.setVisibility(View.VISIBLE);
                binding.btnOfferCounter.setText(R.string.counterProposal);
                binding.imgBTNAccept.setVisibility(View.VISIBLE);
                binding.imgNull.setVisibility(View.VISIBLE);
                binding.imgBTNDelete.setVisibility(View.VISIBLE);
            }else if(proposal.status==2) {
                binding.layoutActionContainer.setVisibility(View.VISIBLE);
                binding.btnOfferCounter.setText(R.string.paymentProcedure);
                binding.imgBTNAccept.setVisibility(View.GONE);
                binding.imgNull.setVisibility(View.GONE);
                binding.imgBTNDelete.setVisibility(View.VISIBLE);
            }
            else {
                binding.layoutActionContainer.setVisibility(View.GONE);
                binding.imgBTNAccept.setVisibility(View.GONE);
                binding.imgNull.setVisibility(View.GONE);
            }
        }
    }
}