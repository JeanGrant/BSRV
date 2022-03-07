package com.example.mentor.Login_Signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mentor.CreateAccount.CreateAcc_Type;
import com.example.mentor.R;
import com.example.mentor.misc.Account_Details;

public class Signup extends AppCompatActivity {

    Button switchLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_signup);
        replaceFragment(new CreateAcc_Type());

        switchLogin = findViewById(R.id.switchLogin);
        switchLogin.setOnClickListener(view -> {startActivity(new Intent(this, Login.class));finish();});

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(isFinishing())
        {
            Account_Details.User_Details.reset();
        }
    }
}
