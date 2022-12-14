package com.example.medcinpatintrecycleview;

import java.io.Serializable;

public class Patient implements Serializable {
    private String fullname ,age ,emai, Cin,numTelephone ;


    public Patient() {
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public Patient(String fullname, String age, String emai, String cin, String numTelephone) {
        this.fullname = fullname;
        this.age = age;
        this.emai = emai;
        Cin = cin;
        this.numTelephone=numTelephone;
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
