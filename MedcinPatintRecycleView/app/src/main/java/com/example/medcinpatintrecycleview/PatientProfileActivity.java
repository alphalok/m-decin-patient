package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
    private View view;


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("voulez vous quitter l'application");
        builder.setMessage("etes vous sur de vouloir quitter l'application");

        builder.setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PatientProfileActivity.super.onBackPressed();
                finish();
            }
        });
        builder.setNegativeButton("Rester", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_patient_profile);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i ==0){
                    view.setSystemUiVisibility(hideSystemUi());
                }
            }
        });




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
        adapter = new MedcinAdapter(this,key);
        adapter.setMedecins(medecins);
        recyclerView.setAdapter(adapter);





        database.addValueEventListener(new ValueEventListener() {
            ArrayList<String> medcinId = new ArrayList<String>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medcinId.clear();
                medecins.clear();
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            view.setSystemUiVisibility(hideSystemUi());
        }

    }

    private int hideSystemUi(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }






}