package com.example.test2;

public class Patient {
    private String nom,prenom,imageUrl;
    private Integer age;
    private String about;



    public Patient(String nom, String prenom, String imageUrl, Integer age) {
        this.nom = nom;
        this.prenom = prenom;
        this.imageUrl = imageUrl;
        this.age = age;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", age=" + age +
                '}';
    }
}
