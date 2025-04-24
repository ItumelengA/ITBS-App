package com.system.itbs2;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Model {
        @PropertyName("ImageUri")
                private String imageUrl;
        @PropertyName("pdfUri")
                private  String pdfUri;
        @PropertyName("Name")
                private String name;
        @PropertyName("Surname")
                private String surname;
        @PropertyName("module")
                private String module;
        @PropertyName("Department")
                private String Department;
        @PropertyName("Status")
                private String Status;
        @PropertyName("time1")
                private String time1;
        @PropertyName("time2")
                private String time2;
        @PropertyName("time3")
                private String time3;
        @PropertyName("Phone number")
        private String phoneNum;
        private boolean accepted;

                public Model() {
                }

                public Model(String imageUrl, String pdfUri, String name, String surname, String module, String department, String status, String time1, String time2, String time3) {
                        this.imageUrl = imageUrl;
                        this.pdfUri = pdfUri;
                        this.name = name;
                        this.surname = surname;
                        this.module = module;
                        Department = department;
                        Status = status;
                        this.time1 = time1;
                        this.time2 = time2;
                        this.time3 = time3;
                }

                public String getImageUrl() {
                        return imageUrl;
                }

                public void setImageUrl(String imageUrl) {
                        this.imageUrl = imageUrl;
                }

                public String getPdfUri() {
                        return pdfUri;
                }

                public void setPdfUri(String pdfUri) {
                        this.pdfUri = pdfUri;
                }

                public String getName() {
                        return name;
                }

                public void setName(String name) {
                        this.name = name;
                }

                public String getSurname() {
                        return surname;
                }

                public void setSurname(String surname) {
                        this.surname = surname;
                }

                public String getModule() {
                        return module;
                }

                public void setModule(String module1) {
                        this.module = module1;
                }

                public String getDepartment() {
                        return Department;
                }

                public void setDepartment(String department) {
                        Department = department;
                }

                public String getStatus() {
                        return Status;
                }

                public void setStatus(String status) {
                        Status = status;
                }

                public String getTime1() {
                        return time1;
                }

                public void setTime1(String time1) {
                        this.time1 = time1;
                }

                public String getTime2() {
                        return time2;
                }

                public void setTime2(String time2) {
                        this.time2 = time2;
                }

                public String getTime3() {
                        return time3;
                }

                public void setTime3(String time3) {
                        this.time3 = time3;
                }
        // Getter and setter for 'accepted'
        public boolean isAccepted() {
                return accepted;
        }

        public void setAccepted(boolean accepted) {
                this.accepted = accepted;
        }

        public String getPhoneNum() {
                return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
                this.phoneNum = phoneNum;
        }
}





