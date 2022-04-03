package com.example.mentor.CreateAccount;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mentor.Homepage.Homepage;
import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccFinalizeBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class createAcc_Finalize extends Fragment {

    View view;
    String email, password, confirmpassword;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FragmentCreateAccFinalizeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccFinalizeBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.imgBTNBack.setOnClickListener(view -> requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new createAcc_Subjects()).commit());
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        binding.btnProceed.setOnClickListener(view -> {
            email = Objects.requireNonNull(binding.inpTXTEmail.getText()).toString().trim();
            password = Objects.requireNonNull(binding.inpTXTPassword.getText()).toString();
            confirmpassword = Objects.requireNonNull(binding.inpTXTConfirmPassword.getText()).toString();
            if (email.isEmpty()) {
                binding.inpTXTEmail.setError("Required Field");
            }
            if (password.isEmpty() || password.trim().isEmpty() || confirmpassword.isEmpty() || confirmpassword.trim().isEmpty()) {
                binding.inpTXTPassword.setError("Required Field");
            } else {
                if (password.equals(binding.inpTXTConfirmPassword.getText().toString())) {
                    fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                        binding.layoutContents.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.VISIBLE);

                        Account_Details.User_Details.setAuthLevel(0);
                        Account_Details.User_Details.setEmail(email);
                        Account_Details.User_Details.setIsAccepting(true);

                        FirebaseUser fUser = fAuth.getCurrentUser();
                        assert fUser != null;
                        DocumentReference df = fStore.collection("Users").document(fUser.getUid());
                        Map<String,Object> userInfo = new HashMap<>();
                        userInfo.put("isMentor", Account_Details.User_Details.getIsMentor());
                        userInfo.put("FullName", Account_Details.User_Details.getFullName());
                        userInfo.put("FB_Username", Account_Details.User_Details.getFbUser());
                        userInfo.put("LinkedIn_Username", Account_Details.User_Details.getlInUser());
                        userInfo.put("bioEssay", Account_Details.User_Details.getBioEssay());
                        userInfo.put("subjectsBinary", Account_Details.User_Details.getSubjects());
                        userInfo.put("isAccepting", Account_Details.User_Details.getIsAccepting());
                        userInfo.put("AuthLevel", Account_Details.User_Details.getAuthLevel());
                        userInfo.put("Email", Account_Details.User_Details.getEmail());
                        if(Account_Details.User_Details.getIsMentor()) {userInfo.put("SubjectRates", Account_Details.User_Details.rates);}
                        userInfo.put("Picture", "");

                        df.set(userInfo);

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
                } else {
                    binding.inpTXTPassword.setError("Password does not match");
                }
            }
        });

        return view;
    }
}