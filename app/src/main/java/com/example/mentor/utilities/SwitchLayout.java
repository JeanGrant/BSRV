package com.example.mentor.utilities;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;

import com.example.mentor.R;

public class SwitchLayout extends AppCompatActivity {
    public static void activityStarter(Context oldLayout, Class<?> newLayout){
        Intent intent = new Intent(oldLayout, newLayout);
        oldLayout.startActivity(intent);
    }

    public static void fragmentStarter(FragmentManager manager, Fragment fragment, String tag){
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment, tag);
        fragmentTransaction.commit();
    }
}
