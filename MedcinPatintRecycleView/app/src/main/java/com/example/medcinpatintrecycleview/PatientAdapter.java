package com.example.medcinpatintrecycleview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Patient> patients = new ArrayList<>();
    private DatabaseReference reference;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
        notifyDataSetChanged();
    }




    public PatientAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.patientitem,parent,false);
        MyViewHolder holder =  new MyViewHolder(view);
        return holder;
    }





    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Patient patient = patients.get(position);

        holder.fullName.setText(patient.getFullname());
        holder.cin.setText(patient.getCin());


        holder.voirDossier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,DossierPatientActivity.class);
                intent.putExtra("PATIENT_CHOISI",patient);
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

        holder.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("RECEIVER_ID",patient.getCin());
                intent.putExtra("RECEIVER_NAME",patient.getFullname());
                intent.putExtra("USER_TYPE",1);
                context.startActivity(intent);
            }
        });

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.supp_patient));
                builder.setMessage(context.getString(R.string.suprrimer_patient));

                builder.setPositiveButton(context.getString(R.string.supprimer), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child("medecins").child(auth.getCurrentUser().getUid()).child("medcinPatients");

                        reference.child(patient.getCin()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Patient a été supprimer", Toast.LENGTH_SHORT).show();
                                    context.startActivity(new Intent(context,MedecinProfileActivity.class));
                                    ((Activity)context).finish();
                                }
                                else {
                                    Toast.makeText(context,context.getString(R.string.erreur), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });



                    }
                });
                builder.setNegativeButton(context.getString(R.string.anuler), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();


                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       private TextView fullName,cin;
       private Button voirDossier;
       private Button sendMessage;

       private RelativeLayout relativeLayout;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName=itemView.findViewById(R.id.textViewFullNom);
            cin=itemView.findViewById(R.id.textViewMCIN);

            voirDossier =itemView.findViewById(R.id.VoirDossierBtn);
            sendMessage = itemView.findViewById(R.id.SendMessageBtn);

            relativeLayout=itemView.findViewById(R.id.relativeLayoutP);


        }

    }
}
