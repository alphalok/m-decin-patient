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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private Button addPatientBtn;


    private DatabaseReference database;
    private PatientAdapter adapter ;
    private ArrayList<Patient> patients;
    private FirebaseUser user;

    private RelativeLayout parent;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        addPatientBtn = findViewById(R.id.addPatientBtn);

        recyclerView = findViewById(R.id.patientsRecycleView);
        parent = findViewById(R.id.relativeLayoutP);

        database = FirebaseDatabase.getInstance().getReference("Users");

        patients= new ArrayList<Patient>();


        user = FirebaseAuth.getInstance().getCurrentUser();



        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("medecins").child(user.getUid()).child("medcinPatients").exists()){
                    ArrayList<String> PatientCin = new ArrayList<String>();
                    for(DataSnapshot dataSnapshot : snapshot.child("medecins").child(user.getUid()).child("medcinPatients").getChildren()){

                        PatientCin.add(String.valueOf(dataSnapshot.getValue()));

                    }
                    for(String cin : PatientCin){
                        if(snapshot.child("patients").child(cin).exists()){
                            Patient patient = snapshot.child("patients").child(cin).getValue(Patient.class);
                            patients.add(patient);
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "FFFaild", Toast.LENGTH_SHORT).show();
                        }

                    }

                    adapter.setPatients(patients);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("tag", "probleme");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });




        adapter = new PatientAdapter(this);
        adapter.setPatients(patients);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(ProfileActivity.this,AddPatientActivity.class));
                startActivity(intent);

            }
        });
    }
}


