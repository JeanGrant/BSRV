package com.example.mentor.Homepage;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mentor.R;
import com.example.mentor.adapters.DailyTimeAdapter;
import com.example.mentor.databinding.FragmentDailyTimePreviewBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.ClickedUser_Schedule;
import com.example.mentor.misc.User;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class dailyTime_preview extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentDailyTimePreviewBinding binding;
    private int hour, minute;
    private FirebaseFirestore fStore;
    private final String fUser = Account_Details.User_Details.getUID();
    private final String fClicked = Account_Details.User_Clicked.getUID();
    private LocalDate selectedDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDailyTimePreviewBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        initLayout();
        initDailyCalendar();
        initSpinner();

        binding.imgBTNRequest.setOnClickListener(view -> {
            binding.cardSendProposal.setVisibility(View.VISIBLE);
            OffsetTime offset = OffsetTime.now();
            binding.btnTimeStartPicker.setText(String.format(Locale.getDefault(), "%02d:%02d", offset.getHour(), offset.getMinute()));
            binding.btnTimeEndPicker.setText(String.format(Locale.getDefault(), "%02d:%02d", offset.getHour(), offset.getMinute()+1));
        });

        binding.imgBTNCancelMSG.setOnClickListener(view -> binding.cardSendProposal.setVisibility(View.GONE));

        binding.imgBTNBack.setOnClickListener(view -> {
            if(fUser.equals(fClicked)) {
                SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new user_Profile(), "user_Profile");
            } else {SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new Search_Users(), "search_Users");}
        });

        binding.btnTimeStartPicker.setOnClickListener(view -> popTimePicker(binding.btnTimeStartPicker));
        binding.btnTimeEndPicker.setOnClickListener(view -> popTimePicker(binding.btnTimeEndPicker));

        binding.btnProceed.setOnClickListener(view -> {
            if(Objects.requireNonNull(binding.inpTXTElaboration.getText()).toString().trim().isEmpty()){
                binding.inpTXTElaboration.setError("This field is required");
            }else{
                fStore = FirebaseFirestore.getInstance();
                Map<String,Object> requestInfo;
                requestInfo = new HashMap<>();
                requestInfo.put("status", 0);
                requestInfo.put("requestorUID", Account_Details.User_Details.getUID());
                requestInfo.put("requestorName", Account_Details.User_Details.getFullName());
                requestInfo.put("requestorPic", Account_Details.User_Details.getPicString());
                requestInfo.put("requestorEmail", Account_Details.User_Details.getEmail());
                requestInfo.put("requestorIsMentor", Account_Details.User_Details.getIsMentor());
                requestInfo.put("requesteeUID", Account_Details.User_Clicked.getUID());
                requestInfo.put("requesteeName", Account_Details.User_Clicked.getFullName());
                requestInfo.put("requesteePic", Account_Details.User_Clicked.getPicString());
                requestInfo.put("requesteeEmail", Account_Details.User_Clicked.getEmail());
                requestInfo.put("requesteeIsMentor", Account_Details.User_Clicked.getIsMentor());
                requestInfo.put("subject", binding.drpSubject.getSelectedItem().toString());
                requestInfo.put("date", binding.txtMonthView.getText().toString());
                requestInfo.put("startTime", binding.btnTimeStartPicker.getText().toString());
                requestInfo.put("endTime", binding.btnTimeEndPicker.getText().toString());
                requestInfo.put("description", binding.inpTXTElaboration.getText().toString());

                String collectionPath = "proposals";
                if(fUser.equals(fClicked)){
                    collectionPath = "schedules";
                }
                String finalCollectionPath = collectionPath;
                fStore.collection("Users").document(fClicked).collection(collectionPath).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentReference document = fStore.collection("Users").document(fClicked).collection(finalCollectionPath).document(fUser);
                        document.set(requestInfo);
                    }
                });
                fStore.collection("Users").document(fUser).collection(collectionPath)
                        .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentReference document = fStore.collection("Users").document(fUser).collection(finalCollectionPath).document(fClicked);
                        document.set(requestInfo);
                    }
                });
                Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_SHORT).show();
                binding.cardSendProposal.setVisibility(View.GONE);
                initDailyCalendar();
            }
        });

        binding.btnPlusDay.setOnClickListener(view -> {
            selectedDate = selectedDate.plusDays(1);
            Account_Details.User_Details.setSetDate(selectedDate);
            Log.i("selectedDay", selectedDate.toString());
            initLayout();
            initDailyCalendar();
            initSpinner();
        });
        binding.btnMinusDay.setOnClickListener(view -> {
            selectedDate = selectedDate.minusDays(1);
            Account_Details.User_Details.setSetDate(selectedDate);
            Log.i("selectedDay", selectedDate.toString());
            initLayout();
            initDailyCalendar();
            initSpinner();
        });

        return viewLayout;
    }

    private void initSpinner() {
        // Spinner click listener
        binding.drpSubject.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<>(Account_Details.User_Details.subjects);
        if(fUser.equals(fClicked)){
            categories.add("Other");
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        binding.drpSubject.setAdapter(dataAdapter);
    }

    private void initLayout(){
        Boolean isAccepting;
        String headerText;
        if(!fUser.equals(fClicked)){
            isAccepting = Account_Details.User_Clicked.getIsAccepting();
            if (isAccepting != null) {
                if (isAccepting) {
                    binding.imgBTNRequest.setColorFilter(android.R.color.holo_green_light);
                } else {
                    binding.imgBTNRequest.setColorFilter(android.R.color.holo_red_dark);
                }
            }
            headerText = Account_Details.User_Clicked.getFullName() + "'s " + getResources().getString(R.string.SchedulePrompt);
        }else{
            headerText = Account_Details.User_Details.getFullName() + "'s " + getResources().getString(R.string.SchedulePrompt);
        }
        binding.txtHeader.setText(headerText);
    }

    private void initDailyCalendar(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy ", java.util.Locale.getDefault());
        selectedDate = Account_Details.User_Details.getSetDate();
        Date date = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        binding.txtMonthView.setText(sdf.format(date));
        getSchedule();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    public void onNothingSelected(AdapterView<?> arg0) {


    }

    public void getSchedule(){
        List<String> listSchedule = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", java.util.Locale.getDefault());

        fStore = FirebaseFirestore.getInstance();
        fStore.collection("Users").document(fClicked).collection("schedules")
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            ClickedUser_Schedule schedule = new ClickedUser_Schedule();
                            if (Objects.equals(queryDocumentSnapshot.getString("date"), binding.txtMonthView.getText().toString())) {
                                schedule.reqName = queryDocumentSnapshot.getString("requestorName");
                                schedule.reqSubject = queryDocumentSnapshot.getString("subject");
                                schedule.reqDate = queryDocumentSnapshot.getString("date");
                                schedule.reqStartTime = queryDocumentSnapshot.getString("startTime");
                                schedule.reqEndTime = queryDocumentSnapshot.getString("endTime");
                                schedule.reqDescription = queryDocumentSnapshot.getString("description");

                                Date comp_StartTime1 = null;
                                Date comp_StartTime2 = null;
                                Date startTime = null;
                                Date endTime = null;
                                try {
                                    comp_StartTime1 = sdf.parse("00:00");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    comp_StartTime2 = sdf.parse("00:30");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    startTime = sdf.parse(schedule.reqStartTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    endTime = sdf.parse(schedule.reqEndTime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Log.i("startTime", startTime + " comp:" + comp_StartTime1 + " - " + comp_StartTime2);
                                Log.i("endTime", schedule.reqEndTime);

                                for (int i = 0; i < 48; i++) {
                                    assert comp_StartTime1 != null;
                                    if ((Objects.requireNonNull(startTime).after(comp_StartTime1) || sdf.format(startTime).equals(sdf.format(comp_StartTime1))) && startTime.before(comp_StartTime2)) {
                                        listSchedule.add(schedule.reqName);

                                        Date comp_EndTime1;
                                        comp_EndTime1 = comp_StartTime1;
                                        Date comp_EndTime2;
                                        comp_EndTime2 = comp_StartTime2;

                                        for (int j = 0; j < 48; j++) {
                                            assert comp_EndTime2 != null;
                                            assert endTime != null;
                                            if (endTime.after(comp_EndTime1) && (endTime.before(comp_EndTime2) || sdf.format(endTime).equals(sdf.format(comp_EndTime2)))) {
                                                listSchedule.set(listSchedule.size() - 1, schedule.reqEndTime);
                                            } else if (comp_EndTime1.before(endTime)) {
                                                listSchedule.add(schedule.reqSubject);
                                            } else {
                                                listSchedule.add("");
                                            }
                                            Objects.requireNonNull(comp_EndTime1).setTime(comp_EndTime1.getTime() + 30 * 60000);
                                            Objects.requireNonNull(comp_EndTime2).setTime(comp_EndTime2.getTime() + 30 * 60000);
                                        }
                                    } else {
                                        listSchedule.add("");
                                    }
                                    Objects.requireNonNull(comp_StartTime1).setTime(comp_StartTime1.getTime() + 30 * 60000);
                                    Objects.requireNonNull(comp_StartTime2).setTime(comp_StartTime2.getTime() + 30 * 60000);
                                }
                            } else {
                                Log.i("date no match", "no match");
                                Log.i("getDate", queryDocumentSnapshot.getString("date") + " and " + binding.txtMonthView.getText().toString());
                                for (int i = 0; i < 48; i++) {
                                    listSchedule.add("");
                                }
                            }
                            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                            binding.recyclerDailyTime.setLayoutManager(mLinearLayoutManager);
                            List<String> dailyTime = new ArrayList<>();

                            Date populate_Time = null;
                            try {
                                populate_Time = sdf.parse("00:00");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < 48; i++) {
                                assert populate_Time != null;
                                dailyTime.add(sdf.format(populate_Time) + " - " + sdf.format(populate_Time.getTime() + 30 * 60000));
                                populate_Time.setTime(populate_Time.getTime() + 30 * 60000);
                            }

                            DailyTimeAdapter dailyTimeAdapter = new DailyTimeAdapter(dailyTime, listSchedule);
                            binding.recyclerDailyTime.setAdapter(dailyTimeAdapter);
                            binding.scrollDailyCalendar.setVisibility(View.VISIBLE);
                        }}});
    }
}