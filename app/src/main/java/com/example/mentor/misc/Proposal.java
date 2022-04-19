package com.example.mentor.misc;

import java.io.Serializable;

public class Proposal implements Serializable {
    public String subject, email, date, startTime, endTime, description, requesteeUID, requestorUID, uid, picString, fullName;
    public Integer status;
}
