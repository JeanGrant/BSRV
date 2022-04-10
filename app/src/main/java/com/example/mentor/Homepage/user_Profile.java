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
import com.example.mentor.databinding.FragmentUserProfileBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.SubjectRates;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class user_Profile extends Fragment implements CalendarAdapter.OnItemListener{

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

        binding.btnMinusMonth.setOnClickListener(view -> previousMonthAction());
        binding.btnPlusMonth.setOnClickListener(view -> nextMonthAction());

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
                    Account_Details.User_Details.setUID(fUser);
                    Account_Details.User_Details.setIsAccepting(documentSnapshot.getBoolean("isAccepting"));
                    Boolean isAccepting = Account_Details.User_Details.getIsAccepting();
                    if(isAccepting != null){
                        if(isAccepting){binding.txtStatus.setText(R.string.CurrentlyAccepting);
                        }else{binding.txtStatus.setText(R.string.NoLongerAccepting);}
                    }

                    Account_Details.User_Details.setIsMentor(documentSnapshot.getBoolean("isMentor"));
                    isMentor = Account_Details.User_Details.getIsMentor();
                    Account_Details.User_Details.setAuthLevel(documentSnapshot.getLong("authLevel").intValue());
                    if(isMentor) {
                        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                            binding.txtAuthLVL.setText(R.string.AuthLVL_0);
                        } else if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                            if(documentSnapshot.getLong("authLevel").intValue()<1) {
                                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                    fStore.collection("Users").document(fUser).update("authLevel", 1);
                                }
                            }
                            binding.txtAuthLVL.setText(R.string.AuthLVL_1);
                        } else if (Account_Details.User_Details.getAuthLevel() == 2) {
                            binding.txtAuthLVL.setText(R.string.AuthLVL_2);
                        } }
                    else{
                        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                            binding.txtAuthLVL.setText(R.string.AuthLVL_0);
                        } else {
                            if(documentSnapshot.getLong("authLevel").intValue()<1) {
                                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                    fStore.collection("Users").document(fUser).update("authLevel", 2);
                                }
                            }
                            binding.txtAuthLVL.setText(R.string.AuthLVL_2);
                        }
                    }
                    Account_Details.User_Details.setFullName(documentSnapshot.getString("fullName"));
                    binding.txtFullName.setText(Account_Details.User_Details.getFullName());
                    Account_Details.User_Details.setEmail(documentSnapshot.getString("email"));
                    binding.txtEmail.setText(Account_Details.User_Details.getEmail());
                    Account_Details.User_Details.setBioEssay(documentSnapshot.getString("bioEssay"));
                    binding.txtBio.setText(Account_Details.User_Details.getBioEssay());

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
            if(isMentor){subjRates.rate = "₱"+rates.get(i)+"/hr";}
            switch (subjects.get(i)) {
                case "Adobe Ps":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_adobe_ps, null);
                    subjRates.name = getResources().getString(R.string.AdobePs);
                    subjRates.hexColor = getColor(getResources(), R.color.AdobePsblue, null);
                    break;
                case "Animation":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_animation, null);
                    subjRates.name = getResources().getString(R.string.Animation);
                    subjRates.hexColor = getColor(getResources(), R.color.AdobeAeViolet, null);
                    break;
                case "Arts":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_arts, null);
                    subjRates.name = getResources().getString(R.string.Arts);
                    subjRates.hexColor = getColor(getResources(), R.color.ArtsPurple, null);
                    break;
                case "AutoCAD":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_autocad, null);
                    subjRates.name = getResources().getString(R.string.AutoCAD);
                    subjRates.hexColor = getColor(getResources(), R.color.AutoCADRed, null);
                    break;
                case "Engineering":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_engineering, null);
                    subjRates.name = getResources().getString(R.string.Engineering);
                    subjRates.hexColor = getColor(getResources(), R.color.EngineeringOrange, null);
                    break;
                case "Languages":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_languages, null);
                    subjRates.name = getResources().getString(R.string.Languages);
                    subjRates.hexColor = getColor(getResources(), R.color.LanguageGreen, null);
                    break;
                case "Law":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_law, null);
                    subjRates.name = getResources().getString(R.string.Law);
                    subjRates.hexColor = getColor(getResources(), R.color.LawBlue, null);
                    break;
                case "MS Office":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_ms_office, null);
                    subjRates.name = getResources().getString(R.string.MSOffice);
                    subjRates.hexColor = getColor(getResources(), R.color.MSOfficeOrange, null);
                    break;
                case "Mathematics":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_mathematics, null);
                    subjRates.name = getResources().getString(R.string.Mathematics);
                    subjRates.hexColor = getColor(getResources(), R.color.MathYellow, null);
                    break;
                case "Programming":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_programming, null);
                    subjRates.name = getResources().getString(R.string.Programming);
                    subjRates.hexColor = getColor(getResources(), R.color.ProgrammingCyan, null);
                    break;
                case "Sciences":
                    subjRates.drawable = ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_subjects_science, null);
                    subjRates.name = getResources().getString(R.string.Sciences);
                    subjRates.hexColor = getColor(getResources(), R.color.ScienceGreen, null);
                    break;
                default:
                    break;
            }
            list_subjrate.add(subjRates);
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
        if(!dayText.equals("")){
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy ", java.util.Locale.getDefault());
            try {
                String selectedMY = sdf.format(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                sdf = new SimpleDateFormat("dd MMMM yyyy ", java.util.Locale.getDefault());
                Date selectedDay = sdf.parse(dayText + " " + selectedMY);
                Log.i("selectedDay", selectedDay.toString());
                assert selectedDay != null;
                LocalDate selectedLocalDay = selectedDay.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Account_Details.User_Details.setSetDate(selectedLocalDay);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("selectedDay", e.getMessage());
                Log.i("selectedDay", dayText + " " + selectedDate);
            }
            Account_Details.User_Clicked.setUID(Account_Details.User_Details.getUID());
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