package com.example.mentor.Login_Signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.mentor.Homepage.Homepage;
import com.example.mentor.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    String email, password;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        binding.btnProceed.setOnClickListener(view -> {
            email = Objects.requireNonNull(binding.inpTXTEmail.getText()).toString().trim();
            password = Objects.requireNonNull(binding.inpTXTPassword.getText()).toString();

            if(email.isEmpty()) {
                binding.inpTXTEmail.setError("Required Field");
            }
            if(password.trim().isEmpty()){
                binding.inpTXTPassword.setError("Required Field");
            }else {
                fAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
                    Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Main_Activity.class));
                    finish();
                }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        binding.switchLogin.setOnClickListener(view -> {startActivity(new Intent(this, Signup.class));finish();});
    }
}