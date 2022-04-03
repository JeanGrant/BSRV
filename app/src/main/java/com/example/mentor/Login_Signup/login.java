package com.example.mentor.Login_Signup;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.mentor.Homepage.Homepage;
import com.example.mentor.databinding.FragmentLoginBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class login extends Fragment {

    private FragmentLoginBinding binding;
    private String email, password;
    private FirebaseAuth fAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        fAuth = FirebaseAuth.getInstance();

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
                    binding.layoutContents.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        Account_Details.User_Details.initUser();
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SwitchLayout.activityStarter(getContext(), Homepage.class);
                    }, 3000);
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.layoutContents.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);});
            }
        });
        return viewLayout;
    }
}