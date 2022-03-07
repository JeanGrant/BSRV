package com.example.mentor.Login_Signup;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.mentor.Homepage.Homepage;
import com.example.mentor.databinding.ActivityMainBinding;
import com.example.mentor.misc.Account_Details;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Main_Activity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Account_Details.User_Details.reset();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        binding.btnSignup.setOnClickListener(view -> nextScreen(Signup.class));
        binding.btnLogin.setOnClickListener(view -> nextScreen(Login.class));

    }

    private void nextScreen(Class<?> replacement){
        startActivity(new Intent(Main_Activity.this, replacement));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser() != null){
            fUser = fAuth.getCurrentUser();
            DocumentReference df = fStore.collection("Users").document(fUser.getUid());
            df.get().addOnSuccessListener(documentSnapshot -> {
                Log.d("TAG", "onSuccess" + documentSnapshot.getData());
                Account_Details.User_Details.setFullName(documentSnapshot.getString("FullName"));
                Account_Details.User_Details.setEmail(documentSnapshot.getString("Email"));
                Account_Details.User_Details.setFBUser(documentSnapshot.getString("FB_Username"));
                Account_Details.User_Details.setLInUser(documentSnapshot.getString("LinkedIn_Username"));
                Account_Details.User_Details.setBioEssay(documentSnapshot.getString("bioEssay"));
                Account_Details.User_Details.setAuthLevel(Objects.requireNonNull(documentSnapshot.getLong("AuthLevel")).intValue());
                Account_Details.User_Details.setSubjects(Objects.requireNonNull(documentSnapshot.getLong("subjectsBinary")).intValue());
                Account_Details.User_Details.setIsMentor(documentSnapshot.getBoolean("isMentor"));
                Account_Details.User_Details.setIsAccepting(documentSnapshot.getBoolean("isAccepting"));
                if(Account_Details.User_Details.getIsMentor()) {
                    Account_Details.User_Details.setCurrSearch(false);
                }else{
                    Account_Details.User_Details.setCurrSearch(true);
                }
                nextScreen(Homepage.class);
            });
        }
    }
}