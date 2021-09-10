package com.google.firebase.ml.mddd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class splash_screen extends AppCompatActivity {

    //Animation t,o,u,r,i,s,t2;
    //TextView text,texo,texu,texr,texi,texs,text2;
    LottieAnimationView views;
    private static int SPLASH_SCREEN = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        views = findViewById(R.id.animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(splash_screen.this,LoginActivity.class);
                    startActivity(i);

              //  startActivity(new Intent(getApplicationContext(), LiveBarcodeScanningActivity.class));

                finish();
                }
        },SPLASH_SCREEN);



    }

}
