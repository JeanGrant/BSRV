package com.example.mentor.Homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.mentor.R;
import com.example.mentor.adapters.UsersAdapter;
import com.example.mentor.databinding.FragmentSearchUsersBinding;
import com.example.mentor.misc.Account_Details;
import com.example.mentor.misc.User;
import com.example.mentor.misc.UserListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Search_Users extends Fragment implements UserListener {

    View view;
    FragmentSearchUsersBinding binding;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    User clickeduser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchUsersBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        getUsers();

        binding.cardUserPreview.setVisibility(View.GONE);

        if(Account_Details.User_Details.getIsMentor()){
            binding.imgBTNRequest.setVisibility(View.VISIBLE);
        }else {
            binding.imgBTNRequest.setVisibility(View.GONE);
        }


        if(Account_Details.User_Details.getCurrSearch()){
            binding.switchMentor.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.switchMentor.setTextColor(this.requireContext().getColor(R.color.white));
        }else {
            binding.switchStudent.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.switchStudent.setTextColor(this.requireContext().getColor(R.color.white));
        }

        binding.switchStudent.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(false);
            getUsers();
            binding.switchStudent.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.switchStudent.setTextColor(this.requireContext().getColor(R.color.white));
            binding.switchMentor.setBackgroundColor(this.requireContext().getColor(R.color.white));
            binding.switchMentor.setTextColor(this.requireContext().getColor(R.color.blue));
        });
        binding.switchMentor.setOnClickListener(view -> {
            Account_Details.User_Details.setCurrSearch(true);
            getUsers();
            binding.switchMentor.setBackgroundColor(this.requireContext().getColor(R.color.blue));
            binding.switchMentor.setTextColor(this.requireContext().getColor(R.color.white));
            binding.switchStudent.setBackgroundColor(this.requireContext().getColor(R.color.white));
            binding.switchStudent.setTextColor(this.requireContext().getColor(R.color.blue));
        });

        binding.imgBTNBack.setOnClickListener(view -> binding.cardUserPreview.setVisibility(View.GONE));

        binding.imgBTNRequest.setOnClickListener(view -> {
            if(clickeduser.isAccepting) {
                //region update student list of received requests
                DocumentReference df = fStore.collection("Users").document(clickeduser.uid);
                Map<String, Object> userInfo = new HashMap<>();
                ArrayList<String> addRequest = new ArrayList<>(clickeduser.requests);
                if(addRequest.contains(Account_Details.User_Details.getUID())){
                    Toast.makeText(getContext(), "Request Failed: You have already sent an application", Toast.LENGTH_SHORT).show();
                }else if(!addRequest.contains(Account_Details.User_Details.getUID())){
                    addRequest.add(Account_Details.User_Details.getUID());
                    Toast.makeText(getContext(), "Mentorship Application sent", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
                }
                userInfo.put("userRequests", addRequest);
                df.update(userInfo);
                //endregion
                //region update mentor list of sent requests
                df = fStore.collection("Users").document(Account_Details.User_Details.getUID());
                userInfo = new HashMap<>();
                addRequest = new ArrayList<>(Account_Details.User_Details.requests);
                userInfo.put("userRequests", addRequest);
                df.update(userInfo);
                getUsers();
                //endregion
            } else {
                Toast.makeText(getContext(), "Student is no longer accepting mentorship", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getUsers(){
        binding.recyclerUsers.setVisibility(View.INVISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        fStore.collection("Users").get().addOnCompleteListener(task -> {
            String currentUserId = FirebaseAuth.getInstance().getUid();
            if(task.isSuccessful() && task.getResult() != null){
                List<User> list_users = new ArrayList<>();
                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    assert currentUserId != null;
                    if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                        continue;
                    }
                    if (!queryDocumentSnapshot.getBoolean("isMentor") == Account_Details.User_Details.getCurrSearch()){
                        continue;
                    }
                    User user = new User();
                    user.fullName = queryDocumentSnapshot.getString("FullName");
                    user.email = queryDocumentSnapshot.getString("Email");
                    user.pictureStr= queryDocumentSnapshot.getString("Picture");
                    user.authLvl = Objects.requireNonNull(queryDocumentSnapshot.getLong("AuthLevel")).intValue();
                    user.bioEssay = queryDocumentSnapshot.getString("bioEssay");
                    user.fbUsername = queryDocumentSnapshot.getString("FB_Username");
                    user.lInUsername = queryDocumentSnapshot.getString("LinkedIn_Username");
                    user.isAccepting = queryDocumentSnapshot.getBoolean("isAccepting");
                    user.isMentor = queryDocumentSnapshot.getBoolean("isMentor");
                    user.subjectsBinary = Objects.requireNonNull(queryDocumentSnapshot.getLong("subjectsBinary")).intValue();
                    user.uid = queryDocumentSnapshot.getId();
                    if(queryDocumentSnapshot.get("userRequests") == null){
                        user.requests.clear();
                    } else{
                        user.requests = (ArrayList<String>) queryDocumentSnapshot.get("userRequests");
                    }
                    list_users.add(user);
                }
                if(list_users.size()>0){
                    binding.progressBar.setVisibility(View.GONE);
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

    @Override
    public void onUserClicked(User user) {
        clickeduser = user;
        initPreview();
    }

    public void initPreview(){
        Boolean isMentor = clickeduser.isMentor;
        if(isMentor != null){
            if(isMentor) {
                binding.txtAccType.setText(R.string.Mentor);
            } else{
                binding.txtAccType.setText(R.string.Student);
            } }

        Boolean isAccepting = clickeduser.isAccepting;
        if(isAccepting != null){
            if(isAccepting){
                binding.txtStatus.setText(R.string.CurrentlyAccepting);
                binding.imgBTNRequest.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_person_add_24));
            } else{
                binding.txtStatus.setText(R.string.NoLongerAccepting);
                binding.imgBTNRequest.setImageDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_baseline_person_add_24_disabled));
            } }

        switch (clickeduser.authLvl){
            case(0):
                binding.txtAuthLVL.setText(R.string.AuthLVL_0);
                break;
            case(1):
                binding.txtAuthLVL.setText(R.string.AuthLVL_1);
                break;
            case(2):
                binding.txtAuthLVL.setText(R.string.AuthLVL_2);
        }

        List<String> lstSubj = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            if ((clickeduser.subjectsBinary & (1 << i)) > 0) {
                switch (i) {
                    case 0:
                        lstSubj.add("Adobe");
                        break;
                    case 1:
                        lstSubj.add("Animation");
                        break;
                    case 2:
                        lstSubj.add("Arts");
                        break;
                    case 3:
                        lstSubj.add("AutoCAD");
                        break;
                    case 4:
                        lstSubj.add("Programming");
                        break;
                    case 5:
                        lstSubj.add("Microsoft");
                        break;
                    case 6:
                        lstSubj.add("Mathematics");
                        break;
                    case 7:
                        lstSubj.add("Sciences");
                        break;
                    case 8:
                        lstSubj.add("Languages");
                        break;
                    case 9:
                        lstSubj.add("Law");
                        break;
                    case 10:
                        lstSubj.add("Engineering");
                        break;
                    default:
                        break;
                }
            }
        }
        String lstSubjString = String.join(" | ", lstSubj);

        if(clickeduser.requests.contains(Account_Details.User_Details.getUID())){
            binding.imgBTNRequest.setVisibility(View.GONE);
        } else {
            binding.imgBTNRequest.setVisibility(View.VISIBLE);
        }

        binding.txtSubjectsList.setText(lstSubjString);
        binding.txtFullName.setText(clickeduser.fullName);
        binding.txtEmail.setText(clickeduser.email);
        binding.txtBioEssay.setText(clickeduser.bioEssay);
        binding.txtFBUsername.setText(clickeduser.fbUsername);
        binding.txtLInUsername.setText(clickeduser.lInUsername);
        binding.cardUserPreview.setVisibility(View.VISIBLE);
    }
}