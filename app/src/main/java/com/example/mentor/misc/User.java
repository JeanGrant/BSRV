package com.example.mentor.misc;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String fullName, email, pictureStr, bioEssay, fbUsername, lInUsername, uid;
    public Boolean isMentor, isAccepting;
    public Integer authLvl;
    public ArrayList<Long> rates = new ArrayList<>();
    public ArrayList<String> subjects = new ArrayList<>();
}
