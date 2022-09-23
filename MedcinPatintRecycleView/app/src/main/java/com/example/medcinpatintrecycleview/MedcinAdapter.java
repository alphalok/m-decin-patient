package com.example.medcinpatintrecycleview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedcinAdapter extends RecyclerView.Adapter<MedcinAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Medcin> medecins = new ArrayList<>();

    public void setMedecins(ArrayList<Medcin> medecins) {
        this.medecins = medecins;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedcinAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.medeciniteme,parent,false);
        MedcinAdapter.MyViewHolder holder = new MedcinAdapter.MyViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull MedcinAdapter.MyViewHolder holder, int position) {

        Medcin medecin = medecins.get(position);

        holder.fullName.setText(medecin.getFullname());
        holder.numOrdre.setText(medecin.getNumOrdre());


        holder.senImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,DossierPatientActivity.class);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return medecins.size();
    }

    public MedcinAdapter(Context context) {
        this.context = context;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView numOrdre, fullName;
        private Button senImg;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            numOrdre=itemView.findViewById(R.id.textViewMNumOrdre);
            fullName=itemView.findViewById(R.id.textViewMFullNom);

            senImg =itemView.findViewById(R.id.SendImg);

        }

    }


}

