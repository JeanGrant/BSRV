package com.example.mentor.Homepage;

import static androidx.core.content.res.ResourcesCompat.getColor;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
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
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class dailyTime_preview extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentDailyTimePreviewBinding binding;
    private int hour, minute;
    private FirebaseFirestore fStore;
    private final Account_Details fClicked = Account_Details.User_Clicked;
    private final Account_Details fUser = Account_Details.User_Details;
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
            binding.cardMainContent.setVisibility(View.GONE);
            OffsetTime offset = OffsetTime.now();
            binding.btnTimeStartPicker.setText(String.format(Locale.getDefault(), "%02d:%02d", offset.getHour(), offset.getMinute()));
            binding.btnTimeEndPicker.setText(String.format(Locale.getDefault(), "%02d:%02d", offset.getHour(), offset.getMinute()+1));
        });

        binding.imgBTNCancelMSG.setOnClickListener(view -> {
            binding.cardSendProposal.setVisibility(View.GONE);
            binding.cardMainContent.setVisibility(View.VISIBLE);
        });

        binding.imgBTNBack.setOnClickListener(view -> {
            if(fUser.getUID().equals(fClicked.getUID())) {
                SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new user_Profile(), "user_Profile");
            } else {
                Bundle bundleOld = this.getArguments();

                Fragment fragment2 = new user_Preview();
                fragment2.setArguments(bundleOld);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment2, "user_Preview")
                        .commit();
            }
        });

        binding.btnTimeStartPicker.setOnClickListener(view -> popTimePicker(binding.btnTimeStartPicker));
        binding.btnTimeEndPicker.setOnClickListener(view -> popTimePicker(binding.btnTimeEndPicker));

        binding.btnProceed.setOnClickListener(view -> {
            if(!binding.drpSubject.getSelectedItem().toString().equals("Occupied") && Objects.requireNonNull(binding.inpTXTElaboration.getText()).toString().trim().isEmpty()){
                binding.inpTXTElaboration.setError("This field is required");
            }else{
                addProposal();
            }
        });

        binding.btnPlusDay.setOnClickListener(view -> {
            selectedDate = selectedDate.plusDays(1);
            fUser.setSetDate(selectedDate);
            Log.i("selectedDay", selectedDate.toString());
            initLayout();
            initDailyCalendar();
            initSpinner();
        });
        binding.btnMinusDay.setOnClickListener(view -> {
            selectedDate = selectedDate.minusDays(1);
            fUser.setSetDate(selectedDate);
            Log.i("selectedDay", selectedDate.toString());
            initLayout();
            initDailyCalendar();
            initSpinner();
        });

        return viewLayout;
    }

    private void addProposal() {
        fStore = FirebaseFirestore.getInstance();
        Map<String,Object> requestInfo;
        requestInfo = new HashMap<>();
        requestInfo.put("status", 0);
        requestInfo.put("requestorUID", fUser.getUID());
        requestInfo.put("requestorName", fUser.getFullName());
        requestInfo.put("requestorPic", fUser.getPicString());
        requestInfo.put("requestorEmail", fUser.getEmail());
        requestInfo.put("requestorIsMentor", fUser.getIsMentor());
        requestInfo.put("requesteeUID", fClicked.getUID());
        requestInfo.put("requesteeName", fClicked.getFullName());
        requestInfo.put("requesteePic", fClicked.getPicString());
        requestInfo.put("requesteeEmail", fClicked.getEmail());
        requestInfo.put("requesteeIsMentor", fClicked.getIsMentor());
        requestInfo.put("subject", binding.drpSubject.getSelectedItem().toString());
        requestInfo.put("date", binding.txtMonthView.getText().toString());
        requestInfo.put("startTime", binding.btnTimeStartPicker.getText().toString());
        requestInfo.put("endTime", binding.btnTimeEndPicker.getText().toString());
        requestInfo.put("description", String.valueOf(binding.inpTXTElaboration.getText()));

        String collectionPath;
        if(fUser.getUID().equals(fClicked.getUID())){collectionPath = "schedules";}
        else{collectionPath = "proposals";}

        fStore.collection("Users").document(fClicked.getUID()).collection(collectionPath).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() > 0) {
                    boolean isDocuExists=false;
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        if (Objects.requireNonNull(requestInfo.get("requestorUID")).equals(queryDocumentSnapshot.get("requestorUID"))) {
                            if (Objects.requireNonNull(requestInfo.get("date")).equals(queryDocumentSnapshot.get("date"))) {
                                if (Objects.requireNonNull(requestInfo.get("subject")).equals(queryDocumentSnapshot.get("subject"))) {
                                    DocumentReference document = fStore.collection("Users").document(fClicked.getUID())
                                            .collection(collectionPath).document(queryDocumentSnapshot.getId());
                                    document.set(requestInfo);
                                    document = fStore.collection("Users").document(fUser.getUID())
                                            .collection(collectionPath).document(queryDocumentSnapshot.getId());
                                    document.set(requestInfo);
                                    isDocuExists = true;
                                }
                            }
                        }
                    }
                    Log.i("fClicked isDocuExists", String.valueOf(isDocuExists));
                    if(!isDocuExists){
                        CollectionReference collection = fStore.collection("Users").document(fClicked.getUID()).collection(collectionPath);
                        collection.add(requestInfo).addOnSuccessListener(documentReference -> fStore.collection("Users").document(fClicked.getUID()).collection(collectionPath).get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                if (task1.getResult().size() > 0) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()) {
                                        if (Objects.requireNonNull(requestInfo.get("requestorUID")).equals(queryDocumentSnapshot.get("requestorUID"))) {
                                            if (Objects.requireNonNull(requestInfo.get("date")).equals(queryDocumentSnapshot.get("date"))) {
                                                if (Objects.requireNonNull(requestInfo.get("subject")).equals(queryDocumentSnapshot.get("subject"))) {
                                                    Log.i("match", queryDocumentSnapshot.getId());
                                                    DocumentReference document = fStore.collection("Users").document(fUser.getUID())
                                                            .collection(collectionPath).document(queryDocumentSnapshot.getId());
                                                    document.set(requestInfo);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }));
                    }
                }else{
                    Log.i("addProposal", "collection does not exist; creating new collection");
                    CollectionReference collection = fStore.collection("Users").document(fClicked.getUID()).collection(collectionPath);
                    collection.add(requestInfo).addOnSuccessListener(documentReference -> fStore.collection("Users").document(fClicked.getUID()).collection(collectionPath).get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            if (task1.getResult().size() > 0) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()) {
                                    if (Objects.requireNonNull(requestInfo.get("requestorUID")).equals(queryDocumentSnapshot.get("requestorUID"))) {
                                        if (Objects.requireNonNull(requestInfo.get("date")).equals(queryDocumentSnapshot.get("date"))) {
                                            if (Objects.requireNonNull(requestInfo.get("subject")).equals(queryDocumentSnapshot.get("subject"))) {
                                                Log.i("match", queryDocumentSnapshot.getId());
                                                DocumentReference document = fStore.collection("Users").document(fUser.getUID())
                                                        .collection(collectionPath).document(queryDocumentSnapshot.getId());
                                                document.set(requestInfo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }));
                }
            }else{
                Log.i("addProposal", "task failed");
            }
        });

        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        binding.cardSendProposal.setVisibility(View.GONE);
        binding.cardMainContent.setVisibility(View.VISIBLE);
        initDailyCalendar();
    }

    private void initSpinner() {
        // Spinner click listener
        binding.drpSubject.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<>(Account_Details.User_Details.subjects);
        if(fUser.getUID().equals(fClicked.getUID())){
            categories.add("Occupied");
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
        if(!fUser.getUID().equals(fClicked.getUID())){
            binding.imgBTNRequest.setVisibility(View.VISIBLE);
            Log.i("different", fUser.getUID() + " & " + fClicked.getUID());
            isAccepting = Account_Details.User_Clicked.getIsAccepting();
            if (isAccepting != null) {
                if (isAccepting) {
                    Log.i("isAccepting", isAccepting.toString());
                    binding.imgBTNRequest.setColorFilter(getColor(getResources(), R.color.green, null), PorterDuff.Mode.SRC_ATOP);
                } else {
                    Log.i("isNotAccepting", isAccepting.toString());
                    binding.imgBTNRequest.setColorFilter(getColor(getResources(), R.color.red, null), PorterDuff.Mode.SRC_ATOP);
                }
            }
            headerText = fClicked.getFullName() + "'s " + getResources().getString(R.string.SchedulePrompt);
        }else{
            Log.i("same", fUser.getUID() + " & " + fClicked.getUID());
            headerText = fUser.getFullName() + "'s " + getResources().getString(R.string.SchedulePrompt);
        }
        binding.txtHeader.setText(headerText);
    }

    private void initDailyCalendar(){
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("d MMMM yyyy ");
        selectedDate = Account_Details.User_Details.getSetDate();
        binding.txtMonthView.setText(sdf.format(selectedDate));
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
        if(binding.drpSubject.getSelectedItem().toString().equals("Occupied")){
            binding.inpTXTElaboration.setVisibility(View.GONE);
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {


    }

    public void getSchedule(){
        List<ClickedUser_Schedule> listSchedule = new ArrayList<>();
        DateTimeFormatter dMy = DateTimeFormatter.ofPattern("d MMMM yyyy");
        DateTimeFormatter Hm = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate selectedDate = LocalDate.parse(binding.txtMonthView.getText().toString().trim(),dMy);

        fStore = FirebaseFirestore.getInstance();
        fStore.collection("Users").document(fClicked.getUID()).collection("schedules").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if (task.getResult().size() > 0) {
                    for (int i = 0; i < 48; i++) {
                        ClickedUser_Schedule schedule = new ClickedUser_Schedule();
                        schedule.reqName = "";
                        listSchedule.add(schedule);
                    }
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        Log.i("schedules", binding.txtMonthView.getText().toString());
                        LocalDate documentDate = LocalDate.parse(queryDocumentSnapshot.getString("date").trim(),dMy);
                        if (documentDate.isEqual(selectedDate)) {
                            Log.i("schedulesEqual", binding.txtMonthView.getText().toString());

                            LocalTime comp_StartTime1 = LocalTime.parse("00:00", Hm);
                            LocalTime comp_StartTime2 = LocalTime.parse("00:30", Hm);
                            LocalTime startTime = LocalTime.parse(queryDocumentSnapshot.getString("startTime"), Hm);
                            LocalTime endTime = LocalTime.parse(queryDocumentSnapshot.getString("endTime"), Hm);

                            Log.i("startTime", startTime + " comp:" + comp_StartTime1 + " - " + comp_StartTime2);
                            Log.i("endTime", Hm.format(endTime));

                            for (int h = 0; h < 48; h++) {
                                if ((startTime.isAfter(comp_StartTime1) || startTime.equals(comp_StartTime1)) && startTime.isBefore(comp_StartTime2)) {

                                    LocalTime comp_EndTime1 = comp_StartTime1;
                                    LocalTime comp_EndTime2 = comp_StartTime2;

                                    for (int j = h; j < 48; j++) {
                                        ClickedUser_Schedule scheduleEnd = new ClickedUser_Schedule();
                                        scheduleEnd.reqName = queryDocumentSnapshot.getString("requestorName");
                                        Log.d("requestorName", scheduleEnd.reqName);
                                        scheduleEnd.reqSubject = queryDocumentSnapshot.getString("subject");
                                        scheduleEnd.reqDate = queryDocumentSnapshot.getString("date");
                                        scheduleEnd.reqStartTime = queryDocumentSnapshot.getString("startTime");
                                        scheduleEnd.reqEndTime = queryDocumentSnapshot.getString("endTime");
                                        scheduleEnd.reqDescription = queryDocumentSnapshot.getString("description");
                                        scheduleEnd.txtColor = getColor(getResources(), R.color.white, null);
                                        switch (scheduleEnd.reqSubject){
                                            case "Adobe Ps":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.AdobePsblue, null);
                                                break;
                                            case "Animation":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.AdobeAeViolet, null);
                                                break;
                                            case "Arts":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.ArtsPurple, null);
                                                break;
                                            case "AutoCAD":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.AutoCADRed, null);
                                                break;
                                            case "Engineering":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.EngineeringOrange, null);
                                                break;
                                            case "Languages":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.LanguageGreen, null);
                                                break;
                                            case "Law":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.LawBlue, null);
                                                break;
                                            case "MS Office":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.MSOfficeOrange, null);
                                                break;
                                            case "Mathematics":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.MathYellow, null);
                                                break;
                                            case "Programming":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.ProgrammingCyan, null);
                                                break;
                                            case "Sciences":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.ScienceGreen, null);
                                                break;
                                            case "Occupied":
                                                scheduleEnd.bgColor = getColor(getResources(), R.color.black, null);
                                                break;
                                            default:
                                                break;
                                        }
                                        if (comp_EndTime1.isBefore(endTime)) {
                                            scheduleEnd.reqName = queryDocumentSnapshot.getString("requestorName");
                                            Log.i("schedulesEndName", scheduleEnd.reqName + " " + scheduleEnd.bgColor);
                                            listSchedule.set(j, scheduleEnd);
                                        } else {
                                            scheduleEnd.reqName = "";
                                            Log.i("schedulesBlankName", scheduleEnd.reqName + " " + scheduleEnd.bgColor);
                                        }
                                        comp_EndTime1 = comp_EndTime1.plusMinutes(30);
                                        comp_EndTime2 = comp_EndTime2.plusMinutes(30);
                                    }
                                } else {
                                    ClickedUser_Schedule schedule = new ClickedUser_Schedule();
                                    schedule.reqName = "";
                                }
                                comp_StartTime1 = comp_StartTime1.plusMinutes(30);
                                comp_StartTime2 = comp_StartTime2.plusMinutes(30);
                            }
                        }
                        else {
                            Log.i("date no match", "no match");
                            Log.i("getDate", queryDocumentSnapshot.getString("date") + " and " + binding.txtMonthView.getText().toString());
                            for (int i = 0; i < 48; i++) {
                                ClickedUser_Schedule schedule = new ClickedUser_Schedule();
                                schedule.reqName = "";
                            }
                        }
                        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                        binding.recyclerDailyTime.setLayoutManager(mLinearLayoutManager);
                        List<String> dailyTime = new ArrayList<>();

                        LocalTime populate_Time = LocalTime.parse("00:00", Hm);

                        for (int i = 0; i < 48; i++) {
                            Log.d("listSchedule", i + " Subject " + listSchedule.get(i).reqSubject);
                            assert populate_Time != null;
                            dailyTime.add(Hm.format(populate_Time) + " - " + Hm.format(populate_Time.plusMinutes(30)));
                            populate_Time = populate_Time.plusMinutes(30);
                        }

                        DailyTimeAdapter dailyTimeAdapter = new DailyTimeAdapter(dailyTime, listSchedule);
                        binding.recyclerDailyTime.setAdapter(dailyTimeAdapter);
                        binding.scrollDailyCalendar.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Log.i("task", String.valueOf(task.getResult().size()));
                    for (int i = 0; i < 48; i++) {
                        ClickedUser_Schedule schedule = new ClickedUser_Schedule();
                        schedule.reqName = "";
                        listSchedule.add(schedule);
                    }
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                    binding.recyclerDailyTime.setLayoutManager(mLinearLayoutManager);
                    List<String> dailyTime = new ArrayList<>();

                    LocalTime populate_Time = LocalTime.parse("00:00", Hm);

                    for (int i = 0; i < 48; i++) {
                        assert populate_Time != null;
                        dailyTime.add(Hm.format(populate_Time) + " - " + Hm.format(populate_Time.plusMinutes(30)));
                        populate_Time = populate_Time.plusMinutes(30);
                    }

                    DailyTimeAdapter dailyTimeAdapter = new DailyTimeAdapter(dailyTime, listSchedule);
                    binding.recyclerDailyTime.setAdapter(dailyTimeAdapter);
                    binding.scrollDailyCalendar.setVisibility(View.VISIBLE);
                }
            }
                });
    }
}