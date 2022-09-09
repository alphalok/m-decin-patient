package com.example.medcinpatintrecycleview;

import java.util.ArrayList;

public class Medcin  {

    private String Fullname ,Age ,Emai, Cin ;
    private ArrayList<String> medcinPatientsCin;

    public Medcin(String fullname, String age, String emai, String cin) {
        Fullname = fullname;
        Age = age;
        Emai = emai;
        Cin = cin;
        medcinPatientsCin= new ArrayList<String>();
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getEmai() {
        return Emai;
    }

    public void setEmai(String emai) {
        Emai = emai;
    }

    public String getCin() {
        return Cin;
    }

    public void setCin(String cin) {
        Cin = cin;
    }

    public ArrayList<String> getPatients() {
        return medcinPatientsCin;
    }

    public void setPatients(ArrayList<String> patientCin) {
        this.medcinPatientsCin = patientCin;
    }
}
