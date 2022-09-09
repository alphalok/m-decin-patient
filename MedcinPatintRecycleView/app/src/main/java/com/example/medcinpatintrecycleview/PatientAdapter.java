package com.example.medcinpatintrecycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Patient> patients = new ArrayList<>();

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
        holder.email.setText(patient.getEmai());
        holder.age.setText(patient.getAge());
        holder.cin.setText(patient.getCin());

    }





    @Override
    public int getItemCount() {
        return patients.size();
    }






    public static class MyViewHolder extends RecyclerView.ViewHolder {
       private TextView age, fullName,cin,email;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            age=itemView.findViewById(R.id.textViewAge);
            fullName=itemView.findViewById(R.id.textViewFullNom);
            cin=itemView.findViewById(R.id.textViewCIN);
            email=itemView.findViewById(R.id.textViewEmail);

        }

    }
}
