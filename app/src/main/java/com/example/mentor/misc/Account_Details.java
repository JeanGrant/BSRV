package com.example.mentor.misc;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;


public enum Account_Details {

    User_Details("", "", "", "", false, false, 0, true, "", false, LocalDate.now(), new ArrayList<>(), new ArrayList<>()),
    User_Clicked("", "", "", "", false, false, 0, true, "", false, LocalDate.now(), new ArrayList<>(), new ArrayList<>());

    public ArrayList<Long> rates;
    public ArrayList<String> subjects;
    private String fullName, email, bioEssay, picString, uID;
    private Boolean isMentor, isAccepting, currSearch, currConnection;
    private Integer authLevel;
    private LocalDate setDate;

    Account_Details(String fullName, String email, String bioEssay, String picString, Boolean isMentor, Boolean isAccepting, Integer authLevel, Boolean currSearch, String uID, Boolean currConnection, LocalDate setDate, ArrayList<String> subjects, ArrayList<Long> rates) {
        this.fullName = fullName;
        this.email = email;
        this.bioEssay = bioEssay;
        this.picString = picString;
        this.isMentor = isMentor;
        this.isAccepting = isAccepting;
        this.authLevel = authLevel;
        this.currSearch = currSearch;
        this.uID = uID;
        this.currConnection = currConnection;
        this.setDate = setDate;
        this.subjects = subjects;
        this.rates = rates;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
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

    public Integer getAuthLevel() {
        return authLevel;
    }

    public Boolean getCurrSearch(){return currSearch;}

    public String getUID(){return uID;}

    public Boolean getCurrConnection(){return currConnection;}

    public LocalDate getSetDate(){return setDate;}

    public ArrayList<String> getSubjects() {return subjects;}

    public ArrayList<Long> getRates(){return rates;}

    public void setFullName(String fullName){this.fullName = fullName;}

    public void setEmail(String email){this.email = email;}

    public void setBioEssay(String bioEssay){this.bioEssay = bioEssay;}

    public void setPicString(String picString){this.picString = picString;}

    public void setIsMentor(Boolean isMentor){this.isMentor = isMentor;}

    public void setIsAccepting(Boolean isAccepting){this.isAccepting = isAccepting;}

    public void setAuthLevel(Integer authLevel){this.authLevel = authLevel;}

    public void setCurrSearch(Boolean currSearch){this.currSearch = currSearch;}

    public void setUID(String uID){this.uID = uID;}

    public void setCurrConnection (Boolean currConnection){this.currConnection = currConnection;}

    public void setSetDate(LocalDate setDate){this.setDate = setDate;}

    public void setSubjects(ArrayList<String> subjects){this.subjects = subjects;}

    public void setRates(ArrayList<Long> rates){this.rates = rates;}

    public void toggleSubject (String item) {
        if (this.subjects.contains(item)){
            this.subjects.remove(item);
        } else {
            this.subjects.add(item);
        }
        this.subjects.sort(String::compareTo);
        Log.i("toggleSubject", this.subjects.toString());
    }

    public void reset(){
        this.fullName = "";
        this.email = "";
        this.bioEssay = "";
        this.picString = "";
        this.isMentor = false;
        this.isAccepting = false;
        this.authLevel = 0;
        this.subjects.clear();
        this.rates.clear();
    }
}
