package com.example.mentor.Homepage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.mentor.Login_Signup.Main_Activity;
import com.example.mentor.R;
import com.example.mentor.databinding.ActivityHomepageBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Homepage extends AppCompatActivity {

    ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.i("Homepage onCreate getUID", Account_Details.User_Details.getUID());

        SwitchLayout.fragmentStarter(getSupportFragmentManager(), new user_Profile(), "user_Profile");
        binding.imgBTNProfile.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_user_filled, null));

        binding.imgBTNHome.setOnClickListener(view -> {
            SwitchLayout.fragmentStarter(getSupportFragmentManager(), new Search_Users(), "search_Users");
            binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_filled, null));
            binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_outline, null));
            binding.imgBTNProfile.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_user_outline, null));
            prop2sched();
        });

        binding.imgBTNConnections.setOnClickListener(view -> {
            SwitchLayout.fragmentStarter(getSupportFragmentManager(), new Connections(), "connections");
            binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_outline, null));
            binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_filled, null));
            binding.imgBTNProfile.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_user_outline, null));
            prop2sched();
        });

        binding.imgBTNProfile.setOnClickListener(view -> {
            SwitchLayout.fragmentStarter(getSupportFragmentManager(), new user_Profile(), "user_Profile");
            binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_outline, null));
            binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_outline, null));
            binding.imgBTNProfile.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_user_filled, null));
        });

        binding.imgBTNLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Main_Activity.class));
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        Fragment myFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (myFragment != null) {
            assert myFragment.getTag() != null;
            switch (myFragment.getTag()) {
                case "user_Profile":
                    Toast.makeText(getApplicationContext(), "Do nothing", Toast.LENGTH_SHORT).show();
                    break;
                case "search_Users":
                case "connections":
                    SwitchLayout.fragmentStarter(getSupportFragmentManager(), new user_Profile(), "user_Profile");
                    binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_outline, null));
                    binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_outline, null));
                    binding.imgBTNProfile.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
                    break;
                case "user_Preview":
                    SwitchLayout.fragmentStarter(getSupportFragmentManager(), new Search_Users(), "search_Users");
                    binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_filled, null));
                    binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_outline, null));
                    binding.imgBTNProfile.setBackgroundResource(Color.alpha(0));
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Error Getting Tag", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Log.i("myFragment", "myFragment is null");
        }
    }

    private void prop2sched(){
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        fStore.collection("Users").document(Account_Details.User_Details.getUID())
                .collection("proposals").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().size()>0){

                    for (QueryDocumentSnapshot qDocSnap : task.getResult()){
                        Long status = qDocSnap.getLong("status");
                        assert status != null;
                        if (status.intValue()==3){
                            Map<String, Object> scheduleInfo;
                            scheduleInfo = new HashMap<>();
                            scheduleInfo.put("requestorUID", qDocSnap.getString("requestorUID"));
                            scheduleInfo.put("requestorName", qDocSnap.getString("requestorName"));
                            scheduleInfo.put("requestorPic", qDocSnap.getString("requestorPic"));
                            scheduleInfo.put("requestorEmail", qDocSnap.getString("requestorEmail"));
                            scheduleInfo.put("requestorIsMentor", qDocSnap.getBoolean("requestorIsMentor"));
                            scheduleInfo.put("requesteeUID", qDocSnap.getString("requesteeUID"));
                            scheduleInfo.put("requesteeName", qDocSnap.getString("requesteeName"));
                            scheduleInfo.put("requesteePic", qDocSnap.getString("requesteePic"));
                            scheduleInfo.put("requesteeEmail", qDocSnap.getString("requesteeEmail"));
                            scheduleInfo.put("requesteeIsMentor", qDocSnap.getBoolean("requesteeIsMentor"));
                            scheduleInfo.put("subject", qDocSnap.getString("subject"));
                            scheduleInfo.put("date", qDocSnap.getString("date"));
                            scheduleInfo.put("startTime", qDocSnap.getString("startTime"));
                            scheduleInfo.put("endTime", qDocSnap.getString("endTime"));
                            scheduleInfo.put("description", qDocSnap.getString("description"));

                            DocumentReference document = fStore.collection("Users").document(String.valueOf(scheduleInfo.get("requestorUID"))).collection("schedules").document(qDocSnap.getId());
                            document.set(scheduleInfo);
                            document = fStore.collection("Users").document(String.valueOf(scheduleInfo.get("requesteeUID"))).collection("schedules").document(qDocSnap.getId());
                            document.set(scheduleInfo);

                            document = fStore.collection("Users").document(String.valueOf(scheduleInfo.get("requestorUID"))).collection("proposals").document(qDocSnap.getId());
                            document.update("status", 4);
                            document = fStore.collection("Users").document(String.valueOf(scheduleInfo.get("requesteeUID"))).collection("proposals").document(qDocSnap.getId());
                            document.update("status", 4);

                            document = fStore.collection("Users").document(String.valueOf(scheduleInfo.get("requestorUID")));
                            document.get().addOnSuccessListener(documentSnapshot -> {

                                DocumentReference document1 = fStore.collection("Users").document(String.valueOf(scheduleInfo.get("requestorUID")));

                                Map<String, Object> history;
                                history = new HashMap<>();

                                if(documentSnapshot.get("transactionHistory") != null){
                                    ArrayList<String> transactionHistory = (ArrayList<String>) documentSnapshot.get("transactionHistory");
                                    if(!transactionHistory.contains(String.valueOf(scheduleInfo.get("requesteeUID")))){
                                        transactionHistory.add(String.valueOf(scheduleInfo.get("requesteeUID")));
                                        history.put("transactionHistory", transactionHistory);
                                        document1.update(history);
                                    }
                                }else{
                                    ArrayList<String> transactionHistory = new ArrayList<>();
                                    transactionHistory.add(String.valueOf(scheduleInfo.get("requesteeUID")));
                                    history.put("transactionHistory", transactionHistory);
                                    document1.update(history);
                                }
                            });

                            document = fStore.collection("Users").document(String.valueOf(scheduleInfo.get("requesteeUID")));
                            document.get().addOnSuccessListener(documentSnapshot -> {

                                DocumentReference document1 = fStore.collection("Users").document(String.valueOf(scheduleInfo.get("requesteeUID")));

                                Map<String, Object> history;
                                history = new HashMap<>();

                                if(documentSnapshot.get("transactionHistory") != null){
                                    ArrayList<String> transactionHistory = (ArrayList<String>) documentSnapshot.get("transactionHistory");
                                    if(!transactionHistory.contains(String.valueOf(scheduleInfo.get("requestorUID")))){
                                        transactionHistory.add(String.valueOf(scheduleInfo.get("requestorUID")));
                                        history.put("transactionHistory", transactionHistory);
                                        document1.update(history);
                                    }
                                }else{
                                    ArrayList<String> transactionHistory = new ArrayList<>();
                                    transactionHistory.add(String.valueOf(scheduleInfo.get("requestorUID")));
                                    history.put("transactionHistory", transactionHistory);
                                    document1.update(history);
                                }

                            });
                        }
                    }
                }
            }
        });
    }
}