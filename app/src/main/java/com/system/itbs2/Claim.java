package com.system.itbs2;

public class Claim {
    public String workId;
    public String tutorialDate;
    String  tutorialDuration;
    public String tutorialSubject;
    int totalFee;
    public String studentName;
    public String tutorName;

    public Claim() {
        // Default constructor required for calls to DataSnapshot.getValue(Claim.class)
    }

    public Claim(String workId, String tutorialDate, String tutorialDuration, String tutorialSubject, int totalFee, String studentName, String tutorName) {
        this.workId = workId;
        this.tutorialDate = tutorialDate;
        this.tutorialDuration = tutorialDuration;
        this.tutorialSubject = tutorialSubject;
        this.totalFee = totalFee;
        this.studentName = studentName;
        this.tutorName = tutorName;
    }
}
