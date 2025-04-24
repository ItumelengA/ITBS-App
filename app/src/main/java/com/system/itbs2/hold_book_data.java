package com.system.itbs2;

public class hold_book_data {
    private String name;
    private String surname;
    private String cellNo;
    private String topicName;
    private String tutorNo;
    private String date;
    private String time;
    private String module;

    public hold_book_data() {
        // Default constructor required for calls to DataSnapshot.getValue(hold_book_data.class)
    }

    public hold_book_data(String name, String surname, String cellNo, String topicName, String tutorNo, String date, String time, String module) {
        this.name = name;
        this.surname = surname;
        this.cellNo = cellNo;
        this.topicName = topicName;
        this.tutorNo = tutorNo;
        this.date = date;
        this.time = time;
        this.module = module;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getCellNo() { return cellNo; }
    public void setCellNo(String cellNo) { this.cellNo = cellNo; }

    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }

    public String getTutorNo() { return tutorNo; }
    public void setTutorNo(String tutorNo) { this.tutorNo = tutorNo; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
}
