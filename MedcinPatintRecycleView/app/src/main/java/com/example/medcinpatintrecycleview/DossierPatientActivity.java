package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DossierPatientActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewNomComplet,textViewNumTel,textViewAge,textViewCin,textViewATCDsperso,textViewATCDsfami,textViewFactRisq,textViewVaccin;
    private EditText editTextAjoutVAcc,editTextAjoutATCDPerso,editTextAjoutATCDFami,editTextAjoutFactRisque;
    private Button AjoutVacciBtn,AjoutATCDsPerso,AjoutATCDsFamil,AjoutFactRisque;
    private Patient patient;

    private DatabaseReference reference;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DossierPatientActivity.this,MedecinProfileActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dossier_patient);

        textViewNomComplet = findViewById(R.id.textViewPNomComplet);
        textViewNumTel = findViewById(R.id.textViewPNumTel);
        textViewAge = findViewById(R.id.textViewPAge);
        textViewCin = findViewById(R.id.textViewPCin);
        textViewATCDsperso = findViewById(R.id.textViewATCDsperso);
        textViewATCDsfami = findViewById(R.id.textViewATCDsfami);
        textViewFactRisq = findViewById(R.id.textViewFactRisq);
        textViewVaccin = findViewById(R.id.textViewVaccin);

        editTextAjoutVAcc = findViewById(R.id.editTextAjoutVAcc);
        editTextAjoutATCDPerso = findViewById(R.id.editTextAjoutATCDPerso);
        editTextAjoutATCDFami = findViewById(R.id.editTextAjoutATCDFami);
        editTextAjoutFactRisque = findViewById(R.id.editTextAjoutFactRisque);


        AjoutVacciBtn = findViewById(R.id.AjoutVacciBtn);
        AjoutATCDsPerso = findViewById(R.id.AjoutATCDsPerso);
        AjoutATCDsFamil = findViewById(R.id.AjoutATCDsFamil);
        AjoutFactRisque = findViewById(R.id.AjoutFactRisque);




        Intent intent = getIntent();
         patient =(Patient) intent.getSerializableExtra("PATIENT_CHOISI");

        ///////////////////////////////////////////////////////////////////////////////////////

        textViewNomComplet.setText(patient.getFullname());
        textViewNumTel.setText(patient.getNumTelephone());
        textViewAge.setText(patient.getAge());
        textViewCin.setText(patient.getCin());

        reference = FirebaseDatabase.getInstance().getReference("Users");

        AjoutVacciBtn.setOnClickListener(this);
        AjoutATCDsFamil.setOnClickListener(this);
        AjoutATCDsPerso.setOnClickListener(this);
        AjoutFactRisque.setOnClickListener(this);

       getDetail();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AjoutVacciBtn:
                AddVacination();
                break;
            case R.id.AjoutATCDsPerso:
                AddAtcdPerso();
                break;
            case R.id.AjoutATCDsFamil:
                AddAtcdFamil();
                break;
            case R.id.AjoutFactRisque:
                AddFactRisque();
        }
    }

    private void AddVacination() {
        String vaccination = editTextAjoutVAcc.getText().toString().trim();

        if(vaccination.isEmpty()){
            editTextAjoutVAcc.setError("veuillez entrer une vaccination");
            editTextAjoutVAcc.requestFocus();
            return;
        }

        reference.child("patients").child(patient.getCin()).child("vaccination").push().setValue(vaccination).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DossierPatientActivity.this, "l'enregistrement a été effectué avec succès", Toast.LENGTH_LONG).show();
                editTextAjoutVAcc.setText("");
            }
        });


    }
    private void AddAtcdPerso() {
        String atcdPerso = editTextAjoutATCDPerso.getText().toString().trim();

        if(atcdPerso.isEmpty()){
            editTextAjoutATCDPerso.setError("veuillez entrer une vaccination");
            editTextAjoutATCDPerso.requestFocus();
            return;
        }

        reference.child("patients").child(patient.getCin()).child("ATCDs_Personnels").push().setValue(atcdPerso).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DossierPatientActivity.this, "l'enregistrement a été effectué avec succès", Toast.LENGTH_LONG).show();
                editTextAjoutATCDPerso.setText("");
            }
        });

    }
    private void AddAtcdFamil() {
        String atcdFamil = editTextAjoutATCDFami.getText().toString().trim();

        if(atcdFamil.isEmpty()){
            editTextAjoutATCDFami.setError("veuillez entrer une vaccination");
            editTextAjoutATCDFami.requestFocus();
            return;
        }

        reference.child("patients").child(patient.getCin()).child("ATCDs_Familiaux").push().setValue(atcdFamil).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DossierPatientActivity.this, "l'enregistrement a été effectué avec succès", Toast.LENGTH_LONG).show();
                editTextAjoutATCDFami.setText("");
            }
        });

    }
    private void AddFactRisque(){
        String FacteurRisque = editTextAjoutFactRisque.getText().toString().trim();

        if(FacteurRisque.isEmpty()){
            editTextAjoutFactRisque.setError("veuillez entrer une vaccination");
            editTextAjoutFactRisque.requestFocus();
            return;
        }

        reference.child("patients").child(patient.getCin()).child("Facteur_Risque").push().setValue(FacteurRisque).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DossierPatientActivity.this, "l'enregistrement a été effectué avec succès", Toast.LENGTH_LONG).show();
                editTextAjoutFactRisque.setText("");
            }
        });

    }





    private void getVaccinations() {
        reference.child("patients").child(patient.getCin()).child("vaccination").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String vaccinations ="";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    vaccinations += String.valueOf(dataSnapshot.getValue())+"\n";
                }
                textViewVaccin.setText(vaccinations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getATCDsPersonnels(){
        reference.child("patients").child(patient.getCin()).child("ATCDs_Personnels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ATCDsPersonnels ="";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ATCDsPersonnels += String.valueOf(dataSnapshot.getValue())+"\n";
                }
                textViewATCDsperso.setText(ATCDsPersonnels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getATCDs_Familiaux(){
        reference.child("patients").child(patient.getCin()).child("ATCDs_Familiaux").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ATCDs_Familiaux ="";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ATCDs_Familiaux += String.valueOf(dataSnapshot.getValue())+"\n";
                }
                textViewATCDsfami.setText(ATCDs_Familiaux);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getDetail(){
        reference.child("patients").child(patient.getCin()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ///////////vaccinations/////////
                String vaccinations ="";
                String ATCDsPersonnels ="";
                String ATCDs_Familiaux ="";
                String FacteursRisque ="";
                for(DataSnapshot dataSnapshot : snapshot.child("vaccination").getChildren()){
                    vaccinations += String.valueOf(dataSnapshot.getValue())+"\n";
                }
                for(DataSnapshot dataSnapshot : snapshot.child("ATCDs_Personnels").getChildren()){
                    ATCDsPersonnels += String.valueOf(dataSnapshot.getValue())+"\n";
                }
                for(DataSnapshot dataSnapshot : snapshot.child("ATCDs_Familiaux").getChildren()){
                    ATCDs_Familiaux += String.valueOf(dataSnapshot.getValue())+"\n";
                }
                for(DataSnapshot dataSnapshot : snapshot.child("Facteur_Risque").getChildren()){
                    FacteursRisque += String.valueOf(dataSnapshot.getValue())+"\n";
                }

                textViewATCDsfami.setText(ATCDs_Familiaux);
                textViewATCDsperso.setText(ATCDsPersonnels);
                textViewVaccin.setText(vaccinations);
                textViewFactRisq.setText(FacteursRisque);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}