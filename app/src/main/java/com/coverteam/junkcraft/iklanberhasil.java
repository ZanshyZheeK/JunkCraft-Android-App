package com.coverteam.junkcraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class iklanberhasil extends AppCompatActivity {

    Animation ttb, btt, app_splash;
    ImageView app_logo;
    Button btn_dashboard,profil;
    TextView title,subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iklanberhasil);

        //load animation
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);

        //load element
        app_logo = findViewById(R.id.applogo);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        btn_dashboard = findViewById(R.id.button_dashboar);
        profil = findViewById(R.id.button_berhasil);

        //runanimation
        app_logo.startAnimation(app_splash);
        title.startAnimation(ttb);
        subtitle.startAnimation(ttb);
        profil.startAnimation(btt);
        btn_dashboard.startAnimation(btt);

        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gomenuutama = new Intent(iklanberhasil.this, Menu_utana.class);
                startActivity(gomenuutama);
                finish();
            }
        });

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goprofil = new Intent(iklanberhasil.this, Profile.class);
                startActivity(goprofil);
                finish();
            }
        });
    }
}
