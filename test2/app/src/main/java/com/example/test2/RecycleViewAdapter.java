package com.example.test2;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {


    private static ArrayList<Patient> patientsArray = new ArrayList<>();
    private Context context;
    public RecycleViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patients_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {///hna howaa rj3at lih final mafhamtch chno lmochkil
        Patient patient = patientsArray.get(position);


        holder.nom.setText(patient.getNom());
        holder.prenom.setText(patient.getPrenom());
        holder.age.setText(patientsArray.get(position).getAge().toString());


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setPosi(position);
                Intent intent = new Intent(context,AboutActivity.class);
                intent.putExtra("POSITION",holder.getAbsoluteAdapterPosition());
                context.startActivity(intent);

            }
        });

        Glide.with(context).asBitmap().load(patientsArray.get(position).getImageUrl()).into(holder.patientimage);


    }

    @Override
    public int getItemCount() {
        return patientsArray.size();
    }


    public void setPatientsArray(ArrayList<Patient> patientsArray) {
        this.patientsArray = patientsArray;
        notifyDataSetChanged();

    }

    public static ArrayList<Patient> getPatientsArray() {
        return patientsArray;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nom, prenom, age;
        private ImageView patientimage;
        private RelativeLayout parent;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom2);
            prenom = itemView.findViewById(R.id.prenom2);
            age = itemView.findViewById(R.id.age2);
            patientimage = itemView.findViewById(R.id.patientphoto);
            parent = itemView.findViewById(R.id.relativeLayoutP);


        }


    }

}
