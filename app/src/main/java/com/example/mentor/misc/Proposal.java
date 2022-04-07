package com.example.mentor.misc;

import java.io.Serializable;

public class Proposal implements Serializable {
    public String subject, email, date, startTime, endTime, description;
    public Boolean isMentor;
    public Integer status;
}
