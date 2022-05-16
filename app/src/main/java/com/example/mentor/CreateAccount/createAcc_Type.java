package com.example.mentor.CreateAccount;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.databinding.FragmentCreateAccTypeBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class createAcc_Type extends Fragment {

    private FragmentCreateAccTypeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateAccTypeBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        initLayout();
        initINPTxt();

        binding.cardMentor.setOnClickListener(view -> {
            Account_Details.User_Details.setIsMentor(true);
            binding.cardStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white));
            binding.btnStudent.setTextColor(ContextCompat.getColor(requireContext(),R.color.SQBTN_txtcolor));
            binding.cardMentor.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue));
            binding.btnMentor.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            binding.layoutNotice.setVisibility(View.VISIBLE);
        });

        binding.btnDismiss.setOnClickListener(view -> binding.layoutNotice.setVisibility(View.GONE));

        binding.cardStudent.setOnClickListener(view -> {
            Account_Details.User_Details.setIsMentor(false);
            binding.cardMentor.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white));
            binding.btnMentor.setTextColor(ContextCompat.getColor(requireContext(),R.color.SQBTN_txtcolor));
            binding.cardStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue));
            binding.btnStudent.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
        });
        binding.btnProceed.setOnClickListener(view -> {
            String fullName = Objects.requireNonNull(binding.inpTXTFullname.getText()).toString().trim();
            String bioEssay = Objects.requireNonNull(binding.inpTXTBio.getText()).toString().trim();

            if (fullName.isEmpty()) {
                binding.inpTXTFullname.setError("Required Field");
            }else{
                if (bioEssay.isEmpty()) {
                    binding.inpTXTBio.setError("Required Field");
                }else {
                    Account_Details.User_Details.setFullName(fullName);
                    Account_Details.User_Details.setBioEssay(bioEssay);
                    SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new createAcc_Subjects(), "createAcc_Subjects");
                }}
        });

        binding.cardUserPic.setOnClickListener(view -> uploadPic());

        return viewLayout;
    }

    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                binding.imgUserPic.setImageURI(uri);
                try {
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                    Bitmap imgBMP = BitmapFactory.decodeStream(inputStream);
                    Account_Details.User_Details.setPicString(encodeImage(imgBMP));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void uploadPic() {
        mGetContent.launch("image/*");
    }

    private void initLayout(){
        String currActivity = requireActivity().getClass().getCanonicalName();

        if(currActivity != null) {
            if (currActivity.equals("com.example.mentor.Homepage.homepage")) {
                binding.layoutUserPic.setVisibility(View.VISIBLE);
                binding.layoutAccType.setVisibility(View.GONE);
                binding.txtMainPrompt.setVisibility(View.GONE);
            }else if(currActivity.equals("com.example.mentor.Login_Signup.Main_Activity")){
                binding.layoutUserPic.setVisibility(View.GONE);
                binding.layoutAccType.setVisibility(View.VISIBLE);
                binding.txtMainPrompt.setVisibility(View.VISIBLE);
            }else {Log.i("getActivity", "currActivity not Equal");}
        }else {Log.i("getActivity", "currActivity null");}
    }

    private void initINPTxt() {
        if(Account_Details.User_Details.getIsMentor() != null){
            if(Account_Details.User_Details.getIsMentor()){
                binding.cardMentor.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue));
                binding.btnMentor.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            }else{
                binding.cardStudent.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blue));
                binding.btnStudent.setTextColor(ContextCompat.getColor(requireContext(),R.color.white));
            }
        }
        if(!Account_Details.User_Details.getFullName().trim().isEmpty()){
            binding.inpTXTFullname.setText(Account_Details.User_Details.getFullName());
        }
        if(!Account_Details.User_Details.getBioEssay().trim().isEmpty()){
            binding.inpTXTBio.setText(Account_Details.User_Details.getBioEssay());
        }
    }
}