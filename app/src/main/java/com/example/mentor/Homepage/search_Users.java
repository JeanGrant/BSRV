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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
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
            Account_Details.User_Details.setCurrSearch(false);
            binding.btnSendProposal.setVisibility(View.GONE);
            getProposals();
        }else {
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
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                    binding.recyclerUsers.setLayoutManager(mLinearLayoutManager);
                    binding.recyclerUsers.setVisibility(View.VISIBLE);
                    UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                    binding.recyclerUsers.setAdapter(usersAdapter);
                    binding.recyclerUsers.setHasFixedSize(true);
                } else{
                    Toast.makeText(getContext(), "No users found", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), "Error getting list of users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProposals(){
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        String fUser = Account_Details.User_Details.getUID();
        List<String> list_uid = new ArrayList<>();
        //List unique UIDs in proposals
        Log.d("recyclerProposals", "list unique UIDs");
        fStore.collection("Users").document(fUser).collection("proposals").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                    Long status = queryDocumentSnapshot.getLong("status");
                    assert status != null;

                    if(queryDocumentSnapshot.get("hideUsers") != null) {
                        ArrayList<String> hideUsersUID = (ArrayList<String>) queryDocumentSnapshot.get("hideUsers");
                        assert hideUsersUID != null;
                        if (hideUsersUID.contains(queryDocumentSnapshot.getId())) {
                            continue;
                        }
                    }

                    if (fUser.equals(queryDocumentSnapshot.getString("requestorUID"))) {
                        if (list_uid.size() > 0) {
                            if (list_uid.contains(queryDocumentSnapshot.getString("requesteeUID")) && status.intValue()<4) {
                                continue;
                            }
                        }
                        list_uid.add(queryDocumentSnapshot.getString("requesteeUID"));
                    } else if (fUser.equals(queryDocumentSnapshot.getString("requesteeUID")) && status.intValue()<4) {
                        if (list_uid.size() > 0) {
                            if (list_uid.contains(queryDocumentSnapshot.getString("requestorUID"))) {
                                continue;
                            }
                        }
                        list_uid.add(queryDocumentSnapshot.getString("requestorUID"));
                    }
                }
                Log.d("recyclerProposals", "list_uid size" + list_uid.size());
                fStore.collection("Users").get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful() && task1.getResult() != null) {
                        List<User> list_users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task1.getResult()) {

                            if(!list_uid.contains(queryDocumentSnapshot.getId())){
                                continue;
                            }

                            if(searchKey != null) {
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
                                        if(!subject.contains(searchKey.toLowerCase(Locale.ROOT))){
                                            continue;
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
                            List<Map.Entry<String, Long>> list = new LinkedList<>(fees.entrySet());

                            if(searchKey.isEmpty()||!searchMethod.equals(getResources().getString(R.string.Subject))) {
                                list.sort(Map.Entry.comparingByValue());
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getValue() > 0L) {
                                        user.minFee = list.get(i).getValue();
                                        break;
                                    }
                                }
                                Collections.reverse(list);
                                user.maxFee = list.get(0).getValue();
                            }else if(searchMethod.equals(getResources().getString(R.string.Subject))){
                                Map<String, Long> toLowerMap = new HashMap<>();
                                for(int i = 0; i < list.size(); i++) {
                                    toLowerMap.put(list.get(i).getKey().toLowerCase(Locale.ROOT), list.get(i).getValue());
                                }
                                Log.i("toLowerMap", toLowerMap.toString());
                                user.minFee = toLowerMap.get(searchKey.toLowerCase(Locale.ROOT));
                                user.maxFee = toLowerMap.get(searchKey.toLowerCase(Locale.ROOT));
                            }
                            Log.i("price range", user.fullName + ": " + user.minFee + " - " + user.maxFee);

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
                            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
                            binding.recyclerUsers.setLayoutManager(mLinearLayoutManager);
                            binding.recyclerUsers.setVisibility(View.VISIBLE);
                            UsersAdapter usersAdapter = new UsersAdapter(list_users, this);
                            binding.recyclerUsers.setAdapter(usersAdapter);
                            binding.recyclerUsers.setHasFixedSize(true);
                        }else{Toast.makeText(getContext(), "No proposals found", Toast.LENGTH_SHORT).show();}}
                    else{Toast.makeText(getContext(), "Error getting list of proposals", Toast.LENGTH_SHORT).show();}
                });
                if (list_uid.isEmpty()) {Toast.makeText(requireContext(), "No proposals found", Toast.LENGTH_SHORT).show();}
            }else{Toast.makeText(getContext(), "Error getting list of proposals", Toast.LENGTH_SHORT).show();}
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
            binding.btnSendProposal.setBackgroundResource(R.drawable.roundedbutton_blue);
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.blue));
        }else {
            binding.btnProposals.setBackgroundResource(R.drawable.roundedbutton_blue);
            binding.btnProposals.setTextColor(this.requireContext().getColor(R.color.white));
            binding.btnSendProposal.setBackgroundResource(R.drawable.roundedbutton_blue_outline);
            binding.btnSendProposal.setTextColor(this.requireContext().getColor(R.color.blue));
        }
    }
}