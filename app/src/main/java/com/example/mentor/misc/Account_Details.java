package com.example.mentor.misc;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public enum Account_Details {

    User_Details("", "", "", "", "", "", false, false, 0, 0, true, "", false, new ArrayList<>(), ""),
    User_Clicked("", "", "", "", "", "", false, false, 0, 0, true, "", false, new ArrayList<>(), "");

    public ArrayList<Long> rates;
    private String fullName, email, fbUser, lInUser, bioEssay, picString, uID, setDate;
    private Boolean isMentor, isAccepting, currSearch, currConnection;
    private Integer subjects, authLevel;

    Account_Details(String fullName, String email, String fbUser, String lInUser, String bioEssay, String picString, Boolean isMentor, Boolean isAccepting, Integer subjects, Integer authLevel, Boolean currSearch, String uID, Boolean currConnection, ArrayList<Long> rates, String setDate) {
        this.fullName = fullName;
        this.email = email;
        this.fbUser = fbUser;
        this.lInUser = lInUser;
        this.bioEssay = bioEssay;
        this.picString = picString;
        this.isMentor = isMentor;
        this.isAccepting = isAccepting;
        this.subjects = subjects;
        this.authLevel = authLevel;
        this.currSearch = currSearch;
        this.uID = uID;
        this.currConnection = currConnection;
        this.rates = rates;
        this.setDate = setDate;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFbUser() {
        return fbUser;
    }

    public String getlInUser() {
        return lInUser;
    }

    public String getBioEssay() {
        return bioEssay;
    }

    public String getPicString() {
        return picString;
    }

    public Boolean getIsMentor() {
        return isMentor;
    }

    public Boolean getIsAccepting() {
        return isAccepting;
    }

    public Integer getSubjects() {
        return subjects;
    }

    public Integer getAuthLevel() {
        return authLevel;
    }

    public Boolean getCurrSearch(){return currSearch;}

    public String getUID(){return uID;}

    public Boolean getCurrConnection(){return currConnection;}

    public ArrayList<Long> getRates(){return rates;}

    public String getSetDate(){return setDate;}

    public void setFullName(String fullName){this.fullName = fullName;}

    public void setEmail(String email){this.email = email;}

    public void setFBUser(String fbUser){this.fbUser = fbUser;}

    public void setLInUser(String lInUser){this.lInUser = lInUser;}

    public void setBioEssay(String bioEssay){this.bioEssay = bioEssay;}

    public void setPicString(String picString){this.picString = picString;}

    public void setIsMentor(Boolean isMentor){this.isMentor = isMentor;}

    public void setIsAccepting(Boolean isAccepting){this.isAccepting = isAccepting;}

    public void setSubjects(Integer subjects){this.subjects = subjects;}

    public void setAuthLevel(Integer authLevel){this.authLevel = authLevel;}

    public void setCurrSearch(Boolean currSearch){this.currSearch = currSearch;}

    public void setUID(String uID){this.uID = uID;}

    public void setCurrConnection (Boolean currConnection){this.currConnection = currConnection;}

    public void setRates(ArrayList<Long> rates){this.rates = rates;}

    public void setSetDate(String setDate){this.setDate = setDate;}

    public void initUser() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseUser fUser = fAuth.getCurrentUser();
        assert fUser != null;
        DocumentReference df = fStore.collection("Users").document(fUser.getUid());
        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess" + documentSnapshot.getData());
            User_Details.setFullName(documentSnapshot.getString("FullName"));
            User_Details.setEmail(documentSnapshot.getString("Email"));
            User_Details.setFBUser(documentSnapshot.getString("FB_Username"));
            User_Details.setLInUser(documentSnapshot.getString("LinkedIn_Username"));
            User_Details.setBioEssay(documentSnapshot.getString("bioEssay"));
            User_Details.setAuthLevel(Objects.requireNonNull(documentSnapshot.getLong("AuthLevel")).intValue());
            User_Details.setSubjects(Objects.requireNonNull(documentSnapshot.getLong("subjectsBinary")).intValue());
            User_Details.setIsMentor(documentSnapshot.getBoolean("isMentor"));
            User_Details.setIsAccepting(documentSnapshot.getBoolean("isAccepting"));
            User_Details.setCurrSearch(true);
            User_Details.setUID(fUser.getUid());
            if(documentSnapshot.get("SubjectRates") != null){
                User_Details.setRates((ArrayList<Long>) documentSnapshot.get("SubjectRates"));
            }
            User_Details.setCurrConnection(false);
        });
    }

    public List<String> initLstSubj() {
        List<String> lstSubj = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            if ((subjects & (1 << i)) > 0) {
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
        return lstSubj;
    }

    public void reset(){
        this.fullName = "";
        this.email = "";
        this.fbUser = "";
        this.lInUser = "";
        this.bioEssay = "";
        this.picString = "";
        this.isMentor = false;
        this.isAccepting = false;
        this.subjects = 0;
        this.authLevel = 0;
        this.rates.clear();
        this.setDate = "";
    }
}
