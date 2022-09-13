package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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

    private Button addPatientBtn,singoutBtn;


    private DatabaseReference database;
    private PatientAdapter adapter ;
    private ArrayList<Patient> patients;
    private ArrayList<String> PatientCin;

    private FirebaseUser user;

    private RelativeLayout parent;
    private RecyclerView recyclerView;

    private FirebaseAuth auth;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        addPatientBtn = findViewById(R.id.addPatientBtn);
        singoutBtn = findViewById(R.id.singOutBtn);

        recyclerView = findViewById(R.id.patientsRecycleView);
        parent = findViewById(R.id.relativeLayoutP);

        database = FirebaseDatabase.getInstance().getReference("Users");

        patients= new ArrayList<Patient>();

        auth =FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientAdapter(this);
        adapter.setPatients(patients);
        recyclerView.setAdapter(adapter);

        PatientCin = medecinCinPatient(user.getUid());

        database.child("patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String cin : PatientCin){
                    if(snapshot.child(cin).exists()){
                        Patient patient = snapshot.child(cin).getValue(Patient.class);
                        patients.add(patient);
                    }
                    else{
                    }
                }
                adapter.setPatients(patients);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        singoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();

            }
        });


        addPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(new Intent(ProfileActivity.this,AddPatientActivity.class));
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.sherach,menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                txtSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                txtSearch(s);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String fullName){

        patients= new ArrayList<Patient>();
        adapter = new PatientAdapter(this);
        adapter.setPatients(patients);
        recyclerView.setAdapter(adapter);

        database.child("patients").orderByChild("fullname").startAt(fullName).endAt(fullName+"~").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    if(PatientCin.contains(patient.getCin())){
                        patients.add(patient);
                    }
                }
                adapter.setPatients(patients);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private ArrayList<String> medecinCinPatient(String medecinUid){
        ArrayList<String> patientCin = new ArrayList<String>();
        database.child("medecins").child(medecinUid).child("medcinPatients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    patientCin.add(String.valueOf(dataSnapshot.getValue()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return patientCin;
    }

    private ArrayList<Patient> medecinPatient(ArrayList<String>patientsCin){
        ArrayList<Patient> patients =new ArrayList<Patient>();
        database.child("patients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(String cin : patientsCin){
                    if(snapshot.child(cin).exists()){
                        Patient patient = snapshot.child(cin).getValue(Patient.class);
                        patients.add(patient);
                    }
                    else{
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return patients;
    }


}

//todo make shure that when the user press back button he dosen't quite the app but show him a toast that are you sure you want to quit


