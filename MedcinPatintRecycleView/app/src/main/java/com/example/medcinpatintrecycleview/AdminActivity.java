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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private FirebaseAuth auth;
    private DatabaseReference database ;

    private NewMedecinAdapter adapter ;
    private ArrayList<Medcin> medecins;
    private Patient patient;
    private FirebaseUser user;

    private  Admin admin;

    private RelativeLayout parent;

    private View view;

    private  FloatingActionButton singOutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_admin);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i ==0){
                    view.setSystemUiVisibility(hideSystemUi());
                }
            }
        });

        recyclerView = findViewById(R.id.adminRecycleView);
        parent = findViewById(R.id.relativeLayoutNM);

        database = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        medecins=new ArrayList<Medcin>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewMedecinAdapter(this);
        adapter.setMedecins(medecins);
        recyclerView.setAdapter(adapter);

        singOutBtn = findViewById(R.id.singOutBtn);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medecins.clear();
                for (DataSnapshot dataSnapshot : snapshot.child("newMedecin").getChildren()){
                    Medcin medcin = dataSnapshot.getValue(Medcin.class);
                    medecins.add(medcin);
                }
                adapter.setMedecins(medecins);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        singOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
                builder.setTitle("voulez vous quitter l'application");
                builder.setMessage("êtes-vous sûr de vouloir vous déconnecter ");

                builder.setPositiveButton("se déconnecter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.signOut();
                        startActivity(new Intent(AdminActivity.this,MainActivity.class));
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
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("voulez vous quitter l'application");
        builder.setMessage("êtes vous sûr de vouloir quitter l'application");

        builder.setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AdminActivity.super.onBackPressed();
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