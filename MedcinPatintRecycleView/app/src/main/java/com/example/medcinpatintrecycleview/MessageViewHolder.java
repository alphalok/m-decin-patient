package com.example.medcinpatintrecycleview;

import android.app.Application;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MessageViewHolder extends RecyclerView.ViewHolder {

    TextView sender,receiver;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public void SetMessage(Application application,String message, String time, String date, String type, String senderUid, String receiverUid,String currentUid){

        sender=itemView.findViewById(R.id.sender_message);
        receiver=itemView.findViewById(R.id.receiver_message);



        if(currentUid.equals(senderUid)){
            receiver.setVisibility(View.GONE);
            sender.setText(message);

        }else if (currentUid.equals(receiverUid)){
            sender.setVisibility(View.GONE);
            receiver.setText(message);

        }

    }
}
