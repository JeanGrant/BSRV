package com.example.mentor.Homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.adapters.UsersScheduleAdapter;
import com.example.mentor.databinding.FragmentPeoplesBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.Proposal;
import com.example.mentor.misc.ProposalListener;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;
import com.example.mentor.schedule_meeting;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class peoples extends Fragment implements UserListener, ProposalListener {

    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FragmentPeoplesBinding binding;
    private String searchKey, searchMethod, searchSort;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPeoplesBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        binding.fabSearch.setVisibility(View.GONE);

        searchMethod = getResources().getString(R.string.Mentor);
        searchSort = "";
        searchKey = "";

        binding.btnProposals.setOnClickListener(view -> {
            binding.fabSearch.setVisibility(View.GONE);
            updateSchedMeets();
        });
        binding.btnHistory.setOnClickListener(view -> getHistory());
        binding.btnFollowing.setOnClickListener(view -> getFollowing());

        binding.fabSearch.setOnClickListener(view -> {
            RelativeLayout.LayoutParams layoutSearch = (RelativeLayout.LayoutParams) binding.layoutSearch.getLayoutParams();
            if(layoutSearch.height == 0){
                binding.layoutSearch.setLayoutParams(new RelativeLayout.LayoutParams(layoutSearch.width, RelativeLayout.LayoutParams.WRAP_CONTENT));
                if(searchKey != null) {
                    if (!searchKey.trim().isEmpty()) {
                        binding.inpTXTSearchEngine.setText(searchKey);
                    }
                }
                binding.layoutSearch.setVisibility(View.VISIBLE);
            } else {
                searchEngine();
                binding.layoutSearch.setLayoutParams(new RelativeLayout.LayoutParams(layoutSearch.width, 0));
                binding.layoutSearch.setVisibility(View.GONE);
            }
        });

        binding.cardMentor.setOnClickListener(view -> {
            searchMethod = getResources().getString(R.string.Mentor);
            binding.cardMentor.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(),R.color.blue));
            binding.txtCardMentor.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white));
            binding.cardSubject.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(),R.color.white));
            binding.txtCardSubject.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.black));
        });

        binding.cardSubject.setOnClickListener(view -> {
            searchMethod = getResources().getString(R.string.Subject);
            binding.cardSubject.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(),R.color.blue));
            binding.txtCardSubject.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white));
            binding.cardMentor.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(),R.color.white));
            binding.txtCardMentor.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.black));
        });

        binding.cardRating.setOnClickListener(view -> {
            if(searchSort.isEmpty()||searchSort.equals("null")||searchSort.equals(getResources().getString(R.string.feeAscend))||searchSort.equals(getResources().getString(R.string.feeDescend))) {
                searchSort = getResources().getString(R.string.ratingAscend);
                binding.cardRating.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.blue));
                binding.txtCardRating.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.imgCardRating.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_keyboard_arrow_up_24));
                binding.imgCardRating.setVisibility(View.VISIBLE);
                binding.cardFee.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.txtCardFee.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black));
                binding.imgCardFee.setVisibility(View.GONE);
            }
            else if(searchSort.equals(getResources().getString(R.string.ratingAscend))) {
                searchSort = getResources().getString(R.string.ratingDescend);
                binding.cardRating.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.blue));
                binding.txtCardRating.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.imgCardRating.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_keyboard_arrow_down_24));
                binding.imgCardRating.setVisibility(View.VISIBLE);
                binding.cardFee.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.txtCardFee.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black));
            }
            else if(searchSort.equals(getResources().getString(R.string.ratingDescend))) {
                searchSort = "";
                binding.cardRating.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.txtCardRating.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black));
                binding.imgCardRating.setVisibility(View.GONE);
                binding.cardFee.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.txtCardFee.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black));
            }
        });

        binding.cardFee.setOnClickListener(view -> {
            if(searchSort.isEmpty()||searchSort.equals("null")||searchSort.equals(getResources().getString(R.string.ratingAscend))||searchSort.equals(getResources().getString(R.string.ratingDescend))) {
                searchSort = getResources().getString(R.string.feeAscend);
                binding.cardFee.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.blue));
                binding.txtCardFee.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.imgCardFee.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_keyboard_arrow_up_24));
                binding.imgCardFee.setVisibility(View.VISIBLE);
                binding.cardRating.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.txtCardRating.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black));
                binding.imgCardRating.setVisibility(View.GONE);
            }
            else if(searchSort.equals(getResources().getString(R.string.feeAscend))) {
                searchSort = getResources().getString(R.string.feeDescend);
                binding.cardFee.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.blue));
                binding.txtCardFee.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.imgCardFee.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_keyboard_arrow_down_24));
                binding.imgCardFee.setVisibility(View.VISIBLE);
                binding.cardRating.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.txtCardRating.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black));
            }
            else if(searchSort.equals(getResources().getString(R.string.feeDescend))) {
                searchSort = "";
                binding.cardFee.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.txtCardFee.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black));
                binding.imgCardFee.setVisibility(View.GONE);
                binding.cardRating.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(), R.color.white));
                binding.txtCardRating.setTextColor(AppCompatResources.getColorStateList(requireContext(), R.color.black));
            }
        });

        updateSchedMeets();

        return viewLayout;
    }

    private void searchEngine() {
        Log.i("searchMethod SE", searchMethod);
        if(searchMethod.equals(getResources().getString(R.string.Mentor))){
            binding.cardMentor.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(),R.color.blue));
            binding.txtCardMentor.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white));
        } else if(searchMethod.equals(getResources().getString(R.string.Subject))){
            binding.cardSubject.setCardBackgroundColor(AppCompatResources.getColorStateList(requireContext(),R.color.blue));
            binding.txtCardSubject.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white));
        }
        if(!String.valueOf(binding.inpTXTSearchEngine.getText()).trim().isEmpty() && !String.valueOf(binding.inpTXTSearchEngine.getText()).trim().equals("null")){
            searchKey = String.valueOf(binding.inpTXTSearchEngine.getText()).trim();
            Log.i("searchKey searchEngine", searchKey);
        } else{
            searchKey="";
        }

        if(binding.btnHistory.getTextColors().equals(AppCompatResources.getColorStateList(requireContext(),R.color.white))){
            getHistory();
        }
        else if(binding.btnFollowing.getTextColors().equals(AppCompatResources.getColorStateList(requireContext(),R.color.white))){
            getFollowing();
        }
    }

    private void getSchedMeets() {
        binding.layoutNoResults.setVisibility(View.GONE);
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.btnProposals.setBackgroundColor(getResources().getColor(R.color.blue,null));
        binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.white));
        binding.btnHistory.setBackgroundColor(getResources().getColor(R.color.transparent, null));
        binding.btnHistory.setTextColor(this.requireContext().getColor(R.color.blue));
        binding.btnFollowing.setBackgroundColor(getResources().getColor(R.color.transparent, null));
        binding.btnFollowing.setTextColor(this.requireContext().getColor(R.color.blue));
        fStore.collection("Users").document(Account_Details.User_Details.getUID()).collection("proposals").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if(task.getResult().size() > 0) {
                    List<Proposal> list_proposal = new ArrayList<>();
                    for (QueryDocumentSnapshot qDocSnap : task.getResult()) {

                        Proposal proposal = new Proposal();

                        Log.i("proposals", "list proposals");

                        if(qDocSnap.get("hideUsers") != null) {
                            ArrayList<String> hideUsersUID = (ArrayList<String>) qDocSnap.get("hideUsers");
                            assert hideUsersUID != null;
                            if (hideUsersUID.contains(qDocSnap.getString("requesteeUID")) || hideUsersUID.contains(qDocSnap.getString("requestorUID"))) {
                                continue;
                            }
                        }
                        Long status = qDocSnap.getLong("status");
                        assert status != null;

                        if (status.intValue() == 4) {
                            if(Account_Details.User_Details.getUID().equals(qDocSnap.getString("requesteeUID"))) {
                                proposal.picString = qDocSnap.getString("requestorPic");
                                proposal.requestorUID = qDocSnap.getString("requestorUID");
                                proposal.fullName = qDocSnap.getString("requestorName");
                                proposal.uid = qDocSnap.getId();
                                proposal.subject = qDocSnap.getString("subject");
                                proposal.date = qDocSnap.getString("date");
                                list_proposal.add(proposal);
                            }else if(Account_Details.User_Details.getUID().equals(qDocSnap.getString("requestorUID"))) {
                                proposal.picString = qDocSnap.getString("requesteePic");
                                proposal.requesteeUID = qDocSnap.getString("requesteeUID");
                                proposal.fullName = qDocSnap.getString("requesteeName");
                                proposal.uid = qDocSnap.getId();
                                proposal.subject = qDocSnap.getString("subject");
                                proposal.date = qDocSnap.getString("date");
                                list_proposal.add(proposal);
                            }
                        }
                    }
                    if(list_proposal.size()>0){
                        binding.progressBar.setVisibility(View.GONE);
                        GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(),2);
                        binding.recyclerUsers.setLayoutManager(mGridLayoutManager);
                        UsersScheduleAdapter usersAdapter = new UsersScheduleAdapter(list_proposal, this);
                        binding.recyclerUsers.setAdapter(usersAdapter);
                        binding.recyclerUsers.setHasFixedSize(true);

                        binding.recyclerUsers.setVisibility(View.VISIBLE);
                    } else{
                        Log.i("proposals", "no list");
                        binding.progressBar.setVisibility(View.GONE);
                        binding.layoutNoResults.setVisibility(View.VISIBLE);
                        binding.txtNoResult.setText(R.string.noSchedules);
                    }
                } else{
                    binding.progressBar.setVisibility(View.GONE);
                    binding.layoutNoResults.setVisibility(View.VISIBLE);
                    binding.txtNoResult.setText(R.string.noSchedules);
                }
            }  else{
                binding.progressBar.setVisibility(View.GONE);
                binding.layoutNoResults.setVisibility(View.VISIBLE);
                binding.txtNoResult.setText(R.string.noSchedules);
            }
        });
    }

    private void getHistory() {
        binding.layoutNoResults.setVisibility(View.GONE);
        if(!Account_Details.User_Details.getIsMentor()) {
            binding.fabSearch.setVisibility(View.VISIBLE);
        }
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.btnHistory.setBackgroundColor(getResources().getColor(R.color.blue, null));
        binding.btnHistory.setTextColor(this.requireContext().getColor(R.color.white));
        binding.btnProposals.setBackgroundColor(getResources().getColor(R.color.transparent, null));
        binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        binding.btnFollowing.setBackgroundColor(getResources().getColor(R.color.transparent, null));
        binding.btnFollowing.setTextColor(this.requireContext().getColor(R.color.blue));
        fStore.collection("Users").document(Account_Details.User_Details.getUID()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.get("transactionHistory") != null) {
                ArrayList<String> uniqueID = (ArrayList<String>) documentSnapshot.get("transactionHistory");
                assert uniqueID != null;
                if(documentSnapshot.get("hideUsers") != null) {
                    ArrayList<String> hideUsersUID = (ArrayList<String>) documentSnapshot.get("hideUsers");
                    assert hideUsersUID != null;
                    for (int i=0; i<hideUsersUID.size(); i++) {
                        if (uniqueID.contains(hideUsersUID.get(i))) {
                            uniqueID.remove(hideUsersUID.get(i));
                        }
                    }
                }
                fStore.collection("Users").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        if (task1.getResult().size() > 0) {
                            List<User> list_users = new ArrayList<>();
                            for (QueryDocumentSnapshot qDocSnap : task1.getResult()) {
                                if(searchKey != null) {
                                    if (!searchKey.trim().isEmpty()) {
                                        if (searchMethod.equals(getResources().getString(R.string.Mentor))) {
                                            String fullname = qDocSnap.getString("fullName");
                                            if(fullname!=null) {
                                                if (!fullname.toLowerCase(Locale.ROOT).contains(searchKey.toLowerCase(Locale.ROOT))) {
                                                    continue;
                                                }
                                            }
                                        } else if (searchMethod.equals(getResources().getString(R.string.Subject))) {
                                            ArrayList<String> subject = (ArrayList<String>) qDocSnap.get("subjects");
                                            if(subject != null) {
                                                for (int i = 0; i < subject.size(); i++) {
                                                    String toLowerCase = subject.get(i).toLowerCase(Locale.ROOT);
                                                    subject.set(i, toLowerCase);
                                                }
                                                if (!subject.contains(searchKey.toLowerCase(Locale.ROOT))) {
                                                    continue;
                                                }
                                            }
                                        }
                                    } else {Log.i("searchKey", "empty");}
                                } else {Log.i("searchKey", "null");}

                                if (uniqueID.contains(qDocSnap.getId())) {
                                    User user = new User();
                                    user.uid = qDocSnap.getId();
                                    user.isMentor = qDocSnap.getBoolean("isMentor");
                                    user.authLvl = Objects.requireNonNull(qDocSnap.getLong("authLevel")).intValue();
                                    user.fullName = qDocSnap.getString("fullName");
                                    user.pictureStr = qDocSnap.getString("picture");
                                    user.email = qDocSnap.getString("email");
                                    user.isAccepting = qDocSnap.getBoolean("isAccepting");

                                    Map<String, Long> fees = (Map<String, Long>) qDocSnap.get("subjectRates");
                                    if(fees!=null) {
                                        List<Map.Entry<String, Long>> list = new LinkedList<>(fees.entrySet());

                                        if (searchKey.isEmpty() || !searchMethod.equals(getResources().getString(R.string.Subject))) {
                                            list.sort(Map.Entry.comparingByValue());
                                            for (int i = 0; i < list.size(); i++) {
                                                if (list.get(i).getValue() > 0L) {
                                                    user.minFee = list.get(i).getValue();
                                                    break;
                                                }
                                            }
                                            Collections.reverse(list);
                                            user.maxFee = list.get(0).getValue();
                                        } else if (searchMethod.equals(getResources().getString(R.string.Subject))) {
                                            Map<String, Long> toLowerMap = new HashMap<>();
                                            for (int i = 0; i < list.size(); i++) {
                                                toLowerMap.put(list.get(i).getKey().toLowerCase(Locale.ROOT), list.get(i).getValue());
                                            }
                                            user.minFee = toLowerMap.get(searchKey.toLowerCase(Locale.ROOT));
                                            user.maxFee = toLowerMap.get(searchKey.toLowerCase(Locale.ROOT));
                                        }
                                    }

                                    if(qDocSnap.getString("picString") != null) {
                                        String picString = qDocSnap.getString("picString");
                                        if (picString != null && !picString.isEmpty() && !picString.equals("null")) {
                                            user.pictureStr = qDocSnap.getString("picString");
                                        }
                                    }
                                    if(user.isMentor) {
                                        Long ratingAve = qDocSnap.getLong("ratingAve");
                                        if(ratingAve != null) {
                                            user.rating = ratingAve.intValue();
                                        }
                                    }
                                    list_users.add(user);
                                }
                            }

                            if(!searchSort.isEmpty()) {
                                if(searchSort.equals(getResources().getString(R.string.ratingAscend))) {
                                    list_users.sort(Comparator.comparing(u -> u.rating));
                                }
                                else if(searchSort.equals(getResources().getString(R.string.ratingDescend))) {
                                    list_users.sort(Comparator.comparing(u -> u.rating));
                                    Collections.reverse(list_users);
                                }
                                else if(searchSort.equals(getResources().getString(R.string.feeAscend))) {
                                    list_users.sort(Comparator.comparing(u -> u.minFee));
                                }
                                else if(searchSort.equals(getResources().getString(R.string.feeDescend))) {
                                    list_users.sort(Comparator.comparing(u -> u.maxFee));
                                    Collections.reverse(list_users);
                                }
                            }

                            if (list_users.size() > 0) {
                                binding.progressBar.setVisibility(View.GONE);
                                GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(),2);
                                binding.recyclerUsers.setLayoutManager(mGridLayoutManager);
                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                                UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                                binding.recyclerUsers.setAdapter(usersAdapter);
                                binding.recyclerUsers.setHasFixedSize(true);

                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                            }  else{
                                binding.progressBar.setVisibility(View.GONE);
                                binding.layoutNoResults.setVisibility(View.VISIBLE);
                                binding.txtNoResult.setText(R.string.noUsers);
                            }
                        }  else{
                            binding.progressBar.setVisibility(View.GONE);
                            binding.layoutNoResults.setVisibility(View.VISIBLE);
                            binding.txtNoResult.setText(R.string.noUsers);
                        }
                    } else{
                        binding.progressBar.setVisibility(View.GONE);
                        binding.layoutNoResults.setVisibility(View.VISIBLE);
                        binding.txtNoResult.setText(R.string.noUsers);
                    }
                });
            }
            else{
                binding.progressBar.setVisibility(View.GONE);
                binding.layoutNoResults.setVisibility(View.VISIBLE);
                binding.txtNoResult.setText(R.string.noUsers);
            }
        });
    }

    private void getFollowing() {
        binding.layoutNoResults.setVisibility(View.GONE);
        if(!Account_Details.User_Details.getIsMentor()) {
            binding.fabSearch.setVisibility(View.VISIBLE);
        }
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.btnFollowing.setBackgroundColor(getResources().getColor(R.color.blue, null));
        binding.btnFollowing.setTextColor(this.requireContext().getColor(R.color.white));
        binding.btnHistory.setBackgroundColor(getResources().getColor(R.color.transparent, null));
        binding.btnHistory.setTextColor(this.requireContext().getColor(R.color.blue));
        binding.btnProposals.setBackgroundColor(getResources().getColor(R.color.transparent, null));
        binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        fStore.collection("Users").document(Account_Details.User_Details.getUID()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.get("followings") != null) {
                ArrayList<String> uniqueID = (ArrayList<String>) documentSnapshot.get("followings");
                assert uniqueID != null;
                if(documentSnapshot.get("hideUsers") != null) {
                    ArrayList<String> hideUsersUID = (ArrayList<String>) documentSnapshot.get("hideUsers");
                    assert hideUsersUID != null;
                    for (int i=0; i<hideUsersUID.size(); i++) {
                        if (uniqueID.contains(hideUsersUID.get(i))) {
                            uniqueID.remove(hideUsersUID.get(i));
                        }
                    }
                }
                fStore.collection("Users").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        if (task1.getResult().size() > 0) {
                            List<User> list_users = new ArrayList<>();
                            for (QueryDocumentSnapshot qDocSnap : task1.getResult()) {
                                if(searchKey != null) {
                                    if (!searchKey.trim().isEmpty()) {
                                        if (searchMethod.equals(getResources().getString(R.string.Mentor))) {
                                            String fullname = qDocSnap.getString("fullName");
                                            if(fullname!=null) {
                                                if (!fullname.toLowerCase(Locale.ROOT).contains(searchKey.toLowerCase(Locale.ROOT))) {
                                                    continue;
                                                }
                                            }
                                        } else if (searchMethod.equals(getResources().getString(R.string.Subject))) {
                                            ArrayList<String> subject = (ArrayList<String>) qDocSnap.get("subjects");
                                            if(subject != null) {
                                                for (int i = 0; i < subject.size(); i++) {
                                                    String toLowerCase = subject.get(i).toLowerCase(Locale.ROOT);
                                                    subject.set(i, toLowerCase);
                                                }
                                                if (!subject.contains(searchKey.toLowerCase(Locale.ROOT))) {
                                                    continue;
                                                }
                                            }
                                        }
                                    } else {Log.i("searchKey", "empty");}
                                } else {Log.i("searchKey", "null");}

                                if (uniqueID.contains(qDocSnap.getId())) {
                                    User user = new User();
                                    user.uid = qDocSnap.getId();
                                    user.isMentor = qDocSnap.getBoolean("isMentor");
                                    user.authLvl = Objects.requireNonNull(qDocSnap.getLong("authLevel")).intValue();
                                    user.fullName = qDocSnap.getString("fullName");
                                    user.pictureStr = qDocSnap.getString("picture");
                                    user.email = qDocSnap.getString("email");
                                    user.isAccepting = qDocSnap.getBoolean("isAccepting");

                                    Map<String, Long> fees = (Map<String, Long>) qDocSnap.get("subjectRates");
                                    if(fees!=null) {
                                        List<Map.Entry<String, Long>> list = new LinkedList<>(fees.entrySet());

                                        if (searchKey.isEmpty() || !searchMethod.equals(getResources().getString(R.string.Subject))) {
                                            list.sort(Map.Entry.comparingByValue());
                                            for (int i = 0; i < list.size(); i++) {
                                                if (list.get(i).getValue() > 0L) {
                                                    user.minFee = list.get(i).getValue();
                                                    break;
                                                }
                                            }
                                            Collections.reverse(list);
                                            user.maxFee = list.get(0).getValue();
                                        } else if (searchMethod.equals(getResources().getString(R.string.Subject))) {
                                            Map<String, Long> toLowerMap = new HashMap<>();
                                            for (int i = 0; i < list.size(); i++) {
                                                toLowerMap.put(list.get(i).getKey().toLowerCase(Locale.ROOT), list.get(i).getValue());
                                            }
                                            user.minFee = toLowerMap.get(searchKey.toLowerCase(Locale.ROOT));
                                            user.maxFee = toLowerMap.get(searchKey.toLowerCase(Locale.ROOT));
                                        }
                                    }

                                    if(user.isMentor) {
                                        Long ratingAve = qDocSnap.getLong("ratingAve");
                                        if(ratingAve != null) {
                                            user.rating = ratingAve.intValue();
                                        }
                                    }
                                    if(qDocSnap.getString("picString") != null) {
                                        String picString = qDocSnap.getString("picString");
                                        if (picString != null && !picString.isEmpty() && !picString.equals("null")) {
                                            user.pictureStr = qDocSnap.getString("picString");
                                        }
                                    }

                                    list_users.add(user);
                                }
                            }

                            if(!searchSort.isEmpty()) {
                                if(searchSort.equals(getResources().getString(R.string.ratingAscend))) {
                                    list_users.sort(Comparator.comparing(u -> u.rating));
                                }
                                else if(searchSort.equals(getResources().getString(R.string.ratingDescend))) {
                                    list_users.sort(Comparator.comparing(u -> u.rating));
                                    Collections.reverse(list_users);
                                }
                                else if(searchSort.equals(getResources().getString(R.string.feeAscend))) {
                                    list_users.sort(Comparator.comparing(u -> u.minFee));
                                }
                                else if(searchSort.equals(getResources().getString(R.string.feeDescend))) {
                                    list_users.sort(Comparator.comparing(u -> u.maxFee));
                                    Collections.reverse(list_users);
                                }
                            }

                            if (list_users.size() > 0) {
                                binding.progressBar.setVisibility(View.GONE);
                                GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
                                binding.recyclerUsers.setLayoutManager(mGridLayoutManager);
                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                                UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                                binding.recyclerUsers.setAdapter(usersAdapter);
                                binding.recyclerUsers.setHasFixedSize(true);

                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                            }  else{
                                binding.progressBar.setVisibility(View.GONE);
                                binding.layoutNoResults.setVisibility(View.VISIBLE);
                                binding.txtNoResult.setText(R.string.noUsers);
                            }
                        }  else{
                            binding.progressBar.setVisibility(View.GONE);
                            binding.layoutNoResults.setVisibility(View.VISIBLE);
                            binding.txtNoResult.setText(R.string.noUsers);
                        }
                    }  else{
                        binding.progressBar.setVisibility(View.GONE);
                        binding.layoutNoResults.setVisibility(View.VISIBLE);
                        binding.txtNoResult.setText(R.string.noUsers);
                    }
                });
            }
            else{
                binding.progressBar.setVisibility(View.GONE);
                binding.layoutNoResults.setVisibility(View.VISIBLE);
                binding.txtNoResult.setText(R.string.noUsers);
            }
        });
    }

    @Override
    public void onUserClicked(User user) {
        Account_Details.User_Clicked.setUID(user.uid);
        Account_Details.User_Clicked.setPicString(user.pictureStr);
        Account_Details.User_Clicked.setFullName(user.fullName);
        Account_Details.User_Clicked.setEmail(user.email);
        Account_Details.User_Clicked.setIsMentor(user.isMentor);
        Account_Details.User_Clicked.setIsAccepting(user.isAccepting);
        Account_Details.User_Clicked.setAuthLevel(user.authLvl);

        Bundle bundle = new Bundle();
        bundle.putBoolean("isHome",false);

        Fragment fragment2 = new user_Preview();
        fragment2.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment2, "user_Preview")
                .commit();
    }

    @Override
    public void onUserClicked(Proposal proposal) {
        if(Account_Details.User_Details.getIsMentor()) {
            Account_Details.User_Clicked.setUID(proposal.requestorUID);
        }else {
            Account_Details.User_Clicked.setUID(proposal.requesteeUID);
        }
        Account_Details.User_Clicked.setPicString(proposal.picString);
        Account_Details.User_Clicked.setFullName(proposal.fullName);

        Bundle bundle = new Bundle();
        bundle.putString("reqUID",proposal.uid);

        Fragment fragment2 = new schedule_meeting();
        fragment2.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment2, "schedule_meeting")
                .commit();
    }

    private void updateSchedMeets() {
        fStore.collection("Users").document(Account_Details.User_Details.getUID()).collection("proposals").get().addOnCompleteListener(task -> {
           if(task.isSuccessful()){
               if(task.getResult().size()>0){
                   for(QueryDocumentSnapshot qDocSnap : task.getResult()){

                       if(qDocSnap.getLong("status") != null) {
                           Long status = qDocSnap.getLong("status");
                           assert status != null;
                           if (status == 4) {

                               if (Account_Details.User_Details.getIsMentor()) {
                                   Log.i("updateSched", "user is mentor");

                                   String uid = qDocSnap.getString("requestorUID");
                                   String fullName = qDocSnap.getString("requestorName");
                                   String picString = qDocSnap.getString("requestorPic");
                                   fStore.collection("Users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                                       if (!fullName.equals(documentSnapshot.getString("fullName")) || !picString.equals(documentSnapshot.getString("picString"))) {
                                           Map<String, Object> update = new HashMap<>();
                                           update.put("requestorName", documentSnapshot.getString("fullName"));
                                           update.put("requestorPic", documentSnapshot.getString("picString"));

                                           Log.i("updateSched", "update df");
                                           DocumentReference df = fStore.collection("Users").document(Account_Details.User_Details.getUID()).collection("proposals").document(qDocSnap.getId());
                                           df.update(update);
                                       }
                                       getSchedMeets();
                                   });
                               } else {
                                   Log.i("updateSched", "user is student");

                                   String uid = qDocSnap.getString("requesteeUID");
                                   String fullName = qDocSnap.getString("requesteeName");
                                   String picString = "";
                                   if(qDocSnap.getString("requesteePic") != null){
                                       picString = qDocSnap.getString("requesteePic");
                                   }
                                   String finalPicString = picString;
                                   fStore.collection("Users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                                       String picStringUser="";
                                       if(documentSnapshot.getString("picString") != null){
                                           picStringUser = documentSnapshot.getString("picString");
                                       }
                                       if (!fullName.equals(documentSnapshot.getString("fullName")) || !finalPicString.equals(picStringUser)) {
                                           Map<String, Object> update = new HashMap<>();
                                           update.put("requesteeName", documentSnapshot.getString("fullName"));
                                           update.put("requesteePic", documentSnapshot.getString("picString"));

                                           Log.i("updateSched", "update df");
                                           Log.i("updateSched", "update name " + documentSnapshot.getString("fullName"));
                                           Log.i("updateSched", "update pic" + documentSnapshot.getString("picString"));

                                           DocumentReference df = fStore.collection("Users").document(Account_Details.User_Details.getUID()).collection("proposals").document(qDocSnap.getId());
                                           df.update(update);
                                       }
                                       getSchedMeets();
                                   });
                               }

                           }
                       }
                   }
               } else {Log.i("updateSched", "task zero");}
           } else {Log.i("updateSched", "task unsuccessful");}
        });
    }
}