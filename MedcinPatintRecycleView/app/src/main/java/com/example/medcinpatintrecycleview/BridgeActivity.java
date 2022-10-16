package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class BridgeActivity extends AppCompatActivity {

    private String medcinId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();


                            String referLink = deepLink.toString();
                            try {
                                referLink = referLink.substring(referLink.lastIndexOf("=") + 1);

                                String userID = referLink.substring(0, referLink.indexOf("-"));

                                medcinId = referLink.substring(userID.length()+1,referLink.lastIndexOf("-"));

                                if(medcinId.equals("isMedecin")){
                                    Intent intent = new Intent(BridgeActivity.this,MedecinRegisterActivity.class);
                                    intent.putExtra("MEDECIN_NUM_ORDRE",userID);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Intent intent = new Intent(BridgeActivity.this,PatientRegisterActivity.class);
                                    intent.putExtra("MEDECIN_NUM_ORDRE",userID);
                                    intent.putExtra("MEDECIN_ID",medcinId);
                                    startActivity(intent);
                                    finish();
                                }




                            } catch (Exception exception) {
                                Log.d("exception", exception.toString());
                            }

                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Patient Registration", "getDynamicLink:onFailure", e);
                    }
                });

    }
}