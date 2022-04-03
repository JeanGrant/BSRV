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

public class Homepage extends AppCompatActivity {

    ActivityHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.i("Homepage onCreate getUID", Account_Details.User_Details.getUID());

        SwitchLayout.fragmentStarter(getSupportFragmentManager(), new User_Profile(), "user_Profile");
        binding.imgBTNProfile.setBackgroundResource(R.drawable.roundedbutton_blue_outline);

        binding.imgBTNHome.setOnClickListener(view -> {
            SwitchLayout.fragmentStarter(getSupportFragmentManager(), new Search_Users(), "search_Users");
            binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_filled, null));
            binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_outline, null));
            binding.imgBTNProfile.setBackgroundResource(Color.alpha(0));
        });

        binding.imgBTNConnections.setOnClickListener(view -> {
            SwitchLayout.fragmentStarter(getSupportFragmentManager(), new Connections(), "connections");
            binding.imgBTNHome.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_home_outline, null));
            binding.imgBTNConnections.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_drawables_contacts_filled, null));
            binding.imgBTNProfile.setBackgroundResource(Color.alpha(0));
        });

        binding.imgBTNProfile.setOnClickListener(view -> {
            SwitchLayout.fragmentStarter(getSupportFragmentManager(), new User_Profile(), "user_Profile");
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
                    SwitchLayout.fragmentStarter(getSupportFragmentManager(), new User_Profile(), "user_Profile");
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
}