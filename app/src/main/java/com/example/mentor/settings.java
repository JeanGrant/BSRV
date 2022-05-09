package com.example.mentor;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.mentor.CreateAccount.createAcc_Type;
import com.example.mentor.Login_Signup.Main_Activity;
import com.example.mentor.databinding.FragmentSettingsBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class settings extends Fragment {

    private FragmentSettingsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        binding.cardProductDesc.setOnClickListener(view -> revealProductDesc());
        binding.cardPromoVid.setOnClickListener(view -> revealPromoVid());
        binding.cardAboutUs.setOnClickListener(view -> revealAboutUs());
        binding.cardFAQs.setOnClickListener(view -> revealFAQs());
        binding.cardWriteFeedback.setOnClickListener(view -> revealWriteFeedback());
        binding.btnSendFeedback.setOnClickListener(view -> sendFeedback());
        binding.btnEditProfile.setOnClickListener(view -> editProfile());
        binding.btnLogout.setOnClickListener(view -> logout());

        String videoPath = "android.resource://" + requireActivity().getPackageName() + "/" + R.raw.promovideo;
        Uri uri = Uri.parse(videoPath);
        binding.videoPromo.setVideoURI(uri);

        MediaController mediaController = new MediaController(requireActivity());
        binding.videoPromo.setMediaController(mediaController);
        mediaController.setAnchorView(binding.videoPromo);

        return viewLayout;
    }

    private void editProfile() {
        SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new createAcc_Type(), "createAcc_Type");
    }

    private void sendFeedback() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("UID", Account_Details.User_Details.getUID());
        feedback.put("feedback", String.valueOf(binding.inpTXTWriteFeedback.getText()).trim());
        fStore.collection("Feedbacks").add(feedback);
        binding.inpTXTWriteFeedback.setText("");
        Toast.makeText(requireContext(),"Thank you for your feedback!", Toast.LENGTH_SHORT).show();
    }

    private void revealWriteFeedback() {
        if (binding.layoutWriteFeedback.getVisibility() == View.VISIBLE) {
            binding.layoutWriteFeedback.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            binding.layoutWriteFeedback.setVisibility(View.GONE);
            binding.imgBTNWriteFeedback.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24));
        }
        else {
            binding.layoutWriteFeedback.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.layoutWriteFeedback.setVisibility(View.VISIBLE);
            binding.imgBTNWriteFeedback.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24));
        }
    }

    private void revealFAQs() {
        if (binding.layoutFAQs.getVisibility() == View.VISIBLE) {
            binding.layoutFAQs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            binding.layoutFAQs.setVisibility(View.GONE);
            binding.imgBTNFAQs.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24));
        }
        else {
            binding.layoutFAQs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.layoutFAQs.setVisibility(View.VISIBLE);
            binding.imgBTNFAQs.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24));
        }
    }

    private void revealAboutUs() {
        if (binding.layoutAboutUs.getVisibility() == View.VISIBLE) {
            binding.layoutAboutUs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            binding.layoutAboutUs.setVisibility(View.GONE);
            binding.imgBTNAboutUs.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24));
        }
        else {
            binding.layoutAboutUs.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.layoutAboutUs.setVisibility(View.VISIBLE);
            binding.imgBTNAboutUs.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24));
        }
    }

    private void revealPromoVid() {
        if (binding.layoutPromoVid.getVisibility() == View.VISIBLE) {
            binding.layoutPromoVid.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            binding.layoutPromoVid.setVisibility(View.GONE);
            binding.imgBTNPromoVid.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24));

            binding.videoPromo.pause();
        }
        else {
            binding.layoutPromoVid.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.layoutPromoVid.setVisibility(View.VISIBLE);
            binding.imgBTNPromoVid.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24));

            binding.videoPromo.start();
        }
    }

    private void revealProductDesc() {
        if (binding.layoutProductDesc.getVisibility() == View.VISIBLE) {
            binding.layoutProductDesc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            binding.layoutProductDesc.setVisibility(View.GONE);
            binding.imgBTNProductDesc.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_down_24));
        }
        else {
            binding.layoutProductDesc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            binding.layoutProductDesc.setVisibility(View.VISIBLE);
            binding.imgBTNProductDesc.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_keyboard_arrow_up_24));
        }
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        SwitchLayout.activityStarter(requireActivity(), Main_Activity.class);
    }
}