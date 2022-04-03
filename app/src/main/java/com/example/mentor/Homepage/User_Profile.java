package com.example.mentor.Homepage;

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
import com.example.mentor.databinding.FragmentUserProfileBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.SubjectRates;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User_Profile extends Fragment implements CalendarAdapter.OnItemListener{

    private FragmentUserProfileBinding binding;
    private ArrayList<Long> rates;
    private ArrayList<String> subjects;
    private Boolean isMentor;
    private LocalDate selectedDate;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        initLayout();
        return viewLayout;
    }

    public void initLayout(){

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.layoutHeader.setVisibility(View.INVISIBLE);
        binding.txtBio.setVisibility(View.INVISIBLE);
        binding.cardSecondary.setVisibility(View.INVISIBLE);

        String fUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        fStore.collection("Users").document(fUser).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                task.addOnSuccessListener(documentSnapshot -> {
                    Account_Details.User_Details.setIsAccepting(documentSnapshot.getBoolean("isAccepting"));
                    Boolean isAccepting = Account_Details.User_Details.getIsAccepting();
                    if(isAccepting != null){
                        if(isAccepting){binding.txtStatus.setText(R.string.CurrentlyAccepting);
                        }else{binding.txtStatus.setText(R.string.NoLongerAccepting);}
                    }
                    Account_Details.User_Details.setAuthLevel(documentSnapshot.getLong("authLevel").intValue());
                    switch (Objects.requireNonNull(Account_Details.User_Details.getAuthLevel())){
                        case(0):
                            binding.txtAuthLVL.setText(R.string.AuthLVL_0);
                            break;
                        case(1):
                            binding.txtAuthLVL.setText(R.string.AuthLVL_1);
                            break;
                        case(2):
                            binding.txtAuthLVL.setText(R.string.AuthLVL_2);
                    }
                    Account_Details.User_Details.setFullName(documentSnapshot.getString("fullName"));
                    binding.txtFullName.setText(Account_Details.User_Details.getFullName());
                    Account_Details.User_Details.setEmail(documentSnapshot.getString("email"));
                    binding.txtEmail.setText(Account_Details.User_Details.getEmail());
                    Account_Details.User_Details.setBioEssay(documentSnapshot.getString("bioEssay"));
                    binding.txtBio.setText(Account_Details.User_Details.getBioEssay());

                    Account_Details.User_Details.setIsMentor(documentSnapshot.getBoolean("isMentor"));
                    isMentor = Account_Details.User_Details.getIsMentor();
                    Account_Details.User_Details.rates = (ArrayList<Long>) documentSnapshot.get("subjectRates");
                    rates = Account_Details.User_Details.rates;
                    Account_Details.User_Details.subjects = (ArrayList<String>) documentSnapshot.get("subjects");
                    subjects = Account_Details.User_Details.subjects;

                    initLstSubj(isMentor, rates, subjects);
                    selectedDate = LocalDate.now();
                    setMonthView();

                    binding.progressBar.setVisibility(View.GONE);
                    binding.layoutHeader.setVisibility(View.VISIBLE);
                    binding.txtBio.setVisibility(View.VISIBLE);
                    binding.cardSecondary.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    private void initLstSubj(Boolean isMentor, ArrayList<Long> rates, ArrayList<String> subjects) {
        List<SubjectRates> list_subjrate = new ArrayList<>();
        for (int i = 0; i < subjects.size(); i++) {
            SubjectRates subjRates = new SubjectRates();
            if(isMentor){
                if(rates.size()>0){
                    subjRates.rate = "₱"+rates.get(i)+"/hr";
                }}
            switch (subjects.get(i)) {
                case "Adobe Ps":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_adobe_ps, null);
                    subjRates.name = getResources().getString(R.string.AdobePs);
                    subjRates.red = 37;
                    subjRates.green = 93;
                    subjRates.blue = 170;
                    Log.i("subjRates wawa", i+subjRates.name);
                    break;
                case "Animation":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_animation, null);
                    subjRates.name = getResources().getString(R.string.Animation);
                    subjRates.red = 52;
                    subjRates.green = 34;
                    subjRates.blue = 76;
                    Log.i("subjRates wawa", i+subjRates.name);
                    break;
                case "Arts":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_arts, null);
                    subjRates.name = getResources().getString(R.string.Arts);
                    subjRates.red = 127;
                    subjRates.green = 38;
                    subjRates.blue = 175;
                    break;
                case "AutoCAD":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_autocad, null);
                    subjRates.name = getResources().getString(R.string.AutoCAD);
                    subjRates.red = 127;
                    subjRates.green = 18;
                    subjRates.blue = 11;
                    break;
                case "Engineering":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_engineering, null);
                    subjRates.name = getResources().getString(R.string.Engineering);
                    subjRates.red = 173;
                    subjRates.green = 79;
                    subjRates.blue = 50;
                    break;
                case "Languages":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_languages, null);
                    subjRates.name = getResources().getString(R.string.Languages);
                    subjRates.red = 107;
                    subjRates.green = 134;
                    subjRates.blue = 34;
                    break;
                case "Law":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_law, null);
                    subjRates.name = getResources().getString(R.string.Law);
                    subjRates.red = 3;
                    subjRates.green = 62;
                    subjRates.blue = 88;
                    break;
                case "MS Office":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_ms_office, null);
                    subjRates.name = getResources().getString(R.string.MSOffice);
                    subjRates.red = 184;
                    subjRates.green = 41;
                    subjRates.blue = 22;
                    break;
                case "Mathematics":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_mathematics, null);
                    subjRates.name = getResources().getString(R.string.Mathematics);
                    subjRates.red = 189;
                    subjRates.green = 143;
                    subjRates.blue = 6;
                    break;
                case "Programming":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_programming, null);
                    subjRates.name = getResources().getString(R.string.Programming);
                    subjRates.red = 43;
                    subjRates.green = 160;
                    subjRates.blue = 189;
                    break;
                case "Sciences":
                    subjRates.drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_subjects_science, null);
                    subjRates.name = getResources().getString(R.string.Sciences);
                    subjRates.red = 24;
                    subjRates.green = 134;
                    subjRates.blue = 55;
                    break;
                default:
                    break;
            }
            list_subjrate.add(subjRates);
            Log.i("subjRates wawa", list_subjrate.toString());
        }
        if(list_subjrate.size()>0){
//            binding.progressBar.setVisibility(View.GONE);
            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
            binding.recyclerSubjects.setLayoutManager(mGridLayoutManager);
            binding.recyclerSubjects.setVisibility(View.VISIBLE);
            SubjectRatesAdapter subjectRatesAdapter = new SubjectRatesAdapter(list_subjrate);
            binding.recyclerSubjects.setAdapter(subjectRatesAdapter);
            binding.recyclerSubjects.setHasFixedSize(true);
        } else{
            Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onItemClick(int position, String dayText) {

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