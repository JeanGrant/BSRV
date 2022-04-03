package com.example.mentor.Login_Signup;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.mentor.CreateAccount.createAcc_Type;
import com.example.mentor.Homepage.Homepage;
import com.example.mentor.R;
import com.example.mentor.databinding.ActivityMainBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Main_Activity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Boolean switchMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null) {
            new Handler().postDelayed(() -> {
                Account_Details.User_Details.initUser();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SwitchLayout.activityStarter(Main_Activity.this, Homepage.class);
            }, 3000);
        }else {
            binding.imgDivider.setVisibility(View.VISIBLE);
            binding.switchSign.setVisibility(View.VISIBLE);
            binding.frameLayout.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
            Account_Details.User_Details.reset();
            SwitchLayout.fragmentStarter(getSupportFragmentManager(), new login(), "login");
        }
        binding.switchSign.setOnClickListener(view -> {
            if(switchMode){
                switchMode = false;
                SwitchLayout.fragmentStarter(getSupportFragmentManager(), new createAcc_Type(), "createAcc_Type");
                binding.switchSign.setText(getResources().getText(R.string.SignupPrompt));
            }else{
                switchMode = true;
                SwitchLayout.fragmentStarter(getSupportFragmentManager(), new login(), "login");
                binding.switchSign.setText(getResources().getText(R.string.LoginPrompt));
            }
        });

    }
}