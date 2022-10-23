package com.example.medcinpatintrecycleview;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView sender,receiver;
    ImageView sender_Img,receiver_Img;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public void SetMessage(Application application,String message, String time, String date, String type, String senderUid, String receiverUid,String currentUid){

        sender=itemView.findViewById(R.id.sender_message);
        receiver=itemView.findViewById(R.id.receiver_message);
        sender_Img =itemView.findViewById(R.id.sender_Img);
        receiver_Img = itemView.findViewById(R.id.receiver_Img);

        if(currentUid.equals(senderUid)){
            if(type.equals("i")){

                receiver.setVisibility(View.GONE);
                sender.setVisibility(View.GONE);
                receiver_Img.setVisibility(View.GONE);
                sender_Img.setVisibility(View.VISIBLE);
                Picasso.get().load(message).into(sender_Img);


            }else if(type.equals("t")){

                receiver.setVisibility(View.GONE);
                sender.setVisibility(View.VISIBLE);
                sender.setText(message);
                sender_Img.setVisibility(View.GONE);
                receiver_Img.setVisibility(View.GONE);

            }

        }else if (currentUid.equals(receiverUid)){

            if(type.equals("i")){

                receiver.setVisibility(View.GONE);
                sender.setVisibility(View.GONE);
                receiver_Img.setVisibility(View.VISIBLE);
                sender_Img.setVisibility(View.GONE);
                Picasso.get().load(message).into(receiver_Img);


            }else if(type.equals("t")){

                receiver.setVisibility(View.VISIBLE);
                sender.setVisibility(View.GONE);
                receiver.setText(message);
                sender_Img.setVisibility(View.GONE);
                receiver_Img.setVisibility(View.GONE);

            }


        }



    }
}
