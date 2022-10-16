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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MedcinAdapter extends RecyclerView.Adapter<MedcinAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Medcin> medecins = new ArrayList<>();
    private DatabaseReference reference;
    private String key;

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

        Medcin medecin1 = medecins.get(position);

        holder.fullName.setText(medecin1.getFullname());
        holder.numOrdre.setText(medecin1.getNumOrdre());


        holder.senImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context,ChatActivity.class);

                reference = FirebaseDatabase.getInstance().getReference().child("Users").child("medecins");


                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot ds : snapshot.getChildren()){
                            String medNumOrdre = String.valueOf(ds.child("numOrdre").getValue());
                            if(medNumOrdre.equals(medecin1.getNumOrdre())){

                                intent.putExtra("RECEIVER_ID",ds.getKey());
                                intent.putExtra("RECEIVER_NAME",medecin1.getFullname());
                                intent.putExtra("USER_TYPE", 0);
                                intent.putExtra("PATIENT_CIN",key);
                                context.startActivity(intent);


                            }
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
    public int getItemCount() {
        return medecins.size();
    }

    public MedcinAdapter(Context context,String key) {
        this.context = context;
        this.key=key;
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

