package com.example.medcinpatintrecycleview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class StartAnimatonActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;

    TextView welcome ;
    ImageView cloud1,cloud2,cloud3;
    LottieAnimationView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animaton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        relativeLayout = findViewById(R.id.mainAnimation);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartAnimatonActivity.this,MainActivity.class));
                finish();
            }
        });

        welcome = findViewById(R.id.welcome);
        cloud1 = findViewById(R.id.cloud1);
        cloud2 = findViewById(R.id.cloud2);
        cloud3 = findViewById(R.id.cloud3);
        animation = findViewById(R.id.animation);

        cloud1.animate().translationY(500).setDuration(1000).setStartDelay(800);
        cloud3.animate().translationX(800).setDuration(2000).setStartDelay(500);
        cloud2.animate().translationX(-800).setDuration(2000).setStartDelay(500);
        welcome.animate().translationY(1550).setDuration(1000).setStartDelay(1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartAnimatonActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);





    }
}

