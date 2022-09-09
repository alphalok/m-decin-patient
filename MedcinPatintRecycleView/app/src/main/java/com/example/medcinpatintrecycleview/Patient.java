package com.example.medcinpatintrecycleview;

public class Patient {
    private String fullname ,age ,emai, Cin ;


    public Patient() {
    }

    public Patient(String fullname, String age, String emai, String cin) {
        this.fullname = fullname;
        this.age = age;
        this.emai = emai;
        Cin = cin;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmai() {
        return emai;
    }

    public void setEmai(String emai) {
        this.emai = emai;
    }

    public String getCin() {
        return Cin;
    }

    public void setCin(String cin) {
        Cin = cin;
    }
}
