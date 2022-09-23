package com.example.medcinpatintrecycleview;

import java.util.ArrayList;

public class Medcin  {

    private String Fullname ,NumTel ,Emai, NumOrdre ;
    private ArrayList<String> medcinPatientsCin;

    public Medcin(String fullname, int numTel, String emai, String numOrdre) {
        Fullname = fullname;
        numTel = numTel;
        Emai = emai;
        NumOrdre = numOrdre;
        medcinPatientsCin= new ArrayList<String>();
    }

    public Medcin(){

    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getEmai() {
        return Emai;
    }

    public void setEmai(String emai) {
        Emai = emai;
    }

    public String getNumTel() {
        return NumTel;
    }

    public void setNumTel(String numTel) {
        NumTel = numTel;
    }

    public String getNumOrdre() {
        return NumOrdre;
    }

    public void setNumOrdre(String numOrdre) {
        NumOrdre = numOrdre;
    }

    public ArrayList<String> getPatients() {
        return medcinPatientsCin;
    }

    public void setPatients(ArrayList<String> patientCin) {
        this.medcinPatientsCin = patientCin;
    }
}
