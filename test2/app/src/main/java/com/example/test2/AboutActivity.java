package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {
    private TextView nom, prenom, age,about;
    private ImageView patientImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        nom = findViewById(R.id.nom2);
        prenom=findViewById(R.id.prenom2);
        age = findViewById(R.id.age2);
        about=findViewById(R.id.about);
        patientImage =findViewById(R.id.patientphoto2);

        ArrayList<String> aboutPatient = new ArrayList<>();
        aboutPatient.add("I inadvertently went to See's Candy last week\n" +
                "in the mall looking for phone repair,\n" +
                " aSee's Candy now charges a dollar -- a full dollar -- for even the simplest of their wee confection offerings\n");
        aboutPatient.add("I inadvertently went to See's Candy last week\n" +
                "in the mall looking for phone repair,\n" +
                " aSee's Candy now charges a dollar -- a full dollar -- for even the simplest of their wee confection offerings\n");
        aboutPatient.add("I inadvertently went to See's Candy last week\n" +
                "in the mall looking for phone repair,\n" +
                " aSee's Candy now charges a dollar -- a full dollar -- for even the simplest of their wee confection offerings\n");
        aboutPatient.add("I inadvertently went to See's Candy last week\n" +
                "in the mall looking for phone repair,\n" +
                " aSee's Candy now charges a dollar -- a full dollar -- for even the simplest of their wee confection offerings\n");


        Intent intent = getIntent();
        if(intent!=null){
            int position = intent.getIntExtra("POSITION",-1);
            if (position!=-1){
                nom.setText(RecycleViewAdapter.getPatientsArray().get(position).getNom());
                prenom.setText(RecycleViewAdapter.getPatientsArray().get(position).getPrenom());
                age.setText(RecycleViewAdapter.getPatientsArray().get(position).getAge().toString());
                about.setText(aboutPatient.get(position));
                Glide.with(this).asBitmap().load(RecycleViewAdapter.getPatientsArray().get(position).getImageUrl()).into(patientImage);


            }
        }




    }
}