package com.example.medcinpatintrecycleview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class FullScreenImgActivity extends AppCompatActivity {
    private ImageView imageView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_img);
        imageView= findViewById(R.id.FullScreenImg);


        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            url = bundle.getString("IMG");

        }
        else {
            Toast.makeText(this, "Erreur", Toast.LENGTH_SHORT).show();
            finish();
        }

        Picasso.get().load(url).into(imageView);
    }
}