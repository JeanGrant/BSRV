package com.example.mentor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mentor.Homepage.peoples;
import com.example.mentor.databinding.FragmentScheduleMeetingBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.utilities.SwitchLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class schedule_meeting extends Fragment {

    private FragmentScheduleMeetingBinding binding;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private final Account_Details fUser = Account_Details.User_Details;
    private final Account_Details fClicked = Account_Details.User_Clicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScheduleMeetingBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        getDetails();

        binding.imgBTNBack.setOnClickListener(view -> {
            SwitchLayout.fragmentStarter(requireActivity().getSupportFragmentManager(), new peoples(), "peoples");
        });

        binding.btnMeetLink.setOnClickListener(view -> {
            if(binding.btnMeetLink.getText().toString().equals(getResources().getString(R.string.meetLink))){
                binding.layoutSetMeetLink.setVisibility(View.VISIBLE);
            }else if (binding.btnMeetLink.getText().toString().equals(getResources().getString(R.string.confirmMeet))){
                binding.layoutConfirmMeet.setVisibility(View.VISIBLE);
                binding.txtConfirmMeet.setVisibility(View.VISIBLE);
                binding.layoutRateMentor.setVisibility(View.GONE);
                binding.txtRefundProcedure.setVisibility(View.GONE);
                binding.btnConfirm.setVisibility(View.VISIBLE);
                binding.btnRefund.setVisibility(View.VISIBLE);
            }
        });

        binding.imgBTNMinimize.setOnClickListener(view -> binding.layoutSetMeetLink.setVisibility(View.GONE));
        binding.imgBTNMinimizeConfirm.setOnClickListener(view -> binding.layoutConfirmMeet.setVisibility(View.GONE));

        binding.btnSetMeetLink.setOnClickListener(view -> setMeetLink());

        binding.btnConfirm.setOnClickListener(view -> {
            if(binding.btnConfirm.getText().toString().equals(getResources().getString(R.string.confirm))){
                binding.txtConfirmMeet.setVisibility(View.GONE);
                binding.layoutRateMentor.setVisibility(View.VISIBLE);
                binding.btnRefund.setVisibility(View.GONE);
                binding.btnConfirm.setText(R.string.SendMessage);
            }else if (binding.btnConfirm.getText().toString().equals(getResources().getString(R.string.SendMessage))){
                updateMentorRating();
                binding.btnConfirm.setText(R.string.confirm);
            }
        });

        binding.btnRefund.setOnClickListener(view -> {
            if(binding.btnRefund.getText().toString().equals(getResources().getString(R.string.refund))) {
                binding.txtConfirmMeet.setVisibility(View.GONE);
                binding.txtRefundProcedure.setVisibility(View.VISIBLE);
                binding.btnConfirm.setVisibility(View.GONE);
                binding.btnRefund.setText(R.string.dismiss);
            }
            else if(binding.btnRefund.getText().toString().equals(getResources().getString(R.string.dismiss))) {
                binding.layoutConfirmMeet.setVisibility(View.GONE);
                binding.btnRefund.setText(R.string.refund);
            }
        });

        return viewLayout;
    }

    private void updateMentorRating() {
        Map<String,Object> rateMentor = new HashMap<>();
        float rating = binding.rateMentor.getRating();
        if(rating==0.0f){
            Toast.makeText(requireContext(), "Please rate your mentor", Toast.LENGTH_SHORT).show();
        }else {
            String comment = String.valueOf(binding.inpTXTCommentMentor.getText());
            LocalDateTime dtNow = LocalDateTime.now();

            rateMentor.put("rating", rating);
            rateMentor.put("comment", comment);
            String dtNowStr = dtNow.getDayOfMonth() + " " + dtNow.getMonth() + " " + dtNow.getYear() + " " + dtNow.getHour() + ":" + dtNow.getMinute();
            rateMentor.put("dateTime", dtNowStr);

            fStore.collection("Users").document(fUser.getUID()).get().addOnSuccessListener(documentSnapshot -> {
                rateMentor.put("rator", documentSnapshot.getString("fullName"));
                CollectionReference collection = fStore.collection("Users").document(fClicked.getUID()).collection("rating");
                collection.add(rateMentor);

                collection.get().addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       if(task.getResult() != null){
                           int ratingSum = 0;
                           int ratingCount = 0;
                           for(QueryDocumentSnapshot qDocSnap : task.getResult()){
                               Long ratingItem = qDocSnap.getLong("rating");
                               assert ratingItem != null;
                               ratingSum += ratingItem.intValue();
                               ratingCount += 1;
                           }
                           Integer ratingAve = ratingSum / ratingCount;
                           DocumentReference df = fStore.collection("Users").document(fClicked.getUID());
                           df.update("ratingAve", ratingAve);

                           getDetails();
                           binding.layoutConfirmMeet.setVisibility(View.GONE);
                       }
                   }
                });
            });

        }
    }

    private void getDetails() {
        Bundle bundle = this.getArguments();
        String reqUID = "";
        if(bundle != null) {
            reqUID = bundle.getString("reqUID");
        }

        Map<String,Object> proposal = new HashMap<>();
        DocumentReference df = fStore.collection("Users").document(Account_Details.User_Details.getUID()).collection("proposals").document(reqUID);
        df.get().addOnSuccessListener(documentSnapshot -> {
            Long status = documentSnapshot.getLong("status");
            if(status != null) {
                proposal.put("status", status.intValue());
            }
            proposal.put("uid", documentSnapshot.getId());
            proposal.put("requesteeUID", documentSnapshot.getString("requesteeUID"));
            proposal.put("requestorUID", documentSnapshot.getString("requestorUID"));
            proposal.put("subject", documentSnapshot.getString("subject"));
            proposal.put("date", documentSnapshot.getString("date"));
            proposal.put("startTime", documentSnapshot.getString("startTime"));
            proposal.put("endTime", documentSnapshot.getString("endTime"));
            proposal.put("description", documentSnapshot.getString("description"));

            String meetLink = documentSnapshot.getString("meetLink");
            if(meetLink == null){meetLink="";}
            if(meetLink.trim().isEmpty()){meetLink = getResources().getString(R.string.noMeetLink);}
            proposal.put("meetLink", meetLink);

            initLayout(proposal);
        });
    }

    private void initLayout(Map<String,Object> proposal){
        String request_status;
        Integer status = (Integer) proposal.get("status");
        switch (status){
            case 0:
                request_status = "Waiting for Mentor's Confirmation";
                break;
            case 1:
                request_status = "Waiting for Student's Confirmation";
                break;
            case 2:
                request_status = "Waiting for Payment";
                break;
            case 3:
                request_status = "Kindly re-Log In to Confirm Payment";
                break;
            case 4:
                request_status = "Cleared Proposal";
                break;
            default:
                request_status = "Error";
                break;
        }
        binding.txtReqStatus.setText(request_status);

        String uid = (String) proposal.get("uid");
        String subject = (String) proposal.get("subject");
        String date = String.valueOf(proposal.get("date")).trim();
        String startTime = (String) proposal.get("startTime");
        String endTime = (String) proposal.get("endTime");
        String time = startTime+" - "+endTime;
        String description = (String) proposal.get("description");
        String meetLink = (String) proposal.get("meetLink");
        String requesteeUID = (String) proposal.get("requesteeUID");
        String requestorUID = (String) proposal.get("requestorUID");

        binding.txtRequestUID.setText(uid);
        binding.txtSubject.setText(subject);
        binding.txtDate.setText(date);
        binding.txtTime.setText(time);
        binding.txtDesc.setText(description);
        binding.txtMeetLink.setText(meetLink);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");
        LocalDateTime DTnow = LocalDateTime.now();
        LocalDateTime DTStartreq = LocalDateTime.parse(date + " " + startTime, dtf);
        LocalDateTime DTEndreq = LocalDateTime.parse(date + " " + endTime, dtf);

        if (DTnow.isBefore(DTStartreq)) {
            if (fUser.getUID().equals(requesteeUID)) {
                binding.btnMeetLink.setVisibility(View.VISIBLE);
                binding.btnMeetLink.setText(getResources().getText(R.string.meetLink));
            } else if (fUser.getUID().equals(requestorUID)) {
                binding.btnMeetLink.setVisibility(View.GONE);
            }
        } else if (DTnow.isEqual(DTStartreq)) {
            binding.btnMeetLink.setVisibility(View.GONE);
        } else if(DTnow.isAfter(DTEndreq)) {
            if (fUser.getUID().equals(requesteeUID)) {
                binding.btnMeetLink.setVisibility(View.GONE);
            } else if (fUser.getUID().equals(requestorUID)) {
                binding.btnMeetLink.setVisibility(View.VISIBLE);
                binding.btnMeetLink.setText(getResources().getText(R.string.confirmMeet));
            }
        }
    }

    private void setMeetLink(){
        String reqUID = binding.txtRequestUID.getText().toString().trim();
        Map<String, Object> meetLink = new HashMap<>();
        meetLink.put("meetLink", String.valueOf(binding.inpTXTMeetLink.getText()).trim());

        DocumentReference df = fStore.collection("Users").document(fUser.getUID()).collection("proposals").document(reqUID);
        df.update(meetLink);

        df = fStore.collection("Users").document(fClicked.getUID()).collection("proposals").document(reqUID);
        df.update(meetLink);

        binding.layoutSetMeetLink.setVisibility(View.GONE);
        getDetails();
    }
}