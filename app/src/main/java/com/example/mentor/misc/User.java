package com.example.mentor.misc;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String fullName, email, pictureStr, bioEssay, fbUsername, lInUsername, uid;
    public Boolean isMentor, isAccepting;
    public Integer authLvl, subjectsBinary;
    public ArrayList<String> requests = new ArrayList<>();
    public ArrayList<Long> rates = new ArrayList<>();
}
