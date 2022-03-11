package com.example.mentor.Homepage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mentor.Login_Signup.Main_Activity;
import com.example.mentor.R;
import com.example.mentor.databinding.ActivityHomepageBinding;
import com.example.mentor.misc.Account_Details;
import com.google.firebase.auth.FirebaseAuth;

public class Homepage extends AppCompatActivity {

    ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.i("Homepage onCreate getUID", Account_Details.User_Details.getUID());

        replaceFragment(new User_Profile());

        binding.imgBTNHome.setOnClickListener(view -> {
            replaceFragment(new Search_Users());
            Log.i("Switch to Search getUID", Account_Details.User_Details.getUID());
            binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_filled, null));
            binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_outline, null));
            binding.imgBTNProfile.setBackgroundResource(R.drawable.roundedbutton_transparent);
        });

        binding.imgBTNConnections.setOnClickListener(view -> {
            replaceFragment(new Connections());
            binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_outline, null));
            binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_filled, null));
            binding.imgBTNProfile.setBackgroundResource(R.drawable.roundedbutton_transparent);
        });

        binding.imgBTNProfile.setOnClickListener(view -> {
            replaceFragment(new User_Profile());
            binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_outline, null));
            binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_outline, null));
            binding.imgBTNProfile.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
        });

        binding.imgBTNLogout.setOnClickListener(view -> {
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