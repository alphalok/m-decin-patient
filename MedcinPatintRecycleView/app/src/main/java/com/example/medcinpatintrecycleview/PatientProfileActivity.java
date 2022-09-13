package com.example.medcinpatintrecycleview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class PatientProfileActivity extends AppCompatActivity {
    private Button singoutBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        auth = FirebaseAuth.getInstance();

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