package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientProfileActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button singoutBtn;

    private FirebaseAuth auth;
    private DatabaseReference database ;

    private MedcinAdapter adapter ;
    private ArrayList<Medcin> medecins;
    private Patient patient;
    private FirebaseUser user;


    private RelativeLayout parent;

    private String key;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        recyclerView = findViewById(R.id.MedecinsRecycleView);
        parent = findViewById(R.id.relativeLayoutM);

        database = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        patient= new Patient();

        Intent intent = getIntent();

        key = intent.getStringExtra("PATIENT_CIN");

        medecins=new ArrayList<Medcin>();

        //medecins.add(new Medcin("ka",065,"elho@gmail.com","kamal123"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedcinAdapter(this);
        adapter.setMedecins(medecins);
        recyclerView.setAdapter(adapter);





        database.addValueEventListener(new ValueEventListener() {
            ArrayList<String> medcinId = new ArrayList<String>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("patients").child(key).child("patientMedcins").getChildren()){
                    medcinId.add(String.valueOf(dataSnapshot.getValue()));
                }
                for(String medId : medcinId){

                    if(snapshot.child("medecins").child(medId).exists()){
                        Medcin medcin = snapshot.child("medecins").child(medId).getValue(Medcin.class);
                        medecins.add(medcin);
                    }
                    else{
                    }
                }
                adapter.setMedecins(medecins);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        singoutBtn = findViewById(R.id.singOutButton);
        singoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(PatientProfileActivity.this,MainActivity.class));
                finish();

            }
        });
    }




}