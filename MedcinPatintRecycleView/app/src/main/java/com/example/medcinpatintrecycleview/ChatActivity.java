package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton sendBtn,camBtn;
    private TextView chatUserName;
    private EditText messageEt;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference1,reference2,reference;
    private MessageMembre messageMembre;

    private String reciver_name,reciver_Id,sender_id;

    private int receiverType;

    private Uri uri;

    private int RECEIVER_IS_MED = 0;
    private int RECEIVER_IS_PAT = 1;

    private static final int PICK_IMG = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        Bundle bundle = getIntent().getExtras();

        recyclerView = findViewById(R.id.messagesRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));

        messageEt = findViewById(R.id.MessageET);
        sendBtn = findViewById(R.id.imageButton);
        camBtn = findViewById(R.id.camButton);
        chatUserName = findViewById(R.id.chatUserName);



        receiverType = getIntent().getIntExtra("USER_TYPE",2);
        messageMembre = new MessageMembre();

        if(bundle != null){
            reciver_Id = bundle.getString("RECEIVER_ID");
            reciver_name = bundle.getString("RECEIVER_NAME");

        }else {
            Toast.makeText(this, "utilisateur manquant", Toast.LENGTH_SHORT).show();
            finish();
        }



        chatUserName.setText(reciver_name);
        chatUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messageEt.getText().toString().isEmpty()){
                    finish();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setTitle("voulez vous quitter la conversation");
                    builder.setMessage("Votre conversation peut etre supprimé");

                    builder.setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    builder.setNegativeButton("Rester", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }

            }
        });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (receiverType == RECEIVER_IS_PAT){
                    sender_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference1 = database.getReference("Message").child(sender_id).child(reciver_Id);
                    reference2 = database.getReference("Message").child(reciver_Id).child(sender_id);
                    SendMessage();
                }else if(receiverType == RECEIVER_IS_MED){
                    sender_id = getIntent().getStringExtra("PATIENT_CIN");
                    reference1 = database.getReference("Message").child(sender_id).child(reciver_Id);
                    reference2 = database.getReference("Message").child(reciver_Id).child(sender_id);
                    SendMessage();
                }
            }
        });

        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMG);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMG && resultCode == RESULT_OK && data != null && data.getData() != null){

            if (receiverType == RECEIVER_IS_PAT){
                sender_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }else if(receiverType == RECEIVER_IS_MED){
                sender_id = getIntent().getStringExtra("PATIENT_CIN");
            }
            uri = data.getData();

            String url = uri.toString();
            Intent intent = new Intent(ChatActivity.this,SendImgActivity.class);
            intent.putExtra("URL",url);
            intent.putExtra("RECEIVER_ID",reciver_Id);
            intent.putExtra("SENDER_ID",sender_id);
            startActivity(intent);

        }
        else {
            Toast.makeText(this, "aucun image a ete selectionné", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(messageEt.getText().toString().isEmpty()){
            ChatActivity.super.onBackPressed();
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("voulez vous quitter la conversation");
            builder.setMessage("Votre conversation peut etre supprimé");

            builder.setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ChatActivity.super.onBackPressed();
                    finish();
                }
            });
            builder.setNegativeButton("Rester", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (receiverType == RECEIVER_IS_PAT){
            sender_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }else if(receiverType == RECEIVER_IS_MED){
            sender_id = getIntent().getStringExtra("PATIENT_CIN");
        }

        reference1 = database.getReference("Message").child(sender_id).child(reciver_Id);
        reference2 = database.getReference("Message").child(reciver_Id).child(sender_id);

       // Toast.makeText(ChatActivity.this, reciver_Id, Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<MessageMembre> options1 =
                new FirebaseRecyclerOptions.Builder<MessageMembre>()
                        .setQuery(reference1,MessageMembre.class).build();


        FirebaseRecyclerAdapter<MessageMembre,MessageViewHolder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<MessageMembre, MessageViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageMembre model) {
                        holder.SetMessage(getApplication(),model.getMessage(),model.getTime(),model.getDate(),model.getType(),model.getSenderUid(),model.getReceiverUid(),sender_id);
                    }

                    @NonNull
                    @Override
                    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.message_layout,parent,false);

                        return new MessageViewHolder(view);
                    }
                };
        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);


    }

    private void SendMessage() {
        String message = messageEt.getText().toString().trim();

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveDate = currentDate.format(cdate.getTime());

        Calendar cTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final String savetime = currentTime.format(cTime.getTime());

        String time = savetime + ":"+ savetime;

        if(message.isEmpty()){
            messageEt.setError("Ecrivez votre message");
            messageEt.requestFocus();
            return;
        }
        messageMembre.setDate(saveDate);
        messageMembre.setTime(savetime);
        messageMembre.setMessage(message);
        messageMembre.setReceiverUid(reciver_Id);
        messageMembre.setSenderUid(sender_id);
        messageMembre.setType("text");

        String id = reference1.push().getKey();
        reference1.child(id).setValue(messageMembre);

        String id1 = reference2.push().getKey();
        reference2.child(id1).setValue(messageMembre);

        messageEt.setText("");

    }
}



//todo fix the image probleme in the recycle view
