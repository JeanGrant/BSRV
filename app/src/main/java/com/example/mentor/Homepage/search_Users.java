package com.example.mentor.Homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.adapters.UsersProposalsAdapter;
import com.example.mentor.databinding.FragmentSearchUsersBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;
import com.google.firebase.auth.FirebaseAuth;
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

public class search_Users extends Fragment implements UserListener {

    private FragmentSearchUsersBinding binding;
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private String searchKey, searchMethod, searchSort;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchUsersBinding.inflate(inflater, container, false);
        View viewLayout = binding.getRoot();

        searchMethod = getResources().getString(R.string.Mentor);
        searchSort = "";
        searchKey = "";

        if(Account_Details.User_Details.getIsMentor()){
            binding.fabSearch.setVisibility(View.GONE);
            Account_Details.User_Details.setCurrSearch(false);
            binding.btnSendProposal.setVisibility(View.GONE);
            Log.d("recyclerProposals", "getProposals");
            getProposals();
        }else {
            binding.fabSearch.setVisibility(View.VISIBLE);
            if (Account_Details.User_Details.getCurrSearch()) {getHideUsers();}
            else {getProposals();}
            initSwitches();
        }

        binding.btnProposals.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(false);
            getProposals();
            initSwitches();
        });
        binding.btnSendProposal.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(true);
            getHideUsers();
            initSwitches();
        });

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

        if(Account_Details.User_Details.getCurrSearch()) {getHideUsers();}
        else{getProposals();}
    }

    private void getHideUsers() {
        fStore.collection("Users").document(Account_Details.User_Details.getUID()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.get("hideUsers") != null){
                ArrayList<String> hideUsersUID = (ArrayList<String>) documentSnapshot.get("hideUsers");
                assert hideUsersUID != null;
                Log.i("hideUsers", hideUsersUID.toString());
                getUsers(hideUsersUID);
            }else {
                Log.i("hideUsers", "Array null");
                ArrayList<String> hideUsersUID = new ArrayList<>();
                getUsers(hideUsersUID);
            }
        });
    }

    private void getUsers(ArrayList<String> hideUsersUID){
        Log.i("list_users","getUsers");
        binding.layoutNoResults.setVisibility(View.GONE);
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        fStore.collection("Users").get().addOnCompleteListener(task -> {
            String currentUserId = FirebaseAuth.getInstance().getUid();
            if(task.isSuccessful() && task.getResult() != null){
                List<User> list_users = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    assert currentUserId != null;
                    //skip if current loaded user is the logged in user
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    //skip if current loaded user's account type is the same as logged in user
                    if (queryDocumentSnapshot.getBoolean("isMentor") == Account_Details.User_Details.getIsMentor()){
                        continue;
                    }
                    Long authLvl = queryDocumentSnapshot.getLong("authLevel");
                    assert authLvl != null;
                    if(authLvl.intValue()!=2) {continue;}

                    if(hideUsersUID.contains(queryDocumentSnapshot.getId())){
                        continue;
                    }

                    if(searchKey != null) {
                        if (!searchKey.trim().isEmpty()) {
                            if (searchMethod.equals(getResources().getString(R.string.Mentor))) {
                                String fullname = queryDocumentSnapshot.getString("fullName");
                                if(fullname!=null) {
                                    if (!fullname.toLowerCase(Locale.ROOT).contains(searchKey.toLowerCase(Locale.ROOT))) {
                                        continue;
                                    }
                                }
                            } else if (searchMethod.equals(getResources().getString(R.string.Subject))) {
                                ArrayList<String> subject = (ArrayList<String>) queryDocumentSnapshot.get("subjects");
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


                    User user = new User();
                    user.uid = queryDocumentSnapshot.getId();
                    user.isMentor = queryDocumentSnapshot.getBoolean("isMentor");
                    user.authLvl = Objects.requireNonNull(queryDocumentSnapshot.getLong("authLevel")).intValue();
                    user.fullName = queryDocumentSnapshot.getString("fullName");
                    user.pictureStr= queryDocumentSnapshot.getString("picture");
                    user.email = queryDocumentSnapshot.getString("email");
                    user.isAccepting = queryDocumentSnapshot.getBoolean("isAccepting");

                    Map<String, Long> fees = (Map<String, Long>) queryDocumentSnapshot.get("subjectRates");
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

                    if(queryDocumentSnapshot.getString("picString") != null) {
                        String picString = queryDocumentSnapshot.getString("picString");
                        if (picString != null && !picString.isEmpty() && !picString.equals("null")) {
                            user.pictureStr = queryDocumentSnapshot.getString("picString");
                        }
                    }

                    if(user.isMentor) {
                        Long ratingAve = queryDocumentSnapshot.getLong("ratingAve");
                        if(ratingAve != null) {
                            user.rating = ratingAve.intValue();
                        } else { user.rating = 0; }
                    }
                    list_users.add(user);
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

                if(list_users.size()>0){
                    binding.progressBar.setVisibility(View.GONE);
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
                    binding.recyclerUsers.setLayoutManager(mGridLayoutManager);
                    binding.recyclerUsers.setVisibility(View.VISIBLE);
                    UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                    binding.recyclerUsers.setAdapter(usersAdapter);
                    binding.recyclerUsers.setHasFixedSize(true);
                } else{
                    binding.progressBar.setVisibility(View.GONE);
                    binding.layoutNoResults.setVisibility(View.VISIBLE);
                    binding.txtNoResult.setText(R.string.noUsers);
                }
            }else{
                binding.progressBar.setVisibility(View.GONE);
                binding.layoutNoResults.setVisibility(View.VISIBLE);
                binding.txtNoResult.setText(R.string.noUsers);
            }
        });
    }

    private void getProposals() {
        Log.i("list_users","getProposals");
        binding.layoutNoResults.setVisibility(View.GONE);
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String fUser = Account_Details.User_Details.getUID();
        List<String> list_uid = new ArrayList<>();
        //List unique UIDs in proposals
        Log.d("recyclerProposals", "list unique UIDs");
        fStore.collection("Users").document(fUser).collection("proposals").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Log.i("list_users","fStore proposals");
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                    Long status = queryDocumentSnapshot.getLong("status");
                    assert status != null;

                    if (queryDocumentSnapshot.get("hideUsers") != null) {
                        ArrayList<String> hideUsersUID = (ArrayList<String>) queryDocumentSnapshot.get("hideUsers");
                        assert hideUsersUID != null;
                        if (hideUsersUID.contains(queryDocumentSnapshot.getId())) {
                            continue;
                        }
                    }

                    if (fUser.equals(queryDocumentSnapshot.getString("requestorUID"))) {
                        Log.d("recyclerProposals", "fUser is requestor");
                        if (list_uid.size() > 0) {
                            if (list_uid.contains(queryDocumentSnapshot.getString("requesteeUID")) && status.intValue() < 4) {
                                continue;
                            }
                        }
                        list_uid.add(queryDocumentSnapshot.getString("requesteeUID"));
                    } else if (fUser.equals(queryDocumentSnapshot.getString("requesteeUID"))) {
                        Log.d("recyclerProposals", "fUser is requestee");
                        if (list_uid.size() > 0) {
                            if (list_uid.contains(queryDocumentSnapshot.getString("requestorUID")) && status.intValue() < 4) {
                                continue;
                            }
                        }
                        list_uid.add(queryDocumentSnapshot.getString("requestorUID"));
                    }
                }
                Log.d("recyclerProposals", "list_uid size" + list_uid.size());
                if(list_uid.size()>0) {
                    fStore.collection("Users").get().addOnCompleteListener(task1 -> {
                        Log.i("list_users", "fStore proposals task1");
                        if (task1.isSuccessful() && task1.getResult() != null) {
                            List<User> list_users = new ArrayList<>();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()) {

                                if (!list_uid.contains(queryDocumentSnapshot.getId())) {
                                    continue;
                                }

                                if (searchKey != null) {
                                    Log.i("searchKey not null", searchKey);
                                    if (!searchKey.trim().isEmpty()) {
                                        if (searchMethod.equals(getResources().getString(R.string.Mentor))) {
                                            if (!queryDocumentSnapshot.getString("fullName").toLowerCase(Locale.ROOT).contains(searchKey.toLowerCase(Locale.ROOT))) {
                                                continue;
                                            }
                                        } else if (searchMethod.equals(getResources().getString(R.string.Subject))) {
                                            ArrayList<String> subject = (ArrayList<String>) queryDocumentSnapshot.get("subjects");
                                            for (int i = 0; i < subject.size(); i++) {
                                                String toLowerCase = subject.get(i).toLowerCase(Locale.ROOT);
                                                subject.set(i, toLowerCase);
                                            }
                                            if (!subject.contains(searchKey.toLowerCase(Locale.ROOT))) {
                                                continue;
                                            }
                                        }
                                    } else {
                                        Log.i("searchKey", "empty");
                                    }
                                } else {
                                    Log.i("searchKey", "null");
                                }


                                User user = new User();
                                user.uid = queryDocumentSnapshot.getId();
                                user.isMentor = queryDocumentSnapshot.getBoolean("isMentor");
                                user.authLvl = Objects.requireNonNull(queryDocumentSnapshot.getLong("authLevel")).intValue();
                                user.fullName = queryDocumentSnapshot.getString("fullName");
                                user.pictureStr = queryDocumentSnapshot.getString("picture");
                                user.email = queryDocumentSnapshot.getString("email");
                                user.isAccepting = queryDocumentSnapshot.getBoolean("isAccepting");

                                if (user.isMentor) {
                                    Map<String, Long> fees = (Map<String, Long>) queryDocumentSnapshot.get("subjectRates");
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
                                        Log.i("toLowerMap", toLowerMap.toString());
                                        user.minFee = toLowerMap.get(searchKey.toLowerCase(Locale.ROOT));
                                        user.maxFee = toLowerMap.get(searchKey.toLowerCase(Locale.ROOT));
                                    }
                                }

                                if (queryDocumentSnapshot.getString("picString") != null) {
                                    String picString = queryDocumentSnapshot.getString("picString");
                                    if (picString != null && !picString.isEmpty() && !picString.equals("null")) {
                                        user.pictureStr = queryDocumentSnapshot.getString("picString");
                                    }
                                }

                                if (user.isMentor) {
                                    Long ratingAve = queryDocumentSnapshot.getLong("ratingAve");
                                    if (ratingAve != null) {
                                        user.rating = ratingAve.intValue();
                                    } else {
                                        user.rating = null;
                                    }
                                }

                                fStore.collection("Users").document(user.uid).collection("proposals").get().addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        if (task2.getResult().size() > 0) {
                                            Log.i("list_users", "fStore proposals task2");
                                            user.numRatingSent = 0;
                                            user.numRatingReceived = 0;
                                            for (QueryDocumentSnapshot qDocSnap : task2.getResult()) {
                                                if (user.isMentor) {
                                                    if (Account_Details.User_Details.getUID().equals(qDocSnap.getString("requestorUID"))) {
                                                        user.numRatingSent += 1;
                                                    }
                                                } else {
                                                    if (Account_Details.User_Details.getUID().equals(qDocSnap.getString("requesteeUID"))) {
                                                        user.numRatingReceived += 1;
                                                    }
                                                }
                                            }

                                            list_users.add(user);

                                            if (!searchSort.isEmpty()) {
                                                if (searchSort.equals(getResources().getString(R.string.ratingAscend))) {
                                                    list_users.sort(Comparator.comparing(u -> u.rating));
                                                } else if (searchSort.equals(getResources().getString(R.string.ratingDescend))) {
                                                    list_users.sort(Comparator.comparing(u -> u.rating));
                                                    Collections.reverse(list_users);
                                                } else if (searchSort.equals(getResources().getString(R.string.feeAscend))) {
                                                    list_users.sort(Comparator.comparing(u -> u.minFee));
                                                } else if (searchSort.equals(getResources().getString(R.string.feeDescend))) {
                                                    list_users.sort(Comparator.comparing(u -> u.maxFee));
                                                    Collections.reverse(list_users);
                                                }
                                            }
                                            Log.i("list_users", String.valueOf(list_users.size()));
                                            if (list_users.size() > 0) {
                                                binding.progressBar.setVisibility(View.GONE);
                                                GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);
                                                binding.recyclerUsers.setLayoutManager(mGridLayoutManager);
                                                binding.recyclerUsers.setVisibility(View.VISIBLE);
                                                UsersProposalsAdapter adapter = new UsersProposalsAdapter(list_users, this);
                                                binding.recyclerUsers.setAdapter(adapter);
                                                binding.recyclerUsers.setHasFixedSize(true);
                                            } else {
                                                Log.i("list_users", "no results");
                                                binding.progressBar.setVisibility(View.GONE);
                                                binding.layoutNoResults.setVisibility(View.VISIBLE);
                                                binding.txtNoResult.setText(R.string.noProposals);
                                            }
                                        } else {
                                            Log.i("list_users", "task2 no results");
                                            binding.progressBar.setVisibility(View.GONE);
                                            binding.layoutNoResults.setVisibility(View.VISIBLE);
                                            binding.txtNoResult.setText(R.string.noProposals);
                                        }
                                    } else {
                                        Log.i("list_users", "task2 unsuccessful");
                                        binding.progressBar.setVisibility(View.GONE);
                                        binding.layoutNoResults.setVisibility(View.VISIBLE);
                                        binding.txtNoResult.setText(R.string.noProposals);
                                    }
                                });
                            }
                        } else {
                            Log.i("list_users", "task1 failed");
                            binding.progressBar.setVisibility(View.GONE);
                            binding.layoutNoResults.setVisibility(View.VISIBLE);
                            binding.txtNoResult.setText(R.string.noProposals);
                        }
                    });
                } else {
                    Log.i("list_users", "task failed");
                    binding.progressBar.setVisibility(View.GONE);
                    binding.layoutNoResults.setVisibility(View.VISIBLE);
                    binding.txtNoResult.setText(R.string.noProposals);
                }
            }
            else {
                Log.i("list_users", "task failed");
                binding.progressBar.setVisibility(View.GONE);
                binding.layoutNoResults.setVisibility(View.VISIBLE);
                binding.txtNoResult.setText(R.string.noProposals);
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
        bundle.putBoolean("isHome",true);

        Fragment fragment2 = new user_Preview();
        fragment2.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment2, "user_Preview")
                .commit();
    }

    public void initSwitches() {
        if(Account_Details.User_Details.getCurrSearch()){
            binding.btnSendProposal.setBackgroundColor(getResources().getColor(R.color.blue,null));
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnProposals.setBackgroundColor(getResources().getColor(R.color.transparent,null));
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        }else {
            binding.btnProposals.setBackgroundColor(getResources().getColor(R.color.blue,null));
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnSendProposal.setBackgroundColor(getResources().getColor(R.color.transparent,null));
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.blue));
        }
    }
}