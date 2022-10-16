package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;

public class AddPatientActivity extends AppCompatActivity {
    private EditText editTextPhoneNumber, editTextCIN;
    private Button registerBtn;
    private ProgressBar progressBar;

    private DatabaseReference reference;

    private String userUid;

    FirebaseAuth auth;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        setContentView(R.layout.activity_add_patient);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                if(i ==0){
                    view.setSystemUiVisibility(hideSystemUi());
                }
            }
        });




        auth = FirebaseAuth.getInstance();
        userUid = auth.getCurrentUser().getUid();

        editTextPhoneNumber = findViewById(R.id.editTextPatientNumTel);
        editTextCIN = findViewById(R.id.editTextPatientCIN);

        registerBtn = findViewById(R.id.patientRegisterBtn);
        progressBar = findViewById(R.id.patientProgressBar);

        /////////// auto search fonction //////////////////

        editTextCIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtSearch(editable.toString());
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String cin = editTextCIN.getText().toString().trim();

                if (cin.isEmpty()) {
                    editTextCIN.setError("entrer votre CIN");
                    editTextCIN.requestFocus();
                }

                if (phoneNumber.isEmpty()) {
                    editTextPhoneNumber.setError("entrer votre nom");
                    editTextPhoneNumber.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                reference = FirebaseDatabase.getInstance().getReference("Users");

                reference.child("patients").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child(cin).exists()){
                                    reference.child("medecins").child(userUid).child("medcinPatients").push().setValue(cin);
                                    reference.child("patients").child(cin).child("patientMedcins").push().setValue(userUid);
                                    Toast.makeText(AddPatientActivity.this, "Patient enregistrer", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(AddPatientActivity.this,MedecinProfileActivity.class));
                                    finish();
                                }
                                else {
                                    registerNewUser(cin,phoneNumber);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

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

    private void registerNewUser(String cin,String phoneNumber) {

        sharReferalLink(cin, phoneNumber);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("medecins").child(userUid).child("medcinPatients").push().setValue(cin);

        Toast.makeText(this, "Patient enregistrer", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AddPatientActivity.this, MedecinProfileActivity.class));
        finish();
    }

    private void sharReferalLink(String patientCin, String phoneNumber) {


        ////////// creer referral Link //////////

        String referralLink = "https://medcinetpatient.page.link/?" +
                "link=https://chuibnrochd.ma/medecinpatients.php?patientid=" + patientCin + "-" +userUid+"-"+
                "&apn=" + getPackageName() +
                "&st=" + "lien referral" +
                "&sd=" + "appuyez pour completer votre inscription" +
                "&si=" + "https://chuibnrochd.ma/wp-content/themes/themes_chu/layout/images/img4/logo.jpg";


        ////////// Raccourcir un long lien de referral //////////

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(referralLink))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Raccourci creer
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            //// envoyer le lien ////
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setPackage("com.whatsapp");
                            String url = "https://api.whatsapp.com/send?phone=" + "+212" + phoneNumber + "&text=" + shortLink.toString();
                            intent.setData(Uri.parse(url));
                            startActivity(intent);

                        } else {
                            Log.d("EROOR", "erreur" + task.getException());
                        }
                    }
                });
    }

    private void txtSearch(String Cin){

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child("patients").orderByChild("cin").startAt(Cin).endAt(Cin+"~").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Patient>patients =new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Patient patient = dataSnapshot.getValue(Patient.class);
                    patients.add(patient);;
                }
                if(patients.size()==1){
                    editTextPhoneNumber.setText(patients.get(0).getNumTelephone());
                }
                else{
                    editTextPhoneNumber.setText("");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}