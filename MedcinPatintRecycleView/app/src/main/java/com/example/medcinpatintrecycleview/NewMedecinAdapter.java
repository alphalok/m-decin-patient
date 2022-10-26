package com.example.medcinpatintrecycleview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;

public class NewMedecinAdapter extends RecyclerView.Adapter<NewMedecinAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Medcin> medecins = new ArrayList<>();
    private DatabaseReference reference;

    public void setMedecins(ArrayList<Medcin> medecins) {
        this.medecins = medecins;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.new_medecin_item,parent,false);
        NewMedecinAdapter.MyViewHolder holder = new NewMedecinAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Medcin medecin = medecins.get(position);

        holder.fullName.setText(medecin.getFullname());
        holder.numOrdre.setText(medecin.getNumOrdre());

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference().child("Users").child("newMedecin");
                reference.child(medecin.getNumOrdre()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Médecin a été supprimer", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(context, "Un probleme a survenu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AddPatientActivity.class);
                intent.putExtra("MED_NUM_ORDRE",medecin.getNumOrdre());
                intent.putExtra("MED_NUM_TELEPHONE",medecin.getNumTel());
                intent.putExtra("USER_TYPE",0);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return medecins.size();
    }


    public NewMedecinAdapter(Context context) {
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView numOrdre, fullName;
        private Button accept,decline;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            numOrdre=itemView.findViewById(R.id.textViewNewMedNumOrdre);
            fullName=itemView.findViewById(R.id.textViewNewMedFullNom);

            accept =itemView.findViewById(R.id.AcceptMedBtn);
            decline = itemView.findViewById(R.id.declineBtn);

        }

    }
    private void sharReferalLink(String Medecin_Num_Odre, String phoneNumber) {


        ////////// creer referral Link //////////
        String PackageName = context.getPackageName();


        String referralLink = "https://medcinetpatient.page.link/?" +
                "link=https://chuibnrochd.ma/medecinpatients.php?medcin=" + Medecin_Num_Odre + "-" +"isMedecin-"+
                "&apn=" + PackageName +
                "&st=" + "lien referral" +
                "&sd=" + "appuyez pour completer votre inscription" +
                "&si=" + "https://chuibnrochd.ma/wp-content/themes/themes_chu/layout/images/img4/logo.jpg";


        ////////// Raccourcir un long lien de referral //////////

        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(referralLink))
                .buildShortDynamicLink().addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
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
                            context.startActivity(intent);

                        } else {
                            Log.d("EROOR", "erreur" + task.getException());
                        }
                    }
                });
    }

}
