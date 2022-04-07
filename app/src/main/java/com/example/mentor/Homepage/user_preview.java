package com.example.mentor.Homepage;

import static androidx.core.content.res.ResourcesCompat.getColor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentor.R;
import com.example.mentor.adapters.CalendarAdapter;
import com.example.mentor.adapters.SubjectRatesAdapter;
import com.example.mentor.databinding.FragmentUserPreviewBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.Proposal;
import com.example.mentor.misc.SubjectRates;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class user_preview extends Fragment implements CalendarAdapter.OnItemListener{

    private FragmentUserPreviewBinding binding;
    private LocalDate selectedDate;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private final String fClicked = Account_Details.User_Clicked.getUID();
    private final String fUser = Account_Details.User_Details.getUID();

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

        binding.imgBTNBack.setOnClickListener(view -> SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new Search_Users(), "search_Users"));
        binding.btnMinusMonth.setOnClickListener(view -> previousMonthAction());
        binding.btnPlusMonth.setOnClickListener(view -> nextMonthAction());
        binding.imgBTNProposals.setOnClickListener(view -> getProposal());
        binding.imgBTNMinimize.setOnClickListener(view -> binding.layoutProposal.setVisibility(View.GONE));
        binding.imgBTNAccept.setOnClickListener(view -> fStore.collection("Users").document(fClicked).collection("proposals")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            //skip if current loaded user is the logged in user
                            if (fUser.equals(queryDocumentSnapshot.getId())) {
                                Map<String,Object> scheduleInfo;
                                scheduleInfo = new HashMap<>();
                                scheduleInfo.put("requestorUID", Account_Details.User_Details.getUID());
                                scheduleInfo.put("requestorName", Account_Details.User_Details.getFullName());
                                scheduleInfo.put("requestorPic", Account_Details.User_Details.getPicString());
                                scheduleInfo.put("requestorEmail", Account_Details.User_Details.getEmail());
                                scheduleInfo.put("requestorIsMentor", Account_Details.User_Details.getIsMentor());
                                scheduleInfo.put("requesteeUID", Account_Details.User_Clicked.getUID());
                                scheduleInfo.put("requesteeName", Account_Details.User_Clicked.getFullName());
                                scheduleInfo.put("requesteePic", Account_Details.User_Clicked.getPicString());
                                scheduleInfo.put("requesteeEmail", Account_Details.User_Clicked.getEmail());
                                scheduleInfo.put("requesteeIsMentor", Account_Details.User_Clicked.getIsMentor());
                                scheduleInfo.put("subject", binding.txtSubject.getText().toString());
                                scheduleInfo.put("date", binding.txtDate.getText().toString());
                                scheduleInfo.put("startTime", binding.txtTime.getText().toString().substring(0,5));
                                scheduleInfo.put("endTime", binding.txtTime.getText().toString().substring(8,13));
                                scheduleInfo.put("description", binding.txtDesc.getText().toString());
                                DocumentReference document = fStore.collection("Users").document(fClicked).collection("proposals").document(fUser);
                                document.delete();
                                document = fStore.collection("Users").document(fClicked).collection("schedules").document(fUser);
                                document.set(scheduleInfo);
                                document = fStore.collection("Users").document(fUser).collection("proposals").document(fClicked);
                                document.delete();
                                document = fStore.collection("Users").document(fUser).collection("schedules").document(fClicked);
                                document.set(scheduleInfo);
                                binding.layoutProposal.setVisibility(View.GONE);
                            }
                        }
            }
        }));
        binding.imgBTNDelete.setOnClickListener(view -> fStore.collection("Users").document(fClicked).collection("proposals")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            //skip if current loaded user is the logged in user
                            if (fUser.equals(queryDocumentSnapshot.getId())) {
                                DocumentReference document = fStore.collection("Users").document(fClicked).collection("proposals").document(fUser);
                                document.delete();
                                document = fStore.collection("Users").document(fUser).collection("proposals").document(fClicked);
                                document.delete();
                                binding.layoutProposal.setVisibility(View.GONE);
                            }
                        }
                    }
                }));

        return layoutView;
    }

    private void getProposal() {
        fStore.collection("Users").document(Account_Details.User_Clicked.getUID()).collection("proposals").get().addOnCompleteListener(task -> {
            String currentUserId = FirebaseAuth.getInstance().getUid();
            if(task.isSuccessful() && task.getResult() != null){
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    assert currentUserId != null;
                    if (!((queryDocumentSnapshot.getString("requestorUID").equals(Account_Details.User_Clicked.getUID())&&queryDocumentSnapshot.getString("requesteeUID").equals(Account_Details.User_Details.getUID()))||(queryDocumentSnapshot.getString("requestorUID").equals(Account_Details.User_Details.getUID())&&queryDocumentSnapshot.getString("requesteeUID").equals(Account_Details.User_Clicked.getUID())))){
                        Log.i("skipProposal", queryDocumentSnapshot.getString("requestorUID") + " " + Account_Details.User_Clicked.getUID());
                        continue;
                    }
                    Proposal proposal = new Proposal();
                    proposal.status = queryDocumentSnapshot.getLong("status").intValue();
                    proposal.subject = queryDocumentSnapshot.getString("subject");
                    proposal.date = queryDocumentSnapshot.getString("date");
                    proposal.startTime = queryDocumentSnapshot.getString("startTime");
                    proposal.endTime = queryDocumentSnapshot.getString("endTime");
                    proposal.description = queryDocumentSnapshot.getString("description");
                    binding.txtSubject.setText(proposal.subject);
                    binding.txtDate.setText(proposal.date);
                    String time = proposal.startTime+" - "+proposal.endTime;
                    binding.txtTime.setText(time);
                    binding.txtDesc.setText(proposal.description);
                    binding.layoutProposal.setVisibility(View.VISIBLE);
                }
            }else{
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
        fStore.collection("Users").document(Account_Details.User_Clicked.getUID()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                task.addOnSuccessListener(documentSnapshot -> {
                    Boolean isAccepting = Account_Details.User_Clicked.getIsAccepting();
                    if(isAccepting != null){
                        if(isAccepting){binding.txtStatus.setText(R.string.CurrentlyAccepting);
                        }else{binding.txtStatus.setText(R.string.NoLongerAccepting);}
                    }
                    binding.txtFullName.setText(Account_Details.User_Clicked.getFullName());
                    binding.txtEmail.setText(Account_Details.User_Clicked.getEmail());
                    Account_Details.User_Clicked.setBioEssay(documentSnapshot.getString("bioEssay"));
                    binding.txtBio.setText(Account_Details.User_Clicked.getBioEssay());

                    Account_Details.User_Clicked.rates = (ArrayList<Long>) documentSnapshot.get("subjectRates");
                    Account_Details.User_Clicked.subjects = (ArrayList<String>) documentSnapshot.get("subjects");

                    initLstSubj();
                    selectedDate = LocalDate.now();
                    setMonthView();

                    binding.progressBar.setVisibility(View.GONE);
                    binding.layoutMainContainer.setVisibility(View.VISIBLE);
                    binding.layoutPreviewHeader.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void initLstSubj() {
        Boolean isMentor = Account_Details.User_Clicked.getIsMentor();
        Log.i("isMentor", isMentor.toString());
        ArrayList<Long> rates = Account_Details.User_Clicked.rates;
        ArrayList<String> subjects = Account_Details.User_Clicked.subjects;
        List<SubjectRates> list_subjrate = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            SubjectRates subjRates = new SubjectRates();
            if(isMentor){subjRates.rate = "₱"+rates.get(i)+"/hr";}
            switch (subjects.get(i)) {
                case "Adobe Ps":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_adobe_ps, null);
                    subjRates.name = getResources().getString(R.string.AdobePs);
                    subjRates.hexColor = getColor(getResources(), R.color.AdobePsblue, null);
                    break;
                case "Animation":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_animation, null);
                    subjRates.name = getResources().getString(R.string.Animation);
                    subjRates.hexColor = getColor(getResources(), R.color.AdobeAeViolet, null);
                    break;
                case "Arts":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_arts, null);
                    subjRates.name = getResources().getString(R.string.Arts);
                    subjRates.hexColor = getColor(getResources(), R.color.ArtsPurple, null);
                    break;
                case "AutoCAD":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_autocad, null);
                    subjRates.name = getResources().getString(R.string.AutoCAD);
                    subjRates.hexColor = getColor(getResources(), R.color.AutoCADRed, null);
                    break;
                case "Engineering":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_engineering, null);
                    subjRates.name = getResources().getString(R.string.Engineering);
                    subjRates.hexColor = getColor(getResources(), R.color.EngineeringOrange, null);
                    break;
                case "Languages":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_languages, null);
                    subjRates.name = getResources().getString(R.string.Languages);
                    subjRates.hexColor = getColor(getResources(), R.color.LanguageGreen, null);
                    break;
                case "Law":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_law, null);
                    subjRates.name = getResources().getString(R.string.Law);
                    subjRates.hexColor = getColor(getResources(), R.color.LawBlue, null);
                    break;
                case "MS Office":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_ms_office, null);
                    subjRates.name = getResources().getString(R.string.MSOffice);
                    subjRates.hexColor = getColor(getResources(), R.color.MSOfficeOrange, null);
                    break;
                case "Mathematics":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_mathematics, null);
                    subjRates.name = getResources().getString(R.string.Mathematics);
                    subjRates.hexColor = getColor(getResources(), R.color.MathYellow, null);
                    break;
                case "Programming":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_programming, null);
                    subjRates.name = getResources().getString(R.string.Programming);
                    subjRates.hexColor = getColor(getResources(), R.color.ProgrammingCyan, null);
                    break;
                case "Sciences":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_science, null);
                    subjRates.name = getResources().getString(R.string.Sciences);
                    subjRates.hexColor = getColor(getResources(), R.color.ScienceGreen, null);
                    break;
                default:
                    break;
            }
            list_subjrate.add(subjRates);
        }
        if(list_subjrate.size()>0){
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
            binding.recyclerSubjects.setLayoutManager(mGridLayoutManager);
            binding.recyclerSubjects.setVisibility(View.VISIBLE);
            SubjectRatesAdapter subjectRatesAdapter = new SubjectRatesAdapter(list_subjrate);
            binding.recyclerSubjects.setAdapter(subjectRatesAdapter);
            binding.recyclerSubjects.setHasFixedSize(true);
        } else{
//            Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals("")){
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy ", Locale.getDefault());
            try {
                Date selectedDay = sdf.parse(dayText + " " + selectedDate);
                assert selectedDay != null;
                LocalDate selectedLocalDay = selectedDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Account_Details.User_Details.setSetDate(selectedLocalDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new dailyTime_preview(), "dailyTime_Preview");
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
}