package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView patientsRecyvleView;
    private RelativeLayout parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        patientsRecyvleView = findViewById(R.id.PatientRecycleView);
        parent = findViewById(R.id.relativeLayoutP);

        ArrayList<Patient> patientArray = new ArrayList<>();
        patientArray.add(new Patient("Valimi","Mohamed","https://st.depositphotos.com/1771835/2038/i/950/depositphotos_20380779-stock-photo-serious-man-portrait-real-high.jpg",41));
        patientArray.add(new Patient("el Kawali","youssef","https://st.depositphotos.com/1224365/2634/i/950/depositphotos_26345985-stock-photo-portrait-of-a-normal-young.jpg",48));
        patientArray.add(new Patient("El Hamid","Brahim","https://media.proprofs.com/images/QM/user_images/2503852/New%20Project%20(23)(81).jpg",38));
        patientArray.add(new Patient("Kassimi","Asmaa","https://thumbs.dreamstime.com/z/real-normal-person-portrait-22299696.jpg",38));


        RecycleViewAdapter adapter = new RecycleViewAdapter(this);

        adapter.setPatientsArray(patientArray);
        patientsRecyvleView.setAdapter(adapter);
        patientsRecyvleView.setLayoutManager(new LinearLayoutManager(this));

    }
}
