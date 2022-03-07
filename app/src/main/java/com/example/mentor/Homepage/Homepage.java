package com.example.mentor.Homepage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mentor.Login_Signup.Main_Activity;
import com.example.mentor.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Homepage extends AppCompatActivity {

    ImageButton btnLogout, btnSearchUsers, btnUserProfile, btnRelationships;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_homepage);

        replaceFragment(new User_Profile());

        btnSearchUsers = findViewById(R.id.imgBTN_home);
        btnLogout = findViewById(R.id.imgBTN_back);
        btnUserProfile = findViewById(R.id.imgBTN_profile);
        btnRelationships = findViewById(R.id.imgBTN_relationships);

        btnSearchUsers.setOnClickListener(view -> {
            replaceFragment(new Search_Users());
            btnSearchUsers.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_filled, null));
            btnUserProfile.setBackgroundResource(R.drawable.roundedbutton_transparent);
        });

        btnUserProfile.setOnClickListener(view -> {
            replaceFragment(new User_Profile());
            btnSearchUsers.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_outline, null));
            btnUserProfile.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
        });

        btnLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Main_Activity.class));
            finish();
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}