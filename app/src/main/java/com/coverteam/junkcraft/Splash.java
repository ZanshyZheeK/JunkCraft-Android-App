package com.coverteam.junkcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {

    Animation app_splash;
    ImageView app_logo;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        //load element
        app_logo = findViewById(R.id.applogo);

        //runanimation
        app_logo.startAnimation(app_splash);

        getUsernameLocal();
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");

        if(username_key_new.isEmpty()){
            //seting 1 detik splash
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //pindah ke getstarted
                    Intent gogetstarted = new Intent(Splash.this, GetStarted.class);
                    startActivity(gogetstarted);
                    finish();
                }
            },2000);
        }
        else {
            //seting 1 detik splash
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //pindah ke menu
                    Intent gohome = new Intent(Splash.this, Menu_utana.class);
                    startActivity(gohome);
                    finish();
                }
            },2000);
        }
    }
}
