package com.example.mentor.misc;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public String fullName, email, pictureStr, uid, proposalUID, subject;
    public Boolean isMentor, isAccepting;
    public Integer authLvl, rating, numRatingSent, numRatingReceived;
    public Long minFee, maxFee;
}
