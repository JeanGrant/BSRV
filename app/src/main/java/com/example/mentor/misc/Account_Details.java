package com.example.mentor.misc;

import java.util.ArrayList;
import java.util.List;


public enum Account_Details {

    User_Details("", "", "", "", "", "", false, false, 0, 0, true);

    private String fullName, email, fbUser, lInUser, bioEssay, picString;
    private Boolean isMentor, isAccepting, currSearch;
    private Integer subjects, authLevel;

    Account_Details(String fullName, String email, String fbUser, String lInUser, String bioEssay, String picString, Boolean isMentor, Boolean isAccepting, Integer subjects, Integer authLevel, Boolean currSearch) {
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFBUser(String fbUser) {
        this.fbUser = fbUser;
    }

    public void setLInUser(String lInUser) {
        this.lInUser = lInUser;
    }

    public void setBioEssay(String bioEssay) {
        this.bioEssay = bioEssay;
    }

    public void setPicString(String picString) {
        this.picString = picString;
    }

    public void setIsMentor(Boolean isMentor) {
        this.isMentor = isMentor;
    }

    public void setIsAccepting(Boolean isAccepting) {
        this.isAccepting = isAccepting;
    }

    public void setSubjects(Integer subjects) {
        this.subjects = subjects;
    }

    public void setAuthLevel(Integer authLevel) {
        this.authLevel = authLevel;
    }

    public void setCurrSearch(Boolean currSearch) {
        this.currSearch = currSearch;
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

    public Boolean reset(){
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

        return true;
    }
}
