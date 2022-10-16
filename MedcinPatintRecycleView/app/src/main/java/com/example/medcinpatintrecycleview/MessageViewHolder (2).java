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


        if(type.equals("text")){
            if(currentUid.equals(senderUid)){
                receiver.setVisibility(View.GONE);
                sender.setText(message);

            }else if (currentUid.equals(receiverUid)){
                sender.setVisibility(View.GONE);
                receiver.setText(message);

            }

        }else if(type.equals("iv")){

            if(currentUid.equals(senderUid)){
                receiver.setVisibility(View.GONE);
                sender.setVisibility(View.GONE);
                receiver_Img.setVisibility(View.GONE);
                sender_Img.setVisibility(View.VISIBLE);
                Picasso.get().load(message).into(sender_Img);

            }else if(currentUid.equals(receiverUid)){
                receiver.setVisibility(View.GONE);
                sender.setVisibility(View.GONE);
                sender_Img.setVisibility(View.GONE);
                receiver_Img.setVisibility(View.VISIBLE);
                Picasso.get().load(message).into(receiver_Img);
            }

        }



    }
}
