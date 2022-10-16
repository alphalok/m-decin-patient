package com.example.medcinpatintrecycleview;

public class Admin {
    private String Emai, Uid;

    public Admin(String emai, String uid) {
        Emai = emai;
        Uid = uid;
    }
    public Admin(){}

    public String getEmai() {
        return Emai;
    }

    public void setEmai(String emai) {
        Emai = emai;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

}
