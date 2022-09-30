package com.example.medcinpatintrecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SendImgActivity extends AppCompatActivity {

    private String url, receiver_id, sender_id;
    private ImageView imageView;
    private TextView dontQuitTxt;
    private Uri imageUri;
    private ProgressBar progressBar;
    private Button sendImgBtn;
    private UploadTask uploadTask;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference reference1,reference2;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    //private Uri uri;
    private MessageMembre messageMembre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_img);


        messageMembre= new MessageMembre();
        storageReference = FirebaseStorage.getInstance().getReference("messageImages");


        imageView = findViewById(R.id.sendImage);
        sendImgBtn = findViewById(R.id.sendImageBtn);
        progressBar = findViewById(R.id.SendImgProgressBar);
        dontQuitTxt = findViewById(R.id.textViewDontQuit);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            url = bundle.getString("URL");
            receiver_id = bundle.getString("RECEIVER_ID");
            sender_id=bundle.getString("SENDER_ID");

        }
        else {
            Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show();
        }

        Picasso.get().load(url).into(imageView);

        imageUri = Uri.parse(url);

        reference1 = database.getReference("Message").child(sender_id).child(receiver_id);
        reference2 = database.getReference("Message").child(receiver_id).child(sender_id);

        sendImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage();
                dontQuitTxt.setVisibility(View.VISIBLE);
            }
        });



    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void sendImage() {

        if(imageUri != null){
            progressBar.setVisibility(View.VISIBLE);
            sendImgBtn.setVisibility(View.GONE);

            final StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();

                        Calendar cdate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                        final String saveDate = currentDate.format(cdate.getTime());

                        Calendar cTime = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                        final String savetime = currentTime.format(cTime.getTime());

                        String time = savetime + ":"+ savetime;


                        messageMembre.setDate(saveDate);
                        messageMembre.setTime(savetime);
                        messageMembre.setMessage(downloadUri.toString());
                        messageMembre.setReceiverUid(receiver_id);
                        messageMembre.setSenderUid(sender_id);
                        messageMembre.setType("iv");

                        String id = reference1.push().getKey();
                        reference1.child(id).setValue(messageMembre);

                        String id1 = reference2.push().getKey();
                        reference2.child(id1).setValue(messageMembre);
                        progressBar.setVisibility(View.GONE);
                        dontQuitTxt.setVisibility(View.INVISIBLE);

                        finish();


                    }
                }
            });




        }else {
            Toast.makeText(this, "sélectionnez une image", Toast.LENGTH_SHORT).show();
        }

    }
}