package com.coverteam.junkcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStarted extends AppCompatActivity {

    Button btn_login;
    Button btn_register;
    Animation ttb, btt;
    ImageView app_logo,app_text;
    TextView text_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //load animation
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);

        //load element
        app_logo = findViewById(R.id.applogo);
        app_text = findViewById(R.id.apptext);
        text_View = findViewById(R.id.textView);
        btn_login = findViewById(R.id.button_login);
        btn_register = findViewById(R.id.button_register);

        //runanimation
        app_logo.startAnimation(ttb);
        app_text.startAnimation(ttb);
        text_View.startAnimation(ttb);
        btn_login.startAnimation(btt);
        btn_register.startAnimation(btt);

        //klik tombol
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gologin = new Intent(GetStarted.this, Login.class);
                startActivity(gologin);
            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goregisterone = new Intent(GetStarted.this, Register_satu.class);
                startActivity(goregisterone);
            }
        });

    }
}
